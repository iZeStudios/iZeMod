package net.izestudios.izemod.injection.mixin;

import net.izestudios.izemod.IzeModImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.TransferState;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConnectScreen.class)
public class MixinConnectScreen {
    @Inject(method = "startConnecting",at = @At("TAIL"))
    private static void startConnecting(Screen screen, Minecraft minecraft, ServerAddress serverAddress, ServerData serverData, boolean bl, TransferState transferState, CallbackInfo ci) {
        final IzeModImpl instance = IzeModImpl.INSTANCE;

        instance.lastServer = serverData;
    }
}
