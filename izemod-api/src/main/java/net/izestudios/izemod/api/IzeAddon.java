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

/**
 * General bridge interface for iZeMod addons. The methods below are called by iZeMod on specific events.
 */
public interface IzeAddon {

    /**
     * Called after iZeMod has loaded all its components. This is the place to register commands, HUD elements, etc.
     *
     * @param api The iZeMod API endpoint
     */
    void onLoad(final IzeModAPIBase api);

    /**
     * Called when iZeMod is shutting down. This is the place to clean up resources, save data, etc.
     *
     * @param api The iZeMod API endpoint
     */
    default void onShutdown(final IzeModAPIBase api) {
    }

}
