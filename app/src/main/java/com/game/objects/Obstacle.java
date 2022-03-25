package com.game.objects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.game.Constants;

public class Obstacle {
    private Bitmap bitmap;
    private float x, y, speed;

    public Obstacle(Bitmap bitmap, float x, float h1){
        float h = (float) (Constants.HEIGHT * 0.1);
        float as = bitmap.getWidth() / (float) bitmap.getHeight();
        int w = Math.round((int) h * as);
        this.bitmap = Bitmap.createScaledBitmap(bitmap, w,(int) h, false);

        this.x = x;
        this.y = h1 - h;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(bitmap, x, y, null);
    }
    public void update(){
        x -= speed;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    public float getX() {
        return x;
    }
    public void setX(float x) {
        this.x = x;
    }
    public float getY() {
        return y;
    }
    public void setY(float y) {
        this.y = y;
    }
    public float getSpeed() {
        return speed;
    }
    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
