package me.gopro336.zenith.feature.hudElement.hudElement;

import me.gopro336.zenith.feature.hudElement.Element;
import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

@AnnotationHelper(name = "Coords", category = Category.HUD)
public class TotemsElement extends Element
{
    private final float[] ticks = new float[20];

    public TotemsElement() {
        setWidth(16);
        setHeight(16);
    }

    @Override
    public void onRender()
    {
        /*int totems = mc.player.inventory.mainInventory.stream()
                .filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING)
                .mapToInt(ItemStack::getCount)
                .sum();
        if (mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) totems += mc.player.getHeldItemOffhand().stackSize;
        ItemStack items = new ItemStack(Items.TOTEM_OF_UNDYING, totems);
        this.itemrender(items);*/
    }

    private static void preitemrender() {
        GL11.glPushMatrix();
        GL11.glDepthMask(true);
        GlStateManager.clear(256);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.scale(1.0f, 1.0f, 0.01f);
    }

    private static void postitemrender() {
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GL11.glPopMatrix();
    }

    private void itemrender(ItemStack itemStack) {
        preitemrender();
        mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, (int) this.x, (int) this.y);
        mc.getRenderItem().renderItemOverlays(mc.fontRenderer, itemStack, (int) this.x, (int) this.y);
        postitemrender();
    }
}
