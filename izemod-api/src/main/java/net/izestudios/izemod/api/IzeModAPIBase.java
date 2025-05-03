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

package net.izestudios.izemod.api;

import java.awt.*;
import java.nio.file.Path;
import net.izestudios.izemod.api.command.CommandHandler;
import net.izestudios.izemod.api.discord.DiscordRPC;
import net.izestudios.izemod.api.hud.HudRendering;

/**
 * General API endpoint for iZeMod components. Get an instance of this class by registering a iZeMod {@link IzeAddon} entry point into the izemod namespace.
 */
public interface IzeModAPIBase {

    /**
     * Get the version of iZeMod. Note that this does not include alpha or beta tags.
     *
     * @return the version of iZeMod
     */
    String version();

    /**
     * Get the root path of iZeMod.
     *
     * @return the root path of iZeMod
     */
    Path path();

    /**
     * The blue color used for all the UI rendering.
     *
     * @return the theme color
     */
    Color themeColor();

    /**
     * The HUD rendering API.
     *
     * @return the HUD rendering API
     */
    HudRendering hudRendering();

    /**
     * The Discord RPC API.
     *
     * @return the Discord RPC API
     */
    DiscordRPC discordRPC();

    /**
     * The command handler API.
     *
     * @return the command handler API
     */
    CommandHandler commandHandler();

}
