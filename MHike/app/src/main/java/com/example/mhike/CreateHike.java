package com.example.mhike;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mhike.database.HikeRepository;
import com.example.mhike.database.QueryContract;
import com.example.mhike.models.Hike;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateHike extends AppCompatActivity {
    private EditText editTextHikeName;
    private EditText editTextLocation;
    private DatePicker datePickerDate;
    private RadioGroup radioGroupParkingAvailable;
    private EditText editTextLength;
    private RadioGroup radioGroupDifficulty;
    private EditText editTextDescription;
    private Button buttonChooseDate;
    private static final int PICK_IMAGE_REQUEST = 1;
    private HikeRepository hikeRepository; // Declare the HikeRepository instance variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_hike);
        hikeRepository = new HikeRepository(this);

        editTextHikeName = findViewById(R.id.editTextHikeName);
        editTextLocation = findViewById(R.id.editTextLocation);
        datePickerDate = findViewById(R.id.datePickerHike);
        radioGroupParkingAvailable = findViewById(R.id.radioGroupParkingAvailable); // Add this line
        editTextLength = findViewById(R.id.editTextLength);
        radioGroupDifficulty = findViewById(R.id.radioGroupDifficulty);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonChooseDate = findViewById(R.id.buttonChooseDate);

        // Handle the "Upload Image" button click
        Button buttonUploadImage = findViewById(R.id.buttonUploadImage);
        buttonUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
        // Handle the "Choose Date" button click to show the DatePicker
        buttonChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        Button buttonSaveHike = findViewById(R.id.buttonSaveHike);
        buttonSaveHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    // Proceed with saving the hike to the database
                    String hikeName = editTextHikeName.getText().toString();
                    String location = editTextLocation.getText().toString();
                    int year = datePickerDate.getYear();
                    int month = datePickerDate.getMonth();
                    int day = datePickerDate.getDayOfMonth();
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day);
                    Date date = calendar.getTime();
                    int selectedParkingRadioButtonId = radioGroupParkingAvailable.getCheckedRadioButtonId(); // Get the selected radio button ID
                    boolean parkingAvailable = selectedParkingRadioButtonId == R.id.radioButtonYes; // Check if Yes radio button is selected
                    String length = editTextLength.getText().toString();

                    // Get the selected difficulty level
                    int selectedRadioButtonId = radioGroupDifficulty.getCheckedRadioButtonId();
                    RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
                    String difficulty = selectedRadioButton.getText().toString();

                    String description = editTextDescription.getText().toString();

                    // Create a new Hike object with the gathered information
                    Hike newHike = new Hike();
                    newHike.setName(hikeName);
                    newHike.setLocation(location);
                    newHike.setDate(date);
                    newHike.setParkingAvailable(parkingAvailable);
                    newHike.setLength(length);
                    newHike.setDifficulty(difficulty);
                    newHike.setDescription(description);
                    Log.e("Hike", newHike.getDate().toString());
                    Log.e("HikeDate", String.valueOf(date));
                    // Save the hike to the database
                    long hikeId = hikeRepository.insertHike(newHike);

                    if (hikeId > -1) {
                        // Hike was successfully inserted
                        Toast.makeText(CreateHike.this, "Hike saved with ID: " + hikeId, Toast.LENGTH_SHORT).show();
                        // Clear the input fields or navigate to another screen as needed
//                        clearInputFields();

                        // Navigate back to the FeedActivity
                        Intent intent = new Intent(CreateHike.this, Feed.class);
                        startActivity(intent);
                    } else {
                        // There was an error inserting the hike
                        Log.e("HikeInsertion", "Failed to save hike to the database. Hike ID: " + hikeId);

                        Toast.makeText(CreateHike.this, "Failed to save hike", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void showDatePicker() {
        int year = datePickerDate.getYear();
        int month = datePickerDate.getMonth();
        int day = datePickerDate.getDayOfMonth();
        TextView dateDisplay = findViewById(R.id.dateDisplay);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        datePickerDate.updateDate(selectedYear, selectedMonth, selectedDay);
                        String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                        TextView dateDisplay = findViewById(R.id.dateDisplay);
                        dateDisplay.setText(selectedDate);
                        dateDisplay.setVisibility(View.VISIBLE);
                    }
                },
                year, month, day
        );

        // Show the DatePicker dialog
        datePickerDialog.show();
    }
    private boolean validateInput() {
        String hikeName = editTextHikeName.getText().toString().trim();
        String location = editTextLocation.getText().toString().trim();
        String date = datePickerDate.getYear() + "-" +
                (datePickerDate.getMonth() + 1) + "-" +
                datePickerDate.getDayOfMonth();

        int selectedParkingRadioButtonId = radioGroupParkingAvailable.getCheckedRadioButtonId();

        String length = editTextLength.getText().toString().trim();

        boolean isValid = true;

        if (hikeName.isEmpty()) {
            Toast.makeText(this, "Please enter a hike name.", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        if (location.isEmpty()) {
            Toast.makeText(this, "Please enter a location.", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        if (date.isEmpty()) {
            Toast.makeText(this, "Please select a date.", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        if (length.isEmpty()) {
            Toast.makeText(this, "Please enter the hike length.", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        if (!isValidDate(date)) {
            Toast.makeText(this, "Please enter a valid date (YYYY-MM-DD).", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        if (selectedParkingRadioButtonId == -1) {
            Toast.makeText(this, "Please select parking availability.", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        int selectedRadioButtonId = radioGroupDifficulty.getCheckedRadioButtonId();
        if (selectedRadioButtonId == -1) {
            Toast.makeText(this, "Please select a difficulty level.", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        return isValid;
    }


    private boolean isValidDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        dateFormat.setLenient(false);

        try {
            dateFormat.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the selected image URI
            Uri imageUri = data.getData();

            // TODO: Handle the selected image (e.g., display it or upload it to a server)
        }
    }
}
