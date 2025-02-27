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

import net.izestudios.izemod.ModImpl;
import net.izestudios.izemod.util.Constants;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public abstract class AbstractInitialScreen extends Screen {

    protected AbstractInitialScreen(final Text title) {
        super(title);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        final int logoX = (this.width / 2) - (2279 / 12);
        final int logoY = this.height / 20;
        final int logoSizeX = 2279 / 6;
        final int logoSizeY = 278 / 6;

        context.drawTexture(RenderLayer::getGuiTextured, Constants.LOGO, logoX, logoY, logoSizeX, logoSizeY, logoSizeX, logoSizeY, logoSizeX, logoSizeY, logoSizeX, logoSizeY);

        final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        final int gray = Formatting.GRAY.getColorValue();

        context.drawText(textRenderer, "Minecraft " + SharedConstants.getGameVersion().getName(), 3, this.height - 22, gray, true);
        context.drawText(textRenderer, "iZeMod " + ModImpl.INSTANCE.getVersion() + " by iZePlayz & EnZaXD", 3, this.height - 11, gray, true);

        final String copyrightText = "Copyright Mojang AB. Do not distribute!";
        context.drawText(textRenderer, copyrightText, this.width - textRenderer.getWidth(copyrightText) - 2, this.height - 11, gray, true);
    }

}

