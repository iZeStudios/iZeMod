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

package net.izestudios.izemod.component.multiplayer;

import com.google.gson.JsonObject;
import java.util.Arrays;
import net.izestudios.izemod.save.AbstractSave;

public final class ServerSaveStates extends AbstractSave {

    public static final ServerSaveStates INSTANCE = new ServerSaveStates();
    public static final int STATE_COUNT = 16;
    private static final String[] STATES = new String[STATE_COUNT];

    static {
        Arrays.fill(STATES, null);
    }

    private ServerSaveStates() {
        super("direct_connect_slots");
    }

    public static void set(final int index, final String state) {
        STATES[index] = state;
        INSTANCE.save();
    }

    public static String get(final int index) {
        return STATES[index];
    }

    @Override
    public void write(final JsonObject object) {
        for (int i = 0; i < STATE_COUNT; i++) {
            final String state = STATES[i];
            if (state != null) {
                object.addProperty(String.valueOf(i), state);
            }
        }
    }

    @Override
    public void read(final JsonObject object) {
        for (int i = 0; i < STATE_COUNT; i++) {
            final String key = String.valueOf(i);
            if (object.has(key) && !object.get(key).isJsonNull()) {
                STATES[i] = object.get(key).getAsString();
            } else {
                STATES[i] = null;
            }
        }
    }

}
