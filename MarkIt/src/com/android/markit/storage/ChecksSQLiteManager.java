package com.android.markit.storage;

import java.util.ArrayList;

import com.android.markit.entry.Mark;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ChecksSQLiteManager implements IChecksSQLiteManager {

    private ChecksSQLiteHelper mSQLiteHelper;

    public ChecksSQLiteManager(Context context) {
        mSQLiteHelper = new ChecksSQLiteHelper(context);
    }

    public long putValues(Mark mark) {
        long newRowId = -1;
        if(mSQLiteHelper != null) {
            SQLiteDatabase db = mSQLiteHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(ChecksReaderContract.ChecksEntry.COLUMN_NAME_LATITUDE, Double.toString(mark.getLatitude()));
            values.put(ChecksReaderContract.ChecksEntry.COLUMN_NAME_LONGITUDE, Double.toString(mark.getLongitude()));
            values.put(ChecksReaderContract.ChecksEntry.COLUMN_NAME_TIME, Long.toString(mark.getTime()));
            if(db != null) {
                newRowId = db.insert(ChecksReaderContract.ChecksEntry.TABLE_NAME, null, values);
                db.close();
            }
        }
        return newRowId;
    }

    public ArrayList<Mark> getValues() {
        ArrayList<Mark> result = new ArrayList<Mark>();
        if(mSQLiteHelper != null) {
            SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
            if(db != null) {
                Cursor cursor = db.query(ChecksReaderContract.ChecksEntry.TABLE_NAME, null, null, null, null, null, null);
                if(cursor != null) {
                    cursor.moveToFirst();
                    int columnLatitudeIndex = cursor.getColumnIndex(ChecksReaderContract.ChecksEntry.COLUMN_NAME_LATITUDE);
                    int columnLongitudeIndex = cursor.getColumnIndex(ChecksReaderContract.ChecksEntry.COLUMN_NAME_LONGITUDE);
                    int columnTimeIndex = cursor.getColumnIndex(ChecksReaderContract.ChecksEntry.COLUMN_NAME_TIME);
                    do {
                        try{
                            String latitude = cursor.getString(columnLatitudeIndex);
                            String longitude = cursor.getString(columnLongitudeIndex);
                            String time = cursor.getString(columnTimeIndex);
                            Mark mark = new Mark(Double.parseDouble(latitude), Double.parseDouble(longitude), Long.parseLong(time));
                            mark.setId(cursor.getInt(0));
                            result.add(mark);
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    } while(cursor.moveToNext());
                }
                db.close();
            }
        }
        return result;
    }

    public void deleteRow(int rawId) {
        if(mSQLiteHelper != null) {
            SQLiteDatabase db = mSQLiteHelper.getWritableDatabase();
            if(db != null) {
                db.delete(ChecksReaderContract.ChecksEntry.TABLE_NAME, ChecksReaderContract.ChecksEntry._ID + "=?", new String[]{String.valueOf(rawId)});
                db.close();
            }
        }
    }
}