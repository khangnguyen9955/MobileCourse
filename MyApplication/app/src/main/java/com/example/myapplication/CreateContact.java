package com.example.myapplication;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class CreateContact extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextDOB;
    private EditText editTextEmail;
    private ImageView profileImageView;
    private Button btnSelectImage;
    private Button btnSave;
    private DatabaseHelper dbHelper;

    private String selectedImageResource;
    private ContactDAO contactDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);

        contactDAO = new ContactDAO(this);

        editTextName = findViewById(R.id.editTextName);
        editTextDOB = findViewById(R.id.editTextDOB);
        editTextEmail = findViewById(R.id.editTextEmail);
        profileImageView = findViewById(R.id.profileImageView);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnSave = findViewById(R.id.btnSave);

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageSelectionDialog();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveContact();
            }
        });
    }
    private void showImageSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Image");

        final String[] imageResources = getResources().getStringArray(R.array.image_resources);

        builder.setItems(imageResources, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedImageResource = imageResources[which];
                profileImageView.setImageResource(getResources().getIdentifier(selectedImageResource, "drawable", getPackageName()));
            }
        });

        builder.show();
    }
    private void saveContact() {
        String name = editTextName.getText().toString();
        String dob = editTextDOB.getText().toString();
        String email = editTextEmail.getText().toString();
        if (isDateValid(dob)) {
            long newRowId = contactDAO.createContact(name, dob, email, selectedImageResource);
            if (newRowId != -1) {
                Toast.makeText(this, "Contact saved successfully", Toast.LENGTH_SHORT).show();
                clearFields();
                Intent intent = new Intent(this, ListContacts.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Error saving contact", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Invalid Date of Birth format. Please use yyyy-MM-dd.", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isDateValid(String date) {
        // The pattern validation for "yyyy-MM-dd" format
        String datePattern = "\\d{4}-\\d{2}-\\d{2}";
        return date.matches(datePattern);
    }
    private void clearFields() {
        editTextName.setText("");
        editTextDOB.setText("");
        editTextEmail.setText("");

    }
}