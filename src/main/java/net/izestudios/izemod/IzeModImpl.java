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
import net.izestudios.izemod.api.IzeModAPI;
import net.izestudios.izemod.api.IzeModAPIBase;
import net.izestudios.izemod.component.theme.ColorTheme;
import java.awt.*;

public final class IzeModImpl implements IzeModAPIBase {

    public static final IzeModImpl INSTANCE = new IzeModImpl();

    private String version;

    public void initialize() {
        IzeModAPI.init(INSTANCE);

        final ModMetadata metadata = FabricLoader.getInstance().getModContainer("izemod").get().getMetadata();
        version = metadata.getVersion().getFriendlyString();
    }

    public String getVersion() {
        return version;
    }

    // --------------------------------------------------------------------------------------------
    // Proxy the most important/used internals to a general API point for mods


    @Override
    public Color getThemeColor() {
        return new Color(0, 125, ColorTheme.getBlue());
    }

}
