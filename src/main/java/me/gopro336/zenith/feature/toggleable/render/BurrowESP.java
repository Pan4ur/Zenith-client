package me.gopro336.zenith.feature.toggleable.render;

import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.property.NumberProperty;
import me.gopro336.zenith.property.Property;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@AnnotationHelper(name = "BurrowESP", description = "Shows blocks that players are burrowed in", category = Category.RENDER)
public final class BurrowESP extends Feature {
    public final Property<Boolean> renderOwn = new Property<>(this, "Render Own", "Renders your own burrow block", false);
    public final NumberProperty<Integer> range = new NumberProperty<>(this, "Range", "The range to search for burrow blocks in", 1, 5, 10);

    public final Property<Boolean> renderBlock = new Property<>(this, "Render", "Allows the burrow blocks to be rendered", true);

    public final Property<RenderModes> renderMode = new Property<>(this, "Render Mode", "The type of box to render", RenderModes.Full);
    public final NumberProperty<Double> outlineWidth = new NumberProperty<>(this, "Outline Width", "The width of the outline", 1.0, 2.0, 5.0);
    public final Property<Color> renderColour = new Property<>(this, "Render Colour", "The colour for the burrow blocks", new Color(91, 79, 208, 220));

    public BurrowESP() {

    }

    private final List<BlockPos> burrowBlocksList = new ArrayList<>();

    @Override
    public void onEnable() {
        if (nullCheck()) return;

        burrowBlocksList.clear();
    }

    @Override
    public void onDisable() {
        if (nullCheck()) return;

        burrowBlocksList.clear();
    }

    public void onUpdate() {
        if (nullCheck()) return;

        burrowBlocksList.clear();

        for (EntityPlayer entityPlayer : mc.world.playerEntities) {
            if (entityPlayer.getDistance(mc.player) <= range.getValue()) {
                if (entityPlayer == mc.player && renderOwn.getValue()) {
                    if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock() == Blocks.OBSIDIAN) {
                        burrowBlocksList.add(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ));
                    }
                } else {
                    if (entityPlayer != mc.player && !renderOwn.getValue()) {
                        if (mc.world.getBlockState(new BlockPos(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ)).getBlock() == Blocks.OBSIDIAN) {
                            burrowBlocksList.add(new BlockPos(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ));
                        }
                    }
                }
            }
        }
    }

    @Listener
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (nullCheck()) return;

        if (renderBlock.getValue()) {
            for (BlockPos burrowBlock : burrowBlocksList) {
                if (burrowBlock != null) {
                    GL11.glLineWidth(outlineWidth.getValue().floatValue());

                    //RenderUtil.draw(burrowBlock, renderMode.getValue() != RenderModes.Outline, renderMode.getValue() != RenderModes.Box, 0, 0, renderColour.getValue());
                }
            }
        }
    }

    public enum RenderModes {
        Box,
        Outline,
        Full
    }
}
