package com.game.layers;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.game.Constants;

public class Background {
    private Bitmap back11, back12;
    private Bitmap back21, back22;
    private Bitmap back31, back32;
    private float xBack11, xBack12, speedBack1;
    private float xBack21, xBack22, speedBack2;
    private float xBack31, xBack32, speedBack3;

    public Background(Bitmap back1, Bitmap back2, Bitmap back3){
        this.back11 = back1;
        this.back12 = back1;

        this.back21 = back2;
        this.back22 = back2;

        this.back31 = back3;
        this.back32 = back3;

        xBack11 = 0; xBack21 = 0; xBack31 = 0;
        xBack12 = Constants.WIDTH - 5; xBack22 = Constants.WIDTH - 5; xBack32 = Constants.WIDTH - 5;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(back11, xBack11, 0, null);
        canvas.drawBitmap(back12, xBack12, 0, null);

        canvas.drawBitmap(back21, xBack21, 0, null);
        canvas.drawBitmap(back22, xBack22, 0, null);

        canvas.drawBitmap(back31, xBack31, 0, null);
        canvas.drawBitmap(back32, xBack32, 0, null);
    }

    public void update(){
        xBack11 -= speedBack1;
        xBack12 -= speedBack1;
        if(xBack12 <= -back12.getWidth()) xBack12 = Constants.WIDTH - 10;
        if(xBack11 <= -back11.getWidth()) xBack11 = Constants.WIDTH - 10;

        xBack21 -= speedBack2;
        xBack22 -= speedBack2;
        if(xBack22 <= -back22.getWidth()) xBack22 = Constants.WIDTH - 10;
        if(xBack21 <= -back21.getWidth()) xBack21 = Constants.WIDTH - 10;

        xBack31 -= speedBack3;
        xBack32 -= speedBack3;
        if(xBack32 <= -back32.getWidth()) xBack32 = Constants.WIDTH - 10;
        if(xBack31 <= -back31.getWidth()) xBack31 = Constants.WIDTH - 10;
    }

    public void setSpeed(float speed) {
        this.speedBack1 = (float) (0.18 * speed);
        this.speedBack2 = (float) (0.26 * speed);
        this.speedBack3 = (float) (0.34 * speed);
    }
}
