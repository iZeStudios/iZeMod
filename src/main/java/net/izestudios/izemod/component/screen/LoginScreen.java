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
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public final class LoginScreen extends AbstractInitialScreen {

    public static final LoginScreen INSTANCE = new LoginScreen();
    public static boolean loggedIn = false;

    private TextFieldWidget passwordField;

    private LoginScreen() {
        super(Text.translatable("screen.login"));
    }

    @Override
    protected void init() {
        super.init();

        this.setupCopyrightTexts();

        final int textFieldY = (int) (this.height / 2.5);

        addDrawableChild(
            ButtonWidget
                .builder(Text.translatable("screens.login.login"), button -> {
                    loggedIn = true;
                    if ("tetris".equals(passwordField.getText())) {
                        client.setScreen(TetrisScreen.INSTANCE);
                    } else {
                        client.setScreen(new TitleScreen());
                    }
                })
                .position(this.width / 2 - 102, textFieldY + 60)
                .size(101, 20)
                .build()
        );

        addDrawableChild(
            ButtonWidget
                .builder(Text.translatable(Constants.TEXT_QUIT), button -> {
                    client.stop();
                })
                .position(this.width / 2 + 2, textFieldY + 60)
                .size(101, 20)
                .build()
        );

        final TextFieldWidget nameField = addDrawableChild(new TextFieldWidget(
            client.textRenderer,
            this.width / 2 - 102,
            textFieldY,
            205,
            20,
            Text.empty()
        ));
        nameField.setText("iZePlayz");

        passwordField = addDrawableChild(new TextFieldWidget(
            client.textRenderer,
            this.width / 2 - 102,
            textFieldY + 30,
            205,
            20,
            Text.empty()
        ));
        passwordField.setRenderTextProvider(
            (text, index) -> Text.of("*".repeat(text.length())).asOrderedText()
        );
        passwordField.setText("123456");
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }

}
