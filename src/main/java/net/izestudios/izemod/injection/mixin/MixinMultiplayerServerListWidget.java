package net.izestudios.izemod.injection.mixin;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.util.logging.UncaughtExceptionLogger;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

@Mixin(MultiplayerServerListWidget.class)
public abstract class MixinMultiplayerServerListWidget {
    static @Final
    @Shadow Logger LOGGER;

    @Redirect(method = "<clinit>", at = @At(
        value = "NEW",
        target = "(ILjava/util/concurrent/ThreadFactory;)Ljava/util/concurrent/ScheduledThreadPoolExecutor;"
    ))
    private static ScheduledThreadPoolExecutor injectStatic_improvePingerPool(int corePoolSize, ThreadFactory threadFactory) {
        return new ScheduledThreadPoolExecutor(0, new ThreadFactoryBuilder()
            .setNameFormat("Server Pinger #%d")
            .setThreadFactory(Thread.ofVirtual().factory())
            .setUncaughtExceptionHandler(new UncaughtExceptionLogger(LOGGER)).build());
    }

    @Mixin(MultiplayerServerListWidget.ServerEntry.class)
    public abstract static class ServerEntry {
        @Redirect(method = "render", at = @At(
            value = "INVOKE",
            target = "Ljava/util/concurrent/ThreadPoolExecutor;submit(Ljava/lang/Runnable;)Ljava/util/concurrent/Future;"
        ))
        public Future<Void> injectRender_replaceSubmitWithExec(ThreadPoolExecutor instance, Runnable runnable) {
            instance.execute(runnable);
            return CompletableFuture.completedFuture(null);
        }
    }
}
