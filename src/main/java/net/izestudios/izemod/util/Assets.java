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

package net.izestudios.izemod.util;

import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.util.Identifier;
import java.io.InputStream;

public final class Assets {

    public static final Identifier LOGO = Identifier.of("izemod", "textures/logo.png");
    public static final Identifier SPLASH_OVERLAY = Identifier.of("izemod", "textures/splash_overlay.png");
    public static final Identifier[] SATURATION_FRAMES = new Identifier[20];
    public static final ButtonTextures BUTTON = new ButtonTextures(
        Identifier.of("izemod", "button"),
        Identifier.of("izemod", "button_disabled"),
        Identifier.of("izemod", "button_highlighted")
    );

    static {
        for (int i = 0; i <= 9; i++) {
            SATURATION_FRAMES[i] = Identifier.of("izemod", "textures/gui/saturation/frame0.png");
        }
        for (int i = 10; i <= 19; i++) {
            SATURATION_FRAMES[i] = Identifier.of("izemod", "textures/gui/saturation/frame" + (i - 9) + ".png");
        }
    }

    public static InputStream icon() {
        return Assets.class.getResourceAsStream("/assets/izemod/textures/icon/icon.png");
    }

}
