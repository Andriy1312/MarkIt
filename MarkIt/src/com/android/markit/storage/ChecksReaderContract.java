package com.android.markit.storage;

import android.provider.BaseColumns;

public final class ChecksReaderContract {

    public ChecksReaderContract() {}

    public static abstract class ChecksEntry implements BaseColumns {

        public static final String TABLE_NAME = "location";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_TIME = "time";

    }
}