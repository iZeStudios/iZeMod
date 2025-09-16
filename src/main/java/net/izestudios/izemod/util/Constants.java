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

package net.izestudios.izemod.util;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;

/**
 * Used to move buttons or to add existing buttons to custom screens. Make sure to keep this up-to-date with the latest Minecraft version.
 */
public final class Constants {

    // Title
    public static final String TEXT_SINGLEPLAYER = "menu.singleplayer";
    public static final String TEXT_MULTIPLAYER = "menu.multiplayer";
    public static final String TEXT_ONLINE = "menu.online";
    public static final String TEXT_OPTIONS = "menu.options";
    public static final String TEXT_QUIT = "menu.quit";
    public static final String TEXT_COPYRIGHT = "title.credits";

    // Multiplayer
    public static final String TEXT_SELECT_SERVER = "selectServer.select";
    public static final String TEXT_DIRECT_CONNECT = "selectServer.direct";
    public static final String TEXT_ADD_SERVER = "selectServer.add";
    public static final String TEXT_EDIT_SERVER = "selectServer.edit";
    public static final String TEXT_DELETE_SERVER = "selectServer.delete";
    public static final String TEXT_REFRESH = "selectServer.refresh";

    // Options
    public static final String OPTIONS_LANGUAGE = "options.language";

    // CommandManager#checkCommand
    public static void checkCommand(final CommandSyntaxException event, final String message) {
        ChatUtil.printErrorMessage(ComponentUtils.fromMessage(event.getRawMessage()));
        if (event.getCursor() < 0) {
            return;
        }

        final int i = Math.max(event.getInput().length(), event.getCursor());
        final MutableComponent text = Component.empty().withStyle(ChatFormatting.GRAY).withStyle(style -> style.withClickEvent(new ClickEvent.SuggestCommand(message)));
        if (i > 10) {
            text.append(CommonComponents.ELLIPSIS);
        }
        text.append(event.getInput().substring(Math.max(0, i - 10), i));
        if (i < event.getInput().length()) {
            text.append(Component.literal(event.getInput().substring(i)).withStyle(ChatFormatting.RED, ChatFormatting.UNDERLINE));
        }
        text.append(Component.translatable("command.context.here").withStyle(ChatFormatting.RED, ChatFormatting.UNDERLINE));
        ChatUtil.printErrorMessage(text);
    }

}
