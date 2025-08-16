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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.CreditsAndAttributionScreen;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import org.jetbrains.annotations.NotNull;
import java.net.URI;
import java.net.URISyntaxException;

public abstract class AbstractInitialScreen extends Screen {

    protected AbstractInitialScreen(final Component title) {
        super(title);
    }

    @Override
    protected void init() {
        super.init();
        setupUpdateNotification();
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        final int logoX = (this.width / 2) - (2279 / 12);
        final int logoY = this.height / 20 + 4;
        RenderUtil.drawLogo(guiGraphics, logoX, logoY, -1);
    }

    private void openWebUrl(final String url) {
        try {
            Util.getPlatform().openUri(new URI(url));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    protected void setupUpdateNotification() {
        final String version = UpdateUtil.getLatestVersion();
        if (version == null || IzeModImpl.ALPHA_VERSION_TAG.equals(version)) {
            return;
        }

        final Component firstUpdateText = Component.translatable("update.first", version).withStyle(style -> style.withColor(ChatFormatting.GOLD));
        this.addRenderableWidget(new PlainTextButton(
            this.width / 2 - font.width(firstUpdateText) / 2,
            this.height - 45,
            200,
            10,
            firstUpdateText,
            button -> this.openWebUrl("https://github.com/iZeStudios/iZeMod/releases/latest"), this.font
        ));
        final Component secondUpdateText = Component.translatable("update.second").withStyle(style -> style.withColor(ChatFormatting.GOLD));
        this.addRenderableWidget(new PlainTextButton(
            this.width / 2 - font.width(secondUpdateText) / 2,
            this.height - 35,
            200,
            10,
            secondUpdateText,
            button -> this.openWebUrl("https://github.com/iZeStudios/iZeMod/releases/latest"), this.font
        ));
    }

    protected void setupCopyrightTexts() {
        this.addRenderableWidget(new PlainTextButton(
            2,
            this.height - 20,
            200,
            10,
            Component.nullToEmpty("iZeMod " + IzeModImpl.INSTANCE.version() + " (" + IzeModImpl.ALPHA_VERSION_NAME + ")"),
            button -> this.openWebUrl("https://izeplayz.de/izemod"), this.font
        ));

        this.addRenderableWidget(new PlainTextButton(
            2,
            this.height - 10,
            200,
            10,
            Component.nullToEmpty("by iZePlayz & EnZaXD"),
            button -> this.openWebUrl("https://izeplayz.de/izemod"), this.font
        ));

        final String minecraftText = "Minecraft " + SharedConstants.getCurrentVersion().name();
        this.addRenderableWidget(new PlainTextButton(
            this.width - font.width(minecraftText) - 2,
            this.height - 20,
            200,
            10,
            Component.nullToEmpty(minecraftText),
            button -> this.minecraft.setScreen(new CreditsAndAttributionScreen(this)), this.font));

        final Component copyrightText = Component.translatable(Constants.TEXT_COPYRIGHT);
        this.addRenderableWidget(new PlainTextButton(
            this.width - font.width(copyrightText) - 2,
            this.height - 10,
            200,
            10,
            copyrightText,
            button -> this.minecraft.setScreen(new CreditsAndAttributionScreen(this)), this.font
        ));
    }

}

