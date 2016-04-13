package com.alphadog.mycontentprovider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import java.sql.SQLException;

/**
 * Created by Alpha Dog on 2016/4/8.
 */
public class StringDataBase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "StringDatabase";
    private static final String TABLE_NAME = "string";
    private static final String KEY_NAME = "str";
//    private static final String SQL_CREATE = "CREATE TABLE" + TABLE_NAME + "(_id INTEGER PRIMARY KEY, string TEXT)";

    private static final String SQL_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_NAME + " TEXT)";
    private static final String SQL_DROP = "DROP TABLE IS EXISTS" + TABLE_NAME;

    public StringDataBase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP);
        onCreate(db);
    }

    public long addString(ContentValues values) throws SQLException {

        long id = getWritableDatabase().insert(TABLE_NAME, "", values);
        if (id <= 0) {
            throw new SQLException("Failed to add an image");
        }

        return id;
    }

    public int deleteString(String id) {
        if (id == null) {
            return getWritableDatabase().delete(TABLE_NAME, null, null);
        } else {
            return getWritableDatabase().delete(TABLE_NAME, "_id=?", new String[]{id});
        }

    }

    public int updateString( ContentValues values,String id) {
//        if (id == null) {
//            return getWritableDatabase().update(TABLE_NAME, values, null, null);
//        } else {
//            return getWritableDatabase().update(TABLE_NAME, values, "_id=?", new String[]{id});
//        }
        return getWritableDatabase().update(TABLE_NAME,values,"_id=?", new String[]{id});
    }

    public Cursor getString(String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder sqb = new SQLiteQueryBuilder();
        sqb.setTables(TABLE_NAME);

//        if (id != null) {
//            Log.i("ID",id);
//            sqb.appendWhere(selection);
//        }
        if (sortOrder == null || sortOrder == "") {
            sortOrder = "_id";
        }

        return sqb.query(getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
    }
}
