package me.gopro336.zenith.feature.hudElement.hudElement;

import me.gopro336.zenith.api.util.font.FontUtil;
import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.feature.command.Command;
import me.gopro336.zenith.feature.hudElement.Element;
import me.gopro336.zenith.property.Property;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumFacing;

import java.awt.*;
import java.text.DecimalFormat;

@AnnotationHelper(name = "Coordinates", description = "Showes your current coordinates", category = Category.HUD)
public class CoordinatesElement extends Element {
    private final Property<Boolean> freecam = new Property<>(this, "FreecamCoords", "", true);
    public Property<Color> textColor = new Property<>(this, "TextColor", "", new Color(255, 85, 255, 255));

    public CoordinatesElement() {
        ScaledResolution displayResolution = new ScaledResolution(Minecraft.getMinecraft());
        setX(4);
        setY(displayResolution.getScaledHeight()-4);
    }

    public void onRender() {
        super.onRender();

        String facing = mc.player.getHorizontalFacing().getName().substring(0, 1).toUpperCase() + mc.player.getHorizontalFacing().getName().substring(1) + Command.SECTIONSIGN + "7 [" + Command.SECTIONSIGN + "r" + getAxis(mc.player.getHorizontalFacing()) + Command.SECTIONSIGN + "7]";
        DecimalFormat df = new DecimalFormat("#.#");
        double x = Double.parseDouble(df.format(freecam.getValue() ? mc.getRenderViewEntity().posX : mc.player.posX));
        double y = Double.parseDouble(df.format(freecam.getValue() ? mc.getRenderViewEntity().posY : mc.player.posY));
        double z = Double.parseDouble(df.format(freecam.getValue() ? mc.getRenderViewEntity().posZ : mc.player.posZ));
        double convertedX = Double.parseDouble(df.format(convertCoords(mc.player.posX)));
        double convertedZ = Double.parseDouble(df.format(convertCoords(mc.player.posZ)));
        String coords = Command.SECTIONSIGN + "7XYZ" + Command.SECTIONSIGN + "r " + x + ", " + y + ", " + z + Command.SECTIONSIGN + "7 [" + Command.SECTIONSIGN + "r" + convertedX + ", " + convertedZ + Command.SECTIONSIGN + "7]";

        float currentWidth = Math.max(FontUtil.getStringWidth(coords), FontUtil.getStringWidth(facing));
        setWidth((int)currentWidth + 1);
        setHeight(FontUtil.getStringHeight() + FontUtil.getStringHeight() + 1);

        FontUtil.drawStringWithShadow(facing, (int) getX(), (int) getY(), textColor.getValue().getRGB());
        FontUtil.drawStringWithShadow(coords, (int) getX(), (int) getY() + FontUtil.getStringHeight(), textColor.getValue().getRGB());
    }

    private double convertCoords(double coord) {
        boolean inHell = (mc.world.getBiome(mc.player.getPosition()).getBiomeName().equals("Hell"));

        return inHell ? coord * 8 : coord / 8;

    }

    private String getAxis(EnumFacing facing) {
        if (facing == EnumFacing.SOUTH) {
            return "+Z";
        } else if (facing == EnumFacing.WEST) {
            return "-X";
        } else if (facing == EnumFacing.NORTH) {
            return "-Z";
        } else if (facing == EnumFacing.EAST) {
            return "+X";
        }

        return null;

    }
}
