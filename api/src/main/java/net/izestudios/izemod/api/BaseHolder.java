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

package net.izestudios.izemod.api;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.ApiStatus;

public final class BaseHolder {

    private static iZeModBase impl;

    @ApiStatus.Internal
    public static void init(final iZeModBase impl) {
        Preconditions.checkArgument(BaseHolder.impl == null, "iZeMod is already initialized");
        BaseHolder.impl = impl;
    }

    public static iZeModBase getImpl() {
        Preconditions.checkState(BaseHolder.impl != null, "iZeMod is not initialized");
        return BaseHolder.impl;
    }

}
