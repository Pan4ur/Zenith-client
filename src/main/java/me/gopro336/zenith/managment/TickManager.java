package me.gopro336.zenith.managment;

import me.gopro336.zenith.Zenith;
import me.gopro336.zenith.api.util.IGlobals;
import me.gopro336.zenith.api.util.MathUtil;
import me.gopro336.zenith.event.network.PacketReceiveEvent;
import me.gopro336.zenith.asm.mixin.imixin.IMinecraft;
import me.gopro336.zenith.asm.mixin.imixin.ITimer;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.util.math.MathHelper;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

public class TickManager implements IGlobals {

    public long prevTime;
    public float[] TPS = new float[20];
    public int currentTick;

    public TickManager() {
        prevTime = -1;

        for (int i = 0, len = TPS.length; i < len; i++) {
            TPS[i] = 0;
        }

        Zenith.INSTANCE.getEventManager().addEventListener(this);
    }

    public float getTPS(TPS tps) {
        switch (tps) {
            case CURRENT:
                return mc.isSingleplayer() ? 20 : (float) MathUtil.roundDouble(MathHelper.clamp(TPS[0], 0, 20), 2);
            case AVERAGE:
                int tickCount = 0;
                float tickRate = 0;

                for (float tick : TPS) {
                    if (tick > 0) {
                        tickRate += tick;
                        tickCount++;
                    }
                }

                return mc.isSingleplayer() ? 20 : (float) MathUtil.roundDouble(MathHelper.clamp((tickRate / tickCount), 0, 20), 2);
        }

        return 0;
    }

    @Listener
    public void onPacketReceive(PacketReceiveEvent event) {
        //ChatUtils.error("it works");
        if (event.getPacket() instanceof SPacketTimeUpdate) {
            if (prevTime != -1) {
                TPS[currentTick % TPS.length] = MathHelper.clamp((20 / ((float) (System.currentTimeMillis() - prevTime) / 1000)), 0, 20);
                currentTick++;
            }

            prevTime = System.currentTimeMillis();
        }
    }

    public void setClientTicks(double ticks) {
        ((ITimer) ((IMinecraft) mc).getTimer()).setTickLength((float) (50 / ticks));
    }

    public enum TPS {
        CURRENT, AVERAGE, NONE
    }
}
