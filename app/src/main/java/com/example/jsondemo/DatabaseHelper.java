package com.example.jsondemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "energyCube";
    public static final String TABLE_NAME = "current";
    public static final String COL_1 = "id";
    public static final String COL_2 = "device";
    public static final String COL_3 = "current";
    public static final String COL_4 = "datetime";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, device INTEGER, current DOUBLE, datetime STRING)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }

    public boolean insertCurrent(int device, double current, String datetime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put(COL_2, device);
        contentValue.put(COL_3, current);
        contentValue.put(COL_4, datetime);

        long result = db.insert(TABLE_NAME, null, contentValue);

        if(result == -1)
            return false;
        else
            return true;
    }


    //----USER TABLE----//

    //insert
    public boolean insertUser(int id, String username, String password , String name, boolean admin) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put("id", id);
        contentValue.put("username", username);
        contentValue.put("password", password);
        contentValue.put("name", name);
        contentValue.put("admin", admin);

        long result = db.insert(TABLE_NAME, null, contentValue);

        if(result == -1)
            return false;
        else
            return true;
    }

}
