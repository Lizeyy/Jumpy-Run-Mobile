package com.game;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;

public class Collision {
    public boolean isCollisionDetected(Bitmap player, int xP, int yP, Bitmap object, int xO, int yO){
        Rect bounds1 = new Rect(xP, yP, xP + player.getWidth(), yP + player.getHeight());
        Rect bounds2 = new Rect(xO, yO, xO + object.getWidth(), yO + object.getHeight());

        if(Rect.intersects(bounds1, bounds2)){
            Rect collisionBounds =  getCollision(bounds1, bounds2);
            for (int i = collisionBounds.left; i < collisionBounds.right; i++) {
                for (int j = collisionBounds.top; j < collisionBounds.bottom; j++) {
                    int playerPixel = player.getPixel(i - xP, j - yP);
                    int objectPixel = object.getPixel(i - xO, j - yO);
                    if (isFilled(playerPixel) && isFilled(objectPixel)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static Rect getCollision(Rect rect1, Rect rect2){
        int left = (int) Math.max(rect1.left, rect2.left);
        int top = (int) Math.max(rect1.top, rect2.top);
        int right = (int) Math.min(rect1.right, rect2.right);
        int bottom = (int) Math.min(rect1.bottom, rect2.bottom);
        return new Rect(left, top, right, bottom);
    }

    private static boolean isFilled(int pixel){
        return pixel != Color.TRANSPARENT;
    }
}
