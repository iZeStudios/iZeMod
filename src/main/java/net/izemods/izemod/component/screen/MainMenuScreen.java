/*
 * This file is part of iZeMod - https://github.com/iZeMods/iZeMod
 * Copyright (C) 2014-2025 the original authors
 *                         - FlorianMichael/EnZaXD <florian.michael07@gmail.com>
 *                         - iZePlayzYT
 * Copyright (C) 2025 GitHub contributors
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

package net.izemods.izemod.component.screen;

import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerWarningScreen;
import net.minecraft.client.gui.screen.option.LanguageOptionsScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.ServerList;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.text.Text;

public final class MainMenuScreen extends AbstractInitialScreen {

    public static final MainMenuScreen INSTANCE = new MainMenuScreen();

    private MainMenuScreen() {
        super(Text.of("Login"));
    }

    @Override
    protected void init() {
        super.init();

        int worldCount = client.getLevelStorage().getLevelList().levels().size();
        int serverCount = new ServerList(client).size();

        int baseY = (int) Math.sqrt((double) (this.height * this.height) / (1.3 * 1.2));
        int leftX = this.width / 2 - 202;
        int rightX = this.width / 2 + 2;

        addMainMenuButton(leftX, baseY, -5, "menu.singleplayer", worldCount + " " + (worldCount == 1 ? "Welt" : "Welten"), () -> this.client.setScreen(new SelectWorldScreen(this)));
        addMainMenuButton(leftX, baseY, -4, "menu.multiplayer", serverCount + " Server", () -> this.client.setScreen(this.client.options.skipMultiplayerWarning ? new MultiplayerScreen(this) : new MultiplayerWarningScreen(this)));
        addMainMenuButton(leftX, baseY, -3, "menu.options", null, () -> this.client.setScreen(new OptionsScreen(this, this.client.options)));
        addMainMenuButton(leftX, baseY, -2, "options.resourcepack", null, () -> this.client.setScreen(new PackScreen(this.client.getResourcePackManager(), this::refreshResourcePacks, this.client.getResourcePackDir(), Text.translatable("resourcePack.title"))));
        addMainMenuButton(leftX, baseY, -1, "options.language", null, () -> this.client.setScreen(new LanguageOptionsScreen(this,this.client.options, this.client.getLanguageManager())));
        addMainMenuButton(leftX, baseY, 0, "menu.quit", null, client::scheduleStop);

        addMainMenuButton(rightX, baseY, -5, "iZeMod Programme", null, () -> {});
        addMainMenuButton(rightX, baseY, -4, "iZeMod Optionen", null, () -> {});
        addMainMenuButton(rightX, baseY, -3, "iZeMod Profil", null, () -> {});
        addMainMenuButton(rightX, baseY, -2, "iZeMod Addons (0 Addons)", null, () -> {});
        addMainMenuButton(rightX, baseY, -1, "iZeMod Changelog", null, () -> {});
        addMainMenuButton(rightX, baseY, 0, ".minecraft Ordner", null, () -> {});
    }

    private void refreshResourcePacks(ResourcePackManager resourcePackManager) {
        this.client.options.refreshResourcePacks(resourcePackManager);
        this.client.setScreen(this);
    }

    private void addMainMenuButton(int x, int baseY, int offset, String key, String extraText, Runnable action) {
        final String buttonText = I18n.translate(key) + (extraText != null ? " (" + extraText + ")" : "");
        addDrawableChild(ButtonWidget.builder(Text.of(buttonText), button -> action.run()).position(x, baseY + (25 * offset)).size(200, 20).build());
    }

}

