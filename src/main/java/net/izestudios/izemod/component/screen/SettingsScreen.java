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

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public final class SettingsScreen extends Screen {

    public static final SettingsScreen INSTANCE = new SettingsScreen();

    private final boolean[] settingsStates = new boolean[8];
    private final ButtonWidget[] toggleButtons = new ButtonWidget[8];
    private int toggleIndex = 0;

    private SettingsScreen() {
        super(Text.translatable("screens.settings"));
    }

    @Override
    protected void init() {
        toggleIndex = 0;
        int baseY = (int) Math.sqrt(((double) (this.height * this.height) / (1.3 * 1.2)));

        this.addDrawableChild(new TextWidget(this.width / 2 - 100, baseY + (25 * -7), 200, 20,
            Text.translatable("screens.settings.title")
                .styled(style -> style.withColor(Formatting.RED).withBold(true)),
            MinecraftClient.getInstance().textRenderer));

        createToggleButton(0, -6, baseY, "screens.settings.option.fps");
        createToggleButton(0, -5, baseY, "screens.settings.option.coords");
        createToggleButton(0, -4, baseY, "screens.settings.option.biome");
        createToggleButton(0, -3, baseY, "screens.settings.option.date");

        createToggleButton(1, -6, baseY, "screens.settings.option.serverip");
        createToggleButton(1, -5, baseY, "screens.settings.option.ping");
        createToggleButton(1, -4, baseY, "screens.settings.option.players");
        createToggleButton(1, -3, baseY, "screens.settings.option.time");

        createButton(0, -2, baseY, "screens.settings.showAll", b -> {
            for (int i = 0; i < settingsStates.length; i++) {
                settingsStates[i] = true;
                if (toggleButtons[i] != null) {
                    toggleButtons[i].setMessage(getColoredTranslatable(settingKeys[i], true));
                }
            }
        });

        createButton(1, -2, baseY, "screens.settings.hideAll", b -> {
            for (int i = 0; i < settingsStates.length; i++) {
                settingsStates[i] = false;
                if (toggleButtons[i] != null) {
                    toggleButtons[i].setMessage(getColoredTranslatable(settingKeys[i], false));
                }
            }
        });

        this.addDrawableChild(ButtonWidget.builder(Text.translatable("screens.general.back"),
                b -> this.client.setScreen(MainMenuScreen.INSTANCE))
            .dimensions(this.width / 2 - 100, baseY + 10, 200, 20)
            .build());
    }

    private final String[] settingKeys = new String[] {
        "screens.settings.option.fps",
        "screens.settings.option.coords",
        "screens.settings.option.biome",
        "screens.settings.option.date",
        "screens.settings.option.serverip",
        "screens.settings.option.ping",
        "screens.settings.option.players",
        "screens.settings.option.time"
    };

    private void createToggleButton(int column, int row, int baseY, String translationKey) {
        int index = toggleIndex++;
        int x = this.width / 2 - 202 + (column * 204);
        int y = baseY + (25 * row);

        ButtonWidget button = ButtonWidget.builder(getColoredTranslatable(translationKey, settingsStates[index]), b -> {
            settingsStates[index] = !settingsStates[index];
            b.setMessage(getColoredTranslatable(translationKey, settingsStates[index]));
        }).dimensions(x, y, 200, 20).build();

        toggleButtons[index] = button;
        this.addDrawableChild(button);
    }

    private void createButton(int column, int row, int baseY, String translationKey, ButtonWidget.PressAction action) {
        int x = this.width / 2 - 202 + (column * 204);
        int y = baseY + (25 * row);
        this.addDrawableChild(ButtonWidget.builder(Text.translatable(translationKey), action).dimensions(x, y, 200, 20).build());
    }

    private Text getColoredTranslatable(String key, boolean active) {
        return Text.translatable("screens.settings.label",
                Text.translatable(key),
                Text.translatable(active ? "screens.settings.option.show" : "screens.settings.option.hide"))
            .copy()
            .styled(style -> style.withColor(active ? Formatting.GREEN : Formatting.RED));
    }
}
