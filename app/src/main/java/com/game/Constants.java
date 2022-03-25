package com.game;

import android.content.res.Resources;

public class Constants {
    public static int WIDTH;
    public static int HEIGHT;

    public static int FPS = 60;

    public Constants(){
        WIDTH = Resources.getSystem().getDisplayMetrics().widthPixels;
        HEIGHT = Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}
