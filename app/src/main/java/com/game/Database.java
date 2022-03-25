package com.game;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "rank";
    private static final String ARTICLE = "rank_list";
    private static final String KEY_ID = "id";
    private static final String NICK = "nick";
    private static final String POINTS = "points";


    public Database(Context context){
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + ARTICLE + "(" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NICK + " TEXT, " +
                POINTS + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NAME);
        onCreate(db);
    }

    public RankList getOneList(String nick){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(ARTICLE, null, NICK + "=?", new String[] {String.valueOf(nick)}, null, null, null, String.valueOf(1));
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            return new RankList(cursor.getString(1), cursor.getInt(2));
        }
        return null;
    }

    public List<RankList> getAll(){
        List<RankList> rankLists = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(ARTICLE, null, null, null, null, null, POINTS + " DESC");
        if(cursor.moveToFirst()){
            do {
                RankList rankList = new RankList(cursor.getString(1), cursor.getInt(2));
                rankLists.add(rankList);
                System.out.println(rankList.getNick() + "- " + rankList.getPoints());
            } while(cursor.moveToNext());
        }
        return rankLists;
    }

    public void add(RankList rankList){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NICK, rankList.getNick());
        contentValues.put(POINTS, rankList.getPoints());
        db.insertOrThrow(ARTICLE, null, contentValues);
        db.close();
    }

    public int update(RankList rankList){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(POINTS, rankList.getPoints());
        return db.update(ARTICLE, contentValues, NICK + " =?", new String[]{rankList.getNick()});
    }
}

