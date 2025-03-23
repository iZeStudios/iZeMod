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

import com.mojang.blaze3d.systems.RenderSystem;
import net.izestudios.izemod.IzeModImpl;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.ColorHelper;

import static net.izestudios.izemod.component.theme.ColorTheme.EMPTY;
import static net.izestudios.izemod.component.theme.ColorTheme.WHITE_128;

public final class RenderUtil {

    public static void drawScaledLogo(final DrawContext context, final float opacity) {
        final MatrixStack matrices = context.getMatrices();
        matrices.push();
        matrices.scale(0.25F, 0.25F, 0.25F);
        drawLogo(context, 6, 6, ColorHelper.getWhite(opacity));
        matrices.pop();
    }

    public static void drawLogo(final DrawContext context, final int x, final int y, final int color) {
        final int logoSizeX = 2279 / 6;
        final int logoSizeY = 278 / 6;
        context.drawTexture(RenderLayer::getGuiTexturedOverlay, Assets.LOGO, x, y, logoSizeX, logoSizeY, logoSizeX, logoSizeY, logoSizeX, logoSizeY, logoSizeX, logoSizeY, color);
    }

    public static void drawBlueFade(final DrawContext context, final int x, final int y, final int width, final int height) {
        context.fill(x, y, width, height, IzeModImpl.INSTANCE.themeColor().getRGB());
        context.fillGradient(x, y, width, height, WHITE_128, EMPTY);
        context.fillGradient(x, y, width, height, 0, Integer.MIN_VALUE);
    }

    public static void drawGradient(final int x, final int y, final int width, final int height, final int startColor, final int endColor, final int startAlpha, final int endAlpha) {
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;

        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
        final BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        buffer.vertex(width, y, 0).color(f1, f2, f3, startAlpha/255F);
        buffer.vertex(x, y, 0).color(f1, f2, f3, startAlpha/255F);
        buffer.vertex(x, height, 0).color(f5, f6, f7, endAlpha/255F);
        buffer.vertex(width, height, 0).color(f5, f6, f7, endAlpha/255F);
        BufferRenderer.drawWithGlobalProgram(buffer.end());
        RenderSystem.disableBlend();
    }

    public static void drawShadow(final int x, final int y, final int width, final int height, final int startColor, final int endColor) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
        final BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        buffer.vertex(x, height, 0F).color(startColor);
        buffer.vertex(width, height, 0F).color(startColor);
        buffer.vertex(width, y, 0F).color(endColor);
        buffer.vertex(x, y, 0F).color(endColor);
        BufferRenderer.drawWithGlobalProgram(buffer.end());
        RenderSystem.disableBlend();
    }

}
