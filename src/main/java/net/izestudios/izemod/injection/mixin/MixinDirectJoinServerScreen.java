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
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.DirectJoinServerScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.ArrayList;
import java.util.List;

@Mixin(DirectJoinServerScreen.class)
public abstract class MixinDirectJoinServerScreen extends Screen {

    @Shadow
    private EditBox ipEdit;

    @Shadow
    private Button selectButton;

    @Unique
    private static final int izeMod$PING_INTERVAL = 10_000;

    @Unique
    private final List<Button> izeMod$serverStateButtons = new ArrayList<>();

    @Unique
    private int izeMod$serverStateIndex = 0;

    @Unique
    private ServerPinger izeMod$serverPinger;

    @Unique
    private long izeMod$lastPingTime = 0;

    protected MixinDirectJoinServerScreen(final Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void moveButtons(CallbackInfo ci) {
        ipEdit.setX(this.width / 2 - 150);
        ipEdit.setY(146);
        ipEdit.setWidth(300);

        selectButton.setPosition(this.width / 2 - 150, this.height / 4 + 150);
        selectButton.setSize(147, 20);

        for (final GuiEventListener element : children()) {
            if (element instanceof Button button && button.getMessage().getString().equals(CommonComponents.GUI_CANCEL.getString())) {
                button.setPosition(this.width / 2 + 3, this.height / 4 + 150);
                button.setSize(147, 20);
            }
        }

        izeMod$serverStateButtons.clear(); // Just in case
        for (int i = 0; i < ServerSaveStates.STATE_COUNT - 1; i++) {
            final int index = i;
            izeMod$serverStateButtons.add(addRenderableWidget(Button.builder(Component.nullToEmpty(izeMod$formatServerStateButton(index)), button -> {
                ServerSaveStates.set(izeMod$serverStateIndex, ipEdit.getValue().isBlank() ? null : ipEdit.getValue().trim());
                izeMod$serverStateIndex = index;
                ipEdit.setValue(ServerSaveStates.get(index));
                button.setFocused(false);
                izeMod$lastPingTime = System.currentTimeMillis() - izeMod$PING_INTERVAL - 1;
            }).pos(ipEdit.getX() + (i * 20), ipEdit.getY() + ipEdit.getHeight()).size(20, 20).build()));
        }

        izeMod$serverPinger = addRenderableWidget(new ServerPinger(this.width / 2 - 150, 51));
        izeMod$updateServerPinger();
    }

    @Inject(method = "updateSelectButtonStatus", at = @At("RETURN"))
    private void resetPingTimer(CallbackInfo ci) {
        // Cause the if to be true after one second
        izeMod$lastPingTime = System.currentTimeMillis() - izeMod$PING_INTERVAL - 1;
    }

    @Override
    public void tick() {
        for (int i = 0; i < ServerSaveStates.STATE_COUNT - 1; i++) {
            final Button button = izeMod$serverStateButtons.get(i);
            button.setMessage(Component.nullToEmpty(izeMod$formatServerStateButton(i)));
        }

        if (System.currentTimeMillis() - izeMod$lastPingTime > izeMod$PING_INTERVAL) {
            izeMod$updateServerPinger();
            izeMod$lastPingTime = System.currentTimeMillis();
        }
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)I"))
    private int moveAddressTitlePosition(GuiGraphics instance, Font textRenderer, Component text, int x, int y, int color, Operation<Integer> original) {
        return original.call(instance, textRenderer, text, ipEdit.getX(), ipEdit.getY() - textRenderer.lineHeight - 5, color);
    }

    @Unique
    private String izeMod$formatServerStateButton(final int index) {
        final boolean exists = ServerSaveStates.get(index) != null;
        final boolean selected = izeMod$serverStateIndex == index;
        if (exists) {
            return (selected ? ChatFormatting.GREEN : ChatFormatting.WHITE) + String.valueOf(index + 1);
        } else {
            return (selected ? ChatFormatting.DARK_GREEN : ChatFormatting.DARK_GRAY) + String.valueOf(index + 1);
        }
    }

    @Unique
    private void izeMod$updateServerPinger() {
        if (ipEdit.getValue().isBlank()) {
            return;
        }

        if (izeMod$serverPinger != null) {
            izeMod$serverPinger.updateServer(ipEdit.getValue().trim());
        }
    }

}
