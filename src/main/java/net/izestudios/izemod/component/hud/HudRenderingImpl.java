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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import com.google.common.base.Preconditions;
import net.izestudios.izemod.api.hud.HudElement;
import net.izestudios.izemod.api.hud.HudRendering;
import net.izestudios.izemod.save.SaveLoader;
import net.izestudios.izemod.util.TimeFormatter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.NotNull;

public final class HudRenderingImpl implements HudRendering {

    public static final HudRenderingImpl INSTANCE = new HudRenderingImpl();
    public final List<HudElement> elements = new ArrayList<>();

    public void init() {
        Preconditions.checkState(elements.isEmpty(), "Hud elements already initialized");

        final NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);

        final Minecraft client = Minecraft.getInstance();
        register("fps", client::getFps);
        register("x", () -> numberFormat.format(client.player.getX()));
        register("y", () -> numberFormat.format(client.player.getY()));
        register("z", () -> numberFormat.format(client.player.getZ()));
        register("biome", () -> {
            final Optional<ResourceKey<Biome>> key = client.level.getBiome(client.player.blockPosition()).unwrapKey();
            if (key.isPresent()) {
                final ResourceLocation identifier = key.get().location();
                if (Objects.equals(identifier.getNamespace(), ResourceLocation.DEFAULT_NAMESPACE)) {
                    return identifier.getPath();
                } else {
                    return identifier.toString();
                }
            } else {
                return null;
            }
        });
        register("ping", () -> {
            if (client.isLocalServer()) {
                return null;
            }

            final PlayerInfo playerListEntry = client.getConnection().getPlayerInfo(client.player.getUUID());
            if (playerListEntry == null) {
                return null;
            } else {
                return playerListEntry.getLatency();
            }
        });
        register("players", () -> {
            if (client.isLocalServer()) {
                return null;
            }

            return client.getConnection().getOnlinePlayers().size();
        });
        register("date", () -> TimeFormatter.DATE_FORMAT.format(new Date()));
        register("time", () -> TimeFormatter.TIME_FORMAT.format(new Date()));

        SaveLoader.INSTANCE.add(new HudSave());
    }

    public void draw(final GuiGraphics guiGraphics) {
        final Font font = Minecraft.getInstance().font;
        final int x = 2;
        int y = 15;
        for (final HudElement element : elements) {
            final String value = element.value();
            if (!element.enabled() || value == null) {
                continue;
            }

            final int keyWidth = font.width(element.key());
            final int arrowWidth = font.width("»");

            guiGraphics.drawString(font, ChatFormatting.DARK_AQUA + element.key(), x, y, -1);
            guiGraphics.drawString(font, ChatFormatting.DARK_AQUA + "»", x + keyWidth, y, -1);
            guiGraphics.drawString(font, ChatFormatting.AQUA + value, x + keyWidth + arrowWidth, y, -1);
            y += 10;
        }
    }

    @Override
    public void addHudElement(final @NotNull HudElement hudElement) {
        Preconditions.checkNotNull(hudElement);
        elements.add(hudElement);
    }

    @Override
    public void removeHudElement(final @NotNull HudElement hudElement) {
        Preconditions.checkNotNull(hudElement);
        elements.remove(hudElement);
    }

    @Override
    public void removeHudElement(final @NotNull String key) {
        Preconditions.checkNotNull(key);
        elements.removeIf(hudElement -> hudElement.key().equals(key));
    }

    private void register(final String key, final Supplier<Object> value) {
        final HudElement element = HudElement.of(Component.translatable("ingame.hud." + key), value);
        elements.add(element);
        element.setEnabled(true);
    }

}
