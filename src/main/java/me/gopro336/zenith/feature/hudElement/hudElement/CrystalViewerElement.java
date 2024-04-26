package me.gopro336.zenith.feature.hudElement.hudElement;

import me.gopro336.zenith.api.util.newRender.RenderUtils2D;
import me.gopro336.zenith.feature.hudElement.Element;
import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.property.NumberProperty;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.item.EntityEnderCrystal;

@AnnotationHelper(name = "CrystalViewer", category = Category.HUD)
public class CrystalViewerElement extends Element {

    public NumberProperty<Integer> scale = new NumberProperty<>(this, "Scale", "", 0, 30, 100);

    EntityEnderCrystal crystal;

    public CrystalViewerElement() {
        this.crystal = new EntityEnderCrystal(mc.world);
        this.crystal.setShowBottom(false);
    }

    @Override
    public void onRender()
    {
        if (mc.world  == null)
            return;

        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);

        RenderUtils2D.drawEntityOnScreen(this.x + 28, this.y + 67, scale.getValue(), this.y + 13, crystal);

        GlStateManager.enableRescaleNormal();
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();

        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    }
}
