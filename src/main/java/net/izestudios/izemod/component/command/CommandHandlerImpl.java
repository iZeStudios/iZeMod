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

package net.izestudios.izemod.component.command;

import com.google.common.base.Preconditions;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.izestudios.izemod.api.command.AbstractCommand;
import net.izestudios.izemod.api.command.CommandHandler;
import net.izestudios.izemod.component.command.impl.AuthorCommand;
import net.izestudios.izemod.component.command.impl.CopyIPCommand;
import net.izestudios.izemod.component.command.impl.CopyMyIPCommand;
import net.izestudios.izemod.component.command.impl.FullbrightCommand;
import net.izestudios.izemod.component.command.impl.GetCoordCommand;
import net.izestudios.izemod.component.command.impl.MCCommand;
import net.izestudios.izemod.component.command.impl.GetIPCommand;
import net.izestudios.izemod.component.command.impl.TestCommand;
import net.izestudios.izemod.component.command.impl.ClearChatCommand;
import net.izestudios.izemod.component.command.impl.UUIDCommand;
import net.izestudios.izemod.util.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.commands.SharedSuggestionProvider;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public final class CommandHandlerImpl implements CommandHandler {

    private static final String PREFIX = "#";
    public static final CommandHandlerImpl INSTANCE = new CommandHandlerImpl();

    private final List<AbstractCommand> commands = new ArrayList<>();

    public final CommandDispatcher<SharedSuggestionProvider> dispatcher = new CommandDispatcher<>();
    public final ClientSuggestionProvider commandSource = new ClientSuggestionProvider(null, Minecraft.getInstance());

    public void init() {
        Preconditions.checkState(commands.isEmpty(), "Commands already initialized");

        addCommand(new TestCommand());
        addCommand(new FullbrightCommand());
        addCommand(new CopyIPCommand());
        addCommand(new GetCoordCommand());
        addCommand(new ClearChatCommand());
        addCommand(new CopyMyIPCommand());
        addCommand(new UUIDCommand());
        addCommand(new MCCommand());
        addCommand(new GetIPCommand());
        addCommand(new AuthorCommand());
    }

    public boolean onChatMessage(final String message) {
        if (message.startsWith(PREFIX)) {
            final StringReader reader = new StringReader(message);
            reader.setCursor(PREFIX.length());

            try {
                dispatcher.execute(reader, commandSource);
            } catch (CommandSyntaxException e) {
                Constants.checkCommand(e, message);
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean onRefreshSuggestions(final StringReader reader) {
        if (reader.canRead(PREFIX.length()) && reader.getString().startsWith(PREFIX, reader.getCursor())) {
            reader.setCursor(reader.getCursor() + PREFIX.length());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void addCommand(final @NotNull AbstractCommand command) {
        Preconditions.checkNotNull(command);
        final LiteralArgumentBuilder<SharedSuggestionProvider> literal = LiteralArgumentBuilder.literal(command.getCommand());
        command.builder(literal);
        final LiteralCommandNode<SharedSuggestionProvider> builder = dispatcher.register(literal);

        for (final String alias : command.getAliases()) {
            if (alias.equals(command.getCommand())) {
                continue;
            }

            dispatcher.register(LiteralArgumentBuilder.<SharedSuggestionProvider>literal(alias).redirect(builder));
        }

        commands.add(command);
    }

    @Override
    public void removeCommand(final @NotNull AbstractCommand command) {
        Preconditions.checkNotNull(command);
        commands.remove(command);
    }

    @Override
    public void removeCommand(final @NotNull String alias) {
        Preconditions.checkNotNull(alias);
        commands.removeIf(command -> {
            for (String commandAlias : command.getAliases()) {
                if (commandAlias.equals(alias)) {
                    return true;
                }
            }
            return false;
        });
    }

}
