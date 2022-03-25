package com.game.threads;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.game.objects.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class PlayerThread extends Thread {
    public boolean running;
    List<Bitmap> bitmapRun, bitmapJump;
    Player player;
    public int j;
    public PlayerThread(boolean running, Player player, List<Bitmap> bitmapRun, List<Bitmap> bitmapJump){
        this.running = running;
        this.player = player;
        this.bitmapRun = bitmapRun;
        this.bitmapJump = bitmapJump;
        this.j = 0;
    }

    @Override
    public void run(){
        int i = 0;
        while(running){
            try {
                if(player.isJump() || player.isDown() || player.isDoubleJump()){
                    if(j < 8){
                        player.setImage(bitmapJump.get(j));
                        j++;
                    }
                } else {
                    player.setImage(bitmapRun.get(i));
                    i++;
                    if(i > 7) i = 0;
                }
                Thread.sleep(80);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
