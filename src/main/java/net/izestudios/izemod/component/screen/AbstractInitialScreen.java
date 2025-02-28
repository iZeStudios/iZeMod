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

import net.izestudios.izemod.IzeModImpl;
import net.izestudios.izemod.util.Constants;
import net.izestudios.izemod.util.RenderUtil;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.CreditsAndAttributionScreen;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import java.net.URI;
import java.net.URISyntaxException;

public abstract class AbstractInitialScreen extends Screen {

    protected AbstractInitialScreen(final Text title) {
        super(title);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        RenderUtil.drawIngameLogo(context, 0.6F);
        final int logoX = (this.width / 2) - (2279 / 12);
        final int logoY = this.height / 20;
        RenderUtil.drawLogo(context, logoX, logoY, -1);
    }

    private void openGitHub() {
        try {
            Util.getOperatingSystem().open(new URI("https://github.com/iZeStudios/iZeMod"));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    protected void setupCopyrightTexts() {
        final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        this.addDrawableChild(new PressableTextWidget(
            2,
            this.height - 20,
            200,
            10,
            Text.of("iZeMod " + IzeModImpl.INSTANCE.getVersion() + " (" + IzeModImpl.ALPHA_VERSION + ")"),
            button -> this.openGitHub(), this.textRenderer
        ));

        this.addDrawableChild(new PressableTextWidget(
            2,
            this.height - 10,
            200,
            10,
            Text.of("by iZePlayz & EnZaXD"),
            button -> this.openGitHub(), this.textRenderer
        ));

        final String minecraftText = "Minecraft " + SharedConstants.getGameVersion().getName();
        this.addDrawableChild(new PressableTextWidget(
            this.width - textRenderer.getWidth(minecraftText) - 2,
            this.height - 20,
            200,
            10,
            Text.of(minecraftText),
            button -> this.client.setScreen(new CreditsAndAttributionScreen(this)), this.textRenderer));

        final Text copyrightText = Text.translatable(Constants.TEXT_COPYRIGHT);
        this.addDrawableChild(new PressableTextWidget(
            this.width - textRenderer.getWidth(copyrightText) - 2,
            this.height - 10,
            200,
            10,
            copyrightText,
            button -> this.client.setScreen(new CreditsAndAttributionScreen(this)), this.textRenderer
        ));
    }

}

