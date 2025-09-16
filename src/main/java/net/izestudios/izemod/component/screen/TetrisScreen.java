/*
 * This file is part of iZeMod - https://github.com/iZeStudios/iZeMod
 * Copyright (C) 2025 iZeStudios and GitHub contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public class TetrisScreen extends Screen {
    public static final TetrisScreen INSTANCE = new TetrisScreen();
    private static final SoundEvent TETRIS_NORMAL = new SoundEvent(ResourceLocation.parse("izemod:tetris1"), Optional.empty());
    private static final SoundEvent TETRIS_FAST = new SoundEvent(ResourceLocation.parse("izemod:tetris2"), Optional.empty());

    static {
        try {
            Registry.register(BuiltInRegistries.SOUND_EVENT, ResourceLocation.fromNamespaceAndPath("izemod", "tetris1"), TETRIS_NORMAL);
            Registry.register(BuiltInRegistries.SOUND_EVENT, ResourceLocation.fromNamespaceAndPath("izemod", "tetris2"), TETRIS_FAST);
        } catch (Exception ignored) {
        }
    }

    private TetrisGame tetrisGame;

    public TetrisScreen() {
        super(Component.translatable("screens.tetris.title"));
    }

    @Override
    @Nullable
    public Music getBackgroundMusic() {
        return null;
    }

    @Override
    protected void init() {
        super.init();
        if (minecraft != null && minecraft.getMusicManager() != null) {
            minecraft.getMusicManager().stopPlaying();
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
    public void resize(Minecraft client, int width, int height) {
        super.resize(client, width, height);
    }

    @Override
    public void tick() {
        super.tick();
        if (tetrisGame != null) {
            tetrisGame.update();
        }
        if (minecraft != null && minecraft.getMusicManager() != null) {
            minecraft.getMusicManager().stopPlaying();
        }
    }

    @Override
    public void render(GuiGraphics c, int mx, int my, float d) {
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

    private void drawControls(GuiGraphics c) {
        String[] lines = {
            Component.translatable("screens.tetris.controls.a").getString() + " = " + Component.translatable("screens.tetris.controls.left").getString(),
            Component.translatable("screens.tetris.controls.d").getString() + " = " + Component.translatable("screens.tetris.controls.right").getString(),
            Component.translatable("screens.tetris.controls.w").getString() + " = " + Component.translatable("screens.tetris.controls.down").getString(),
            Component.translatable("screens.tetris.controls.s").getString() + " = " + Component.translatable("screens.tetris.controls.drop").getString(),
            Component.translatable("screens.tetris.controls.space").getString() + " = " + Component.translatable("screens.tetris.controls.rotate").getString(),
            Component.translatable("screens.tetris.controls.shift").getString() + " = " + Component.translatable("screens.tetris.controls.rotate").getString(),
            Component.translatable("screens.tetris.controls.p").getString() + " = " + Component.translatable("screens.tetris.controls.pause").getString(),
            Component.translatable("screens.tetris.controls.esc").getString() + " = " + Component.translatable("screens.tetris.controls.back").getString()
        };
        int lh = font.lineHeight + 2;
        int th = lines.length * lh;
        int x = 10;
        int y = (height - th) / 2;
        for (String line : lines) {
            c.drawString(font, line, x, y, 0xFFFFFFFF);
            y += lh;
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
            Tetromino[] v = {I, O, T, S, Z, J, L};
            return v[(int) (Math.random() * v.length)];
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

    private static class TetrisMusicInstance extends AbstractSoundInstance {
        public TetrisMusicInstance(SoundEvent event) {
            super(event, SoundSource.MASTER, RandomSource.create());
            this.looping = true;
            this.delay = 0;
            this.volume = 1.0F;
            this.pitch = 1.0F;
            this.x = 0;
            this.y = 0;
            this.z = 0;
            this.attenuation = SoundInstance.Attenuation.NONE;
        }
    }

    private class TetrisGame {
        private static int highScore = 0;
        private static boolean loadedHS = false;
        private static File HS_FILE;
        private final int bw = 10, bh = 20;
        private final int[][] board = new int[bw][bh];
        private TetrominoWrapper cur, nxt;
        private int cx, cy;
        private boolean paused = false;
        private boolean gameOver = false;
        private long lastUpdate = System.currentTimeMillis();
        private long updateInt = 500;
        private int score = 0;
        private int linesClearedTotal = 0;
        private int level = 1;
        private TetrisMusicInstance music;
        private boolean playing = false;

        private long roundTimeMs = 0;
        private long lastTickTime = System.currentTimeMillis();

        private int clearAnimationTicks = 0;
        private List<Integer> linesToClear = new ArrayList<>();

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
            HS_FILE = new File(Minecraft.getInstance().gameDirectory, "tetrisHighscore.txt");
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
            } catch (IOException ignored) {
            }
        }

        private void startMusic() {
            if (playing) return;
            if (minecraft != null && minecraft.getSoundManager() != null) {
                if (level >= 5) {
                    music = new TetrisMusicInstance(TETRIS_FAST);
                } else {
                    music = new TetrisMusicInstance(TETRIS_NORMAL);
                }
                minecraft.getSoundManager().play(music);
                playing = true;
            }
        }

        private void stopMusic() {
            if (playing && music != null) {
                if (minecraft != null) {
                    minecraft.getSoundManager().stop(music);
                }
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

            if (clearAnimationTicks > 0) {
                clearAnimationTicks -= (int) dt;
                if (clearAnimationTicks <= 0) {
                    removeLines();
                    applyPartialGravity();
                    recheckAll();
                }
                return;
            }

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

        private void recheckAll() {
            List<Integer> newLines = findFullLines();
            if (!newLines.isEmpty()) {
                linesToClear = newLines;
                clearAnimationTicks = 500;
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
            List<Integer> newLines = findFullLines();
            if (!newLines.isEmpty()) {
                linesToClear = newLines;
                clearAnimationTicks = 500;
            }
        }

        private List<Integer> findFullLines() {
            List<Integer> lines = new ArrayList<>();
            for (int row = 0; row < bh; row++) {
                boolean full = true;
                for (int col = 0; col < bw; col++) {
                    if (board[col][row] == 0) {
                        full = false;
                        break;
                    }
                }
                if (full) {
                    lines.add(row);
                }
            }
            return lines;
        }

        private void removeLines() {
            if (linesToClear.isEmpty()) return;
            int lines = linesToClear.size();
            linesClearedTotal += lines;
            addScoreNES(lines);
            updateLevel();
            checkHighScore();

            for (int row : linesToClear) {
                for (int sr = row; sr > 0; sr--) {
                    for (int col = 0; col < bw; col++) {
                        board[col][sr] = board[col][sr - 1];
                    }
                }
                for (int col = 0; col < bw; col++) {
                    board[col][0] = 0;
                }
            }
            linesToClear.clear();
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
                for (int[] dir : new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}}) {
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
                for (int[] dir : new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}}) {
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
            int points = switch (lines) {
                case 1 -> 40 * level;
                case 2 -> 100 * level;
                case 3 -> 300 * level;
                case 4 -> 1200 * level;
                default -> 0;
            };
            score += points;
        }

        private void updateLevel() {
            int newLevel = 1 + (linesClearedTotal / 10);
            if (newLevel > level) {
                level = newLevel;
                adjustSpeed();
                if (!paused && !gameOver) {
                    if (newLevel == 5) {
                        stopMusic();
                        startMusic();
                    }
                }
            }
        }

        private void adjustSpeed() {
            updateInt = 500 - (level - 1) * 16L;
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

        public void render(GuiGraphics c) {
            int margin = 40;
            int aw = width - margin;
            int ah = height - margin;
            int cs = Math.min(aw / bw, ah / bh);
            if (cs < 1) cs = 1;
            int bpw = bw * cs;
            int bph = bh * cs;
            int sx = (width - bpw) / 2;
            int sy = (height - bph) / 2;

            c.fill(sx, sy, sx + bpw, sy + bph, 0xFF000000);

            if (!linesToClear.isEmpty()) {
                float fade = clearAnimationTicks / 500f;
                for (int y : linesToClear) {
                    for (int x = 0; x < bw; x++) {
                        int color = board[x][y];
                        if (color != 0) {
                            int origAlpha = (color >> 24) & 0xFF;
                            int newAlpha = (int) (origAlpha * fade);
                            int newColor = (newAlpha << 24) | (color & 0x00FFFFFF);
                            int px = sx + x * cs;
                            int py = sy + y * cs;
                            c.fill(px, py, px + cs, py + cs, newColor);
                        }
                    }
                }
            }

            for (int x = 0; x < bw; x++) {
                for (int y = 0; y < bh; y++) {
                    int color = board[x][y];
                    if (color != 0) {
                        if (linesToClear.contains(y)) {
                            continue;
                        }
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

            c.fill(sx + bpw, sy + bph, sx + bpw + 1, sy + bph + 1, 0xFF444444);

            int ix = sx + bpw + 10;
            int iy = sy + 4;

            c.drawString(font, Component.translatable("screens.tetris.highScore"), ix, iy, 0xFFFFFFFF);
            iy += font.lineHeight + 1;
            c.drawString(font, String.valueOf(highScore), ix, iy, 0xFFFFFF00);
            iy += font.lineHeight + 8;

            c.drawString(font, Component.translatable("screens.tetris.next"), ix, iy, 0xFFFFFFFF);
            iy += font.lineHeight + 2;

            int pcs = cs;
            int boxWidth = pcs * 5;
            int boxHeight = pcs * 4;
            int boxY = iy;

            c.fill(ix, boxY, ix + boxWidth, boxY + boxHeight, 0xC0000000);

            if (nxt != null) {
                int[][] shape = nxt.cells();
                int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
                int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
                for (int[] p : shape) {
                    if (p[0] < minX) minX = p[0];
                    if (p[0] > maxX) maxX = p[0];
                    if (p[1] < minY) minY = p[1];
                    if (p[1] > maxY) maxY = p[1];
                }
                int shapeWidthInCells = (maxX - minX + 1);
                int shapeHeightInCells = (maxY - minY + 1);
                int shapeWidth = shapeWidthInCells * pcs;
                int shapeHeight = shapeHeightInCells * pcs;
                int centerX = ix + boxWidth / 2;
                int centerY = boxY + boxHeight / 2;
                int shapeLeft = centerX - shapeWidth / 2;
                int shapeTop = centerY - shapeHeight / 2;
                for (int[] p : shape) {
                    int xx = p[0] - minX;
                    int yy = p[1] - minY;
                    int px = shapeLeft + xx * pcs;
                    int py = shapeTop + yy * pcs;
                    c.fill(px, py, px + pcs, py + pcs, nxt.color());
                }

                int outlineColor = 0x00000000;
                for (int gx = 0; gx <= shapeWidthInCells; gx++) {
                    int lineX = shapeLeft + gx * pcs;
                    c.fill(lineX, shapeTop, lineX + 1, shapeTop + shapeHeight, outlineColor);
                }
                for (int gy = 0; gy <= shapeHeightInCells; gy++) {
                    int lineY = shapeTop + gy * pcs;
                    c.fill(shapeLeft, lineY, shapeLeft + shapeWidth, lineY + 1, outlineColor);
                }
            }

            iy += boxHeight + 10;

            c.drawString(font, Component.translatable("screens.tetris.level"), ix, iy, 0xFFFFFFFF);
            iy += font.lineHeight + 1;
            c.drawString(font, String.valueOf(level), ix, iy, 0xFFFF0000);
            iy += font.lineHeight + 10;

            c.drawString(font, Component.translatable("screens.tetris.score"), ix, iy, 0xFFFFFFFF);
            iy += font.lineHeight + 1;
            c.drawString(font, String.valueOf(score), ix, iy, 0xFFFF8000);
            iy += font.lineHeight + 10;

            c.drawString(font, Component.translatable("screens.tetris.lines"), ix, iy, 0xFFFFFFFF);
            iy += font.lineHeight + 1;
            c.drawString(font, String.valueOf(linesClearedTotal), ix, iy, 0xFFFF00FF);
            iy += font.lineHeight + 10;

            c.drawString(font, Component.translatable("screens.tetris.time"), ix, iy, 0xFFFFFFFF);
            iy += font.lineHeight + 1;
            long totalMs = roundTimeMs;
            long h = totalMs / 3600000;
            long r = totalMs % 3600000;
            long m = r / 60000;
            r = r % 60000;
            long s = r / 1000;
            long ms = r % 1000;
            String timeStr = (h > 0)
                ? String.format("%02d:%02d:%02d.%03d", h, m, s, ms)
                : String.format("%02d:%02d.%03d", m, s, ms);
            c.drawString(font, timeStr, ix, iy, 0xFF00FF00);

            if (paused) {
                c.fill(sx, sy, sx + bpw, sy + bph, 0xA0000000);
                drawCenteredShadow(c, Component.translatable("screens.tetris.paused"), 0xFFFFFFFF);
            } else if (gameOver) {
                c.fill(sx, sy, sx + bpw, sy + bph, 0xA0000000);
                String line1 = I18n.get("screens.tetris.gameOver1");
                String line2 = I18n.get("screens.tetris.gameOver2");
                int w1 = font.width(line1);
                int w2 = font.width(line2);
                int x1 = (width - w1) / 2;
                int x2 = (width - w2) / 2;
                int yMid = height / 2;
                c.drawString(font, line1, x1, yMid - 10, 0xFF00FFFF);
                c.drawString(font, line2, x2, yMid + 10, 0xFF0000FF);
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
                case 87 -> {
                    if (canMove(cur, cx, cy + 1)) {
                        cy++;
                        h = true;
                    }
                }
                case 83 -> {
                    while (canMove(cur, cx, cy + 1)) {
                        cy++;
                    }
                    h = true;
                }
                case 32, 340, 344 -> {
                    if (!isOShape(cur)) {
                        TetrominoWrapper r = cur.rotate();
                        if (canMove(r, cx, cy)) {
                            cur = r;
                            h = true;
                        }
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

        private boolean isOShape(TetrominoWrapper piece) {
            int[][] c = piece.cells();
            int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
            int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
            for (int[] p : c) {
                if (p[0] < minX) minX = p[0];
                if (p[0] > maxX) maxX = p[0];
                if (p[1] < minY) minY = p[1];
                if (p[1] > maxY) maxY = p[1];
            }
            int width = (maxX - minX) + 1;
            int height = (maxY - minY) + 1;
            return (width == 2 && height == 2 && c.length == 4);
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

        private void drawCenteredShadow(GuiGraphics c, Component msg, int color) {
            int w = font.width(msg);
            int xx = (width - w) / 2;
            int yy = height / 2;
            c.drawString(font, msg, xx, yy, color);
        }

        private void playClickSound() {
            // ClickableWidget.playClickSound(MinecraftClient.getInstance().getSoundManager());
        }

        private TetrominoData randomData() {
            int[][] shape;
            switch (Tetromino.randomT()) {
                case I -> shape = new int[][]{{-1, 0}, {0, 0}, {1, 0}, {2, 0}};
                case O -> shape = new int[][]{{0, 0}, {1, 0}, {0, 1}, {1, 1}};
                case T -> shape = new int[][]{{-1, 0}, {0, 0}, {1, 0}, {0, 1}};
                case S -> shape = new int[][]{{0, 0}, {1, 0}, {-1, 1}, {0, 1}};
                case Z -> shape = new int[][]{{-1, 0}, {0, 0}, {0, 1}, {1, 1}};
                case J -> shape = new int[][]{{-1, 0}, {0, 0}, {1, 0}, {1, 1}};
                case L -> shape = new int[][]{{-1, 0}, {0, 0}, {1, 0}, {-1, 1}};
                default -> shape = new int[][]{{-1, 0}, {0, 0}, {1, 0}, {2, 0}};
            }
            int[] mainColors = {
                0xFFFF0000, 0xFFFF7F00, 0xFFFFFF00, 0xFF7FFF00,
                0xFF00FF00, 0xFF00FF7F, 0xFF00FFFF, 0xFF007FFF,
                0xFF0000FF, 0xFF7F00FF, 0xFFFF00FF, 0xFFFF007F
            };
            int color = mainColors[(int) (Math.random() * mainColors.length)];
            return new TetrominoData(shape, color);
        }
    }
}
