/*
 * This file is part of iZeMod - https://github.com/iZeStudios/iZeMod
 * Copyright (C) 2026 iZeStudios and GitHub contributors
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
import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class RenameCommand extends AbstractCommand {
    public RenameCommand() {
        super(Component.translatable("commands.rename"), "rename");
    }

    @Override
    public void builder(final LiteralArgumentBuilder<SharedSuggestionProvider> builder) {
        builder.then(argument("name", StringArgumentType.greedyString())
                .executes(commandContext -> {
                    if (!Minecraft.getInstance().player.isCreative()) {
                        printErrorMessage(Component.translatable("commands.rename.gamemode"));
                        return FAILURE;
                    }

                    final ItemStack mainHandItem = Minecraft.getInstance().player.getMainHandItem();
                    if (mainHandItem == ItemStack.EMPTY) {
                        printErrorMessage(Component.translatable("commands.rename.empty"));
                        return FAILURE;
                    }

                    String name = StringArgumentType.getString(commandContext, "name");
                    mainHandItem.set(DataComponents.CUSTOM_NAME, Component.literal(name));

                    Minecraft.getInstance().player.setItemInHand(InteractionHand.MAIN_HAND, mainHandItem);
                    Minecraft.getInstance().player.connection.send(new ServerboundSetCreativeModeSlotPacket(EquipmentSlot.MAINHAND.getId(), mainHandItem));

                    printSuccessMessage(Component.translatable("commands.rename.success"));
                    return SUCCESS;
                }))
            .executes(commandContext -> {
                printErrorMessage(Component.translatable("commands.rename.usage"));
                return FAILURE;
            });
    }

}
