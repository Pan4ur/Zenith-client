package me.gopro336.zenith.userInterface.clickgui.button.buttons;

import me.gopro336.zenith.Zenith;
import me.gopro336.zenith.api.util.ColorUtils;
import me.gopro336.zenith.api.util.font.FontUtil;
import me.gopro336.zenith.api.util.newRender.RenderUtils2D;
import me.gopro336.zenith.userInterface.screen.ColorPicker;
import me.gopro336.zenith.userInterface.clickgui.button.SettingComponent;
import me.gopro336.zenith.feature.toggleable.Client.ClickGuiFeature;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.property.Property;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class ColorSettingComponent/* extends SettingComponent */{
/*
    private Property<Color> value;
    private boolean pickerOpen;

    public ColorSettingComponent(Feature feature, Property<Color> value, int X, int Y, int W, int H, boolean isSub)
    {
        super(feature, X, Y, W, H, isSub);
        this.value = value;
        pickerOpen = false;
    }

//    final ColorPicker picker = new ColorPicker(value);

    public static Color finalColor;

    @Override
    public void onRender(int mouseX, int mouseY, float partialTicks) {
        super.onRender(mouseX, mouseY, partialTicks);
        preComponentRender(false);

        RenderUtils2D.drawRect(getPosX()+ getWidth()-13-1, getPosY()+1, getPosX()+ getWidth()-1, getPosY()+ getHeight()-1, ColorUtils.changeAlpha(value.getValue().getRGB(), 255));

        FontUtil.drawString(value.getName(), (float) (getPosX() + 6)+(isSubSetting ? 3 : 0), (float) (getPosY() + 4));

        if (isWithinBuffer(mouseX, mouseY)) {
            if (GuiUtil.ldown) {
                pickerOpen = false;
            }
            if (GuiUtil.rdown) {
                mc.displayGuiScreen(new ColorPicker(value, mc.currentScreen));

                pickerOpen = true;
            }
        }

        //if (pickerOpen) drawColourPicker(value, getX()+getW(), getY(), mX, mY);

        /*if (GuiUtil.ldown){
            picker.dragX = mX - picker.X;
            picker.dragY = mY - picker.Y;
        }*/

        //if (picker.isOpen()) picker.onRender();

  /*      if (isSubSetting) RenderUtils2D.drawRect(getPosX()+2, getPosY(), getPosX()+3, getPosY()+ getHeight(), ClickGuiFeature.accentColor.getValue().getRGB());


        if (ClickGuiFeature.dot.getValue() && getSubCount()>0) {
            FontUtil.drawString("...", (float) ((getPosX() + getWidth() - 3) - FontUtil.getStringWidth("...")), (float) (getPosY() + 4), new Color(255, 255, 255, 255).getRGB());
        }
        postComponentRender();
    }

    public static void drawPicker(Property<Color> colourProperty, int mouseX, int mouseY, int pickerX, int pickerY, int hueSliderX, int hueSliderY, int alphaSliderX, int alphaSliderY) {
        float[] color = new float[] {
                Color.RGBtoHSB(colourProperty.getValue().getRed(), colourProperty.getValue().getGreen(), colourProperty.getValue().getBlue(), null)[0],
                Color.RGBtoHSB(colourProperty.getValue().getRed(), colourProperty.getValue().getGreen(), colourProperty.getValue().getBlue(), null)[1],
                Color.RGBtoHSB(colourProperty.getValue().getRed(), colourProperty.getValue().getGreen(), colourProperty.getValue().getBlue(), null)[2]
        };

        boolean pickingColour = false;
        boolean pickingHue = false;
        boolean pickingAlpha = false;

        int pickerWidth = 100;
        int pickerHeight = 100;

        int hueSliderWidth = 100;
        int hueSliderHeight = 10;

        int alphaSliderWidth = 100;
        int alphaSliderHeight = 10;

        if (GuiUtil.lheld && GuiUtil.mouseOver(pickerX, pickerY, pickerX + pickerWidth, pickerY + pickerHeight)) {
            pickingColour = true;
        }

        if (GuiUtil.lheld && GuiUtil.mouseOver(hueSliderX, hueSliderY, hueSliderX + hueSliderWidth, hueSliderY + hueSliderHeight)) {
            pickingHue = true;
        }

        if (GuiUtil.lheld && GuiUtil.mouseOver(alphaSliderX, alphaSliderY, alphaSliderX + alphaSliderWidth, alphaSliderY + alphaSliderHeight)) {
            pickingAlpha = true;
        }

        if (pickingHue) {
            float restrictedX = (float) Math.min(Math.max(hueSliderX, mouseX), hueSliderX + hueSliderWidth);
            color[0] = (restrictedX - (float) hueSliderX) / hueSliderWidth;
        }

        if (pickingAlpha) {
            float restrictedX = (float) Math.min(Math.max(alphaSliderX, mouseX), alphaSliderX + alphaSliderWidth);
            //colourProperty.setValue(getColorFromHex(ColorUtils.changeAlpha(colourProperty.getValue().getRGB(), (int)(1 - (restrictedX - (float) alphaSliderX) / alphaSliderWidth))));
            colourProperty.setAlpha(1 - (restrictedX - (float) alphaSliderX) / alphaSliderWidth);
        }

        if (pickingColour) {
            float restrictedX = (float) Math.min(Math.max(pickerX, mouseX), pickerX + pickerWidth);
            float restrictedY = (float) Math.min(Math.max(pickerY, mouseY), pickerY + pickerHeight);

            color[1] = (restrictedX - (float) pickerX) / pickerWidth;
            color[2] = 1 - (restrictedY - (float) pickerY) / pickerHeight;
        }

        //Gui.drawRect(pickerX - 3, pickerY - 2, pickerX + pickerWidth + 2, pickerY + pickerHeight + 30, Colours.clientColourPicker.getValue().getRGB());
        Gui.drawRect(pickerX - 3, pickerY - 2, pickerX + pickerWidth + 2, pickerY + pickerHeight + 30, new Color(20, 20, 20, ClickGuiFeature.backalpha.getValue()).getRGB());
        Gui.drawRect(pickerX - 2, pickerY - 2, pickerX + pickerWidth + 2, pickerY + pickerHeight + 30, 0xFF212121);

        int selectedColor = Color.HSBtoRGB(color[0], 1.0f, 1.0f);

        float selectedRed = (selectedColor >> 16 & 0xFF) / 255.0f;
        float selectedGreen = (selectedColor >> 8 & 0xFF) / 255.0f;
        float selectedBlue = (selectedColor & 0xFF) / 255.0f;

        RenderUtils2D.drawPickerBase(pickerX, pickerY, pickerWidth, pickerHeight, selectedRed, selectedGreen, selectedBlue, colourProperty.getAlpha());

        drawHueSlider(hueSliderX, hueSliderY, hueSliderWidth, hueSliderHeight, color[0]);

        int cursorX = (int) (pickerX + color[1] * pickerWidth);
        int cursorY = (int) ((pickerY + pickerHeight) - color[2] * pickerHeight);

        Gui.drawRect(cursorX - 2, cursorY - 2, cursorX + 2, cursorY + 2, -1);

        drawAlphaSlider(alphaSliderX, alphaSliderY, alphaSliderWidth, alphaSliderHeight, selectedRed, selectedGreen, selectedBlue, colourProperty.getAlpha());

        finalColor = integrateAlpha(new Color(Color.HSBtoRGB(color[0], color[1], color[2])), colourProperty.getAlpha());
    }

    public static Color getColorFromHex(int hexColor) {
        int r = (hexColor & 0xFF0000) >> 16;
        int g = (hexColor & 0xFF00) >> 8;
        int b = (hexColor & 0xFF);
        return new Color(r, g, b);
    }

    public static Color integrateAlpha(Color color, float alpha) {
        float red = (float) color.getRed() / 255;
        float green = (float) color.getGreen() / 255;
        float blue = (float) color.getBlue() / 255;

        //if (alpha>255) return new Color(red, green, blue, 255);
        return new Color(red, green, blue, alpha);
    }

    public static void drawHueSlider(int x, int y, int width, int height, float hue) {
        int step = 0;

        if (height > width) {
            Gui.drawRect(x, y, x + width, y + 4, 0xFFFF0000);

            y += 4;

            for (int colorIndex = 0; colorIndex < 5; colorIndex++) {
                int previousStep = Color.HSBtoRGB((float) step / 5, 1.0f, 1.0f);
                int nextStep = Color.HSBtoRGB((float) (step + 1) / 5, 1.0f, 1.0f);

                RenderUtils2D.drawGradientRect(x, y + step * (height / 5), x + width, y + (step + 1) * (height / 5), previousStep, nextStep);

                step++;
            }

            int sliderMinY = (int) (y + (height * hue)) - 4;

            Gui.drawRect(x, sliderMinY - 1, x + width, sliderMinY + 1, -1);
        } else {
            for (int colorIndex = 0; colorIndex < 5; colorIndex++) {
                int previousStep = Color.HSBtoRGB((float) step / 5, 1.0f, 1.0f);
                int nextStep = Color.HSBtoRGB((float) (step + 1) / 5, 1.0f, 1.0f);

                RenderUtils2D.gradient(x + step * (width / 5), y, x + (step + 1) * (width / 5), y + height, previousStep, nextStep, true);

                step++;
            }

            int sliderMinX = (int) (x + (width * hue));

            Gui.drawRect(sliderMinX - 1, y, sliderMinX + 1, y + height, -1);
        }
    }

    public static void drawAlphaSlider(int x, int y, int width, int height, float red, float green, float blue, float alpha) {
        boolean left = true;

        int checkerBoardSquareSize = height / 2;

        for (int squareIndex = -checkerBoardSquareSize; squareIndex < width; squareIndex += checkerBoardSquareSize) {
            if (!left) {
                Gui.drawRect(x + squareIndex, y, x + squareIndex + checkerBoardSquareSize, y + height, 0xFFFFFFFF);
                Gui.drawRect(x + squareIndex, y + checkerBoardSquareSize, x + squareIndex + checkerBoardSquareSize, y + height, 0xFF909090);

                if (squareIndex < width - checkerBoardSquareSize) {
                    int minX = x + squareIndex + checkerBoardSquareSize;
                    int maxX = Math.min(x + width, x + squareIndex + checkerBoardSquareSize * 2);

                    Gui.drawRect(minX, y, maxX, y + height, 0xFF909090);
                    Gui.drawRect(minX,y + checkerBoardSquareSize, maxX, y + height, 0xFFFFFFFF);
                }
            }

            left = !left;
        }

        RenderUtils2D.drawLeftGradientRect(x, y, x + width, y + height, new Color(red, green, blue, 1).getRGB(), 0);

        int sliderMinX = (int) (x + width - (width * alpha));

        Gui.drawRect(sliderMinX - 1, y,  sliderMinX + 1, y + height, -1);
    }

    public static void drawColourPicker(Property<Color> Property, int x, int y, int mouseX, int mouseY) {
        //drawPicker(Property, mouseX, mouseY, x + 3, y + height + (boost * height) + 2, x + 3, y + height + (boost * height) + 103, x + 3, y + height + (boost * height) + 115);
        drawPicker(Property, mouseX, mouseY, x + 3, y + 3, x + 3, y + 103, x + 3, y + 115);
        Property.setValue(finalColor);
    }

    @Override
    public void onUpdate() {
        setHidden(!value.isVisible());
    }

    @Override
    public Property<?> getProperty(){
        return value;
    }*/
}
