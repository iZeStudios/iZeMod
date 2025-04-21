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
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractContainerWidget;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.izestudios.izemod.component.theme.ColorTheme.BLACK;
import static net.izestudios.izemod.component.theme.ColorTheme.BLACK_200;

@Mixin(AbstractSelectionList.class)
public abstract class MixinAbstractSelectionList extends AbstractContainerWidget {

    @Unique
    private static final int izeMod$SHADOW_HEIGHT = 4;

    public MixinAbstractSelectionList(final int i, final int j, final int k, final int l, final Component component) {
        super(i, j, k, l, component);
    }

    @Inject(method = "renderListBackground", at = @At("HEAD"), cancellable = true)
    private void cancelBackground(GuiGraphics guiGraphics, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "renderListSeparators", at = @At("HEAD"), cancellable = true)
    private void replaceHeaderAndFooter(GuiGraphics guiGraphics, CallbackInfo ci) {
        ci.cancel();
        final int height = guiGraphics.guiHeight();

        guiGraphics.fill(getX(), 0, getRight(), getY(), IzeModImpl.INSTANCE.themeColor().getRGB());
        RenderUtil.drawGradient(guiGraphics, getX(), 0, getRight(), getY(), BLACK, BLACK, 150, 50);
        RenderUtil.drawShadow(guiGraphics, getX(), getY(), getWidth(), getY() + izeMod$SHADOW_HEIGHT, 0, BLACK_200);

        guiGraphics.fill(getX(), getBottom(), getRight(), height, IzeModImpl.INSTANCE.themeColor().getRGB());
        RenderUtil.drawGradient(guiGraphics, getX(), 0, getRight(), getY(), BLACK, BLACK, 50, 150);
        RenderUtil.drawShadow(guiGraphics, getX(), getBottom() - izeMod$SHADOW_HEIGHT, getWidth(), getBottom(), BLACK_200, 0);
    }

}
