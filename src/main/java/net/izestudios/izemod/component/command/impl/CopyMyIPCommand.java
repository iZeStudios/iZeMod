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

package net.izestudios.izemod.component.command.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.izestudios.izemod.api.command.AbstractCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

public final class CopyMyIPCommand extends AbstractCommand {

    public CopyMyIPCommand() {
        super(Component.translatable("commands.copymyip"), "copymyip");
    }

    @Override
    public void builder(final LiteralArgumentBuilder<SharedSuggestionProvider> builder) {
        builder.executes(commandContext -> {
            try {
                printSuccessMessage(Component.translatable("commands.copymyip.success"));
                Minecraft.getInstance().keyboardHandler.setClipboard(getIP());
                return SUCCESS;
            } catch (final Exception e) {
                return FAILURE;
            }
        });
    }

    private static String getIP() throws Exception {
        URL url;
        try {
            url = new URI("https://checkip.amazonaws.com/").toURL();
        } catch (Exception e) {
            url = new URI("https://ipv4bot.whatismyipaddress.com/").toURL();
        }
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
        return bufferedReader.readLine();
    }

}
