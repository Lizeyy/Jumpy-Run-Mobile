package com.game.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.game.Constants;

public class Player {
    private static float PURPOSE_H;
    private static float MAX_ONE_JUMP;
    private static float SIZE_JUMP;

    private Bitmap image;
    private float x, y, jumpStrength = 30, weight = 0.7F, dy;

    private boolean jump, down, doubleJump;

    public Player(float h){
        PURPOSE_H = h;
        MAX_ONE_JUMP = (float) (Constants.HEIGHT * 0.3);
        SIZE_JUMP = (float) (PURPOSE_H - MAX_ONE_JUMP - Constants.HEIGHT * 0.07);

        this.x = (float) (Constants.WIDTH * 0.05);
        this.y = PURPOSE_H;
        this.jump = false;
        this.down = false;
        this.doubleJump = false;
    }
    public Player(float h, float previousY, boolean previousJump, boolean previousDoubleJump, boolean previousDown){
        PURPOSE_H = h;
        MAX_ONE_JUMP = (float) (Constants.HEIGHT * 0.3);
        SIZE_JUMP = (float) (PURPOSE_H - MAX_ONE_JUMP - Constants.HEIGHT * 0.07);

        this.x = (float) (Constants.WIDTH * 0.05);
        this.y = previousY;
        this.jump = previousJump;
        this.down = previousDown;
        this.doubleJump = previousDoubleJump;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(image, x, y, null);
    }

    public void update(){
        if(jump){
            y -= jumpStrength * 0.25;
            jumpStrength += weight;
            if(y < MAX_ONE_JUMP) {
                jump = false;
                down = true;
            }
        } else if (down){
            y += jumpStrength * 0.1;
            jumpStrength += weight;
            if(PURPOSE_H <= y) {
                y = PURPOSE_H;
                down = false;
                doubleJump = false;
            }
        } else if (doubleJump){
            y -= jumpStrength * 0.3;
            jumpStrength += weight;
            if(y < dy) {
                down = true;
            }
        }
    }

    public boolean ifJump(){
        return PURPOSE_H != y;
    }


    public static float getPurposeH() {
        return PURPOSE_H;
    }
    public static void setPurposeH(float purposeH) {
        PURPOSE_H = purposeH;
    }
    public static float getMaxOneJump() {
        return MAX_ONE_JUMP;
    }
    public static void setMaxOneJump(float maxOneJump) {
        MAX_ONE_JUMP = maxOneJump;
    }
    public static float getSizeJump() {
        return SIZE_JUMP;
    }
    public static void setSizeJump(float sizeJump) {
        SIZE_JUMP = sizeJump;
    }
    public Bitmap getImage() {
        return image;
    }
    public void setImage(Bitmap bitmap){
        image  = bitmap;
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
    public float getJumpStrength() {
        return jumpStrength;
    }
    public void setJumpStrength(float jumpStrength) {
        this.jumpStrength = jumpStrength;
    }
    public float getWeight() {
        return weight;
    }
    public void setWeight(float weight) {
        this.weight = weight;
    }
    public float getDy() {
        return dy;
    }
    public void setDy(float dy) {
        this.dy = dy;
    }
    public boolean isJump() {
        return jump;
    }
    public void setJump(boolean jump) {
        this.jump = jump;
    }
    public boolean isDown() {
        return down;
    }
    public void setDown(boolean down) {
        this.down = down;
    }
    public boolean isDoubleJump() {
        return doubleJump;
    }
    public void setDoubleJump(boolean doubleJump) {
        this.doubleJump = doubleJump;
    }
}
