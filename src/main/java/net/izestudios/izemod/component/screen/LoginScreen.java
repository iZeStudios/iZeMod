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

import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public final class LoginScreen extends AbstractInitialScreen {

    public static final LoginScreen INSTANCE = new LoginScreen();
    public static boolean loggedIn = false;

    private LoginScreen() {
        super(Text.of("Login"));
    }

    @Override
    protected void init() {
        super.init();

        final int buttonY = (int) (this.height / Math.sqrt(1.3 * 1.2));
        final int textFieldY = (int) (this.height / 2.5);

        addDrawableChild(ButtonWidget.builder(Text.of("Einloggen"), button -> {
            loggedIn = true;
            client.setScreen(new TitleScreen());
        }).position(this.width / 2 - 102, buttonY).size(101, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.of("Spiel beenden"), button -> {
            client.stop();
        }).position(this.width / 2 + 2, buttonY).size(101, 20).build());

        final TextFieldWidget nameField = addDrawableChild(new TextFieldWidget(
                client.textRenderer, this.width / 2 - 101, textFieldY, 202, 20, Text.empty()));
        final TextFieldWidget passwordField = addDrawableChild(new TextFieldWidget(
                client.textRenderer, this.width / 2 - 101, textFieldY + 30, 202, 20, Text.empty()));

        passwordField.setRenderTextProvider((s, integer) -> Text.of("*".repeat(s.length())).asOrderedText());
        nameField.setText("iZePlayz");
        passwordField.setText("123456");
    }

}

