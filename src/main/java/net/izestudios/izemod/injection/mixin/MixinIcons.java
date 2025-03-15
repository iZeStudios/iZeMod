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

import net.izestudios.izemod.util.Assets;
import net.minecraft.client.util.Icons;
import net.minecraft.resource.InputSupplier;
import net.minecraft.resource.ResourcePack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import java.io.InputStream;
import java.util.List;

@Mixin(Icons.class)
public abstract class MixinIcons {
    @Redirect(method = "getIcons", at = @At(value = "INVOKE", target = "Ljava/util/List;of(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/util/List;"))
    private List<? extends InputSupplier<InputStream>> replaceProcessIcon(Object a, Object b, Object c, Object d, Object f) {
        return Assets.windowIcons();
    }


    @Redirect(method = "getMacIcon", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Icons;getIcon(Lnet/minecraft/resource/ResourcePack;Ljava/lang/String;)Lnet/minecraft/resource/InputSupplier;"))
    private InputSupplier<InputStream> replaceProcessIconMac(Icons instance, ResourcePack resourcePack, String fileName) {
        return Assets.windowIconMac();
    }
}
