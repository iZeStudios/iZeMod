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
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.option.ServerList;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DirectConnectScreen.class)
public abstract class MixinDirectConnectScreen extends Screen {
    private static final @Unique String[] serverSaved = {"", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
    private static @Unique int serverCurrentIdx = 0;

    private final ButtonWidget[] serverSavedButtons = new ButtonWidget[15];
    private @Unique MultiplayerServerListWidget serverWidget;
    private @Unique ServerList dummyServerList;

    private @Shadow TextFieldWidget addressField;

    @Shadow
    @Final
    private Screen parent;

    @Shadow
    @Final
    private ServerInfo serverEntry;

    protected MixinDirectConnectScreen(final Text title) {
        super(title);
    }

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void injectInit_dummyScreenInit(CallbackInfo ci) {
        MultiplayerScreen dummyScreen = parent instanceof MultiplayerScreen screen ? screen : new MultiplayerScreen(this);
        serverWidget = new MultiplayerServerListWidget(dummyScreen, MinecraftClient.getInstance(), 0, 0, 0, 0);
        dummyServerList = new ServerList(MinecraftClient.getInstance());
        dummyServerList.add(new ServerInfo("", "", ServerInfo.ServerType.OTHER), false);
        setEntry(serverSaved[serverCurrentIdx]);
    }

    private @Unique void setEntry(String address) {
        dummyServerList.set(0, new ServerInfo(address, address, ServerInfo.ServerType.OTHER));
        serverWidget.setServers(dummyServerList);
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
            this.addDrawableChild(serverSavedButtons[i] = ButtonWidget.builder(Text.literal(String.valueOf(finalI + 1)), button -> {
                    addressField.setText(serverSaved[serverCurrentIdx = finalI]);
                    setEntry(this.addressField.getText());
                })
                .position(addressField.getX() + (20 * i), addressField.getY() + addressField.getHeight())
                .size(20, 20)
                .tooltip(Tooltip.of(serverSaved[i].isBlank() ? Text.empty() : Text.literal(serverSaved[i])))
                .build());
        }
        updateButtons();
    }

    @Redirect(method = "render", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)I",
        shift = At.Shift.BY
    ))
    public int injectRender_fixTitle(DrawContext instance, TextRenderer font, Text text, int x, int y, int color) {
        return instance.drawTextWithShadow(font, text, this.addressField.getX(), this.addressField.getY() - font.fontHeight, color);
    }

    @Inject(method = "render", at = @At(value = "HEAD"))
    public void injectRender_pinger_1(
        DrawContext context,
        int mouseX, int mouseY,
        float delta, CallbackInfo ci
    ) {
        serverSaved[serverCurrentIdx] = addressField.getText();
    }

    @Inject(method = "keyPressed", at = @At(value = "HEAD"))
    public void injectKeyPressed_entry(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        MultiplayerServerListWidget.SERVER_PINGER_THREAD_POOL.getQueue().clear();
    }

    @Inject(method = "onAddressFieldChanged", at = @At(value = "HEAD"))
    public void injectOnAddressFieldChanged_updateEntry(CallbackInfo ci) {
        setEntry(this.addressField.getText());
    }

    @Inject(method = "onAddressFieldChanged", at = @At(value = "RETURN"))
    public void injectOnAddressFieldChanged_updateButtons(CallbackInfo ci) {
        updateButtons();
    }

    public void updateButtons() {
        for (int i = 0; i < serverSavedButtons.length; i++) {
            ButtonWidget button = serverSavedButtons[i];
            if (button == null) break;
            button.setMessage(Text.literal(button.getMessage().getString())
                .withColor(TextColor.fromFormatting(i == serverCurrentIdx ? Formatting.GREEN :
                    serverSaved[i].isBlank() ? Formatting.DARK_GRAY :
                        Formatting.WHITE).getRgb()));
        }
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
    public void injectRender_pinger_2(
        DrawContext context,
        int mouseX, int mouseY,
        float delta, CallbackInfo ci
    ) {
        int gradientX = this.addressField.getX(), gradientX1 = this.addressField.getX() + this.addressField.getWidth();
        int gradientY = this.addressField.getY() / 2 - 13 - 41 + 20, gradientY1 = this.addressField.getY() / 2 - 13 + 41 + 20;
        // the background of our pinger
        context.fillGradient(
            gradientX, gradientY,
            gradientX1, gradientY1,
            0xFF318e9e, 0xFF2b8495
        );

        if (serverWidget.getFirst() != null && serverWidget.getFirst() instanceof MultiplayerServerListWidget.ServerEntry entry) {
            ServerInfo server = entry.getServer();
            serverWidget.renderEntry(
                context, mouseX, mouseY, delta, 0,
                gradientX, gradientY,
                gradientX1 - gradientX, gradientY1 - gradientY
            );
            int red = TextColor.fromFormatting(Formatting.RED).getRgb(), turquoise = TextColor.fromFormatting(Formatting.AQUA).getRgb();
            context.drawText(client.textRenderer, Text
                    .literal("Version: ").withColor(red)
                    .append(server == null ? Text.empty() : Text.literal(server.version.getString()).withColor(turquoise)),
                gradientX + 4, gradientY + 32 + client.textRenderer.fontHeight, -1, false);
            context.drawText(client.textRenderer, Text
                    .literal("Protokoll: ").withColor(red)
                    .append(server == null ? Text.empty() : Text.literal(String.valueOf(server.protocolVersion)).withColor(turquoise)),
                gradientX + 4, gradientY + 32 + (client.textRenderer.fontHeight * 2), -1, false);
            context.drawText(client.textRenderer, Text
                    .literal("Ping: ").withColor(red)
                    .append(server == null ? Text.empty() : Text.literal(String.valueOf(server.ping)).withColor(turquoise)),
                gradientX + 4, gradientY + 32 + (client.textRenderer.fontHeight * 3), -1, false);
            context.drawText(client.textRenderer, Text
                    .literal("Spieler: ").withColor(red)
                    .append(server == null || server.players == null ? Text.empty() : Text.literal("%d".formatted(server.players.online())).withColor(turquoise))
                    .append(server == null ? Text.empty() : Text.literal("/").withColor(TextColor.fromFormatting(Formatting.DARK_GRAY).getRgb()))
                    .append(server == null || server.players == null ? Text.empty() : Text.literal("%d".formatted(server.players.max())).withColor(turquoise)),
                gradientX + 4, gradientY + 32 + (client.textRenderer.fontHeight * 4), -1, false);
        }
    }
}
