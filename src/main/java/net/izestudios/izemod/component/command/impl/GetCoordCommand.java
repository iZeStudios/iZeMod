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
import java.text.NumberFormat;
import net.izestudios.izemod.api.command.AbstractCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;

public final class GetCoordCommand extends AbstractCommand {

    public GetCoordCommand() {
        super(Component.translatable("commands.getcoord"), "getcoord");
    }

    @Override
    public void builder(final LiteralArgumentBuilder<SharedSuggestionProvider> builder) {
        builder.executes(commandContext -> {
            final NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setMaximumFractionDigits(3);

            final Vec3 position = Minecraft.getInstance().player.position();
            final String x = numberFormat.format(position.x());
            final String y = numberFormat.format(position.y());
            final String z = numberFormat.format(position.z());
            Minecraft.getInstance().keyboardHandler.setClipboard(x + " " + y + " " + z);

            printSuccessMessage(Component.translatable("commands.getcoord.success"));
            return SUCCESS;
        });
    }

}
