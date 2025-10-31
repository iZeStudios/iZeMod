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

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class DebugScreen extends Screen {

    public static final DebugScreen INSTANCE = new DebugScreen();
    private final Component title = Component.translatable("screens.debug.title").withStyle(style -> style.withColor(ChatFormatting.RED).withBold(true));

    private DebugScreen() {
        super(Component.translatable("screens.debug"));
    }

    @Override
    protected void init() {
        final int baseY = (int) Math.sqrt(((double) (this.height * this.height) / (1.3 * 1.2)));

        this.addRenderableWidget(new StringWidget(this.width / 2 - (minecraft.font.width(title) / 2), baseY + (25 * -7), 200, 20, title, minecraft.font));

        this.createButton(0, -6, baseY, "screens.debug.toLoginScreen", b -> minecraft.setScreen(LoginScreen.INSTANCE));
        this.createButton(0, -5, baseY, "screens.debug.unusedTodo", null);
        this.createButton(0, -4, baseY, "screens.debug.unusedTodo", null);
        this.createButton(0, -3, baseY, "screens.debug.unusedTodo", null);
        this.createButton(0, -2, baseY, "screens.debug.unusedTodo", null);
        this.createButton(0, -1, baseY, "screens.debug.unusedTodo", null);

        this.createButton(1, -6, baseY, "screens.debug.tetris", b -> minecraft.setScreen(TetrisScreen.INSTANCE));
        this.createButton(1, -5, baseY, "screens.debug.unusedTodo", null);
        this.createButton(1, -4, baseY, "screens.debug.unusedTodo", null);
        this.createButton(1, -3, baseY, "screens.debug.unusedTodo", null);
        this.createButton(1, -2, baseY, "screens.debug.unusedTodo", null);
        this.createButton(1, -1, baseY, "screens.debug.unusedTodo", null);

        this.addRenderableWidget(Button.builder(Component.translatable("screens.debug.back"), b -> this.minecraft.setScreen(MainMenuScreen.INSTANCE)).bounds(this.width / 2 - 100, baseY + 10, 200, 20).build());
    }

    private void createButton(final int column, final int row, final int baseY, final String textKey, final Button.OnPress action) {
        int x = this.width / 2 - 202 + (column * 204);
        int y = baseY + (25 * row);
        final Button button = this.addRenderableWidget(Button.builder(Component.translatable(textKey), action == null ? e -> {
        } : action).bounds(x, y, 200, 20).build());
        if (action == null) {
            button.active = false;
        }
    }

}
