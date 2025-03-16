package net.izestudios.izemod.injection.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.multiplayer.DirectConnectScreen;
import net.minecraft.client.option.ServerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerList.class)
public abstract class MixinServerList {
    @Inject(method = "loadFile", at = @At(value = "HEAD"), cancellable = true)
    public void injectSaveFile_noLoadForDirectConnect(CallbackInfo ci) {
        if (MinecraftClient.getInstance().currentScreen instanceof DirectConnectScreen) {
            ci.cancel();
        }
    }

    @Inject(method = "saveFile", at = @At(value = "HEAD"), cancellable = true)
    public void injectSaveFile_noSaveForDirectConnect(CallbackInfo ci) {
        if (MinecraftClient.getInstance().currentScreen instanceof DirectConnectScreen) {
            ci.cancel();
        }
    }
}
