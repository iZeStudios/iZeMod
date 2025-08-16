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
import net.minecraft.client.Minecraft;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.WrittenBookContent;

public final class AuthorCommand extends AbstractCommand {

    public AuthorCommand() {
        super(Component.translatable("commands.author"), "author");
    }

    @Override
    public void builder(final LiteralArgumentBuilder<SharedSuggestionProvider> builder) {
        builder.then(argument("author", StringArgumentType.string()).executes(commandContext -> {
            if (!Minecraft.getInstance().player.isCreative()) {
                printErrorMessage(Component.translatable("commands.author.gamemode"));
                return FAILURE;
            }

            final ItemStack mainHandItem = Minecraft.getInstance().player.getMainHandItem();
            if (!mainHandItem.is(Items.WRITTEN_BOOK)) {
                printErrorMessage(Component.translatable("commands.author.invalid"));
                return FAILURE;
            }

            final WrittenBookContent content = mainHandItem.get(DataComponents.WRITTEN_BOOK_CONTENT);
            if (content == null) {
                printErrorMessage(Component.translatable("commands.author.empty"));
                return FAILURE;
            }

            final String author = StringArgumentType.getString(commandContext, "author");
            final WrittenBookContent newContent = new WrittenBookContent(content.title(), author, content.generation(), content.pages(), content.resolved());
            mainHandItem.set(DataComponents.WRITTEN_BOOK_CONTENT, newContent);


            printSuccessMessage(Component.translatable("commands.author.success", author));
            return SUCCESS;
        }));
    }

}
