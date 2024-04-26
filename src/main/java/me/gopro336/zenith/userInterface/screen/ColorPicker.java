package me.gopro336.zenith.userInterface.screen;

import me.gopro336.zenith.api.util.color.RenderUtil;
import me.gopro336.zenith.api.util.font.FontUtil;
import me.gopro336.zenith.api.util.newRender.RenderUtils2D;
import me.gopro336.zenith.feature.toggleable.Client.ClickGuiFeature;
import me.gopro336.zenith.property.Property;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.io.IOException;

public class ColorPicker extends GuiScreen {

    int height = 201;
    int width = 153;//350;
    int barH = 15;
    public int X = 0;
    public int Y = 0;
    public int dragX = 0;
    public int dragY = 0;
    public boolean open = false;
    private boolean isDragging = false;
    private boolean renderGui = true;
    private final GuiScreen prev;
    private int deltaX;
    private int deltaY;

    public static boolean ldown = false;
    public static boolean lheld = false;
    public static boolean rdown = false;
    public static int mX;
    public static int mY;

    Property<Color> colorProp;
    public static Color finalColor;

    public ColorPicker(Property<Color> color, GuiScreen click){
        colorProp = color;
        prev = (GuiScreen)click;

        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());

        X = (int)((res.getScaledWidth() / 2) - width / 2.0F);
        Y = (int)((res.getScaledHeight() / 2) - height / 2.0F);

        ldown = false;
        lheld = false;
        rdown = false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        if (isDragging) {

            X = mouseX - this.deltaX;
            Y = mouseY - this.deltaY;
        }

        if (renderGui) prev.drawScreen(-1, -1, partialTicks);

        RenderUtils2D.drawRect(X, Y+barH, X+width, Y+height, new Color(30, 30, 30, 160).getRGB());
        //RenderUtils2D.drawRect(X, Y, X+width, Y+20, 0xFF5733FF);
        RenderUtils2D.drawRect(X, Y, X+width, Y+barH, ClickGuiFeature.color.getValue().getRGB());

        if (ClickGuiFeature.thin.getValue()) {
            RenderUtil.drawRectOutline(X, Y, X + width, Y+height, 0.5d, new Color((Math.min(ClickGuiFeature.color.getValue().getRed() + 10, 255)), (Math.min(ClickGuiFeature.color.getValue().getGreen() + 10, 255)), (Math.min(ClickGuiFeature.color.getValue().getBlue() + 10, 255)), (Math.min(ClickGuiFeature.color.getValue().getAlpha() + 10, 255))).getRGB());
        }
        else {
            RenderUtil.drawRectOutline(X, Y, X + width, Y+height, 1d, new Color((Math.min(ClickGuiFeature.color.getValue().getRed() + 10, 255)), (Math.min(ClickGuiFeature.color.getValue().getGreen() + 10, 255)), (Math.min(ClickGuiFeature.color.getValue().getBlue() + 10, 255)), (Math.min(ClickGuiFeature.color.getValue().getAlpha() + 10, 255))).getRGB());
        }

        if (renderGui){
            RenderUtil.drawRectOutline(X+width-60, Y+1, X+width-10, Y+14, 0.5, 0xffffffff);
            FontUtil.drawCenteredString("Hide Gui", X+width-35, Y+2, 0xffffffff);
        } else {
            RenderUtil.drawRectOutline(X+width-60, Y+3, X+width-10, Y+17, 0.5, 0xffffffff);
            FontUtil.drawCenteredString("Show Gui", X+width-35, Y+5, 0xffffffff);
        }

        FontUtil.drawCenteredString(colorProp.getName(), X+(width/2), Y+5, 0xffffffff);

