package com.samiapps.kv.roobaruduniya;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by KV on 2/7/17.
 */

public class FavDb extends SQLiteOpenHelper {
    public static final int Database_version=3;
    public static final String Database_name="favdb";
    FavDb(Context context)
    {
        super(context,Database_name,null,Database_version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_Fav_TABLE = "CREATE TABLE " +RoobaruContract.tableName+"(" +RoobaruContract.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +RoobaruContract.COLUMN_KEY+ " TEXT NOT NULL);";
       // Log.d("checksql",SQL_CREATE_Fav_TABLE);
        db.execSQL(SQL_CREATE_Fav_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+RoobaruContract.tableName);
        onCreate(db);

    }
    public void insertKey(SQLiteDatabase db,String key)
    {
        ContentValues cv=new ContentValues();
        cv.put(RoobaruContract.COLUMN_KEY,key);
        db.insert(RoobaruContract.tableName,null,cv);
       // Log.d("insertkey","key inserted");

    }
    public void deleteKey(SQLiteDatabase db,String key)
    {
        ContentValues cv=new ContentValues();
        cv.put(RoobaruContract.COLUMN_KEY,key);

        db.delete(RoobaruContract.tableName,"key=?",new String[]{key});
       // Log.d("deletedkey","key");

    }
    public Cursor getKey(SQLiteDatabase db)
    {
        String[] projection={RoobaruContract.COLUMN_ID,RoobaruContract.COLUMN_KEY};
        Cursor c=db.query(RoobaruContract.tableName,projection,null,null,null,null,null);

        return c;
    }
    public Cursor queryKey(SQLiteDatabase db,String key)
    {
        String[] projection={RoobaruContract.COLUMN_KEY};
        Cursor c=db.query(RoobaruContract.tableName,projection,"key=?",new String[]{key},null,null,null);
        return c;
    }
}
