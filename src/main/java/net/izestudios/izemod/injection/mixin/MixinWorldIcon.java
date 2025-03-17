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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static net.izestudios.izemod.util.Assets.SATURATION_FRAMES;

@Mixin(WorldIcon.class)
public abstract class MixinWorldIcon {

    @Unique
    private static final int iZeMod$WAIT_FRAMES = 7;

    @Unique
    private int izeMod$currentFrame = 1;

    @Unique
    private int izeMod$waitCounter = 0;

    @Unique
    private boolean izeMod$reversing = true;

    @Redirect(method = "getTextureId", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screen/world/WorldIcon;UNKNOWN_SERVER_ID:Lnet/minecraft/util/Identifier;"))
    private Identifier replaceUnknownServerIcon() {
        if (++izeMod$waitCounter >= iZeMod$WAIT_FRAMES) {
            izeMod$currentFrame += izeMod$reversing ? -1 : 1;

            if (izeMod$currentFrame <= 0 || izeMod$currentFrame >= SATURATION_FRAMES.length - 1) {
                izeMod$reversing = !izeMod$reversing;
                izeMod$currentFrame = Math.max(0, Math.min(SATURATION_FRAMES.length - 1 - 1, izeMod$currentFrame));
            }

            izeMod$waitCounter = 0;
        }

        return SATURATION_FRAMES[izeMod$currentFrame];
    }

}
