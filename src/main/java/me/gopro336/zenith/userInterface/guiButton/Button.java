package me.gopro336.zenith.userInterface.guiButton;

import me.gopro336.zenith.api.util.font.FontUtil;

public class Button {
    float X;
    float Y;
    float X2;
    float Y2;
    float width;
    float height;

    boolean toggled;
    boolean hovered;
    String text;

    public Button(String t, float x, float y, float param1, float param2, Enum<?> mod){
        X=x;
        Y=y;
        X2=param1;
        Y2=param2;
        if (mod.equals(model.Width_Height)){
            width=param1;
            height=param2;
        } else {
            width=Math.abs(param1-x);
            height=Math.abs(param2-y);
        }
        toggled = false;
        hovered = false;
        text = t;
    }

    public void render(){
        FontUtil.drawCenteredString(text, X+(width/2), Y+(height/2), 0xffffff);
        //RenderUtils2D.drawRoundedRectOutline();
    }

    public enum model{
        Normal,
        Width_Height;
    }

    public boolean isToggled(){
        return toggled;
    }
    public boolean isHovered(){
        return hovered;
    }
    public void setToggled(boolean in){
        toggled = in;
    }
    @Deprecated
    public void setHovered(boolean in){
        hovered = in;
    }
    public float getWidth(){
        return width;
    }
    public float getHeight(){
        return height;
    }
    public float getX(){
        return X;
    }
    public float getY(){
        return Y;
    }
    public float getX2(){
        return X2;
    }
    public float getY2(){
        return Y2;
    }
}
