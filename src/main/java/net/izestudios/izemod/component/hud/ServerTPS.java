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

import net.minecraft.util.Mth;

public final class ServerTPS {

    private static long lastTimestamp = -1;
    private static float tps = 20.0f;

    public static void update() {
        final long now = System.currentTimeMillis();
        if (lastTimestamp != -1) {
            final long timeDiff = now - lastTimestamp;
            final float calculatedTps = 20.0f / (timeDiff / 1000.0f);

            tps = Mth.clamp(calculatedTps, 0.0f, 20.0f);
        }
        lastTimestamp = now;
    }

    public static float getTps() {
        return tps;
    }

}
