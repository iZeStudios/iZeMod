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

package net.izestudios.izemod.injection.mixin;

import net.minecraft.client.gui.screen.world.WorldIcon;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldIcon.class)
public class MixinWorldIcon {

    int cycleTime = 5000; // 5 seconds cycle time
    int timeInCycle = (int) (System.currentTimeMillis() % cycleTime);
    int frame;

    @Redirect(method = "getTextureId", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screen/world/WorldIcon;UNKNOWN_SERVER_ID:Lnet/minecraft/util/Identifier;"))
    private Identifier replaceButtonTextures() {


        if (timeInCycle < 1500) { // 0 to 10 in 1.5 seconds
            frame = timeInCycle / 150;
        } else if (timeInCycle < 2500) { // 1 second break
            frame = 10;
        } else if (timeInCycle < 4000) { // 10 to 0 in 1.5 seconds
            frame = 10 - ((timeInCycle - 2500) / 150);
        } else { // 1 second break
            frame = 0;
        }

        return Identifier.of("izemod", "logosaturation/frame" + frame + ".png");
    }
}
