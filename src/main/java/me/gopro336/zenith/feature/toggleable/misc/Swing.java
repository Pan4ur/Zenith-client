package me.gopro336.zenith.feature.toggleable.misc;

import me.gopro336.zenith.event.network.PacketSendEvent;
import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.property.Property;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

@AnnotationHelper(name = "Swing", category = Category.MISC, description = "Swings with your offhand")
public class Swing extends Feature {

    public Property<Enum> mode = new Property<>(this, "Mode", "", Modes.Offhand);
    public Property<Boolean> noAnimation = new Property<>(this, "CancelAnimation", "", true);
    public Property<Boolean> noReset = new Property<>(this, "NoReset", "", false);
    public Property<Boolean> dropSwing = new Property<>(this, "DropSwing", "", false);

    private enum Modes {
        Offhand, Mainhand, Switch
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (Modes.Offhand.equals(mode.getValue())) {
            mc.player.swingingHand = EnumHand.OFF_HAND;
        } else if (Modes.Mainhand.equals(mode.getValue())) {
            mc.player.swingingHand = EnumHand.MAIN_HAND;
        }

        if (mc.gameSettings.keyBindDrop.isPressed() && dropSwing.getValue())
            mc.player.swingArm(EnumHand.MAIN_HAND);
    }

    @Listener
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacket() instanceof CPacketAnimation && noAnimation.getValue())
            event.setCanceled(true);
    }
}
