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

import net.fabricmc.loader.api.FabricLoader;
import net.izestudios.izemod.addon.AddonManager;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerWarningScreen;
import net.minecraft.client.gui.screen.option.LanguageOptionsScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.ServerList;
import net.minecraft.client.realms.gui.screen.RealmsMainScreen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;

import static net.izestudios.izemod.util.Constants.*;

public final class MainMenuScreen extends AbstractInitialScreen {

    public static final MainMenuScreen INSTANCE = new MainMenuScreen();

    private MainMenuScreen() {
        super(Text.translatable("screen.title"));
    }

    @Override
    protected void init() {
        super.init();
        setupCopyrightTexts();

        final int addons = AddonManager.INSTANCE.count();
        final int worldCount = client.getLevelStorage().getLevelList().levels().size();
        final int serverCount = new ServerList(client).size();
        final int mods = FabricLoader.getInstance().getAllMods().size() - addons;

        int baseY = (int) Math.sqrt((double) (this.height * this.height) / (1.3 * 1.2));
        int leftX = this.width / 2 - 202;
        int rightX = this.width / 2 + 2;

        final String worldText = I18n.translate("screens.title.world");
        final String worldsText = I18n.translate("screens.title.worlds");
        final String serverText = I18n.translate("screens.title.server");

        final String programText = I18n.translate("screens.title.program");
        final String optionsText = I18n.translate("screens.title.options");
        final String modsText = I18n.translate("screens.title.mods", mods);
        final String addonsText = I18n.translate("screens.title.addons", addons);
        addMainMenuButton(leftX, baseY, -5, TEXT_SINGLEPLAYER, worldCount + " " + (worldCount == 1 ? worldText : worldsText), () -> this.client.setScreen(new SelectWorldScreen(this)));
        addMainMenuButton(leftX, baseY, -4, TEXT_MULTIPLAYER, serverCount + " " + serverText, () -> this.client.setScreen(this.client.options.skipMultiplayerWarning ? new MultiplayerScreen(this) : new MultiplayerWarningScreen(this)));
        addMainMenuButton(leftX, baseY, -3, TEXT_ONLINE, null, () -> this.client.setScreen(new RealmsMainScreen(this)));
        addMainMenuButton(leftX, baseY, -2, OPTIONS_LANGUAGE, null, () -> this.client.setScreen(new LanguageOptionsScreen(this,this.client.options, this.client.getLanguageManager())));
        addMainMenuButton(leftX, baseY, -1, TEXT_OPTIONS, null, () -> this.client.setScreen(new OptionsScreen(this, this.client.options)));

        addMainMenuButton(rightX, baseY, -5, optionsText, null, () -> this.client.setScreen(SettingsScreen.INSTANCE));
        addMainMenuButton(rightX, baseY, -4, programText, null, () -> this.client.setScreen(DebugScreen.INSTANCE));
        addMainMenuButton(rightX, baseY, -3, addonsText, null, () -> this.client.setScreen(DebugScreen.INSTANCE));
        addMainMenuButton(rightX, baseY, -2, modsText, null, () -> this.client.setScreen(DebugScreen.INSTANCE));
        addMainMenuButton(rightX, baseY, -1, TEXT_QUIT, null, client::scheduleStop);
    }

    private void addMainMenuButton(int x, int baseY, int offset, String key, String extraText, Runnable action) {
        final String buttonText = I18n.translate(key) + (extraText != null ? " (" + extraText + ")" : "");
        addDrawableChild(ButtonWidget.builder(Text.of(buttonText), button -> action.run()).position(x, baseY + (25 * offset)).size(200, 20).build());
    }

}

