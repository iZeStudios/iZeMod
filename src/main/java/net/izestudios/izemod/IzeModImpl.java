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

package net.izestudios.izemod;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.izestudios.izemod.addon.AddonManager;
import net.izestudios.izemod.api.IzeModAPIBase;
import net.izestudios.izemod.api.command.CommandHandler;
import net.izestudios.izemod.api.discord.DiscordRPC;
import net.izestudios.izemod.api.hud.HudRendering;
import net.izestudios.izemod.component.command.CommandHandlerImpl;
import net.izestudios.izemod.component.hud.HudRenderingImpl;
import net.izestudios.izemod.component.theme.ColorTheme;
import net.izestudios.izemod.component.discord.DiscordRPCImpl;
import java.awt.*;

public final class IzeModImpl implements IzeModAPIBase {

    // To be removed once we leave alpha
    public static final String ALPHA_VERSION = "0.4.1";
    public static final String ALPHA_VERSION_TAG = "v" + ALPHA_VERSION + "-alpha";
    public static final String ALPHA_VERSION_NAME = "Alpha v" + ALPHA_VERSION;

    public static final IzeModImpl INSTANCE = new IzeModImpl();

    private String version;

    public void initialize() {
        final ModMetadata metadata = FabricLoader.getInstance().getModContainer("izemod").get().getMetadata();
        version = metadata.getVersion().getFriendlyString();
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

        DiscordRPCImpl.INSTANCE.start();
    }

    public void lateInitialize() {
        HudRenderingImpl.INSTANCE.init();
        CommandHandlerImpl.INSTANCE.init();

        // Run addons as late as possible
        AddonManager.INSTANCE.run(addon -> addon.onLoad(this));
    }

    private void shutdown() {
        DiscordRPCImpl.INSTANCE.stop();

        // Run addons as late as possible
        AddonManager.INSTANCE.run(addon -> addon.onShutdown(this));
    }

    // --------------------------------------------------------------------------------------------
    // Proxy the most important/used internals to a general API point for mods

    @Override
    public String version() {
        return version;
    }

    @Override
    public Color themeColor() {
        return new Color(0, 125, ColorTheme.getBlue());
    }

    @Override
    public HudRendering hudRendering() {
        return HudRenderingImpl.INSTANCE;
    }

    @Override
    public DiscordRPC discordRPC() {
        return DiscordRPCImpl.INSTANCE;
    }

    @Override
    public CommandHandler commandHandler() {
        return CommandHandlerImpl.INSTANCE;
    }

}
