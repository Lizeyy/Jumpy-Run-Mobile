package com.game.layers;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.game.Constants;

public class Grass {
    private Bitmap grass1, grass2;
    private float xGrass1, xGrass2, speedGrass, h;

    public Grass(Bitmap grass, float h){
        this.grass1 = grass;
        this.grass2 = grass;
        this.h = h;

        xGrass1 = 0;
        xGrass2 = Constants.WIDTH - 5;
        speedGrass = (float) 1.6;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(grass1, xGrass1, h, null);
        canvas.drawBitmap(grass2, xGrass2, h, null);
    }

    public void update(){
        xGrass1 -= speedGrass;
        xGrass2 -= speedGrass;
        if(xGrass2 <= -grass2.getWidth()) xGrass2 = Constants.WIDTH - 5;
        if(xGrass1 <= -grass1.getWidth()) xGrass1 = Constants.WIDTH - 5;
    }

    public void setSpeed(float speed) {
        this.speedGrass = (float) (0.5 * speed);
    }
}
