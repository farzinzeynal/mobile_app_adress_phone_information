package com.example.user.specialinformation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper
{

    private static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "mUsers";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "name";
    private static final String COL_3 = "phone";
    private static final String COL_4 = "adress";
    private static final String COL_5 = "image";



    public DatabaseHelper(@Nullable Context context) {
        super(context, TABLE_NAME, null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        String createTable = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, phone VARCHAR, adress VARCHAR, image BLOB)";
        sqLiteDatabase.execSQL(createTable);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


    public boolean insertData(String name, String phone, String adress, byte[] image)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, phone);
        contentValues.put(COL_4, adress);
        contentValues.put(COL_5, image);

        long result = db.insert(TABLE_NAME, null,contentValues);

        if (result==-1) { return false;}
        else
        { return true; }
    }


    public Cursor getData()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query,null);
        return data;
    }


    public boolean deleteRow(String value)
    {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_NAME,COL_1 + "=" + value, null) > 0;
    }

}
