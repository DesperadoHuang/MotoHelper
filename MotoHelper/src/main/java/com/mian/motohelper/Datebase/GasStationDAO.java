package com.mian.motohelper.Datebase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tools.MyTools;

import java.util.ArrayList;

/**
 * Created by WilsonHuang on 2015/12/30.
 */
public class GasStationDAO {
    public static final String TABLE_NAME = "GasStation";//table名稱
    public static final String KEY_ID = "_id";//主鍵

    public static final String TYPE_COLUMN = "stationType";//加油站類型欄位
    public static final String LATITUDE_COLUMN = "latitude";//緯度
    public static final String LONGITUDE_COLUMN = "longitude";//經度
    public static final String ADDRESS_COLUMN = "address";//地址
    public static final String PHONENUMBER_COLUMN = "phoneNumber";//電話

    public static final String TEXT_NOT_NULL = " TEXT NOT NULL";
    public static final String REAL_NOT_NULL = " REAL NOT NULL";
    public static final String COMMA_SEP = ",";

    //建立table
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TYPE_COLUMN + TEXT_NOT_NULL + COMMA_SEP +
                    LATITUDE_COLUMN + REAL_NOT_NULL + COMMA_SEP +
                    LONGITUDE_COLUMN + REAL_NOT_NULL + COMMA_SEP +
                    ADDRESS_COLUMN + TEXT_NOT_NULL + COMMA_SEP +
                    PHONENUMBER_COLUMN + TEXT_NOT_NULL + ")";

    //刪除table
    public static final String DELETE_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    private SQLiteDatabase database;

    public GasStationDAO(Context context) {
        database = GasStationDBHelper.getDatabase(context);
    }

    //關閉資料庫
    public void closeDB() {
        database.close();
    }

    /**
     * 將所有資料新增至資料庫
     */
    public void insert() {

        ArrayList<GasStationInfo> list = new StationData().getAllStation();
        for (int index = 0; index < list.size(); index++) {
            GasStationInfo station = list.get(index);
            ContentValues cv = new ContentValues();
            cv.put(GasStationDAO.TYPE_COLUMN, station.getType());
            cv.put(GasStationDAO.LATITUDE_COLUMN, station.getLatitude());
            cv.put(GasStationDAO.LONGITUDE_COLUMN, station.getLongitude());
            cv.put(GasStationDAO.ADDRESS_COLUMN, station.getAddress());
            cv.put(GasStationDAO.PHONENUMBER_COLUMN, station.getPhoneNumber());

            database.insert(TABLE_NAME, null, cv);
            MyTools.myLog("成功新增第 " + (index + 1) + " 筆資料" +
                    " type:" + station.getType() +
                    " latitude:" + station.getLatitude() +
                    " longitude:" + station.getLongitude() +
                    " address:" + station.getAddress() +
                    " phoneNumber:" + station.getPhoneNumber());
        }
        MyTools.myLog("新增資料結束!!");
    }

    /**
     * @param gasStationInfo
     * @return
     */
    public boolean update(GasStationInfo gasStationInfo) {

        return false;
    }

    /**
     * 刪除table
     *
     * @return
     */
    public boolean deleteTable() {
        database.execSQL(DELETE_TABLE);
        return false;
    }

    /**
     * 取得所有資料的Cursor物件
     *
     * @return
     */
    public Cursor getAllCursor() {

        return null;
    }

}
