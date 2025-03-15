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

import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.resource.InputSupplier;
import net.minecraft.util.Identifier;

public final class Assets {

    public static final Identifier LOGO = Identifier.of("izemod", "logo.png");
    public static final Identifier LOADING_LOGO = Identifier.of("izemod", "loadinglogo.png");
    public static final ButtonTextures BUTTON = new ButtonTextures(
        Identifier.of("izemod", "button"),
        Identifier.of("izemod", "button_disabled"),
        Identifier.of("izemod", "button_highlighted")
    );
    public static final Identifier[] SATURATION_FRAMES = new Identifier[20];
    private static final List<InputSupplier<InputStream>> WINDOW_ICONS;
    private static final InputSupplier<InputStream> WINDOW_ICON_MAC;

    static {
        try {
            WINDOW_ICONS = List.of(
                InputSupplier.create(Path.of(Assets.class.getResource("/assets/izemod/window/16x16.png").toURI())),
                InputSupplier.create(Path.of(Assets.class.getResource("/assets/izemod/window/32x32.png").toURI())),
                InputSupplier.create(Path.of(Assets.class.getResource("/assets/izemod/window/48x48.png").toURI())),
                InputSupplier.create(Path.of(Assets.class.getResource("/assets/izemod/window/128x128.png").toURI())),
                InputSupplier.create(Path.of(Assets.class.getResource("/assets/izemod/window/256x256.png").toURI()))
            );
            WINDOW_ICON_MAC = InputSupplier.create(Path.of(Assets.class.getResource("/assets/izemod/window/mac.icns").toURI()));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    static {
        for (int i = 0; i <= 9; i++) {
            SATURATION_FRAMES[i] = Identifier.of("izemod", "textures/gui/saturation/frame0.png");
        }
        for (int i = 10; i <= 19; i++) {
            SATURATION_FRAMES[i] = Identifier.of("izemod", "textures/gui/saturation/frame" + (i - 9) + ".png");
        }
    }

    public static InputStream icon() {
        return Assets.class.getResourceAsStream("/assets/izemod/icon.png");
    }

    public static List<InputSupplier<InputStream>> windowIcons() {
        return WINDOW_ICONS;
    }

    public static InputSupplier<InputStream> windowIconMac() {
        return WINDOW_ICON_MAC;
    }
}
