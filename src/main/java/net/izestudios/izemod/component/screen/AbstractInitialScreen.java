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
import net.izestudios.izemod.util.UpdateUtil;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.CreditsAndAttributionScreen;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.client.resource.language.I18n;
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
    protected void init() {
        super.init();
        setupUpdateNotification();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        final int logoX = (this.width / 2) - (2279 / 12);
        final int logoY = this.height / 20 + 4;
        RenderUtil.drawLogo(context, logoX, logoY, -1);
    }

    private void openWebUrl(final String url) {
        try {
            Util.getOperatingSystem().open(new URI(url));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    protected void setupUpdateNotification() {
        final String version = UpdateUtil.getLatestVersion();
        if (version == null || IzeModImpl.ALPHA_VERSION_TAG.equals(version)) {
            return;
        }

        final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        final Text firstUpdateText = Text.translatable("update.first", version).styled(style -> style.withColor(Formatting.GOLD));
        this.addDrawableChild(new PressableTextWidget(
            this.width / 2 - textRenderer.getWidth(firstUpdateText) / 2,
            this.height - 45,
            200,
            10,
            firstUpdateText,
            button -> this.openWebUrl("https://github.com/iZeStudios/iZeMod/releases/latest"), this.textRenderer
        ));
        final Text secondUpdateText = Text.translatable("update.second").styled(style -> style.withColor(Formatting.GOLD));
        this.addDrawableChild(new PressableTextWidget(
            this.width / 2 - textRenderer.getWidth(secondUpdateText) / 2,
            this.height - 35,
            200,
            10,
            secondUpdateText,
            button -> this.openWebUrl("https://github.com/iZeStudios/iZeMod/releases/latest"), this.textRenderer
        ));
    }

    protected void setupCopyrightTexts() {
        final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        this.addDrawableChild(new PressableTextWidget(
            2,
            this.height - 20,
            200,
            10,
            Text.of("iZeMod " + IzeModImpl.INSTANCE.getVersion() + " (" + IzeModImpl.ALPHA_VERSION_NAME + ")"),
            button -> this.openWebUrl("https://izeplayz.de/izemod"), this.textRenderer
        ));

        this.addDrawableChild(new PressableTextWidget(
            2,
            this.height - 10,
            200,
            10,
            Text.of("by iZePlayz & EnZaXD"),
            button -> this.openWebUrl("https://izeplayz.de/izemod"), this.textRenderer
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

