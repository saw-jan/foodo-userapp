package com.foodo.foodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "order.db";
    public static final String TB_NAME = "tb_order";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "RID";
    public static final String COL_3 = "ITEM";
    public static final String COL_4 = "PRICE";
    public static final String COL_5 = "QTY";
    public static final String COL_6 = "RNAME";
    public static final String COL_7 = "UID";

    public DatabaseHelper(Context context){
        super(context, DB_NAME,null,4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TB_NAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT,RID INTEGER,ITEM TEXT,PRICE INTEGER,QTY INTEGER,RNAME TEXT,UID INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public boolean insertData(String rid,String rname,String item,String price,String qty, String uid){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,rid);
        contentValues.put(COL_3,item);
        contentValues.put(COL_4,price);
        contentValues.put(COL_5,qty);
        contentValues.put(COL_6,rname);
        contentValues.put(COL_7,uid);
        long result = db.insert(TB_NAME,null,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }
    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+TB_NAME+" ORDER BY RID",null);
        return res;
    }
    public Cursor getRes(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT DISTINCT RID, RNAME FROM "+TB_NAME+" ORDER BY RID",null);
        return res;
    }
    public Cursor getItems(String rid){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT ITEM, PRICE, QTY FROM "+TB_NAME+" WHERE RID="+rid+" ORDER BY ITEM",null);
        return res;
    }
    public Cursor getItemCount(String rid){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT COUNT(ITEM) AS NOITM FROM "+TB_NAME+" WHERE RID="+rid+" GROUP BY RID",null);
        return res;
    }
    public Cursor getItemSum(String rid){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT SUM(PRICE) AS TOTAL FROM "+TB_NAME+" WHERE RID="+rid+" GROUP BY RNAME",null);
        return res;
    }
    public Integer deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TB_NAME, "RID = ?",new String[]{id});
    }
    public Cursor clearDB(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("DELETE FROM "+TB_NAME,null);
        return res;
    }
}
