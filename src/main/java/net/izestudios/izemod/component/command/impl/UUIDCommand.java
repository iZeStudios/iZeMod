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
import java.util.UUID;
import net.izestudios.izemod.api.command.AbstractCommand;
import net.izestudios.izemod.util.MojangAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

public class UUIDCommand extends AbstractCommand {

    public UUIDCommand() {
        super(Component.translatable("commands.uuid"), "uuid");
    }

    @Override
    public void builder(final LiteralArgumentBuilder<SharedSuggestionProvider> builder) {
        builder.then(argument("name", StringArgumentType.string()).executes(commandContext -> {
            final String name = StringArgumentType.getString(commandContext, "name");
            final String uuid = MojangAPI.fetchUUID(name);
            if (uuid == null) {
                printErrorMessage(Component.translatable("commands.uuid.invalid"));
                return FAILURE;
            }

            Minecraft.getInstance().keyboardHandler.setClipboard(uuid);
            printSuccessMessage(Component.translatable("commands.uuid.success"));
            return SUCCESS;
        })).executes(commandContext -> {
            final UUID uuid = Minecraft.getInstance().getUser().getProfileId();
            if (uuid == null) {
                printErrorMessage(Component.translatable("commands.uuid.invalid"));
                return FAILURE;
            }

            Minecraft.getInstance().keyboardHandler.setClipboard(uuid.toString());
            printSuccessMessage(Component.translatable("commands.uuid.success"));
            return SUCCESS;
        });
    }

}
