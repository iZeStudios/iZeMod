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

import com.google.common.base.Preconditions;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a HUD element. Use functions below to create instances or implement this interface into more complex classes.
 */
public abstract class HudElement {

    public static HudElement of(final @NotNull Component key, final @NotNull Component value) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);
        return new HudElement() {
            @Override
            public String name() {
                return key.getContents() instanceof TranslatableContents content ? content.getKey() : key();
            }

            @Override
            public String key() {
                return key.getString();
            }

            @Override
            public String value() {
                return value.getString();
            }
        };
    }

    public static HudElement of(final @NotNull String key, final @NotNull String value) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);
        return new HudElement() {
            @Override
            public String key() {
                return key;
            }

            @Override
            public String value() {
                return value;
            }
        };
    }

    public static HudElement of(final @NotNull Component key, final @NotNull Supplier<Object> valueSupplier) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(valueSupplier);
        return new HudElement() {
            @Override
            public String name() {
                return key.getContents() instanceof TranslatableContents content ? content.getKey() : key();
            }

            @Override
            public String key() {
                return key.getString();
            }

            @Override
            public String value() {
                final Object value = valueSupplier.get();
                return value == null ? null : value.toString();
            }
        };
    }

    private boolean enabled;

    /**
     * @return The name of the HUD element. This is used for identifying the element and for config saving.
     */
    public String name() {
        return key();
    }

    /**
     * @return The literal key of the HUD element. This is used for rendering.
     */
    public abstract String key();

    /**
     * @return The value of the HUD element. Return null if the value is not available.
     */
    public abstract String value();

    /**
     * @return Whether the HUD element is enabled.
     */
    public boolean enabled() {
        return enabled;
    }

    /**
     * Sets whether the HUD element is enabled.
     *
     * @param enabled Whether the HUD element is enabled.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
