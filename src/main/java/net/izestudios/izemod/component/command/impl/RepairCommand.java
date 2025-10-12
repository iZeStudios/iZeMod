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
import net.minecraft.client.Minecraft;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class RepairCommand extends AbstractCommand {

    public RepairCommand() {
        super(Component.translatable("commands.repair"), "repair");
    }

    @Override
    public void builder(final LiteralArgumentBuilder<SharedSuggestionProvider> builder) {
        builder.executes(commandContext -> {
            if (!Minecraft.getInstance().player.isCreative()) {
                printErrorMessage(Component.translatable("commands.repair.gamemode"));
                return FAILURE;
            }

            final ItemStack mainHandItem = Minecraft.getInstance().player.getMainHandItem();
            if (mainHandItem == ItemStack.EMPTY) {
                printErrorMessage(Component.translatable("commands.repair.empty"));
                return FAILURE;
            }
            if (!mainHandItem.isDamageableItem()) {
                printErrorMessage(Component.translatable("commands.repair.invalid"));
                return FAILURE;
            }

            mainHandItem.setDamageValue(0);

            Minecraft.getInstance().player.setItemInHand(InteractionHand.MAIN_HAND, mainHandItem);
            Minecraft.getInstance().player.connection.send(new ServerboundSetCreativeModeSlotPacket(EquipmentSlot.MAINHAND.getId(), mainHandItem));

            printSuccessMessage(Component.translatable("commands.repair.success"));
            return SUCCESS;
        });
    }

}
