package me.gopro336.zenith.managment;

import me.gopro336.zenith.Zenith;
import me.gopro336.zenith.api.util.IGlobals;
import me.gopro336.zenith.event.network.PacketReceiveEvent;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.util.math.MathHelper;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

import java.util.Arrays;

public class TpsManager implements IGlobals
{

    static final float[] ticks = new float[20];

    private long last_update_tick;
    private int next_index = 0;

    public TpsManager() {
        Zenith.INSTANCE.getEventManager().addEventListener(this);
    }

    @Listener
    public void onPacketSend(PacketReceiveEvent event) {
        if (event.getPacket() instanceof SPacketTimeUpdate) {
            update_time();
        }
    }

    public float get_tick_rate() {
        float num_ticks = 0.0f;
        float sum_ticks = 0.0f;

        for (float tick : ticks) {
            if (tick > 0.0f) {
                sum_ticks += tick;
                num_ticks += 1.0f;
            }
        }

        return MathHelper.clamp(sum_ticks / num_ticks, 0.0f, 20.0f);
    }

    public void reset_tick() {
        this.next_index       = 0;
        this.last_update_tick = -1L;

        Arrays.fill(ticks, 0.0f);
    }

    public void update_time() {
        if (this.last_update_tick != -1L) {
            float time = (float) (System.currentTimeMillis() - this.last_update_tick) / 1000.0f;
            ticks[(this.next_index % ticks.length)] = MathHelper.clamp(20.0f / time, 0.0f, 20.0f);

            this.next_index += 1;
        }

        this.last_update_tick = System.currentTimeMillis();
    }
}