        drawColourPicker(colorProp, X+3, Y+barH+3, mouseX, mouseY);
        mouseListen(mouseX, mouseY);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    //@Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (isHover(X+width-60, Y+3, 50, 17, mouseX, mouseY)&& mouseButton==0){
            renderGui = !renderGui;
        }

            //if (mouseX > this.x && mouseX < this.x + this.guiWidth && mouseY > this.y && mouseY < this.y + this.totalY) {
        if (isHover(X, Y, width, height, mouseX, mouseY)) {

            if (mouseY < Y + barH) {
                deltaX = mouseX - X;
                deltaY = mouseY - Y;
                isDragging = true;
            }

            //this.items.forEach(item -> item.clicked(mouseButton));

        } else if (!isDragging) {

            mc.displayGuiScreen(prev);

        }

        if (mouseButton == 0) {
            lclickListen();
        }
        if (mouseButton == 1) {
            rclickListen();
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (state == 0) {
            releaseListen();
        }
        isDragging = false;
    }

    public int[] getInitialLocation(){
        int[] cords = {0, 0};
        cords[0] = (new ScaledResolution(mc).getScaledWidth()/2)-(width/2);
        cords[1] = (new ScaledResolution(mc).getScaledHeight()/2)-(height/2);
        return cords;
    }

    private boolean isHover(int X, int Y, int W, int H, int mX, int mY) {
        return mX >= X && mX <= X + W && mY >= Y && mY <= Y + H;
    }

    public void setDragging(boolean drag) {
        this.isDragging = drag;
    }

    public void close(){
        //ClickGUI.isFocused = true;
        open = false;
    }

    public void open(){
        int[] cords = getInitialLocation();
        X = cords[0];
        Y = cords[1];
        open = true;
    }

    public boolean isOpen(){
        return open;
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

        int pickerWidth = 150;
        int pickerHeight = 150;

        int hueSliderWidth = 150;
        int hueSliderHeight = 10;

        int alphaSliderWidth = 150;
        int alphaSliderHeight = 10;

        if (lheld && mouseOver(pickerX, pickerY, pickerX + pickerWidth, pickerY + pickerHeight)) {
            pickingColour = true;
        }

        if (lheld && mouseOver(hueSliderX, hueSliderY, hueSliderX + hueSliderWidth, hueSliderY + hueSliderHeight)) {
            pickingHue = true;
        }

        if (lheld && mouseOver(alphaSliderX, alphaSliderY, alphaSliderX + alphaSliderWidth, alphaSliderY + alphaSliderHeight)) {
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
        //Gui.drawRect(pickerX - 3, pickerY - 2, pickerX + pickerWidth + 2, pickerY + pickerHeight + 30, new Color(20, 20, 20, ClickGuiModule.backalpha.getValue()).getRGB());
        //Gui.drawRect(pickerX - 2, pickerY - 2, pickerX + pickerWidth + 2, pickerY + pickerHeight + 30, 0xFF212121);

        int selectedColor = Color.HSBtoRGB(color[0], 1.0f, 1.0f);

        float selectedRed = (selectedColor >> 16 & 0xFF) / 255.0f;
        float selectedGreen = (selectedColor >> 8 & 0xFF) / 255.0f;
        float selectedBlue = (selectedColor & 0xFF) / 255.0f;

        RenderUtils2D.drawPickerBase(pickerX, pickerY, pickerWidth, pickerHeight, selectedRed, selectedGreen, selectedBlue, colourProperty.getAlpha());

        drawHueSlider(hueSliderX, hueSliderY, hueSliderWidth, hueSliderHeight, color[0]);

        int cursorX = (int) (pickerX + color[1] * pickerWidth);
        int cursorY = (int) ((pickerY + pickerHeight) - color[2] * pickerHeight);

        //Gui.drawRect(cursorX - 2, cursorY - 2, cursorX + 2, cursorY + 2, -1);
        Gui.drawRect(cursorX - 2, cursorY - 2, cursorX + 2, cursorY + 2, 0xffffffff);

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
        drawPicker(Property, mouseX, mouseY, x, y, x, y + 153, x, y + 166);
        Property.setValue(finalColor);
    }

    public static void lclickListen() {
        ldown = true;
        lheld = true;
    }

    public static void rclickListen() {
        rdown = true;
    }

    public static void releaseListen() {
        lheld = false;
    }

    public static void mouseListen(int mouseX, int mouseY) {
        mX = mouseX;
        mY = mouseY;
        ldown = false;
        rdown = false;
    }

    public static boolean mouseOver(int minX, int minY, int maxX, int maxY) {
        return mX >= minX && mY >= minY && mX <= maxX && mY <= maxY;
    }
}
