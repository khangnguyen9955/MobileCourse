package com.example.mhike.database;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.mhike.HikeQueryOptions;
import com.example.mhike.database.DatabaseHelper;
import com.example.mhike.database.TableContract;
import com.example.mhike.models.Hike;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

public class HikeRepository implements QueryContract.HikeRepository {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public HikeRepository(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    @Override
    public long insertHike(Hike hike) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TableContract.HikeEntry.COLUMN_NAME, hike.getName());
        values.put(TableContract.HikeEntry.COLUMN_LOCATION, hike.getLocation());
        values.put(TableContract.HikeEntry.COLUMN_DATE, hike.getDate().toString());
        values.put(TableContract.HikeEntry.COLUMN_PARKING_AVAILABLE, hike.isParkingAvailable() ? 1 : 0);
        values.put(TableContract.HikeEntry.COLUMN_LENGTH, hike.getLength());
        values.put(TableContract.HikeEntry.COLUMN_DIFFICULTY, hike.getDifficulty());
        values.put(TableContract.HikeEntry.COLUMN_DESCRIPTION, hike.getDescription());
        values.put(TableContract.HikeEntry.COLUMN_IMAGE_BLOB, hike.getImageBlob());
        values.put(TableContract.HikeEntry.COLUMN_CREATED_AT, new Date().toString());
        values.put(TableContract.HikeEntry.COLUMN_UPDATED_AT, new Date().toString());
        values.put(TableContract.HikeEntry.COLUMN_RATING, hike.getRating());


        long hikeId = -1;

        try {
            hikeId = database.insert(TableContract.HIKES_TABLE_NAME, null, values);
        } catch (SQLException e) {
            Log.e(TAG, "Error inserting hike into database: " + e.getMessage());
            e.printStackTrace();
        } finally {
            database.close();
        }

