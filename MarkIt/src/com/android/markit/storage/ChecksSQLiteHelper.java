package com.android.markit.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ChecksSQLiteHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Checks.db";

    public ChecksSQLiteHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if(db != null)
            db.execSQL(SimpleDBOperations.CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(db != null) {
            db.execSQL(SimpleDBOperations.DELETE_ENTRIES);
            onCreate(db);
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(db != null)
            onUpgrade(db, oldVersion, newVersion);
    }

    private static final class SimpleDBOperations {

        public static final String TEXT_TYPE = " TEXT";
        public static final String COMMA_SEPARATION = ",";
        public static final String CREATE_ENTRIES = "CREATE TABLE " + ChecksReaderContract.ChecksEntry.TABLE_NAME
                + " (" + ChecksReaderContract.ChecksEntry._ID + " INTEGER PRIMARY KEY,"
                + ChecksReaderContract.ChecksEntry.COLUMN_NAME_LATITUDE + TEXT_TYPE + COMMA_SEPARATION
                + ChecksReaderContract.ChecksEntry.COLUMN_NAME_LONGITUDE + TEXT_TYPE + COMMA_SEPARATION
                + ChecksReaderContract.ChecksEntry.COLUMN_NAME_TIME + TEXT_TYPE
                + " )";
        public static final String DELETE_ENTRIES = "DROP TABLE IF EXISTS " + ChecksReaderContract.ChecksEntry.TABLE_NAME;
    }
}