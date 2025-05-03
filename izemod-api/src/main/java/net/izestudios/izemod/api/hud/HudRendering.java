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

package net.izestudios.izemod.api.hud;

import org.jetbrains.annotations.NotNull;

/**
 * Endpoint to extend the HUD rendering.
 */
public interface HudRendering {

    /**
     * Adds a HUD element to the rendering.
     *
     * @param hudElement The HUD element to add.
     */
    void addHudElement(final @NotNull HudElement hudElement);

    /**
     * Removes a HUD element from the rendering.
     *
     * @param hudElement The HUD element to remove.
     */
    void removeHudElement(final @NotNull HudElement hudElement);

    /**
     * Removes a HUD element from the rendering by the key. If the key is not found, nothing happens.
     *
     * @param key The key of the HUD element to remove.
     */
    void removeHudElement(final @NotNull String key);

}
