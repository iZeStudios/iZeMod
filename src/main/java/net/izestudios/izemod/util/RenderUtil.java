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

package net.izestudios.izemod.util;

import net.izestudios.izemod.IzeModImpl;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.util.ARGB;
import org.joml.Matrix3x2fStack;

import static net.izestudios.izemod.component.theme.ColorTheme.EMPTY;
import static net.izestudios.izemod.component.theme.ColorTheme.WHITE_128;

public final class RenderUtil {

    public static void drawScaledLogo(final GuiGraphics context, final float opacity) {
        final Matrix3x2fStack matrices = context.pose();
        matrices.pushMatrix();
        matrices.scale(0.25F, 0.25F);
        drawLogo(context, 6, 6, ARGB.white(opacity));
        matrices.popMatrix();
    }

    public static void drawLogo(final GuiGraphics context, final int x, final int y, final int color) {
        final int logoSizeX = 2279 / 6;
        final int logoSizeY = 278 / 6;
        context.blit(RenderPipelines.GUI_TEXTURED, Assets.LOGO, x, y, logoSizeX, logoSizeY, logoSizeX, logoSizeY, logoSizeX, logoSizeY, logoSizeX, logoSizeY, color);
    }

    public static void drawBlueFade(final GuiGraphics context, final int x, final int y, final int width, final int height) {
        context.fill(x, y, width, height, IzeModImpl.INSTANCE.themeColor().getRGB());
        context.fillGradient(x, y, width, height, WHITE_128, EMPTY);
        context.fillGradient(x, y, width, height, 0, Integer.MIN_VALUE);
    }

    public static void drawGradient(final GuiGraphics context, final int x, final int y, final int width, final int height, final int startColor, final int endColor, final int startAlpha, final int endAlpha) {
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;

        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;

        final int startColorWithAlpha = ARGB.colorFromFloat(startAlpha, f1, f2, f3);
        final int endColorWithAlpha = ARGB.colorFromFloat(endAlpha, f5, f6, f7);
        context.fillGradient(x, y, width, height, startColorWithAlpha, endColorWithAlpha);
    }

    public static void drawShadow(final GuiGraphics context, final int x, final int y, final int width, final int height, final int startColor, final int endColor) {
        // TODO find a replacement
    }

}
