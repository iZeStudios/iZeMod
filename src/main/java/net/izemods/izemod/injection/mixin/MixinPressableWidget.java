/*
 * This file is part of iZeMod - https://github.com/iZeMods/iZeMod
 * Copyright (C) 2014-2025 the original authors
 *                         - FlorianMichael/EnZaXD <florian.michael07@gmail.com>
 *                         - iZePlayzYT
 * Copyright (C) 2025 GitHub contributors
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

package net.izemods.izemod.injection.mixin;

import net.izemods.izemod.util.Constants;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.widget.PressableWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PressableWidget.class)
public abstract class MixinPressableWidget {

    @Redirect(method = "renderWidget", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/widget/PressableWidget;TEXTURES:Lnet/minecraft/client/gui/screen/ButtonTextures;"))
    private ButtonTextures replaceButtonTextures() {
        return Constants.BUTTON;
    }

}
