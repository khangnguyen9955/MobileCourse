package com.example.mhike.database;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.LinearLayout;

import com.example.mhike.models.Observation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ObservationRepository implements QueryContract.ObservationRepository {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public ObservationRepository(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    @Override
    public long insertObservation(Observation observation) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TableContract.ObservationEntry.COLUMN_HIKE_ID, observation.getHikeId());
        values.put(TableContract.ObservationEntry.COLUMN_NAME, observation.getName());
        values.put(TableContract.ObservationEntry.COLUMN_DATE, formatDate(observation.getDate()));
        values.put(TableContract.ObservationEntry.COLUMN_COMMENTS, observation.getComments());
        values.put(TableContract.ObservationEntry.COLUMN_IMAGE_BLOB, observation.getImageBlob());
        values.put(TableContract.ObservationEntry.COLUMN_CREATED_AT,new Date().toString());
        values.put(TableContract.ObservationEntry.COLUMN_UPDATED_AT,new Date().toString());
        Log.i("insertObservation", observation.getName() + " " + observation.getDate() + " " + observation.getComments());
        Log.i("insertObservation", String.valueOf(observation.getHikeId()));
        long observationId = -1;

        try {
            observationId = database.insert(TableContract.OBSERVATIONS_TABLE_NAME, null, values);
        } catch (SQLException e) {
            Log.e(TAG, "Error inserting observation into database: " + e.getMessage());
            e.printStackTrace();
        } finally {
            database.close();
        }

        return observationId;
    }

    @Override
    public Observation getObservation(int observationId) {
        database = dbHelper.getReadableDatabase();
        Observation observation = null;
        Cursor cursor = null;

        try {
            String[] projection = {
                    TableContract.ObservationEntry._ID,
                    TableContract.ObservationEntry.COLUMN_HIKE_ID,
                    TableContract.ObservationEntry.COLUMN_NAME,
                    TableContract.ObservationEntry.COLUMN_DATE,
                    TableContract.ObservationEntry.COLUMN_COMMENTS,
                    TableContract.ObservationEntry.COLUMN_IMAGE_BLOB
            };

            String selection = TableContract.ObservationEntry._ID + " = ?";
            String[] selectionArgs = {String.valueOf(observationId)};

            cursor = database.query(
                    TableContract.OBSERVATIONS_TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                observation = cursorToObservation(cursor);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            database.close();
        }

        return observation;
    }

    @Override
    public List<Observation> getObservationsForHike(int hikeId) {
        database = dbHelper.getReadableDatabase();
        List<Observation> observationList = new ArrayList<>();
        Cursor cursor = null;

        try {
            String[] projection = {
                    TableContract.ObservationEntry._ID,
                    TableContract.ObservationEntry.COLUMN_HIKE_ID,
                    TableContract.ObservationEntry.COLUMN_NAME,
                    TableContract.ObservationEntry.COLUMN_DATE,
                    TableContract.ObservationEntry.COLUMN_COMMENTS,
                    TableContract.ObservationEntry.COLUMN_IMAGE_BLOB
            };
            Log.e("getObservationsForHike", String.valueOf(hikeId));
            String selection = TableContract.ObservationEntry.COLUMN_HIKE_ID + " = ?";
            String[] selectionArgs = {String.valueOf(hikeId)};

            cursor = database.query(
                    TableContract.OBSERVATIONS_TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
            Log.i("getObservationsForHike", String.valueOf(cursor.getCount()));
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Observation observation = cursorToObservation(cursor);
                    Log.i("Observation", observation.getName());
                    observationList.add(observation);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            database.close();
        }

        return observationList;
    }

    @Override
    public int updateObservation(Observation observation) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TableContract.ObservationEntry.COLUMN_HIKE_ID, observation.getHikeId());
        values.put(TableContract.ObservationEntry.COLUMN_NAME, observation.getName());
        values.put(TableContract.ObservationEntry.COLUMN_DATE,formatDate(observation.getDate()));
        values.put(TableContract.ObservationEntry.COLUMN_COMMENTS, observation.getComments());
        values.put(TableContract.ObservationEntry.COLUMN_IMAGE_BLOB, observation.getImageBlob());

        String selection = TableContract.ObservationEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(observation.getId())};

        int rowsUpdated = 0;

        try {
            rowsUpdated = database.update(
                    TableContract.OBSERVATIONS_TABLE_NAME,
                    values,
                    selection,
                    selectionArgs
            );
        } catch (SQLException e) {
        } finally {
            database.close();
        }

        return rowsUpdated;
    }

    @Override
    public void deleteObservation(int observationId) {
        database = dbHelper.getWritableDatabase();
        String selection = TableContract.ObservationEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(observationId)};

        try {
            database.delete(
                    TableContract.OBSERVATIONS_TABLE_NAME,
                    selection,
                    selectionArgs
            );
        } catch (SQLException e) {
        } finally {
            database.close();
        }
    }

    @SuppressLint("Range")
    private Observation cursorToObservation(Cursor cursor) {
        Observation observation = new Observation();
        observation.setId(cursor.getInt(cursor.getColumnIndex(TableContract.ObservationEntry._ID)));
        observation.setHikeId(cursor.getInt(cursor.getColumnIndex(TableContract.ObservationEntry.COLUMN_HIKE_ID)));
        observation.setName(cursor.getString(cursor.getColumnIndex(TableContract.ObservationEntry.COLUMN_NAME)));
        observation.setImageBlob(cursor.getBlob(cursor.getColumnIndex(TableContract.ObservationEntry.COLUMN_IMAGE_BLOB)));
        observation.setDate(parseDate(cursor.getString(cursor.getColumnIndex(TableContract.ObservationEntry.COLUMN_DATE))));
        observation.setComments(cursor.getString(cursor.getColumnIndex(TableContract.ObservationEntry.COLUMN_COMMENTS)));
        return observation;
    }

    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date);
    }

    private Date parseDate(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            return dateFormat.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
