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

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.izestudios.izemod.api.command.AbstractCommand;
import net.izestudios.izemod.util.Fetcher;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

public final class GetIPCommand extends AbstractCommand {

    public GetIPCommand() {
        super(Component.translatable("commands.getip"), "getip");
    }

    @Override
    public void builder(final LiteralArgumentBuilder<SharedSuggestionProvider> builder) {
        builder.then(argument("domain", StringArgumentType.string()).executes(commandContext -> {
            final String domain = StringArgumentType.getString(commandContext, "domain");

            return sendIP(domain);
        })).executes(commandContext -> {
            final Minecraft client = Minecraft.getInstance();

            if (!client.isSingleplayer()) {
                final String domain = client.getCurrentServer().ip;
                return sendIP(domain);
            } else {
                printErrorMessage(Component.translatable("commands.getip.singleplayer"));
                return FAILURE;
            }
        });
    }

    private int sendIP(final String domain) {
        final String fetched = Fetcher.fetchDomain2IP(domain);

        if (fetched != null) {
            Minecraft.getInstance().keyboardHandler.setClipboard(fetched);
            printSuccessMessage(Component.translatable("commands.getip.success"));
            return SUCCESS;
        } else {
            printErrorMessage(Component.translatable("commands.getip.invalid"));
            return FAILURE;
        }
    }

}
