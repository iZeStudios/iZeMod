/*
 * This file is part of iZeMod - https://github.com/iZeStudios/iZeMod
 * Copyright (C) 2026 iZeStudios and GitHub contributors
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

package net.izestudios.izemod.util;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import org.joml.Matrix3x2fStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class PolygonalBackgroundRenderer {

    private static final List<Point> POINTS = new ArrayList<>();
    private static final Random RANDOM = new Random();
    private static int POINT_COUNT = 100;
    private static final float MAX_DISTANCE = 120.0f;
    private static final float SPEED_MULTIPLIER = 15.0f;
    private static final float LINE_OPACITY = 0.1f;
    private static final float MOUSE_LINE_OPACITY = 0.5f;
    private static final float LINE_THICKNESS = 0.4f;
    private static int lastWidth = -1;
    private static int lastHeight = -1;
    private static long lastTime = -1;

    public static void render(GuiGraphics guiGraphics, int width, int height, int mouseX, int mouseY) {
        if (width != lastWidth || height != lastHeight) {
            POINT_COUNT = calculatePointCount(width, height);
            initPoints(width, height);
            lastWidth = width;
            lastHeight = height;
        }

        final long now = System.currentTimeMillis();
        if (lastTime == -1) lastTime = now;
        float delta = (now - lastTime) / 1000.0f;
        lastTime = now;
        if (delta > 0.1f) delta = 0.1f;

        updatePoints(width, height, delta);

        final float r = Color.WHITE.getRed() / 255.0f;
        final float g = Color.WHITE.getGreen() / 255.0f;
        final float b = Color.WHITE.getBlue() / 255.0f;

        for (int i = 0; i < POINTS.size(); i++) {
            final Point p1 = POINTS.get(i);
            for (int j = i + 1; j < POINTS.size(); j++) {
                final Point p2 = POINTS.get(j);
                final float distSq = p1.distSq(p2);
                if (distSq < MAX_DISTANCE * MAX_DISTANCE) {
                    final float dist = Mth.sqrt(distSq);
                    final float alpha = 1.0f - (dist / MAX_DISTANCE);
                    final int color = ARGB.colorFromFloat(alpha * LINE_OPACITY, r, g, b);
                    drawLine(guiGraphics, p1.x, p1.y, p2.x, p2.y, LINE_THICKNESS, color);
                }
            }

            // Connect to mouse
            final float distSqMouse = p1.distSq(mouseX, mouseY);
            if (distSqMouse < MAX_DISTANCE * MAX_DISTANCE) {
                final float dist = Mth.sqrt(distSqMouse);
                final float alpha = 1.0f - (dist / MAX_DISTANCE);
                final int color = ARGB.colorFromFloat(alpha * MOUSE_LINE_OPACITY, r, g, b);
                drawLine(guiGraphics, p1.x, p1.y, mouseX, mouseY, LINE_THICKNESS, color);
            }
        }
    }

    private static void initPoints(int width, int height) {
        POINTS.clear();
        for (int i = 0; i < POINT_COUNT; i++) {
            POINTS.add(new Point(
                RANDOM.nextFloat() * width,
                RANDOM.nextFloat() * height,
                (RANDOM.nextFloat() - 0.5f) * SPEED_MULTIPLIER,
                (RANDOM.nextFloat() - 0.5f) * SPEED_MULTIPLIER
            ));
        }
    }

    private static void updatePoints(int width, int height, float delta) {
        for (final Point p : POINTS) {
            p.x += p.vx * delta;
            p.y += p.vy * delta;

            if (p.x < 0) {
                p.x = 0;
                p.vx *= -1;
            } else if (p.x > width) {
                p.x = width;
                p.vx *= -1;
            }
            if (p.y < 0) {
                p.y = 0;
                p.vy *= -1;
            } else if (p.y > height) {
                p.y = height;
                p.vy *= -1;
            }
        }
    }

    private static void drawLine(GuiGraphics guiGraphics, float x1, float y1, float x2, float y2, float thickness, int color) {
        final float dx = x2 - x1;
        final float dy = y2 - y1;
        final float dist = Mth.sqrt(dx * dx + dy * dy);
        if (dist <= 0) return;
        final float angle = (float) Math.atan2(dy, dx);

        final Matrix3x2fStack pose = guiGraphics.pose();
        pose.pushMatrix();
        pose.translate(x1, y1);
        pose.rotate(angle);
        pose.scale(dist, thickness);
        guiGraphics.fill(0, 0, 1, 1, color);
        pose.popMatrix();
    }

    private static class Point {
        float x, y;
        float vx, vy;

        Point(float x, float y, float vx, float vy) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
        }

        float distSq(Point other) {
            float dx = x - other.x;
            float dy = y - other.y;
            return dx * dx + dy * dy;
        }

        float distSq(float ox, float oy) {
            float dx = x - ox;
            float dy = y - oy;
            return dx * dx + dy * dy;
        }
    }

    private static int calculatePointCount(int width, int height) {
        final int screenArea = width * height;
        return Math.max(50, Math.min(500, screenArea / 2000));
    }
}
