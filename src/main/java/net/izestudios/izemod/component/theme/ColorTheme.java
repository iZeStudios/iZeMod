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

    public static final int BASE_BLUE = new Color(0, 75, 100).getRGB();
    public static final int EMPTY = new Color(255, 255, 255, 0).getRGB();
    public static final int BLACK = new Color(0, 0, 0).getRGB();
    public static final int WHITE_128 = new Color(255, 255, 255, 128).getRGB();
    public static final int BLACK_50 = new Color(0, 0, 0, 50).getRGB();
    public static final int BLACK_150 = new Color(0, 0, 0, 150).getRGB();
    public static final int BLACK_200 = new Color(0, 0, 0, 200).getRGB();

    private static int blue = 150;
    private static boolean isReversing = false;
    private static long lastUpdate = System.currentTimeMillis();

    public static int getBlue() {
        return blue;
    }

    public static void tick() {
        if (System.currentTimeMillis() - lastUpdate > 30) {
            int currentColor = blue;
            if (currentColor > 200) {
                isReversing = true;
            } else if (currentColor < 100) {
                isReversing = false;
            }
            if (isReversing) {
                blue = currentColor - 1;
            } else {
                blue = currentColor + 1;
            }
            lastUpdate = System.currentTimeMillis();
        }
    }

}
