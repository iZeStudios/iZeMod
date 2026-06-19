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

import java.io.InputStream;
import net.izestudios.izemod.util.Assets;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ResourceProvider.class)
public interface MixinResourceProvider {

    @Inject(method = "open", at = @At("HEAD"), cancellable = true)
    private void forceIzeModAsciiFont(ResourceLocation location, CallbackInfoReturnable<InputStream> cir) {
        if (!Assets.ASCII_FONT.equals(location)) {
            return;
        }

        final InputStream stream = Assets.asciiFont();
        if (stream != null) {
            cir.setReturnValue(stream);
        }
    }

}
