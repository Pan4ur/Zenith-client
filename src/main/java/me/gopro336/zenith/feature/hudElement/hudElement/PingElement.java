package me.gopro336.zenith.feature.hudElement.hudElement;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gopro336.zenith.feature.hudElement.Element;
import me.gopro336.zenith.api.util.font.FontUtil;
import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.property.NumberProperty;

import java.util.Objects;

@AnnotationHelper(name = "Ping", category = Category.HUD)
public class PingElement extends Element {

    public NumberProperty<Integer> pingOffset = new NumberProperty<>(this, "PingOffset", "", -50, 0, 50);

    public PingElement() {
    }

    @Override
    public void onRender()
    {
        String pingString;
        pingString = "Ping " + ChatFormatting.WHITE + (getPing()+pingOffset.getValue()) + "ms";
        this.setWidth((int) FontUtil.getStringWidth(pingString));
        this.setHeight((int) FontUtil.getFontHeight());
        FontUtil.drawStringWithShadow(pingString, getX(), getY(), -1);
    }

    private int getPing() {
        if (mc.player != null && mc.getConnection() != null && mc.getConnection().getPlayerInfo(mc.player.getName()) != null) {
            return Objects.requireNonNull(mc.getConnection().getPlayerInfo(mc.player.getName())).getResponseTime();
        }

        return -1;
    }
}
