/*
 * This file is part of iZeMod - https://github.com/iZeMods/iZeMod
 * Copyright (C) 2014-2025 the original authors
 *                         - FlorianMichael/EnZaXD <florian.michael07@gmail.com>
 *                         - iZePlayzYT
 * Copyright (C) 2025 GitHub contributors
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

package net.izemods.izemod;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.izemods.izemod.api.BaseHolder;
import net.izemods.izemod.api.iZeModBase;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.session.Session;
import net.minecraft.util.Util;
import java.util.Optional;
import java.util.UUID;

public final class ModImpl implements iZeModBase {

    public static final ModImpl INSTANCE = new ModImpl();

    private String version;

    public void initialize() {
        BaseHolder.init(INSTANCE);

        final ModMetadata metadata = FabricLoader.getInstance().getModContainer("izemod").get().getMetadata();
        version = metadata.getVersion().getFriendlyString();
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            MinecraftClient.getInstance().session = new Session(
                "iZeMod" + Util.getMeasuringTimeMs() % 10000000L,
                UUID.randomUUID(),
                "00000000000000000000000000000000",
                Optional.empty(),
                Optional.empty(),
                Session.AccountType.MOJANG
            );
        }
    }

    public String getVersion() {
        return version;
    }

    // --------------------------------------------------------------------------------------------
    // Proxy the most important/used internals to a general API point for mods

}
