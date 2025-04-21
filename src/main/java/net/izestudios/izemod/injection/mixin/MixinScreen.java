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

import net.izestudios.izemod.component.discord.DiscordRPCImpl;
import net.izestudios.izemod.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class MixinScreen {

    @Shadow
    public int width;

    @Shadow
    public int height;

    @Shadow
    protected Minecraft minecraft;

    @Shadow
    public abstract Component getTitle();

    @Inject(method = "renderPanorama", at = @At("HEAD"), cancellable = true)
    private void changeBackground(GuiGraphics guiGraphics, float partialTick, CallbackInfo ci) {
        ci.cancel();
        RenderUtil.drawBlueFade(guiGraphics, 0, 0, this.width, this.height);
    }

    @Inject(method = "renderMenuBackground(Lnet/minecraft/client/gui/GuiGraphics;IIII)V", at = @At("HEAD"), cancellable = true)
    private void removeDarkening(GuiGraphics guiGraphics, int x, int y, int width, int height, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "init()V", at = @At("HEAD"))
    private void setIdleDiscordRPC(CallbackInfo ci) {
        final String username = this.minecraft.getUser().getName();
        DiscordRPCImpl.INSTANCE.update("Username: " + username, null);
    }

}
