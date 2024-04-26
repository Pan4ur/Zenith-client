package me.gopro336.zenith.feature.hudElement.hudElement;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gopro336.zenith.feature.hudElement.Element;
import me.gopro336.zenith.api.util.font.FontUtil;
import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import net.minecraft.util.math.MathHelper;

@AnnotationHelper(name = "Tps", category = Category.HUD)
public class TpsElement extends Element
{
    private final float[] ticks = new float[20];

    public TpsElement() {
    }

    @Override
    public void onRender()
    {
        String tpsString = "TPS " + ChatFormatting.WHITE + String.format("%.2f", getTickRate());
        FontUtil.drawStringWithShadow(tpsString, getX(), getY(), -1);
        this.setWidth((int) FontUtil.getStringWidth(tpsString));
        this.setHeight((int) FontUtil.getFontHeight());
    }

    private float getTickRate() {
        int tickCount = 0;
        float tickRate = 0.0f;

        for (float tick : this.ticks) {
            if (tick > 0.0f) {
                tickRate += tick;
                tickCount++;
            }
        }

        return MathHelper.clamp((tickRate / tickCount), 0.0f, 20.0f);
    }

}
