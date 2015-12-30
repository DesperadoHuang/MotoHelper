package com.mian.motohelper.Datebase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by WilsonHuang on 2015/12/30.
 */
public class GasStationDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "GasStation.db";
    public static final int VERSION = 1;
    private static SQLiteDatabase database;

    public GasStationDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static SQLiteDatabase getDatabase(Context context) {
        if (database == null || !(database.isOpen())) {
            database = new GasStationDBHelper(context, DATABASE_NAME, null, VERSION).getWritableDatabase();
        }
        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
