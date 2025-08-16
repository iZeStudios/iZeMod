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
import java.util.Optional;
import net.izestudios.izemod.api.command.AbstractCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.resolver.ResolvedServerAddress;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.client.multiplayer.resolver.ServerNameResolver;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

public final class GetIPCommand extends AbstractCommand {

    public GetIPCommand() {
        super(Component.translatable("commands.getip"), "getip");
    }

    @Override
    public void builder(final LiteralArgumentBuilder<SharedSuggestionProvider> builder) {
        builder.then(argument("address", StringArgumentType.string()).executes(commandContext -> {
            final String address = StringArgumentType.getString(commandContext, "address");

            return sendIP(address);
        })).executes(commandContext -> {
            if (Minecraft.getInstance().isSingleplayer()) {
                printErrorMessage(Component.translatable("commands.getip.singleplayer"));
                return FAILURE;
            }

            final String address = Minecraft.getInstance().getCurrentServer().ip;
            return sendIP(address);
        });
    }

    private int sendIP(final String address) {
        Optional<ResolvedServerAddress> serverAddress = ServerNameResolver.DEFAULT.resolveAddress(ServerAddress.parseString(address));
        if (serverAddress.isEmpty()) {
            printErrorMessage(Component.translatable("commands.getip.invalid"));
            return FAILURE;
        }

        final ResolvedServerAddress resolvedAddress = serverAddress.get();
        Minecraft.getInstance().keyboardHandler.setClipboard(resolvedAddress.getHostIp());
        printSuccessMessage(Component.translatable("commands.getip.success"));
        return SUCCESS;
    }

}
