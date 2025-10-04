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

import java.util.Optional;
import java.util.UUID;
import net.fabricmc.loader.api.FabricLoader;
import net.izestudios.izemod.IzeModImpl;
import net.izestudios.izemod.component.theme.ColorTheme;
import net.izestudios.izemod.util.Assets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.client.main.GameConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Shadow
    public User user;

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/LoadingOverlay;registerTextures(Lnet/minecraft/client/renderer/texture/TextureManager;)V"))
    private void initialize(GameConfig gameConfig, CallbackInfo ci) {
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            user = new User(
                "iZeMod" + String.format("%06d", (int) (Math.random() * 1000000)),
                UUID.randomUUID(),
                "00000000000000000000000000000000",
                Optional.empty(),
                Optional.empty()
            );
        }

        LoadingOverlay.MOJANG_STUDIOS_LOGO_LOCATION = Assets.SPLASH_OVERLAY;
        IzeModImpl.INSTANCE.lateInitialize();
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tickColorTheme(CallbackInfo ci) {
        ColorTheme.tick();
    }

    @Inject(method = "createTitle", at = @At(value = "HEAD"), cancellable = true)
    private void replaceWindowTitle(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue("iZeMod " + IzeModImpl.INSTANCE.version() + " (" + IzeModImpl.ALPHA_VERSION_NAME + ")");
    }

}
