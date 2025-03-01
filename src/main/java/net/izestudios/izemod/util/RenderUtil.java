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
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.ColorHelper;

public final class RenderUtil {

    public static void drawScaledLogo(final DrawContext context, final float opacity) {
        final MatrixStack matrices = context.getMatrices();
        matrices.push();
        matrices.scale(0.25F, 0.25F, 0.25F);
        drawLogo(context, 4, 4, ColorHelper.getWhite(opacity));
        matrices.pop();
    }

    public static void drawLogo(final DrawContext context, final int x, final int y, final int color) {
        final int logoSizeX = 2279 / 6;
        final int logoSizeY = 278 / 6;
        context.drawTexture(RenderLayer::getGuiTexturedOverlay, Assets.LOGO, x, y, logoSizeX, logoSizeY, logoSizeX, logoSizeY, logoSizeX, logoSizeY, logoSizeX, logoSizeY, color);
    }

    public static void drawBlueFade(final DrawContext context, final int x, final int y, final int width, final int height) {
        context.fill(x, y, width, height, IzeModImpl.INSTANCE.getThemeColor().getRGB());
        context.fillGradient(x, y, width, height, -2130706433, 16777215);
        context.fillGradient(x, y, width, height, 0, Integer.MIN_VALUE);
    }

}
