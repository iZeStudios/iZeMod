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

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.ArrayList;
import java.util.List;
import net.izestudios.izemod.api.command.AbstractCommand;
import net.izestudios.izemod.component.command.CommandHandlerImpl;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;

public final class HelpCommand extends AbstractCommand {

    private static final int CMDS_PER_PAGE = 5;

    public HelpCommand() {
        super(Component.translatable("commands.help"), "help");
    }

    private int execute(final int page) {
        final List<AbstractCommand> commands = new ArrayList<>(CommandHandlerImpl.INSTANCE.getCommands());

        final int pageCount = Math.max((int) Math.ceil((double) commands.size() / CMDS_PER_PAGE), 1);
        if (page > pageCount || page < 1) {
            printErrorMessage(Component.translatable("commands.help.invalid", page));
            return FAILURE;
        }

        printEmptyLine();
        printPrefixedChatMessage(Component.translatable("commands.help.success", page, pageCount).withStyle(ChatFormatting.GOLD));
        printEmptyLine();

        final int start = (page - 1) * CMDS_PER_PAGE;
        final int end = Math.min(page * CMDS_PER_PAGE, commands.size());

        for (int i = start; i < end; i++) {
            final AbstractCommand command = commands.get(i);
            final String alias = CommandHandlerImpl.PREFIX + command.getCommand();

            final MutableComponent commandComponent = Component.literal(alias).withStyle(ChatFormatting.RED);
            printChatMessage(commandComponent.withStyle(style -> {
                    if (command.getDescription() != null) {
                        style = style.withHoverEvent(new HoverEvent.ShowText(command.getDescription()));
                    }
                    return style.withClickEvent(new ClickEvent.SuggestCommand(alias));
                }
            ));
        }
        printEmptyLine();
        return SUCCESS;
    }

    @Override
    public void builder(final LiteralArgumentBuilder<SharedSuggestionProvider> builder) {
        builder.executes(context -> execute(1)).then(argument("page", IntegerArgumentType.integer(1)).executes(context -> {
            final int page = IntegerArgumentType.getInteger(context, "page");
            return execute(page);
        }));
    }

}
