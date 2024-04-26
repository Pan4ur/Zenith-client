package me.gopro336.zenith.feature.toggleable.render;

import me.gopro336.zenith.api.util.font.FontUtil;
import me.gopro336.zenith.asm.mixin.imixin.ICPacketChatMessage;
import me.gopro336.zenith.event.network.PacketReceiveEvent;
import me.gopro336.zenith.event.render.Render2DEvent;
import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.feature.Feature;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.network.play.client.CPacketChatMessage;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

import java.util.ArrayList;
import java.util.List;

/**
 * Todo: finish this module
 */
@AnnotationHelper(name = "KillDisplay", description = "displayes kill messages onscreen like dotgod", category = Category.RENDER)
public class KillDisplay extends Feature {

    @Listener
    public void onChat(PacketReceiveEvent event){
        if (event.getPacket() instanceof CPacketChatMessage){
            if (((CPacketChatMessage) event.getPacket()).getMessage().toLowerCase().contains(mc.player.getName())) {

            }
        }
    }
}
