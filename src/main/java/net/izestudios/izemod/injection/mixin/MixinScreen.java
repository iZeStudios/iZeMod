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

import net.izestudios.izemod.IzeModImpl;
import net.izestudios.izemod.component.theme.ColorTheme;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
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

    @Inject(method = "renderPanoramaBackground", at = @At("HEAD"), cancellable = true)
    private void changeBackground(DrawContext context, float delta, CallbackInfo ci) {
        ColorTheme.tick();
        context.fill(0, 0, this.width, this.height, IzeModImpl.INSTANCE.getThemeColor().getRGB());
        context.fillGradient(0, 0, this.width, this.height, -2130706433, 16777215);
        context.fillGradient(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
        ci.cancel();
    }

}
