/*
 * This file is part of iZeMod - https://github.com/iZeMods/iZeMod
 * Copyright (C) 2014-2025 the original authors
 *                         - FlorianMichael/EnZaXD <florian.michael07@gmail.com>
 *                         - iZePlayzYT
 * Copyright (C) 2025 GitHub contributors
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

package net.izemods.izemod.injection.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.izemods.izemod.component.theme.ColorTheme;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TextFieldWidget.class)
public abstract class MixinTextFieldWidget extends ClickableWidget {

    public MixinTextFieldWidget(final int x, final int y, final int width, final int height, final Text message) {
        super(x, y, width, height, message);
    }

    @Redirect(method = "renderWidget", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/TextFieldWidget;drawsBackground()Z"))
    private boolean replaceTextFieldStyle(TextFieldWidget instance, @Local(argsOnly = true) DrawContext context) {
        context.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), ColorTheme.DARK_BLUE);
        return false;
    }

}
