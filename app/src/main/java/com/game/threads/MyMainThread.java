package com.game.threads;

import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.view.SurfaceHolder;

import com.game.views.GameView;
import com.game.views.MenuView;

public class MyMainThread extends Thread implements Runnable {
    private SurfaceHolder surfaceHolder;
    public boolean running;
    private int state;
    /*  s1 = menu
        s2 = wybór
        s3 = gra
        s4 = pauza
        s5 i 6 = koniec gry
     */

    private Handler handler;
    private Handler handlerPoints;

    private GameView gameView;
    private MenuView menuView;
    private static Canvas canvas;

    private double points;
    private double multiplier;
    private float speed;

    private int targetFPS = 60;
    private double averageFPS;

    public MyMainThread(Handler handler){
        super();
        this.state = 0;
        this.handler = handler;

        speed = 5;
    }

    @Override
    public void run(){
        long startTime, timeMillis, waitTime, totalTime = 0, targetTime = 1000 / targetFPS;
        int frameCount = 0;

        while (running){
            startTime = System.nanoTime();
            Message message = new Message();
            Bundle bundle = new Bundle();

            switch (state){
                case 3:
                    canvas = null;
                    try {
                        canvas = this.surfaceHolder.lockCanvas();
                        synchronized (surfaceHolder) {
                            this.gameView.update();
                            this.gameView.draw(canvas);

                            this.points += multiplier;
                            bundle.putInt("POINTS",(int) points);
                            message.setData(bundle);
                            handlerPoints.sendMessage(message);
                            gameView.setSpeed(speed);
                        }
                    } catch (Exception e){} finally {
                        if(canvas != null){
                            try {
                                surfaceHolder.unlockCanvasAndPost(canvas);
                            } catch (Exception e) {e.printStackTrace();}
                        }
                    }
                    break;
                case 6:
                    bundle.putBoolean("GAME_OVER", true);
                    message.setData(bundle);
                    handler.sendMessage(message);
                    this.state = 5;
                    break;
            }


            timeMillis = (System.nanoTime() - startTime) /1000000;
            waitTime = targetTime - timeMillis;
            try {
                this.sleep(waitTime);
            } catch (Exception e){}
            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if(frameCount == targetFPS){
                averageFPS = 1000 / ((totalTime/frameCount) / 1000000);
                frameCount = 0;
                totalTime = 0;
            //    System.out.println(averageFPS);
            }
        }

    }

    public void setRunning(boolean isRunning){
        running = isRunning;
    }

    public void setGame(SurfaceHolder surfaceHolder, GameView gameView){
        this.state = 3;
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
        this.points = 0;
        this.multiplier = 0.05;
    }
    public void setMenu(){
        this.state = 1;
    //    this.surfaceHolder = surfaceHolder;
    //    this.menuView = menuView;
    }
    public void setPause(){
        this.state = 4;
    }
    public void setEnd(){
        this.state = 6;
    }

    public void setHandlerPoints(Handler handler){
        this.handlerPoints = handler;
    }
    public void setMultiplier(Double multiplier) {
        this.multiplier += multiplier;
    }
    public float getSpeed() {
        return speed;
    }
    public void setSpeed(float speed) {
        this.speed += speed;
    }
    public void resetGame(){
        this.speed = 5;
        this.multiplier = 0.1;
    }

    public int getPoints() {
        return (int) points;
    }
    public void setPoints(int point) {
        this.points = point;
    }

}
