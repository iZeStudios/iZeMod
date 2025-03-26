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

import com.google.gson.JsonObject;
import net.izestudios.izemod.api.hud.HudElement;
import net.izestudios.izemod.save.AbstractSave;

public final class HudSave extends AbstractSave {

    public HudSave() {
        super("hud");
    }

    @Override
    public void write(final JsonObject object) {
        for (final HudElement element : HudRenderingImpl.INSTANCE.elements) {
            object.addProperty(element.name(), element.enabled());
        }
    }

    @Override
    public void read(final JsonObject object) {
        for (final HudElement element : HudRenderingImpl.INSTANCE.elements) {
            if (object.has(element.name())) {
                element.setEnabled(object.get(element.name()).getAsBoolean());
            }
        }
    }

}
