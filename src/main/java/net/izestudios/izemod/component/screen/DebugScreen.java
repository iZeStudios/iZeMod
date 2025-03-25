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

public final class DebugScreen extends Screen {

    public static final DebugScreen INSTANCE = new DebugScreen();

    private DebugScreen() {
        super(Text.translatable("screens.debug"));
    }

    @Override
    protected void init() {
        int baseY = (int) Math.sqrt(((double) (this.height * this.height) / (1.3 * 1.2)));

        this.addDrawableChild(new TextWidget(this.width / 2 - 100, baseY + (25 * -7), 200, 20,
            Text.translatable("screens.debug.title")
                .styled(style -> style.withColor(Formatting.RED).withBold(true)),
            MinecraftClient.getInstance().textRenderer));

        this.createButton(0, -6, baseY, "screens.debug.toLoginScreen", b -> client.setScreen(LoginScreen.INSTANCE));
        this.createButton(0, -5, baseY, "screens.general.unusedTodo", b -> b.active = false);
        this.createButton(0, -4, baseY, "screens.general.unusedTodo", b -> b.active = false);
        this.createButton(0, -3, baseY, "screens.general.unusedTodo", b -> b.active = false);
        this.createButton(0, -2, baseY, "screens.general.unusedTodo", b -> b.active = false);
        this.createButton(0, -1, baseY, "screens.general.unusedTodo", b -> b.active = false);

        this.createButton(1, -6, baseY, "screens.debug.tetris", b -> client.setScreen(TetrisScreen.INSTANCE));
        this.createButton(1, -5, baseY, "screens.general.unusedTodo", b -> b.active = false);
        this.createButton(1, -4, baseY, "screens.general.unusedTodo", b -> b.active = false);
        this.createButton(1, -3, baseY, "screens.general.unusedTodo", b -> b.active = false);
        this.createButton(1, -2, baseY, "screens.general.unusedTodo", b -> b.active = false);
        this.createButton(1, -1, baseY, "screens.general.unusedTodo", b -> b.active = false);

        this.addDrawableChild(ButtonWidget.builder(Text.translatable("screens.general.back"),
                b -> this.client.setScreen(MainMenuScreen.INSTANCE))
            .dimensions(this.width / 2 - 100, baseY + 10, 200, 20)
            .build());
    }

    private void createButton(int column, int row, int baseY, String textKey, ButtonWidget.PressAction action) {
        int x = this.width / 2 - 202 + (column * 204);
        int y = baseY + (25 * row);
        this.addDrawableChild(ButtonWidget.builder(Text.translatable(textKey), action).dimensions(x, y, 200, 20).build());
    }
}
