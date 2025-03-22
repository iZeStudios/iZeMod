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

package net.izestudios.izemod.addon;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.izestudios.izemod.api.IzeAddon;

public final class AddonManager {

    public static final String ENTRYPOINT_MARKER = "izemod";
    public static final AddonManager INSTANCE = new AddonManager();

    public void run(final IzeAddonExecutor executor) {
        for (final EntrypointContainer<IzeAddon> container : FabricLoader.getInstance().getEntrypointContainers(ENTRYPOINT_MARKER, IzeAddon.class)) {
            executor.execute(container.getEntrypoint());
        }
    }

    public int count() {
        return FabricLoader.getInstance().getEntrypointContainers(ENTRYPOINT_MARKER, IzeAddon.class).size();
    }

    @FunctionalInterface
    public interface IzeAddonExecutor {

        void execute(final IzeAddon addon);

    }

}
