package com.game.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.game.Collision;
import com.game.Constants;
import com.game.R;
import com.game.layers.Background;
import com.game.layers.Grass;
import com.game.objects.Obstacle;
import com.game.objects.Player;
import com.game.threads.MyMainThread;
import com.game.threads.PlayerThread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {
    private static int LINE = (int) (Constants.HEIGHT * 0.95);
    private float previousY, speed;
    private boolean previousJump, previousDoubleJump, previousDown;
    private int previousPoint = 0;

    private MyMainThread myMainThread;
    private PlayerThread playerThread;

    private Player player;
    private Background background;
    private Grass grass;
    private Collision collision = new Collision();

    private List<Obstacle> obstacles = new ArrayList<>();

    private boolean block, obst, hit;


    public GameView(Context context, AttributeSet attributeSet){
        super(context);
        getHolder().addCallback(this);

        for(Thread t: Thread.getAllStackTraces().keySet()) if(t instanceof MyMainThread) myMainThread = (MyMainThread) t;

        setFocusable(true);
        setOnTouchListener(this);
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){}

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        myMainThread.setGame(getHolder(), this);
        myMainThread.setPoints(previousPoint);

        Bitmap background1 = BitmapFactory.decodeResource(getResources(), R.drawable.game_bg_1_1).copy(Bitmap.Config.RGB_565, true);
        background1 = Bitmap.createScaledBitmap(background1, Constants.WIDTH, Constants.HEIGHT, false);
        Bitmap background2 = BitmapFactory.decodeResource(getResources(), R.drawable.game_bg_1_2).copy(Bitmap.Config.ARGB_8888, true);
        background2 = Bitmap.createScaledBitmap(background2, Constants.WIDTH, Constants.HEIGHT, false);
        Bitmap background3 = BitmapFactory.decodeResource(getResources(), R.drawable.game_bg_1_3).copy(Bitmap.Config.ARGB_8888, true);
        background3 = Bitmap.createScaledBitmap(background3, Constants.WIDTH, Constants.HEIGHT, false);
        background = new Background(background1, background2, background3);

        Bitmap grass1 = BitmapFactory.decodeResource(getResources(), R.drawable.grass);
        grass1 = Bitmap.createScaledBitmap(grass1, Constants.WIDTH, (int) (Constants.HEIGHT * 0.6), false);
        grass = new Grass(grass1, Constants.HEIGHT - grass1.getHeight());

        List<Bitmap> bitmapsRun = new ArrayList<>();
        bitmapsRun.add(BitmapFactory.decodeResource(getResources(), R.drawable.cat_run1));
        bitmapsRun.add(BitmapFactory.decodeResource(getResources(), R.drawable.cat_run2));
        bitmapsRun.add(BitmapFactory.decodeResource(getResources(), R.drawable.cat_run3));
        bitmapsRun.add(BitmapFactory.decodeResource(getResources(), R.drawable.cat_run4));
        bitmapsRun.add(BitmapFactory.decodeResource(getResources(), R.drawable.cat_run5));
        bitmapsRun.add(BitmapFactory.decodeResource(getResources(), R.drawable.cat_run6));
        bitmapsRun.add(BitmapFactory.decodeResource(getResources(), R.drawable.cat_run7));
        bitmapsRun.add(BitmapFactory.decodeResource(getResources(), R.drawable.cat_run8));

        List<Bitmap> bitmapsJump = new ArrayList<>();
        bitmapsJump.add(BitmapFactory.decodeResource(getResources(), R.drawable.cat_jump1));
        bitmapsJump.add(BitmapFactory.decodeResource(getResources(), R.drawable.cat_jump2));
        bitmapsJump.add(BitmapFactory.decodeResource(getResources(), R.drawable.cat_jump3));
        bitmapsJump.add(BitmapFactory.decodeResource(getResources(), R.drawable.cat_jump4));
        bitmapsJump.add(BitmapFactory.decodeResource(getResources(), R.drawable.cat_jump5));
        bitmapsJump.add(BitmapFactory.decodeResource(getResources(), R.drawable.cat_jump6));
        bitmapsJump.add(BitmapFactory.decodeResource(getResources(), R.drawable.cat_jump7));
        bitmapsJump.add(BitmapFactory.decodeResource(getResources(), R.drawable.cat_jump8));

        float h = (float) (Constants.HEIGHT * 0.3);
        float as = bitmapsRun.get(0).getWidth() / (float) bitmapsRun.get(0).getHeight();
        int w = Math.round((int) h * as);
        for(int i = 0; i < 8; i++){
            bitmapsRun.set(i, Bitmap.createScaledBitmap(bitmapsRun.get(i), w,(int) h, false));
            bitmapsJump.set(i, Bitmap.createScaledBitmap(bitmapsJump.get(i), w,(int) h, false));
        }

        if(previousY > 0) player = new Player(LINE - h, previousY, previousJump, previousDoubleJump, previousDown);
        else player = new Player(LINE - h);

        playerThread = new PlayerThread(true, player, bitmapsRun, bitmapsJump);
        playerThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        previousY = player.getY();
        previousJump = player.isJump();
        previousDoubleJump = player.isDoubleJump();
        previousDown = player.isDown();
        previousPoint = myMainThread.getPoints();
    }

    public void update(){
        background.update();
        background.setSpeed(speed);
        grass.update();
        grass.setSpeed(speed);

        player.update();
        if(!player.isJump()) block = false;

        obst = true;
        if(obstacles.size() > 0){
            for(Obstacle ob: obstacles){
                ob.setSpeed(speed);
                ob.update();
                if(ob.getX() > Constants.WIDTH * 0.8) obst = false;
                if(ob.getX() < 0 - ob.getBitmap().getWidth() * 1.5) {
                    obstacles.remove(ob);
                    ob = null;
                }

                hit = collision.isCollisionDetected(player.getImage(),(int) player.getX(),(int) player.getY(), ob.getBitmap(),(int) ob.getX(),(int) ob.getY());
                if(hit){
                    myMainThread.setEnd();
                }
            }
        }

        if(obstacles.size() <= 3 && obst){
            float x = new Random().nextInt((int) (Constants.WIDTH * 0.3)) + Constants.WIDTH;
            Obstacle obs = new Obstacle(BitmapFactory.decodeResource(getResources(), R.drawable.obs1), x, LINE);
            obstacles.add(obs);
        }
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        if(canvas != null){
            background.draw(canvas);
            for(Obstacle ob: obstacles){
                ob.draw(canvas);
            }
            player.draw(canvas);
        }

        grass.draw(canvas);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
         if(event.getAction() == MotionEvent.ACTION_DOWN) {
            if (!block) {
                if (!player.isDoubleJump() && player.ifJump()) {
                    player.setJumpStrength(30);
                    player.setDown(false);
                    player.setJump(false);
                    player.setDoubleJump(true);
                    playerThread.j = 0;
                    player.setDy(player.getY() - player.getSizeJump());
                }
                if (!player.ifJump()) {
                    player.setJumpStrength(30);
                    player.setJump(true);
                    playerThread.j = 0;
                }
                block = true;
                return true;
            }
        }
        if(event.getAction() == MotionEvent.ACTION_UP) {
            block = false;
        }
        return false;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
