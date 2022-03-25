package com.game.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.game.Collision;
import com.game.Database;
import com.game.R;
import com.game.RankList;
import com.game.threads.MyMainThread;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class EndActivity extends AppCompatActivity {
    MyMainThread myMainThread;
    Database database = new Database(this);
    int points;
    String nick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        for(Thread t: Thread.getAllStackTraces().keySet()) if(t instanceof MyMainThread) myMainThread = (MyMainThread) t;
        points = myMainThread.getPoints();

        TextView point = findViewById(R.id.point);
        point.setText(String.valueOf(points));

        Button restart = findViewById(R.id.restart);
        Button menu = findViewById(R.id.menu);

        try {
            Context context = getApplicationContext();
            File file = new File(context.getFilesDir(), "nick");
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[fileInputStream.available()];
            fileInputStream.read(buffer);
            fileInputStream.close();
            String response = new String(buffer);
            JSONObject jsonObject = new JSONObject(response);
            nick = jsonObject.getString("NICK");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        RankList rankList = database.getOneList(nick);
        RankList newRank = new RankList(nick, points);
        if(rankList != null && rankList.getPoints() < points){
            database.update(newRank);
        } else if (rankList == null){
            database.add(newRank);
        }

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMainThread.setRunning(false);
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMainThread.resetGame();
                startActivity(new Intent(getApplicationContext(), GameActivity.class));
            }
        });
    }


    public void onBackPressed(){
        myMainThread.setRunning(false);
        finish();
        startActivity(new Intent(getApplicationContext(), MenuActivity.class));
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