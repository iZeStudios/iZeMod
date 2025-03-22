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

package net.izestudios.izemod.api.discord;

/**
 * Endpoint to interact with the Discord Rich Presence. The methods here do not throw exceptions, but simply discard invalid usage.
 */
public interface DiscordRPC {

    /**
     * Starts the Discord Rich Presence.
     */
    void start();

    /**
     * Stops the Discord Rich Presence.
     */
    void stop();

    /**
     * Updates the Discord Rich Presence with the given first and second line.
     *
     * @param first  The first line to display.
     * @param second The second line to display.
     */
    void update(final String first, final String second);

}
