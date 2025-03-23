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
import net.minecraft.command.CommandSource;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class EnchantCommand extends AbstractCommand {

    public EnchantCommand() {
        super("enchant");
    }

    @Override
    public void builder(final LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("enchant", string())
            .then(argument("level", integer())
                .executes(commandContext -> {
                    if (client.player == null || client.interactionManager == null) {
                        printErrorMessage("Player not found!");
                        return FAILURE;
                    }

                    if (!client.player.getAbilities().creativeMode) {
                        printErrorMessage("§c§lError: §3You need to be in creative mode.");
                        return FAILURE;
                    }

                    ItemStack item = client.player.getMainHandStack();
                    if (item.isEmpty()) {
                        printErrorMessage("§c§lError: §3You need to hold an item.");
                        return FAILURE;
                    }

                    String enchantName = getString(commandContext, "enchant");
                    int level = getInteger(commandContext, "level");

                    Enchantment enchantment = Registry.ENCHANTMENT.get(new Identifier(enchantName));
                    if (enchantment == null) {
                        printErrorMessage("There is no enchantment with the name '" + enchantName + "'");
                        return FAILURE;
                    }

                    item.addEnchantment(enchantment, level);
                    printSuccessMessage(enchantment.getName(level).getString() + " added to " + item.getName().getString() + ".");
                    return SUCCESS;
                })
            )
        );
    }
}
