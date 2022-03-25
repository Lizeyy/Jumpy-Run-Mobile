package com.game.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.game.Constants;
import com.game.R;
import com.game.threads.MyMainThread;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class MenuActivity extends AppCompatActivity {
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editText = findViewById(R.id.nick);
        try {
            Context context = getApplicationContext();
            File file = new File(context.getFilesDir(), "nick");
            if(file.exists()){
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] buffer = new byte[fileInputStream.available()];
                fileInputStream.read(buffer);
                fileInputStream.close();
                String response = new String(buffer);
                JSONObject jsonObject = new JSONObject(response);
                editText.setText(jsonObject.getString("NICK"));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Constants.WIDTH = displayMetrics.widthPixels;
        Constants.HEIGHT = displayMetrics.heightPixels;

        handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(Message message) {
                super.handleMessage(message);
                Bundle bundle = message.getData();

                if(bundle.getBoolean("GAME_OVER")){
                    startActivity(new Intent(getApplicationContext(), EndActivity.class));
                }
            }
        };

        MyMainThread myMainThread = new MyMainThread(handler);
        myMainThread.setRunning(true);
        myMainThread.setMenu();
        myMainThread.start();

        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().length() > 0){
                    Context context = getApplicationContext();
                    JSONObject jsonObject = new JSONObject();
                    try{
                        jsonObject.put("NICK", editText.getText().toString());

                        File file = new File(context.getFilesDir(), "nick");
                        FileWriter fileWriter = new FileWriter(file);
                        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                        bufferedWriter.write(jsonObject.toString());
                        bufferedWriter.close();
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(getApplicationContext(), GameActivity.class));
                }
            }
        });

        findViewById(R.id.rank).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RankActivity.class));

            }
        });




        findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });
        findViewById(R.id.credit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCredit();
            }
        });
    }


    private void dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Wyjście z gry")
                .setMessage("Czy na pewno chcesz wyjść z gry?")
                .setIcon(R.drawable.icon)
                .setPositiveButton("TAK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("NIE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    private void dialogCredit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle(";D")
                .setMessage("Gra stworzona przez Izabelę Wojtowicz")
                .setIcon(R.drawable.icon)
                .show();
    }

    public void onBackPressed(){
        dialog();
    }

    @Override
    protected void onResume(){
        super.onResume();

        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if(Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}