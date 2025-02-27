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

package net.izestudios.izemod.component.theme;

import java.awt.*;

public final class ColorTheme {

    public static final int DARK_BLUE = new Color(0, 75, 100).getRGB();

    private static final int COLOR_UPPER_LIMIT = 200;
    private static final int COLOR_LOWER_LIMIT = 100;
    private static final long UPDATE_INTERVAL_MS = 30L;

    private static int rgba = 150;
    private static long lastUpdate = System.currentTimeMillis();
    private static boolean isReversing = false;

    public static int getBlue() {
        return rgba;
    }

    public static void tick() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastUpdate > UPDATE_INTERVAL_MS) {
            lastUpdate = currentTime;

            if (rgba >= COLOR_UPPER_LIMIT) {
                isReversing = true;
            } else if (rgba <= COLOR_LOWER_LIMIT) {
                isReversing = false;
            }
            rgba += isReversing ? -1 : 1;
        }
    }

}
