/*
 * This file is part of iZeMod - https://github.com/iZeStudios/iZeMod
 * Copyright (C) 2025 iZeStudios and GitHub contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.izestudios.izemod.component.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.sound.AbstractSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

public class TetrisScreen extends Screen {
    private static final SoundEvent TETRIS_ID = new SoundEvent(Identifier.of("izemod:tetris"), Optional.empty());
    static {
        try {
            Registry.register(Registries.SOUND_EVENT, Identifier.of("izemod", "tetris"), TETRIS_ID);
        } catch (Exception ignored) {}
    }

    public static final TetrisScreen INSTANCE = new TetrisScreen();
    private TetrisGame tetrisGame;

    public TetrisScreen() {
        super(Text.translatable("screens.tetris.title"));
    }

    @Override
    @Nullable
    public MusicSound getMusic() {
        return null;
    }

    @Override
    protected void init() {
        super.init();
        if (client != null && client.getMusicTracker() != null) {
            client.getMusicTracker().stop();
        }
        if (tetrisGame == null) {
            tetrisGame = new TetrisGame();
        }
    }

    @Override
    public void removed() {
        super.removed();
        if (tetrisGame != null) {
            tetrisGame.stopMusic();
            tetrisGame.saveHighScore();
        }
        tetrisGame = null;
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        super.resize(client, width, height);
    }

    @Override
    public void tick() {
        super.tick();
        if (tetrisGame != null) {
            tetrisGame.update();
        }
        if (client != null && client.getMusicTracker() != null) {
            client.getMusicTracker().stop();
        }
    }

    @Override
    public void render(DrawContext c, int mx, int my, float d) {
        super.render(c, mx, my, d);
        if (tetrisGame != null) {
            tetrisGame.render(c);
        }
        drawControls(c);
    }

    @Override
    public boolean keyPressed(int key, int sc, int mod) {
        if (tetrisGame != null && tetrisGame.handleKeyPress(key)) {
            return true;
        }
        return super.keyPressed(key, sc, mod);
    }

    private void drawControls(DrawContext c) {
        String[] lines = {
            Text.translatable("screens.tetris.controls.a").getString() + " = " + Text.translatable("screens.tetris.controls.left").getString(),
            Text.translatable("screens.tetris.controls.d").getString() + " = " + Text.translatable("screens.tetris.controls.right").getString(),
            Text.translatable("screens.tetris.controls.s").getString() + " = " + Text.translatable("screens.tetris.controls.down").getString(),
            Text.translatable("screens.tetris.controls.w").getString() + " = " + Text.translatable("screens.tetris.controls.drop").getString(),
            Text.translatable("screens.tetris.controls.space").getString() + " = " + Text.translatable("screens.tetris.controls.rotate").getString(),
            Text.translatable("screens.tetris.controls.shift").getString() + " = " + Text.translatable("screens.tetris.controls.rotate").getString(),
            Text.translatable("screens.tetris.controls.p").getString() + " = " + Text.translatable("screens.tetris.controls.pause").getString(),
            Text.translatable("screens.tetris.controls.esc").getString() + " = " + Text.translatable("screens.tetris.controls.back").getString()
        };
        int lh = textRenderer.fontHeight + 2;
        int th = lines.length * lh;
        int x = 10;
        int y = (height - th) / 2;
        for (String line : lines) {
            c.drawTextWithShadow(textRenderer, line, x, y, 0xFFFFFFFF);
            y += lh;
        }
    }

    private class TetrisGame {
        private final int bw = 10, bh = 20;
        private final int[][] board = new int[bw][bh];
        private TetrominoWrapper cur, nxt;
        private int cx, cy;
        private boolean paused = false, gameOver = false;
        private long lastUpdate = System.currentTimeMillis();
        private long updateInt = 500;
        private int score = 0;
        private int linesClearedTotal = 0;
        private int level = 1;
        private static int highScore = 0;
        private static boolean loadedHS = false;
        private static File HS_FILE;

        private TetrisMusicInstance music;
        private boolean playing = false;

        private long roundTimeMs = 0;
        private long lastTickTime = System.currentTimeMillis();

        public TetrisGame() {
            loadHighScore();
            clearBoard();
            nxt = new TetrominoWrapper(randomData());
            spawnPiece();
            startMusic();
        }

        private void loadHighScore() {
            if (loadedHS) return;
            loadedHS = true;
            HS_FILE = new File(MinecraftClient.getInstance().runDirectory, "tetris_highscore.txt");
            if (!HS_FILE.exists()) {
                highScore = 0;
                return;
            }
            try (BufferedReader br = new BufferedReader(new FileReader(HS_FILE))) {
                String line = br.readLine();
                if (line != null) {
                    highScore = Integer.parseInt(line.trim());
                }
            } catch (Exception e) {
                highScore = 0;
            }
        }

        public void saveHighScore() {
            if (score > highScore) {
                highScore = score;
            }
            if (HS_FILE == null) return;
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(HS_FILE))) {
                bw.write(String.valueOf(highScore));
            } catch (IOException ignored) {}
        }

        private void startMusic() {
            if (playing) return;
            if (client != null && client.getSoundManager() != null) {
                music = new TetrisMusicInstance();
                client.getSoundManager().play(music);
                playing = true;
            }
        }

        private void stopMusic() {
            if (playing && music != null) {
                client.getSoundManager().stop(music);
                music = null;
                playing = false;
            }
        }

        private void pauseMusic() {
            stopMusic();
        }

        private void resumeMusic() {
            startMusic();
        }

        private void clearBoard() {
            for (int x = 0; x < bw; x++) {
                for (int y = 0; y < bh; y++) {
                    board[x][y] = 0;
                }
            }
        }

        private void spawnPiece() {
            cur = nxt;
            cx = bw / 2;
            cy = 0;
            nxt = new TetrominoWrapper(randomData());
            if (!canMove(cur, cx, cy)) {
                gameOver = true;
                stopMusic();
                checkHighScore();
            }
        }

        public void update() {
            if (paused || gameOver) return;
            long now = System.currentTimeMillis();
            long dt = now - lastTickTime;
            lastTickTime = now;
            roundTimeMs += dt;
            if (now - lastUpdate >= updateInt) {
                if (canMove(cur, cx, cy + 1)) {
                    cy++;
                } else {
                    lockPiece();
                    spawnPiece();
                }
                lastUpdate = now;
            }
        }

        private void lockPiece() {
            for (int[] p : cur.cells()) {
                int x = cx + p[0];
                int y = cy + p[1];
                if (x >= 0 && x < bw && y >= 0 && y < bh) {
                    board[x][y] = cur.color();
                }
            }
            int linesCleared = checkLines();
            if (linesCleared > 0) {
                linesClearedTotal += linesCleared;
                addScoreNES(linesCleared);
                updateLevel();
                checkHighScore();
            }
        }

        private int checkLines() {
            int lines = 0;
            for (int row = 0; row < bh; row++) {
                boolean full = true;
                for (int col = 0; col < bw; col++) {
                    if (board[col][row] == 0) {
                        full = false;
                        break;
                    }
                }
                if (full) {
                    lines++;
                    for (int sr = row; sr > 0; sr--) {
                        for (int col = 0; col < bw; col++) {
                            board[col][sr] = board[col][sr - 1];
                        }
                    }
                    for (int col = 0; col < bw; col++) {
                        board[col][0] = 0;
                    }
                }
            }
            applyPartialGravity();
            return lines;
        }

        private void applyPartialGravity() {
            boolean[][] visited = new boolean[bw][bh];
            for (int x = 0; x < bw; x++) {
                int y = bh - 1;
                if (board[x][y] != 0) {
                    bfsSupport(x, y, visited);
                }
            }

            for (int y = bh - 1; y >= 0; y--) {
                for (int x = 0; x < bw; x++) {
                    if (board[x][y] != 0 && !visited[x][y]) {
                        List<int[]> cluster = gatherCluster(x, y, visited);
                        dropCluster(cluster);
                    }
                }
            }
        }

        private void bfsSupport(int sx, int sy, boolean[][] visited) {
            Queue<int[]> queue = new ArrayDeque<>();
            queue.add(new int[]{sx, sy});
            visited[sx][sy] = true;
            while (!queue.isEmpty()) {
                int[] cur = queue.poll();
                int cx = cur[0];
                int cy = cur[1];
                for (int[] dir : new int[][]{{1,0},{-1,0},{0,1},{0,-1}}) {
                    int nx = cx + dir[0];
                    int ny = cy + dir[1];
                    if (nx >= 0 && nx < bw && ny >= 0 && ny < bh) {
                        if (!visited[nx][ny] && board[nx][ny] != 0) {
                            visited[nx][ny] = true;
                            queue.add(new int[]{nx, ny});
                        }
                    }
                }
            }
        }

        private List<int[]> gatherCluster(int sx, int sy, boolean[][] visited) {
            List<int[]> cluster = new ArrayList<>();
            Queue<int[]> queue = new ArrayDeque<>();
            queue.add(new int[]{sx, sy});
            visited[sx][sy] = true;
            while (!queue.isEmpty()) {
                int[] cur = queue.poll();
                cluster.add(cur);
                int cx = cur[0];
                int cy = cur[1];
                for (int[] dir : new int[][]{{1,0},{-1,0},{0,1},{0,-1}}) {
                    int nx = cx + dir[0];
                    int ny = cy + dir[1];
                    if (nx >= 0 && nx < bw && ny >= 0 && ny < bh) {
                        if (!visited[nx][ny] && board[nx][ny] != 0) {
                            visited[nx][ny] = true;
                            queue.add(new int[]{nx, ny});
                        }
                    }
                }
            }
            return cluster;
        }

        private void dropCluster(List<int[]> cluster) {
            int color = board[cluster.get(0)[0]][cluster.get(0)[1]];
            for (int[] c : cluster) {
                board[c[0]][c[1]] = 0;
            }
            boolean canFall = true;
            int drop = 0;
            while (true) {
                for (int[] c : cluster) {
                    int nx = c[0];
                    int ny = c[1] + (drop + 1);
                    if (ny >= bh || board[nx][ny] != 0) {
                        canFall = false;
                        break;
                    }
                }
                if (!canFall) break;
                drop++;
            }
            for (int[] c : cluster) {
                int nx = c[0];
                int ny = c[1] + drop;
                board[nx][ny] = color;
            }
        }

        private void addScoreNES(int lines) {
            int points = 0;
            switch (lines) {
                case 1 -> points = 40 * level;
                case 2 -> points = 100 * level;
                case 3 -> points = 300 * level;
                case 4 -> points = 1200 * level;
            }
            score += points;
        }

        private void updateLevel() {
            int newLevel = 1 + (linesClearedTotal / 10);
            if (newLevel > level) {
                level = newLevel;
                adjustSpeed();
            }
        }

        private void adjustSpeed() {
            updateInt = 500 - (level - 1) * 40;
            if (updateInt < 100) {
                updateInt = 100;
            }
        }

        private void checkHighScore() {
            if (score > highScore) {
                highScore = score;
            }
        }

        private boolean canMove(TetrominoWrapper piece, int nx, int ny) {
            for (int[] p : piece.cells()) {
                int x = nx + p[0];
                int y = ny + p[1];
                if (x < 0 || x >= bw || y < 0 || y >= bh) {
                    return false;
                }
                if (board[x][y] != 0) {
                    return false;
                }
            }
            return true;
        }

        public void render(DrawContext c) {
            int margin = 40;
            int aw = width - margin;
            int ah = height - margin;
            int cs = Math.min(aw / bw, ah / bh);
            if (cs < 1) cs = 1;
            int bpw = bw * cs;
            int bph = bh * cs;
            int sx = (width - bpw) / 2;
            int sy = (height - bph) / 2;
            c.fill(sx - 2, sy - 2, sx + bpw + 2, sy + bph + 2, 0xFF000000);

            for (int x = 0; x < bw; x++) {
                for (int y = 0; y < bh; y++) {
                    int color = board[x][y];
                    if (color != 0) {
                        int px = sx + x * cs;
                        int py = sy + y * cs;
                        c.fill(px, py, px + cs, py + cs, color);
                    }
                }
            }

            if (!gameOver && !paused && cur != null) {
                for (int[] p : cur.cells()) {
                    int x = cx + p[0];
                    int y = cy + p[1];
                    int px = sx + x * cs;
                    int py = sy + y * cs;
                    c.fill(px, py, px + cs, py + cs, cur.color());
                }
            }

            for (int x = 0; x <= bw; x++) {
                int px = sx + x * cs;
                c.fill(px, sy, px + 1, sy + bph, 0xFF444444);
            }
            for (int y = 0; y <= bh; y++) {
                int py = sy + y * cs;
                c.fill(sx, py, sx + bpw, py + 1, 0xFF444444);
            }

            int ix = sx + bpw + 10;
            int iy = sy + 10;

            c.drawTextWithShadow(textRenderer, Text.translatable("screens.tetris.score"), ix, iy, 0xFFFFFFFF);
            iy += textRenderer.fontHeight + 2;
            c.drawTextWithShadow(textRenderer, String.valueOf(score), ix, iy, 0xFF00FF00);
            iy += textRenderer.fontHeight + 10;

            c.drawTextWithShadow(textRenderer, Text.translatable("screens.tetris.highScore"), ix, iy, 0xFFFFFFFF);
            iy += textRenderer.fontHeight + 2;
            c.drawTextWithShadow(textRenderer, String.valueOf(highScore), ix, iy, 0xFF00FF00);
            iy += textRenderer.fontHeight + 10;

            c.drawTextWithShadow(textRenderer, Text.translatable("screens.tetris.level"), ix, iy, 0xFFFFFFFF);
            iy += textRenderer.fontHeight + 2;
            c.drawTextWithShadow(textRenderer, String.valueOf(level), ix, iy, 0xFF00FF00);
            iy += textRenderer.fontHeight + 10;

            c.drawTextWithShadow(textRenderer, Text.translatable("screens.tetris.time"), ix, iy, 0xFFFFFFFF);
            iy += textRenderer.fontHeight + 2;
            long totalMs = roundTimeMs;
            long h = totalMs / 3600000;
            long r = totalMs % 3600000;
            long m = r / 60000;
            r = r % 60000;
            long s = r / 1000;
            long ms = r % 1000;
            String timeStr;
            if (h > 0) {
                timeStr = String.format("%02d:%02d:%02d.%03d", h, m, s, ms);
            } else {
                timeStr = String.format("%02d:%02d.%03d", m, s, ms);
            }
            c.drawTextWithShadow(textRenderer, timeStr, ix, iy, 0xFF00FF00);
            iy += textRenderer.fontHeight + 10;

            c.drawTextWithShadow(textRenderer, Text.translatable("screens.tetris.next"), ix, iy, 0xFFFFFFFF);
            iy += textRenderer.fontHeight + 6;
            int pcs = (int)(cs * 0.75);
            if (pcs < 1) pcs = 1;
            int psx = ix;
            int psy = iy;

            if (nxt != null) {
                int[][] shape = nxt.cells();
                int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
                int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
                for (int[] p : shape) {
                    if (p[0] < minX) minX = p[0];
                    if (p[0] > maxX) maxX = p[0];
                    if (p[1] < minY) minY = p[1];
                    if (p[1] > maxY) maxY = p[1];
                }
                int offsetX = -minX;
                int offsetY = -minY;
                for (int[] p : shape) {
                    int xx = p[0] + offsetX;
                    int yy = p[1] + offsetY;
                    int px = psx + xx * pcs;
                    int py = psy + yy * pcs;
                    c.fill(px, py, px + pcs, py + pcs, nxt.color());
                }
            }

            if (paused) {
                drawCenteredShadow(c, Text.translatable("screens.tetris.paused"), 0xFFFFFF00);
            } else if (gameOver) {
                drawCenteredShadow(c, Text.translatable("screens.tetris.gameOver"), 0xFFFF0000);
            }
        }

        public boolean handleKeyPress(int key) {
            if (gameOver) {
                if (key == 32) {
                    playClickSound();
                    restartGame();
                    return true;
                }
                return false;
            }
            if (paused) {
                if (key == 80) {
                    playClickSound();
                    paused = false;
                    resumeMusic();
                    return true;
                }
                return false;
            }
            boolean h = false;
            switch (key) {
                case 65 -> {
                    if (canMove(cur, cx - 1, cy)) {
                        cx--;
                        h = true;
                    }
                }
                case 68 -> {
                    if (canMove(cur, cx + 1, cy)) {
                        cx++;
                        h = true;
                    }
                }
                case 83 -> {
                    if (canMove(cur, cx, cy + 1)) {
                        cy++;
                        h = true;
                    }
                }
                case 87 -> {
                    while (canMove(cur, cx, cy + 1)) {
                        cy++;
                    }
                    h = true;
                }
                case 32, 340, 344 -> {
                    TetrominoWrapper r = cur.rotate();
                    if (canMove(r, cx, cy)) {
                        cur = r;
                        h = true;
                    }
                }
                case 80 -> {
                    paused = true;
                    h = true;
                    pauseMusic();
                }
            }
            if (h) {
                playClickSound();
            }
            return h;
        }

        private void restartGame() {
            clearBoard();
            score = 0;
            linesClearedTotal = 0;
            level = 1;
            updateInt = 500;
            gameOver = false;
            paused = false;
            nxt = new TetrominoWrapper(randomData());
            spawnPiece();
            stopMusic();
            startMusic();
            roundTimeMs = 0;
            lastTickTime = System.currentTimeMillis();
        }

        private void drawCenteredShadow(DrawContext c, Text msg, int color) {
            int w = textRenderer.getWidth(msg);
            int xx = (width - w) / 2;
            int yy = height / 2;
            c.drawTextWithShadow(textRenderer, msg, xx, yy, color);
        }

        private void playClickSound() {
            if (MinecraftClient.getInstance().player != null) {
                MinecraftClient.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1.0F, 1.0F);
            }
        }

        private TetrominoData randomData() {
            int[][] shape;
            switch (Tetromino.randomT()) {
                case I -> shape = new int[][]{{-1,0},{0,0},{1,0},{2,0}};
                case O -> shape = new int[][]{{0,0},{1,0},{0,1},{1,1}};
                case T -> shape = new int[][]{{-1,0},{0,0},{1,0},{0,1}};
                case S -> shape = new int[][]{{0,0},{1,0},{-1,1},{0,1}};
                case Z -> shape = new int[][]{{-1,0},{0,0},{0,1},{1,1}};
                case J -> shape = new int[][]{{-1,0},{0,0},{1,0},{1,1}};
                case L -> shape = new int[][]{{-1,0},{0,0},{1,0},{-1,1}};
                default -> shape = new int[][]{{-1,0},{0,0},{1,0},{2,0}};
            }
            int[] mainColors = {
                0xFFFF0000, 0xFFFF7F00, 0xFFFFFF00, 0xFF7FFF00,
                0xFF00FF00, 0xFF00FF7F, 0xFF00FFFF, 0xFF007FFF,
                0xFF0000FF, 0xFF7F00FF, 0xFFFF00FF, 0xFFFF007F
            };
            int color = mainColors[(int)(Math.random() * mainColors.length)];
            return new TetrominoData(shape, color);
        }
    }

    private enum Tetromino {
        Empty,
        I,
        O,
        T,
        S,
        Z,
        J,
        L;
        public static Tetromino randomT() {
            Tetromino[] v = {I,O,T,S,Z,J,L};
            return v[(int)(Math.random()*v.length)];
        }
    }

    private record TetrominoData(int[][] shape, int color) {
        public TetrominoData rotate() {
            int[][] nc = new int[shape.length][2];
            for (int i = 0; i < shape.length; i++) {
                nc[i][0] = shape[i][1];
                nc[i][1] = -shape[i][0];
            }
            return new TetrominoData(nc, color);
        }
    }

    private record TetrominoWrapper(TetrominoData d) {
        public int[][] cells() {
            return d.shape();
        }
        public int color() {
            return d.color();
        }
        public TetrominoWrapper rotate() {
            return new TetrominoWrapper(d.rotate());
        }
    }

    private class TetrisMusicInstance extends AbstractSoundInstance {
        public TetrisMusicInstance() {
            super(TETRIS_ID, SoundCategory.MUSIC, Random.create());
            this.repeat = true;
            this.repeatDelay = 0;
            this.volume = 1.0F;
            this.pitch = 1.0F;
            this.x = 0;
            this.y = 0;
            this.z = 0;
            this.attenuationType = SoundInstance.AttenuationType.NONE;
        }
    }
}
