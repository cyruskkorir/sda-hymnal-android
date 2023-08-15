package com.example.sdahymnal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.sdahymnal.DbContract.Entry;

public class DbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "HymnReader.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " +
                    Entry.TABLE_NAME + " (" +
                    Entry._ID + " INTEGER PRIMARY KEY," +
                    Entry.HYMN_NUMBER + " INTEGER," +
                    Entry.HYMN_TEXT + " TEXT)";
    private static final String SQL_CREATE_FAVORITE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " +
                    DbContract.FavoriteEntry.TABLE_NAME + " (" +
                    DbContract.FavoriteEntry._ID + " INTEGER PRIMARY KEY," +
                    DbContract.FavoriteEntry.FAVORITE_NUMBER + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =  "DROP TABLE IF EXISTS " + Entry.TABLE_NAME;
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_FAVORITE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }




}
