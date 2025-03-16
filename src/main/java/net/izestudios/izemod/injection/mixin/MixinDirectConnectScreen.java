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

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.DirectConnectScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DirectConnectScreen.class)
public abstract class MixinDirectConnectScreen extends Screen {
    private @Unique
    static final String[] serversSaved = {"", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
    private @Unique
    static int serversCurrentIdx = 0;

    private @Shadow TextFieldWidget addressField;

    protected MixinDirectConnectScreen(final Text title) {
        super(title);
    }

    @Redirect(method = "init", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;build()Lnet/minecraft/client/gui/widget/ButtonWidget;",
        ordinal = 0
    ))
    public ButtonWidget injectInit_mergeButtons_1(ButtonWidget.Builder instance) {
        return instance.dimensions(this.width / 2 - 150, this.height / 2 + 32 + 20, 148, 20).build();
    }

    @Redirect(method = "init", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;build()Lnet/minecraft/client/gui/widget/ButtonWidget;",
        ordinal = 1
    ))
    public ButtonWidget injectInit_mergeButtons_2(ButtonWidget.Builder instance) {
        return instance.dimensions(this.width / 2 + 2, this.height / 2 + 32 + 20, 148, 20).build();
    }

    @Redirect(method = "init", at = @At(
        value = "NEW",
        target = "(Lnet/minecraft/client/font/TextRenderer;IIIILnet/minecraft/text/Text;)Lnet/minecraft/client/gui/widget/TextFieldWidget;"
    ))
    public TextFieldWidget injectInit_largerTextField(TextRenderer font, int x, int y, int width, int height, Text text) {
        return new TextFieldWidget(font, MinecraftClient.getInstance().getWindow().getScaledWidth() / 2 - 150, this.height / 2 + 10, 300, height, text);
    }

    @Inject(method = "init", at = @At(value = "RETURN"))
    public void injectInit_tempButtons(CallbackInfo ci) {
        // temporary server buttons
        for (int i = 0; i < 15; i++) {
            final int finalI = i;
            this.addDrawableChild(ButtonWidget.builder(Text.literal(String.valueOf(finalI + 1)), button -> {
                serversCurrentIdx = finalI;
                addressField.setText(String.valueOf(finalI));
            }).position(addressField.getX() + (20 * i), addressField.getY() + addressField.getHeight()).size(20, 20).tooltip(Tooltip.of(Text.of(serversSaved[i]))).build());
        }
    }

    @Redirect(method = "render", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)I",
        shift = At.Shift.BY
    ))
    public int injectRender_fixTitle(DrawContext instance, TextRenderer font, Text text, int x, int y, int color) {
        return instance.drawTextWithShadow(font, text, this.addressField.getX(), this.addressField.getY() - font.fontHeight, color);
    }

    /*
    82 x 200
    // Icon + rechts motd
        Server-Version: // der server version string trimmed
        Server-Protocol: // die protokoll id
        Ping zum Server: // der ping zum server in ms
        Spieler Online: // vonGRAU/bis
    // alles türkis
     */
    @Inject(method = "render", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/client/gui/screen/Screen;render(Lnet/minecraft/client/gui/DrawContext;IIF)V",
        shift = At.Shift.AFTER
    ))
    public void injectRender_pinger(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        int gradientX = this.addressField.getX(), gradientX1 = this.addressField.getX() + this.addressField.getWidth();
        int gradientY = this.addressField.getY() / 2 - 13 - 41 + 20, gradientY1 = this.addressField.getY() / 2 - 13 + 41 + 20;
        // the background of our pinger
        context.fillGradient(
            gradientX, gradientY,
            gradientX1, gradientY1,
            0xFF318e9e, 0xFF2b8495
        );
    }
}
