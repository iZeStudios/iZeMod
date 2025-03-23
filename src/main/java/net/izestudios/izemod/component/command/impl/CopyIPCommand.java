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
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;

public final class CopyIPCommand extends AbstractCommand {

    public CopyIPCommand() {
        super(Text.translatable("commands.copyip"), "copyip");
    }

    @Override
    public void builder(final LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(commandContext -> {
            final MinecraftClient client = MinecraftClient.getInstance();
            if (client.isInSingleplayer()) {
                printErrorMessage(Text.translatable("commands.copyip.singleplayer"));
                return FAILURE;
            }

            final String address = client.getCurrentServerEntry().address;
            client.keyboard.setClipboard(address);
            printSuccessMessage(Text.translatable("commands.copyip.success"));
            return SUCCESS;
        });
    }

}
