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

import net.fabricmc.loader.api.FabricLoader;
import net.izestudios.izemod.IzeModImpl;
import net.izestudios.izemod.component.theme.ColorTheme;
import net.izestudios.izemod.util.Assets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.session.Session;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Optional;
import java.util.UUID;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {

    @Mutable
    @Shadow
    public Session session;

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/lang/System;currentTimeMillis()J"))
    private void initialize(RunArgs args, CallbackInfo ci) {
        SplashOverlay.LOGO = Assets.SPLASH_OVERLAY;
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            session = new Session(
                "iZeMod" + Util.getMeasuringTimeMs() % 10000000L,
                UUID.randomUUID(),
                "00000000000000000000000000000000",
                Optional.empty(),
                Optional.empty(),
                Session.AccountType.MOJANG
            );
        }

        IzeModImpl.INSTANCE.lateInitialize();
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tickColorTheme(CallbackInfo ci) {
        ColorTheme.tick();
    }

    @Inject(method = "getWindowTitle", at = @At(value = "HEAD"), cancellable = true)
    private void replaceWindowTitle(CallbackInfoReturnable<String> cir) {
         cir.setReturnValue("iZeMod " + IzeModImpl.INSTANCE.getVersion() + " (" + IzeModImpl.ALPHA_VERSION_NAME + ")");
    }

}
