package com.game.activities;

import androidx.annotation.MainThread;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.game.R;
import com.game.threads.MyMainThread;

import java.util.Random;

public class GameActivity extends Activity {
    private MyMainThread myMainThread;
    private int points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        for(Thread t: Thread.getAllStackTraces().keySet()) if(t instanceof MyMainThread) myMainThread = (MyMainThread) t;

        Button pause = findViewById(R.id.pause);
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pause();
            }
        });

        final int[] i = {1};
        TextView point = findViewById(R.id.points);
        Handler handlerPoints = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(Message message) {
                super.handleMessage(message);
                Bundle bundle = message.getData();
                points = bundle.getInt("POINTS", 0);
                point.setText(String.valueOf(points));

                if(points > 20 * i[0] && new Random().nextInt(100) < 1){
                    myMainThread.setMultiplier(0.005 * i[0]);
                    myMainThread.setSpeed((float) 0.09 * i[0]);
                    i[0]++;
                }
            }
        };

        myMainThread.setHandlerPoints(handlerPoints);
    }

    private void pause(){
        myMainThread.setPause();
        startActivity(new Intent(getApplicationContext(), PauseActivity.class));
    }

    protected void onPause(){
        myMainThread.setPause();
        super.onPause();
    }

    public void onBackPressed(){
        pause();
    }

    protected void onResume(){
        super.onResume();

        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if(Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}