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

import java.awt.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;

public class ChatUtil {

    public static MutableComponent CHAT_PREFIX = Component.literal("[").withStyle(style -> style.withColor(ChatFormatting.DARK_AQUA))
        .append(Component.literal("iZeMod").withStyle(style -> style.withColor(ChatFormatting.AQUA)))
        .append(Component.literal("] ").withStyle(style -> style.withColor(ChatFormatting.DARK_AQUA)));

    public static void printPrefixedMessage(final String message) {
        printPrefixedChatMessage(Component.literal(message), null, null);
    }

    public static void printPrefixedMessage(final Component message) {
        printPrefixedChatMessage(message, null, null);
    }

    public static void printSuccessMessage(final String message) {
        printSuccessMessage(Component.literal(message));
    }

    public static void printSuccessMessage(final Component message) {
        printSuccessMessage(message, null, null);
    }

    public static void printSuccessMessage(final String message, final String tooltip) {
        printSuccessMessage(Component.literal(message), tooltip);
    }

    public static void printSuccessMessage(final String message, final String tooltip, final String suggestion) {
        printSuccessMessage(Component.literal(message), tooltip, suggestion);
    }

    public static void printSuccessMessage(final Component message, final String tooltip) {
        printSuccessMessage(message, tooltip, null);
    }

    public static void printSuccessMessage(final Component message, final String tooltip, final String suggestion) {
        printPrefixedChatMessage(message.copy().withColor(ChatFormatting.GREEN.getColor()), tooltip, suggestion);
    }

    public static void printErrorMessage(final String message) {
        printErrorMessage(Component.literal(message));
    }

    public static void printErrorMessage(final Component message) {
        printPrefixedChatMessage(message.copy().withColor(ChatFormatting.RED.getColor()), null, null);
    }

    public static void printPrefixedChatMessage(final String message) {
        printPrefixedChatMessage(message, null, null);
    }

    public static void printPrefixedChatMessage(final String message, final String tooltip, final String suggestion) {
        printPrefixedChatMessage(Component.literal(message), tooltip, suggestion);
    }

    public static void printPrefixedChatMessage(final Component message) {
        printPrefixedChatMessage(message, null, null);
    }

    public static void printPrefixedChatMessage(final Component message, final String tooltip, final String suggestion) {
        Minecraft.getInstance().gui.getChat().addMessage(CHAT_PREFIX.copy().append(message).withStyle(style -> {
            if (tooltip != null) {
                style = style.withHoverEvent(new HoverEvent.ShowText(Component.literal(tooltip)));
            }
            if (suggestion != null) {
                style = style.withClickEvent(new ClickEvent.SuggestCommand(suggestion));
            }
            return style;
        }));
    }

    public static void printEmptyLine() {
        printChatMessage("");
    }

    public static void printChatMessage(final String message) {
        printChatMessage(Component.literal(message));
    }

    public static void printChatMessage(final Component message) {
        Minecraft.getInstance().gui.getChat().addMessage(message);
    }

    public static Component colored(final String text, final ChatFormatting color) {
        return colored(Component.literal(text), color);
    }

    public static Component colored(final Component text, final ChatFormatting color) {
        return text.copy().withStyle(style -> style.withColor(color));
    }

    public static Component colored(final Component text, final Color color) {
        return colored(text, color.getRGB());
    }

    public static Component colored(final Component text, final int color) {
        return text.copy().withStyle(style -> style.withColor(TextColor.fromRgb(color)));
    }

    public static void sendChatMessage(final String message) {
        final ClientPacketListener listener = Minecraft.getInstance().getConnection();
        if (listener == null) {
            throw new IllegalStateException("ChatUtils#sendChatMessage called before the client was connected to a server");
        }

        if (message.startsWith("/")) {
            listener.sendCommand(message);
        } else {
            listener.sendChat(message);
        }
    }

}
