package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class ListContacts extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ContactsAdapter adapter;
    private ContactDAO contactDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_contacts);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactDAO = new ContactDAO(this);

        loadProfilesFromDatabase();

        findViewById(R.id.btnAddContact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListContacts.this, CreateContact.class));
            }
        });
    }

    private void loadProfilesFromDatabase() {
        List<Contact> contactList = contactDAO.getAllContacts();
        adapter = new ContactsAdapter(this, contactList);
        recyclerView.setAdapter(adapter);
    }
}
