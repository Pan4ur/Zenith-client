package me.gopro336.zenith.feature.toggleable.render;

import me.gopro336.zenith.event.network.PacketSendEvent;
import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.feature.Feature;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

/**
 * dumb shit wtf am I thinking
 */
@AnnotationHelper(name = "PacketRender", category = Category.RENDER)
public class RotationViewer extends Feature {
    private static float yaw = 0;
    private static float pitch = 0;

    @Listener
    public void onPacketSend(PacketSendEvent event) {

    }

    public static float getYaw() {
        return yaw;
    }

    public static float getPitch() {
        return pitch;
    }

    public static void setYaw(float yaw) {
        RotationViewer.yaw = yaw;
    }

    public static void setPitch(float pitch) {
        RotationViewer.pitch = pitch;
    }
}