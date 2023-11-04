package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class ContactDAO {
    private DatabaseHelper dbHelper;

    public ContactDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long insertContact(String name, String dob, String email, String imageResource) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_DOB, dob);
        values.put(DatabaseHelper.COLUMN_EMAIL, email);
        values.put(DatabaseHelper.COLUMN_IMAGE, imageResource);

        try {
            return db.insert(DatabaseHelper.TABLE_NAME, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return -1;
    }

    @SuppressLint("Range")
    public List<Contact> getAllContacts() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME, null, null, null, null, null, null);

        List<Contact> contactList = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
                String dob = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DOB));
                String email = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EMAIL));
                String imageResource = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_IMAGE));
                Contact contact = new Contact(name, dob, email, imageResource);
                contactList.add(contact);
            }
            cursor.close();
        }

        return contactList;
    }
    public long createContact(String name, String dob, String email, String imageResource) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_DOB, dob);
        values.put(DatabaseHelper.COLUMN_EMAIL, email);
        values.put(DatabaseHelper.COLUMN_IMAGE, imageResource);

        try {
            return db.insert(DatabaseHelper.TABLE_NAME, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return -1;
    }
}
