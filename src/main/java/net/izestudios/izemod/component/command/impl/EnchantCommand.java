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
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public final class EnchantCommand extends AbstractCommand {

    public EnchantCommand() {
        super(Text.translatable("commands.enchant"), "enchant");
    }

    @Override
    public void builder(final LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("enchant", string())
            .then(argument("level", integer())
                .executes(commandContext -> {
                    if (client.player == null || client.interactionManager == null) {
                        printErrorMessage(Text.translatable("commands.enchant.error.player_not_found"));
                        return FAILURE;
                    }

                    if (!client.player.getAbilities().creativeMode) {
                        printErrorMessage(Text.translatable("commands.enchant.error.not_creative"));
                        return FAILURE;
                    }

                    final ItemStack item = client.player.getMainHandStack();
                    if (item.isEmpty()) {
                        printErrorMessage(Text.translatable("commands.enchant.error.no_item"));
                        return FAILURE;
                    }

                    final String enchantName = getString(commandContext, "enchant");
                    final int level = getInteger(commandContext, "level");

                    Enchantment enchantment = Registry.ENCHANTMENT.get(new Identifier(enchantName));
                    if (enchantment == null) {
                        printErrorMessage(Text.translatable("commands.enchant.error.invalid_enchant", enchantName));
                        return FAILURE;
                    }

                    item.addEnchantment(enchantment, level);
                    printSuccessMessage(Text.translatable("commands.enchant.success", enchantment.getName(level).getString(), item.getName().getString()));
                    return SUCCESS;
                })
            )
        );
    }
}
