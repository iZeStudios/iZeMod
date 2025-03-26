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

package net.izestudios.izemod.save;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;

public final class SaveLoader {

    public static final SaveLoader INSTANCE = new SaveLoader();

    private final List<AbstractSave> saves = new ArrayList<>();
    private boolean initialized;

    public void init() {
        Preconditions.checkState(!initialized, "SaveLoader already initialized");
        initialized = true;

        saves.forEach(AbstractSave::init);
    }

    public void save() {
        Preconditions.checkState(initialized, "SaveLoader not initialized");

        saves.forEach(AbstractSave::save);
    }

    public void add(final AbstractSave save) {
        Preconditions.checkNotNull(save);
        Preconditions.checkState(!initialized, "SaveLoader already initialized");
        saves.add(save);
    }

}
