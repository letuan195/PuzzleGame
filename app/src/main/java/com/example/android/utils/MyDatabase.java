package com.example.android.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.android.adapter.ItemHighScore;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by tuanlv.k57 on 23/11/2014.
 */
public class MyDatabase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "HighScoreManager";
    public static final int DATABASE_VERSON = 1;

    public static final String TABLE_NAME_3 = "HighScore3";
    public static final String TABLE_NAME_4 = "HighScore4";
    public static final String TABLE_NAME_5 = "HighScore5";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_MOVE = "move";
    public static final String COLUMN_TIME = "time";

    public MyDatabase(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSON);
    }

    /**add highscore into database*/
    public void addHighScore3(ItemHighScore item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME,item.getName());
        values.put(COLUMN_MOVE,""+item.getMove());
        values.put(COLUMN_TIME,""+item.getTime());
        db.insert(TABLE_NAME_3, null, values);
        db.close();
    }
    public void addHighScore4(ItemHighScore item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME,item.getName());
        values.put(COLUMN_MOVE,""+item.getMove());
        values.put(COLUMN_TIME,""+item.getTime());
        db.insert(TABLE_NAME_4, null, values);
        db.close();
    }
    public void addHighScore5(ItemHighScore item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME,item.getName());
        values.put(COLUMN_MOVE,""+item.getMove());
        values.put(COLUMN_TIME,""+item.getTime());
        db.insert(TABLE_NAME_5, null, values);
        db.close();
    }

    /** Getting single contact*/
    public ItemHighScore getHighScore(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME_3, new String[]{COLUMN_ID, COLUMN_NAME,COLUMN_MOVE,COLUMN_TIME},
                COLUMN_ID + "=?", new String[]{String.valueOf(id)},null,null,null,null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        ItemHighScore item = new ItemHighScore(Integer.parseInt(cursor.getString(0)),
                                               cursor.getString(1),
                                               Integer.parseInt(cursor.getString(2)),
                                               Integer.parseInt(cursor.getString(3)));
        return item;
    }

    public ArrayList<ItemHighScore> getAllHighScore3(){
        ArrayList<ItemHighScore> arrayList = new ArrayList<ItemHighScore>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_3;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do {
                ItemHighScore item = new ItemHighScore(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),Integer.parseInt(cursor.getString(2)),
                        Integer.parseInt(cursor.getString(3)));
                arrayList.add(item);
            } while (cursor.moveToNext());
        }
        return arrayList;
    }
    public ArrayList<ItemHighScore> getAllHighScore4(){
        ArrayList<ItemHighScore> arrayList = new ArrayList<ItemHighScore>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_4;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do {
                ItemHighScore item = new ItemHighScore(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),Integer.parseInt(cursor.getString(2)),
                        Integer.parseInt(cursor.getString(3)));
                arrayList.add(item);
            } while (cursor.moveToNext());
        }
        return arrayList;
    }
    public ArrayList<ItemHighScore> getAllHighScore5(){
        ArrayList<ItemHighScore> arrayList = new ArrayList<ItemHighScore>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_5;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do {
                ItemHighScore item = new ItemHighScore(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),Integer.parseInt(cursor.getString(2)),
                        Integer.parseInt(cursor.getString(3)));
                arrayList.add(item);
            } while (cursor.moveToNext());
        }
        return arrayList;
    }

    /**update single highscore*/
    public int updateHighScore(ItemHighScore item){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, item.getName());
        cv.put(COLUMN_MOVE,""+item.getMove());
        cv.put(COLUMN_TIME,""+item.getTime());

        return db.update(TABLE_NAME_3,cv,COLUMN_ID + " = ?", new String[]{String.valueOf(item.getId())});
    }

    /**delete single highscore*/
    public void deleteHighScore3(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_3, null,null);
        db.close();
    }
    public void deleteHighScore4(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_4, null,null);
        db.close();
    }
    public void deleteHighScore5(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_5, null,null);
        db.close();
    }

    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME_3;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ TABLE_NAME_3 + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_MOVE + " TEXT,"
                + COLUMN_TIME + " TEXT"+")");
        db.execSQL("CREATE TABLE "+ TABLE_NAME_4 + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_MOVE + " TEXT,"
                + COLUMN_TIME + " TEXT"+")");
        db.execSQL("CREATE TABLE "+ TABLE_NAME_5 + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_MOVE + " TEXT,"
                + COLUMN_TIME + " TEXT"+")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_3);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_4);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_5);
        onCreate(db);
    }
}