        return hikeId;
    }

    @Override
    public Hike getHike(int hikeId) {
        database = dbHelper.getReadableDatabase();
        Hike hike = null;
        Cursor cursor = null;

        try {
            String[] projection = {
                    TableContract.HikeEntry._ID,
                    TableContract.HikeEntry.COLUMN_NAME,
                    TableContract.HikeEntry.COLUMN_LOCATION,
                    TableContract.HikeEntry.COLUMN_DATE,
                    TableContract.HikeEntry.COLUMN_PARKING_AVAILABLE,
                    TableContract.HikeEntry.COLUMN_LENGTH,
                    TableContract.HikeEntry.COLUMN_DIFFICULTY,
                    TableContract.HikeEntry.COLUMN_DESCRIPTION,
                    TableContract.HikeEntry.COLUMN_IMAGE_BLOB,
                    TableContract.HikeEntry.COLUMN_RATING

            };

            String selection = TableContract.HikeEntry._ID + " = ?";
            String[] selectionArgs = {String.valueOf(hikeId)};

            cursor = database.query(
                    TableContract.HIKES_TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                hike = cursorToHike(cursor);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            database.close();
        }
        return hike;
    }

    @Override
    public List<Hike> getAllHikes() {
        database = dbHelper.getReadableDatabase();
        List<Hike> hikeList = new ArrayList<>();
        Cursor cursor = null;

        try {
            String[] projection = {
                    TableContract.HikeEntry._ID,
                    TableContract.HikeEntry.COLUMN_NAME,
                    TableContract.HikeEntry.COLUMN_LOCATION,
                    TableContract.HikeEntry.COLUMN_DATE,
                    TableContract.HikeEntry.COLUMN_PARKING_AVAILABLE,
                    TableContract.HikeEntry.COLUMN_LENGTH,
                    TableContract.HikeEntry.COLUMN_DIFFICULTY,
                    TableContract.HikeEntry.COLUMN_DESCRIPTION,
                    TableContract.HikeEntry.COLUMN_IMAGE_BLOB,
                    TableContract.HikeEntry.COLUMN_RATING

            };

            cursor = database.query(
                    TableContract.HIKES_TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Hike hike = cursorToHike(cursor);
                    hikeList.add(hike);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            database.close();
        }

        return hikeList;
    }

    @Override
    public int updateHike(Hike hike) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TableContract.HikeEntry.COLUMN_NAME, hike.getName());
        values.put(TableContract.HikeEntry.COLUMN_LOCATION, hike.getLocation());
        values.put(TableContract.HikeEntry.COLUMN_DATE, hike.getDate().toString()); // Assuming date is stored as a string
        values.put(TableContract.HikeEntry.COLUMN_PARKING_AVAILABLE, hike.isParkingAvailable() ? 1 : 0);
        values.put(TableContract.HikeEntry.COLUMN_LENGTH, hike.getLength());
        values.put(TableContract.HikeEntry.COLUMN_DIFFICULTY, hike.getDifficulty());
        values.put(TableContract.HikeEntry.COLUMN_DESCRIPTION, hike.getDescription());
        values.put(TableContract.HikeEntry.COLUMN_IMAGE_BLOB, hike.getImageBlob());
        values.put(TableContract.HikeEntry.COLUMN_RATING, hike.getRating());


        String selection = TableContract.HikeEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(hike.getId())};

        int rowsUpdated = 0;

        try {
            rowsUpdated = database.update(
                    TableContract.HIKES_TABLE_NAME,
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

    public void deleteHike(int hikeId) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String selectionHike = TableContract.HikeEntry._ID + " = ?";
        String[] selectionArgsHike = {String.valueOf(hikeId)};

        deleteObservationsForHike(hikeId, database);

        try {
            database.delete(
                    TableContract.HIKES_TABLE_NAME,
                    selectionHike,
                    selectionArgsHike
            );
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            database.close();
        }
    }

    private void deleteObservationsForHike(int hikeId, SQLiteDatabase database) {
        String selectionObservations = TableContract.ObservationEntry.COLUMN_HIKE_ID + " = ?";
        String[] selectionArgsObservations = {String.valueOf(hikeId)};

        try {
            database.delete(
                    TableContract.OBSERVATIONS_TABLE_NAME,
                    selectionObservations,
                    selectionArgsObservations
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("Range")
    private Hike cursorToHike(Cursor cursor) {
        Log.i(TAG, "cursorToHike: " + cursor.toString());
        int idIndex = cursor.getColumnIndex(TableContract.HikeEntry._ID);
        int nameIndex = cursor.getColumnIndex(TableContract.HikeEntry.COLUMN_NAME);
        int locationIndex = cursor.getColumnIndex(TableContract.HikeEntry.COLUMN_LOCATION);
        int dateIndex = cursor.getColumnIndex(TableContract.HikeEntry.COLUMN_DATE);
        int parkingIndex = cursor.getColumnIndex(TableContract.HikeEntry.COLUMN_PARKING_AVAILABLE);
        int lengthIndex = cursor.getColumnIndex(TableContract.HikeEntry.COLUMN_LENGTH);
        int difficultyIndex = cursor.getColumnIndex(TableContract.HikeEntry.COLUMN_DIFFICULTY);
        int descriptionIndex = cursor.getColumnIndex(TableContract.HikeEntry.COLUMN_DESCRIPTION);
        int imageBlobIndex = cursor.getColumnIndex(TableContract.HikeEntry.COLUMN_IMAGE_BLOB);
        int ratingIndex = cursor.getColumnIndex(TableContract.HikeEntry.COLUMN_RATING); // Add this line

        long hikeId = cursor.getLong(idIndex);
        String hikeName = cursor.getString(nameIndex);
        String hikeLocation = cursor.getString(locationIndex);

        // Parse the date string from the cursor into a Date object
        String dateStr = cursor.getString(dateIndex);
        Date hikeDate = parseDate(dateStr);
        float hikeRating = cursor.getFloat(ratingIndex);

        boolean parkingAvailable = cursor.getInt(parkingIndex) == 1; // Assuming 1 means "Yes" and 0 means "No"
        float hikeLength = cursor.getFloat(lengthIndex);
        String hikeDifficulty = cursor.getString(difficultyIndex);
        String hikeDescription = cursor.getString(descriptionIndex);
        byte[] imageBlob = cursor.getBlob(imageBlobIndex);
        Hike hike = new Hike();
        hike.setId((int) hikeId);
        hike.setName(hikeName);
        hike.setLocation(hikeLocation);
        hike.setDate(hikeDate);
        hike.setParkingAvailable(parkingAvailable);
        hike.setLength(hikeLength);
        hike.setDifficulty(hikeDifficulty);
        hike.setDescription(hikeDescription);
        hike.setImageBlob(imageBlob);
        hike.setRating(hikeRating);
        return hike;
    }

    public List<Hike> getFilteredHikes(HikeQueryOptions queryOptions, String query) {
        database = dbHelper.getReadableDatabase();
        List<Hike> hikeList = new ArrayList<>();
        Cursor cursor = null;

        try {
            String[] projection = {
                    TableContract.HikeEntry._ID,
                    TableContract.HikeEntry.COLUMN_NAME,
                    TableContract.HikeEntry.COLUMN_LOCATION,
                    TableContract.HikeEntry.COLUMN_DATE,
                    TableContract.HikeEntry.COLUMN_PARKING_AVAILABLE,
                    TableContract.HikeEntry.COLUMN_LENGTH,
                    TableContract.HikeEntry.COLUMN_DIFFICULTY,
                    TableContract.HikeEntry.COLUMN_DESCRIPTION,
                    TableContract.HikeEntry.COLUMN_IMAGE_BLOB,
                    TableContract.HikeEntry.COLUMN_RATING
            };

            String selection = constructFilterSelection(queryOptions, query);
            String[] selectionArgs = constructSelectionArgs(queryOptions, query);

            String orderBy = constructOrderByClause(queryOptions);

            cursor = database.query(
                    TableContract.HIKES_TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    orderBy
            );

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Hike hike = cursorToHike(cursor);
                    hikeList.add(hike);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            database.close();
        }

        return hikeList;
    }

    // Helper method to construct the WHERE clause for filtering
    private String constructFilterSelection(HikeQueryOptions queryOptions, String query) {
        List<String> conditions = new ArrayList<>();

        if (queryOptions.isFilterByName()) {
            conditions.add(TableContract.HikeEntry.COLUMN_NAME + " LIKE ? COLLATE NOCASE");
        }
        if (queryOptions.isFilterByLocation()) {
            conditions.add(TableContract.HikeEntry.COLUMN_LOCATION + " LIKE ? COLLATE NOCASE");
        }

        if (!queryOptions.isFilterByName() && !queryOptions.isFilterByLocation()) {
            conditions.add(TableContract.HikeEntry.COLUMN_NAME + " LIKE ? COLLATE NOCASE");
        }

        return conditions.isEmpty() ? null : "(" + String.join(" OR ", conditions) + ")";
    }

    private String[] constructSelectionArgs(HikeQueryOptions queryOptions, String query) {
        List<String> argsList = new ArrayList<>();

        if (queryOptions.isFilterByName()) {
            argsList.add("%" + query + "%");
        }
        if (queryOptions.isFilterByLocation()) {
            argsList.add("%" + query + "%");
        }

        if(!queryOptions.isFilterByName() && !queryOptions.isFilterByLocation()) {
            argsList.add("%" + query + "%");
        }

        return argsList.toArray(new String[0]);
    }


    private String constructOrderByClause(HikeQueryOptions queryOptions) {
        List<String> orderByList = new ArrayList<>();
        Stack<String> stack = queryOptions.getStack();
        for (String sortField : stack){
            if(sortField.equals("date desc")){
                orderByList.add(TableContract.HikeEntry.COLUMN_DATE + " DESC");
            }else if (sortField.equals("date asc")){
                orderByList.add(TableContract.HikeEntry.COLUMN_DATE + " ASC");
            }
            if(sortField.equals("length desc")) {
                orderByList.add(TableContract.HikeEntry.COLUMN_LENGTH+ " DESC");
            }
            else if (sortField.equals("length asc")) {
                orderByList.add(TableContract.HikeEntry.COLUMN_LENGTH+ " ASC");
            }
            if (sortField.equals("rating desc")){
                orderByList.add(TableContract.HikeEntry.COLUMN_RATING + " DESC");
            }
            else if(sortField.equals("rating asc")){
                orderByList.add(TableContract.HikeEntry.COLUMN_RATING + " ASC");
            }
        }
        queryOptions.clearStack();
        return String.join(", ", orderByList);
    }




    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date);
    }

    private Date parseDate(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault());
            return dateFormat.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
