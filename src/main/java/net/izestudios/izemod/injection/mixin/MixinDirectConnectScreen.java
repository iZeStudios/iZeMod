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

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.izestudios.izemod.component.multiplayer.ServerPinger;
import net.izestudios.izemod.component.multiplayer.ServerSaveStates;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.DirectConnectScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.ArrayList;
import java.util.List;

@Mixin(DirectConnectScreen.class)
public abstract class MixinDirectConnectScreen extends Screen {

    @Shadow
    private TextFieldWidget addressField;

    @Shadow
    private ButtonWidget selectServerButton;

    @Unique
    private static final int izeMod$PING_INTERVAL = 10_000;

    @Unique
    private final List<ButtonWidget> izeMod$serverStateButtons = new ArrayList<>();

    @Unique
    private int izeMod$serverStateIndex = 0;

    @Unique
    private ServerPinger izeMod$serverPinger;

    @Unique
    private long izeMod$lastPingTime = 0;

    protected MixinDirectConnectScreen(final Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void moveButtons(CallbackInfo ci) {
        addressField.setX(this.width / 2 - 150);
        addressField.setY(146);
        addressField.setWidth(300);

        selectServerButton.setPosition(this.width / 2 - 150, this.height / 4 + 150);
        selectServerButton.setDimensions(147, 20);

        for (final Element element : children()) {
            if (element instanceof ButtonWidget button && button.getMessage().getString().equals(ScreenTexts.CANCEL.getString())) {
                button.setPosition(this.width / 2 + 3, this.height / 4 + 150);
                button.setDimensions(147, 20);
            }
        }

        izeMod$serverStateButtons.clear(); // Just in case
        for (int i = 0; i < ServerSaveStates.STATE_COUNT - 1; i++) {
            final int index = i;
            izeMod$serverStateButtons.add(addDrawableChild(ButtonWidget.builder(Text.of(izeMod$formatServerStateButton(index)), button -> {
                ServerSaveStates.set(izeMod$serverStateIndex, addressField.getText().isBlank() ? null : addressField.getText().trim());
                izeMod$serverStateIndex = index;
                addressField.setText(ServerSaveStates.get(index));
                button.setFocused(false);
                izeMod$lastPingTime = System.currentTimeMillis() - izeMod$PING_INTERVAL - 1;
            }).position(addressField.getX() + (i * 20), addressField.getY() + addressField.getHeight()).size(20, 20).build()));
        }

        izeMod$serverPinger = addDrawableChild(new ServerPinger(this.width / 2 - 150, 51));
        izeMod$updateServerPinger();
    }

    @Inject(method = "onAddressFieldChanged", at = @At("RETURN"))
    private void resetPingTimer(CallbackInfo ci) {
        // Cause the if to be true after one second
        izeMod$lastPingTime = System.currentTimeMillis() - izeMod$PING_INTERVAL - 1;
    }

    @Override
    public void tick() {
        for (int i = 0; i < ServerSaveStates.STATE_COUNT - 1; i++) {
            final ButtonWidget button = izeMod$serverStateButtons.get(i);
            button.setMessage(Text.of(izeMod$formatServerStateButton(i)));
        }

        if (System.currentTimeMillis() - izeMod$lastPingTime > izeMod$PING_INTERVAL) {
            izeMod$updateServerPinger();
            izeMod$lastPingTime = System.currentTimeMillis();
        }
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)I"))
    private int moveAddressTitlePosition(DrawContext instance, TextRenderer textRenderer, Text text, int x, int y, int color, Operation<Integer> original) {
        return original.call(instance, textRenderer, text, addressField.getX(), addressField.getY() - textRenderer.fontHeight - 5, color);
    }

    @Unique
    private String izeMod$formatServerStateButton(final int index) {
        final boolean exists = ServerSaveStates.get(index) != null;
        final boolean selected = izeMod$serverStateIndex == index;
        if (exists) {
            return (selected ? Formatting.GREEN : Formatting.WHITE) + String.valueOf(index + 1);
        } else {
            return (selected ? Formatting.DARK_GREEN : Formatting.DARK_GRAY) + String.valueOf(index + 1);
        }
    }

    @Unique
    private void izeMod$updateServerPinger() {
        if (addressField.getText().isBlank()) {
            return;
        }

        if (izeMod$serverPinger != null) {
            izeMod$serverPinger.updateServer(addressField.getText().trim());
        }
    }

}
