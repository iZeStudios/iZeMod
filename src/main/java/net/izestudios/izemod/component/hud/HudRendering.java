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

package net.izestudios.izemod.component.hud;

import net.izestudios.izemod.api.hud.HudElement;
import net.izestudios.izemod.util.TimeFormatter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public final class HudRendering {

    public static final List<HudElement> elements = new ArrayList<>();

    static {
        final NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);

        final MinecraftClient client = MinecraftClient.getInstance();
        register("fps", client::getCurrentFps);
        register("x", () -> numberFormat.format(client.player.getX()));
        register("y", () -> numberFormat.format(client.player.getY()));
        register("z", () -> numberFormat.format(client.player.getZ()));
        register("biome", () -> {
            final Optional<RegistryKey<Biome>> key = client.world.getBiome(client.player.getBlockPos()).getKey();
            if (key.isPresent()) {
                final Identifier identifier = key.get().getValue();
                if (Objects.equals(identifier.getNamespace(), Identifier.DEFAULT_NAMESPACE)) {
                    return identifier.getPath();
                } else {
                    return identifier.toString();
                }
            } else {
                return null;
            }
        });
        register("ping", () -> {
            if (client.isInSingleplayer()) {
                return null;
            }

            final PlayerListEntry playerListEntry = client.getNetworkHandler().getPlayerListEntry(client.player.getUuid());
            if (playerListEntry == null) {
                return null;
            } else {
                return playerListEntry.getLatency();
            }
        });
        register("players", () -> {
            if (client.isInSingleplayer()) {
                return null;
            }

            final Collection<PlayerListEntry> playerList = client.getNetworkHandler().getPlayerList();
            if (playerList == null) {
                return null;
            } else {
                return playerList.size();
            }
        });
        register("date", () -> TimeFormatter.DATE_FORMAT.format(new Date()));
        register("time", () -> TimeFormatter.TIME_FORMAT.format(new Date()));
    }

    public static void init() {
    }

    public static void render(final DrawContext context) {
        final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        final int x = 2;
        int y = 15;
        for (final HudElement element : elements) {
            final String value = element.value();
            if (value == null) {
                continue;
            }

            final int keyWidth = context.drawTextWithShadow(textRenderer, I18n.translate(element.key()), x, y, Formatting.DARK_AQUA.getColorValue());
            final int arrowWidth = context.drawTextWithShadow(textRenderer, "»", x + keyWidth, y, Formatting.DARK_AQUA.getColorValue());
            context.drawTextWithShadow(textRenderer, value, x + arrowWidth, y, Formatting.AQUA.getColorValue());
            y += 10;
        }
    }

    private static void register(final String key, final Supplier<Object> value) {
        elements.add(HudElement.of(Text.translatable("ingame.hud." + key), value));
    }

}
