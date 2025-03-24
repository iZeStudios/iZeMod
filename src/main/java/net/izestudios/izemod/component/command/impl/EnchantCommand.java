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
import net.minecraft.command.CommandSource;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.command.argument.EnchantmentArgumentType;

public final class EnchantCommand extends AbstractCommand {

    public EnchantCommand() {
        super(Text.translatable("commands.enchant"), "enchantment");
    }

    @Override
    public void builder(final LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("add")
            .then(argument("enchantment", EnchantmentArgumentType.enchantment())
                .executes(ctx -> addEnchantment(ctx.getSource(), ctx.getArgument("enchantment", Enchantment.class), 1))
                .then(argument("level", IntegerArgumentType.integer(1))
                    .executes(ctx -> addEnchantment(ctx.getSource(), ctx.getArgument("enchantment", Enchantment.class), IntegerArgumentType.getInteger(ctx, "level")))
                )
            )
        );

        builder.then(literal("remove")
            .then(argument("enchantment", EnchantmentArgumentType.enchantment())
                .executes(ctx -> removeEnchantment(ctx.getSource(), ctx.getArgument("enchantment", Enchantment.class)))
            )
        );

        builder.then(literal("all")
            .executes(ctx -> addAllEnchantments(ctx.getSource()))
        );

        builder.then(literal("clear")
            .executes(ctx -> clearEnchantments(ctx.getSource()))
        );
    }

    private int addEnchantment(CommandSource source, Enchantment enchantment, int level) {
        if (client.player == null || !client.player.getAbilities().creativeMode) {
            printErrorMessage(Text.translatable("commands.enchant.error.not_creative"));
            return FAILURE;
        }

        ItemStack item = client.player.getMainHandStack();
        if (item.isEmpty()) {
            printErrorMessage(Text.translatable("commands.enchant.error.no_item"));
            return FAILURE;
        }

        item.addEnchantment(enchantment, level);
        printSuccessMessage(Text.translatable("commands.enchant.success", enchantment.getName(level).getString(), item.getName().getString()));
        return SUCCESS;
    }

    private int removeEnchantment(CommandSource source, Enchantment enchantment) {
        if (client.player == null || !client.player.getAbilities().creativeMode) {
            printErrorMessage(Text.translatable("commands.enchant.error.not_creative"));
            return FAILURE;
        }

        ItemStack item = client.player.getMainHandStack();
        if (item.isEmpty()) {
            printErrorMessage(Text.translatable("commands.enchant.error.no_item"));
            return FAILURE;
        }

        if (!EnchantmentHelper.get(item).containsKey(enchantment)) {
            printErrorMessage(Text.translatable("commands.enchant.error.not_found", enchantment.getName(1).getString()));
            return FAILURE;
        }

        EnchantmentHelper.set(Collections.emptyMap(), item);
        printSuccessMessage(Text.translatable("commands.enchant.removed", enchantment.getName(1).getString(), item.getName().getString()));
        return SUCCESS;
    }

    private int addAllEnchantments(CommandSource source) {
        if (client.player == null || !client.player.getAbilities().creativeMode) {
            printErrorMessage(Text.translatable("commands.enchant.error.not_creative"));
            return FAILURE;
        }

        ItemStack item = client.player.getMainHandStack();
        if (item.isEmpty()) {
            printErrorMessage(Text.translatable("commands.enchant.error.no_item"));
            return FAILURE;
        }

        for (Enchantment enchantment : Registries.ENCHANTMENT) {
            item.addEnchantment(enchantment, 1);
        }

        printSuccessMessage(Text.translatable("commands.enchant.all_added", item.getName().getString()));
        return SUCCESS;
    }

    private int clearEnchantments(CommandSource source) {
        if (client.player == null || !client.player.getAbilities().creativeMode) {
            printErrorMessage(Text.translatable("commands.enchant.error.not_creative"));
            return FAILURE;
        }

        ItemStack item = client.player.getMainHandStack();
        if (item.isEmpty()) {
            printErrorMessage(Text.translatable("commands.enchant.error.no_item"));
            return FAILURE;
        }

        EnchantmentHelper.set(Collections.emptyMap(), item);
        printSuccessMessage(Text.translatable("commands.enchant.clear", item.getName().getString()));
        return SUCCESS;
    }
}
