package com.mian.motohelper.Datebase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by WilsonHuang on 2015/12/30.
 */
public class GasStationDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Station.db";//資料庫名稱
    public static final int VERSION = 1;//資料庫版本編號
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
        db.execSQL(GasStationDAO.CREATE_TABLE);//執行SQL敘述建立table
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(GasStationDAO.DELETE_TABLE);//執行SQL敘述刪除table
        onCreate(db);
    }
}
