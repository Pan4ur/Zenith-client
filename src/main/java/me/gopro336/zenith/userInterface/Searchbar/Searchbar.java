package me.gopro336.zenith.userInterface.Searchbar;

import me.gopro336.zenith.userInterface.clickgui.Frame;
import me.gopro336.zenith.userInterface.clickgui.ClickGUI;

import java.util.ArrayList;

public class Searchbar {

    public boolean open = false;
    public float offset = 0;
    public float width = 145;
    public float buffer = 15;

    public void render(){
        if (!open) return;

        /**
         *
         * render searchbar
         */
    }

    /**
     * shift gui
     * for loop to see if we need to shift gui pannels
     */
    public void init(){

        ArrayList<Frame> moveList = new ArrayList<Frame>();

        for (Frame f : ClickGUI.getFrames()){
            if (f.getPosX()<=width+buffer) moveList.add(f);
        }
        /*
        AnimationUtil can only be used in render events (it relies on constantly being called)
        for (WindowRW f : moveList){
            AnimationUtil.expand()
        }*/
    }

    public void open(){
        /**
         * incorperate animations here.
         */
        open = true;
    }

    public void close(){
        /**
         * incorperate animations here.
         */
        open = false;
    }
}
