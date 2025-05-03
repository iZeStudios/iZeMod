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

package net.izestudios.izemod.api.command;

import com.google.common.base.Preconditions;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.izestudios.izemod.util.ChatUtil;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

/**
 * Command abstraction. Override {@link #builder(LiteralArgumentBuilder)} to create the command structure.
 */
public abstract class AbstractCommand extends ChatUtil /*Allows for direct method access*/ {

    public static final int SUCCESS = 1;
    public static final int FAILURE = 0;

    private final String[] aliases;
    private final Component description;

    public AbstractCommand(final String command) {
        this.description = null;
        this.aliases = new String[] { command };
    }

    public AbstractCommand(final Component description, final String... aliases) {
        Preconditions.checkNotNull(description);
        Preconditions.checkState(aliases.length > 0, "No aliases provided");
        for (final String alias : aliases) {
            Preconditions.checkState(alias != null && !alias.isEmpty(), "Invalid alias");
        }
        this.description = description;
        this.aliases = aliases;
    }

    protected LiteralArgumentBuilder<SharedSuggestionProvider> literal(final String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    protected RequiredArgumentBuilder<SharedSuggestionProvider, ?> argument(final String name, final ArgumentType<?> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }

    public abstract void builder(final LiteralArgumentBuilder<SharedSuggestionProvider> builder);

    public Component getDescription() {
        return description;
    }

    public String getCommand() {
        return aliases[0];
    }

    public String[] getAliases() {
        return aliases;
    }

}
