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

import net.izestudios.izemod.util.Constants;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;

public final class LoginScreen extends AbstractInitialScreen {

    public static final LoginScreen INSTANCE = new LoginScreen();
    public static boolean loggedIn = false;

    private EditBox passwordField;

    private LoginScreen() {
        super(Component.translatable("screen.login"));
    }

    @Override
    protected void init() {
        super.init();

        this.setupCopyrightTexts();

        final int textFieldY = (int) (this.height / 2.5);

        addRenderableWidget(
            Button
                .builder(Component.translatable("screens.login.login"), button -> {
                    loggedIn = true;
                    if ("tetris".equals(passwordField.getValue())) {
                        minecraft.setScreen(TetrisScreen.INSTANCE);
                    } else {
                        minecraft.setScreen(new TitleScreen());
                    }
                })
                .pos(this.width / 2 - 102, textFieldY + 60)
                .size(101, 20)
                .build()
        );

        addRenderableWidget(
            Button
                .builder(Component.translatable(Constants.TEXT_QUIT), button -> {
                    minecraft.stop();
                })
                .pos(this.width / 2 + 2, textFieldY + 60)
                .size(101, 20)
                .build()
        );

        final EditBox nameField = addRenderableWidget(new EditBox(
            font,
            this.width / 2 - 102,
            textFieldY,
            205,
            20,
            Component.empty()
        ));
        nameField.setValue("iZePlayz");

        passwordField = addRenderableWidget(new EditBox(
            font,
            this.width / 2 - 102,
            textFieldY + 30,
            205,
            20,
            Component.empty()
        ));
        passwordField.addFormatter(
            (text, index) -> Component.nullToEmpty("*".repeat(text.length())).getVisualOrderText()
        );
        passwordField.setValue("123456");
    }

}
