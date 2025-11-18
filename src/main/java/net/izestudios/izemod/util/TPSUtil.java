package net.izestudios.izemod.util;

import net.minecraft.util.Mth;

public final class TPSUtil {
    private static final TPSUtil INSTANCE = new TPSUtil();
    private long lastTimestamp = -1;
    private float tps = 20.0f;

    public void onTimeUpdate() {
        final long now = System.currentTimeMillis();
        if (this.lastTimestamp != -1) {
            final long timeDiff = now - this.lastTimestamp;
            final float calculatedTps = 20.0f / (timeDiff / 1000.0f);

            this.tps = Mth.clamp(calculatedTps, 0.0f, 20.0f);
        }
        this.lastTimestamp = now;
    }

    public float getTps() {
        return this.tps;
    }
    public static TPSUtil getInstance() {
        return INSTANCE;
    }
}
