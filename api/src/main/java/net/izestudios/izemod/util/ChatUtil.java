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

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import java.awt.*;

public class ChatUtil {

    public static MutableText CHAT_PREFIX = Text.literal("[").styled(style -> style.withColor(Formatting.DARK_AQUA))
        .append(Text.literal("iZeMod").styled(style -> style.withColor(Formatting.AQUA)))
        .append(Text.literal("] ").styled(style -> style.withColor(Formatting.DARK_AQUA)));

    public static void printPrefixedMessage(final String message) {
        printPrefixedChatMessage(Text.literal(message), null, null);
    }

    public static void printPrefixedMessage(final Text message) {
        printPrefixedChatMessage(message, null, null);
    }

    public static void printSuccessMessage(final String message) {
        printSuccessMessage(Text.literal(message));
    }

    public static void printSuccessMessage(final Text message) {
        printSuccessMessage(message, null, null);
    }

    public static void printSuccessMessage(final String message, final String tooltip) {
        printSuccessMessage(Text.of(message), tooltip);
    }

    public static void printSuccessMessage(final String message, final String tooltip, final String suggestion) {
        printSuccessMessage(Text.literal(message), tooltip, suggestion);
    }

    public static void printSuccessMessage(final Text message, final String tooltip) {
        printSuccessMessage(message, tooltip, null);
    }

    public static void printSuccessMessage(final Text message, final String tooltip, final String suggestion) {
        printPrefixedChatMessage(message.copy().withColor(Formatting.GREEN.getColorValue()), tooltip, suggestion);
    }

    public static void printErrorMessage(final String message) {
        printErrorMessage(Text.literal(message));
    }

    public static void printErrorMessage(final Text message) {
        printPrefixedChatMessage(message.copy().withColor(Formatting.RED.getColorValue()), null, null);
    }

    public static void printPrefixedChatMessage(final String message) {
        printPrefixedChatMessage(message, null, null);
    }

    public static void printPrefixedChatMessage(final String message, final String tooltip, final String suggestion) {
        printPrefixedChatMessage(Text.literal(message), tooltip, suggestion);
    }

    public static void printPrefixedChatMessage(final Text message) {
        printPrefixedChatMessage(message, null, null);
    }

    public static void printPrefixedChatMessage(final Text message, final String tooltip, final String suggestion) {
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(CHAT_PREFIX.copy().append(message).styled(style -> {
            if (tooltip != null) {
                style = style.withHoverEvent(new HoverEvent.ShowText(Text.literal(tooltip)));
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
        printChatMessage(Text.literal(message));
    }

    public static void printChatMessage(final Text message) {
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(message);
    }

    public static Text colored(final String text, final Formatting color) {
        return colored(Text.literal(text), color);
    }

    public static Text colored(final Text text, final Formatting color) {
        return text.copy().styled(style -> style.withColor(color));
    }

    public static Text colored(final Text text, final Color color) {
        return colored(text, color.getRGB());
    }

    public static Text colored(final Text text, final int color) {
        return text.copy().styled(style -> style.withColor(TextColor.fromRgb(color)));
    }

    public static void sendChatMessage(final String message) {
        final ClientPlayNetworkHandler handler = MinecraftClient.getInstance().getNetworkHandler();
        if (handler == null) {
            throw new IllegalStateException("ChatUtils#sendChatMessage called before the client was connected to a server");
        }

        if (message.startsWith("/")) {
            handler.sendChatCommand(message);
        } else {
            handler.sendChatMessage(message);
        }
    }

}
