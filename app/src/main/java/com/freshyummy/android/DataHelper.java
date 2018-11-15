package com.freshyummy.android;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by afi on 26/03/18.
 */

public class DataHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "cart.db";
    private static final int DATABASE_VERSION = 1;
    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table cart(idkeranjang INTEGER PRIMARY KEY AUTOINCREMENT, iddetailproduk text null, jumlahbeli text null);";
        Log.d("Data", "onCreate: " + sql);
        db.execSQL(sql);
    }

    public void onInsert(SQLiteDatabase db, String iddetailproduk, String jumlahbeli){
        String sql = "INSERT INTO cart(iddetailproduk, jumlahbeli) VALUES ('"+iddetailproduk+"','"+jumlahbeli+"');";
        db.execSQL(sql);
    }

    public void onUpdate(SQLiteDatabase db, String iddetailproduk, String jumlahbeli){
        String sql = "update cart set jumlahbeli='"+jumlahbeli+"' where iddetailproduk='"+iddetailproduk+"'";
        db.execSQL(sql);
    }

    public void onDelete(SQLiteDatabase db, String iddetailproduk){
        String sql = "delete from cart where iddetailproduk='"+iddetailproduk+"'";
        db.execSQL(sql);
    }

    public void onDeleteAll(SQLiteDatabase db){
        String sql = "delete from cart";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
    }
}
