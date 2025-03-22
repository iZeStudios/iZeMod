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
import net.minecraft.client.resource.language.I18n;
import net.minecraft.command.CommandSource;

public class FullbrightCommand extends AbstractCommand {

    static Boolean fullbright = false;

    public FullbrightCommand() {
        super("Fullbright");
    }

    @Override
    public void builder(final LiteralArgumentBuilder<CommandSource> builder) {

        builder.executes(commandContext -> {
            toggleFullbright();
            return SUCCESS;
        });


    }

    private void toggleFullbright() {
        fullbright = !fullbright;
        String statusMessage = fullbright ? "ingame.fullbright.enabled" : "ingame.fullbright.disabled";
        printSuccessMessage(I18n.translate(statusMessage));
    }


    public static Boolean getFullbright() {
        return fullbright;
    }
}
