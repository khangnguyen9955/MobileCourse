package com.example.mhike.database;

public final class TableContract {

    private TableContract() {}

    public static final String HIKES_TABLE_NAME = "hikes";
    public static final String OBSERVATIONS_TABLE_NAME = "observations";

    public static class HikeEntry {
        public static final String _ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_LOCATION = "location";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_PARKING_AVAILABLE = "parking_available";
        public static final String COLUMN_LENGTH = "length";
        public static final String COLUMN_DIFFICULTY = "difficulty";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_IMAGE_BLOB = "image_blob";
        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String COLUMN_UPDATED_AT = "updated_at";
        public static final String COLUMN_RATING = "rating";

        public static final String CREATE_HIKES_TABLE =
                "CREATE TABLE " + HIKES_TABLE_NAME + " (" +
                        HikeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        HikeEntry.COLUMN_NAME + " TEXT NOT NULL," +
                        HikeEntry.COLUMN_LOCATION + " TEXT NOT NULL," +
                        HikeEntry.COLUMN_DATE + " TEXT NOT NULL," +
                        HikeEntry.COLUMN_PARKING_AVAILABLE + " INTEGER NOT NULL," +
                        HikeEntry.COLUMN_LENGTH + " TEXT NOT NULL," +
                        HikeEntry.COLUMN_DIFFICULTY + " TEXT NOT NULL," +
                        HikeEntry.COLUMN_DESCRIPTION + " TEXT," +
                        HikeEntry.COLUMN_IMAGE_BLOB + " BLOB, " +
                        HikeEntry.COLUMN_RATING + " REAL," +
                        HikeEntry.COLUMN_CREATED_AT + " TEXT NOT NULL," +
                        HikeEntry.COLUMN_UPDATED_AT + " TEXT NOT NULL" +
                        ");";
    }

    public static class ObservationEntry {
        public static final String _ID = "_id";
        public static final String COLUMN_HIKE_ID = "hike_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_COMMENTS = "comments";
        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String COLUMN_UPDATED_AT = "updated_at";

        public static final String CREATE_OBSERVATIONS_TABLE =
                "CREATE TABLE " + OBSERVATIONS_TABLE_NAME + " (" +
                        ObservationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        ObservationEntry.COLUMN_HIKE_ID + " INTEGER NOT NULL," +
                        ObservationEntry.COLUMN_NAME + " TEXT NOT NULL," +
                        ObservationEntry.COLUMN_DATE + " TEXT NOT NULL," +
                        ObservationEntry.COLUMN_COMMENTS + " TEXT," +
                        ObservationEntry.COLUMN_CREATED_AT + " TEXT NOT NULL," +
                        ObservationEntry.COLUMN_UPDATED_AT + " TEXT NOT NULL" +
                        ");";
    }
}
