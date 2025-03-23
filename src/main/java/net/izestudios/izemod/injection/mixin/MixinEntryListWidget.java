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
import net.izestudios.izemod.util.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ContainerWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.izestudios.izemod.component.theme.ColorTheme.BLACK;
import static net.izestudios.izemod.component.theme.ColorTheme.BLACK_200;

@Mixin(EntryListWidget.class)
public abstract class MixinEntryListWidget extends ContainerWidget {

    @Unique
    private static final int izeMod$SHADOW_HEIGHT = 4;

    public MixinEntryListWidget(final int i, final int j, final int k, final int l, final Text text) {
        super(i, j, k, l, text);
    }

    @Inject(method = "drawMenuListBackground", at = @At("HEAD"), cancellable = true)
    private void cancelBackground(DrawContext context, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "drawHeaderAndFooterSeparators", at = @At("HEAD"), cancellable = true)
    private void replaceHeaderAndFooter(DrawContext context, CallbackInfo ci) {
        ci.cancel();
        final int height = context.getScaledWindowHeight();

        context.fill(getX(), 0, getRight(), getY(), IzeModImpl.INSTANCE.themeColor().getRGB());
        RenderUtil.drawGradient(getX(), 0, getRight(), getY(), BLACK, BLACK, 150, 50);
        RenderUtil.drawShadow(getX(), getY(), getWidth(), getY() + izeMod$SHADOW_HEIGHT, 0, BLACK_200);

        context.fill(getX(), getBottom(), getRight(), height, IzeModImpl.INSTANCE.themeColor().getRGB());
        RenderUtil.drawGradient(getX(), 0, getRight(), getY(), BLACK, BLACK, 50, 150);
        RenderUtil.drawShadow(getX(), getBottom() - izeMod$SHADOW_HEIGHT, getWidth(), getBottom(), BLACK_200, 0);
    }

}
