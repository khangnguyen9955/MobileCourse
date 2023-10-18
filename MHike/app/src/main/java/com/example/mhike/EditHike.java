package com.example.mhike;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mhike.database.HikeRepository;
import com.example.mhike.models.Hike;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditHike extends AppCompatActivity {

    private TextView hikeNameEditText;
    private TextView hikeLocationEditText;
    private TextView hikeLengthEditText;
    private TextView hikeDescriptionEditText;
    private RadioGroup radioGroupDifficulty;
    private RadioGroup radioGroupParkingAvailable;
    private DatePicker datePickerDate;

    private Button buttonChooseDate;
    private Button saveHikeButton;
    private Button editImageButton;
    private TextView dateDisplay;
    private int hikeId;
    private byte[] imageBlob;

    private HikeRepository hikeRepository;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Edit Hike");
        setContentView(R.layout.activity_edit_hike);

        hikeRepository = new HikeRepository(this);

        hikeNameEditText = findViewById(R.id.editTextHikeName);
        hikeLocationEditText = findViewById(R.id.editTextLocation);
        hikeLengthEditText = findViewById(R.id.editTextLength);
        datePickerDate = findViewById(R.id.editDatePickerHike);
        buttonChooseDate = findViewById(R.id.editButtonChooseDate);

        radioGroupDifficulty = findViewById(R.id.editRadioGroupDifficulty);
        RadioButton radioButtonEasy = findViewById(R.id.radioButtonEasy);
        RadioButton radioButtonMedium = findViewById(R.id.radioButtonMedium);
        RadioButton radioButtonHard = findViewById(R.id.radioButtonHard);

        radioGroupParkingAvailable = findViewById(R.id.editRadioGroupParkingAvailable);
        RadioButton radioButtonYes = findViewById(R.id.radioButtonYes);
        RadioButton radioButtonNo = findViewById(R.id.radioButtonNo);

        dateDisplay = findViewById(R.id.editDateDisplay);
        hikeDescriptionEditText = findViewById(R.id.editTextDescription);
        saveHikeButton = findViewById(R.id.buttonSaveEditedHike);
        editImageButton = findViewById(R.id.editButtonUploadImage);

        hikeId = getIntent().getIntExtra("hikeId", -1);
        Hike selectedHike = hikeRepository.getHike(hikeId);

        if (selectedHike != null) {
            hikeNameEditText.setText(selectedHike.getName());
            hikeLocationEditText.setText(selectedHike.getLocation());
            hikeLengthEditText.setText(selectedHike.getLength());
            imageBlob = selectedHike.getImageBlob();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MMM dd", Locale.US);
            String formattedDate = dateFormat.format(selectedHike.getDate());
            dateDisplay.setText(formattedDate);
            // Check the appropriate radio button based on the difficulty
            String difficulty = selectedHike.getDifficulty();
            if ("Easy".equals(difficulty)) {
                radioButtonEasy.setChecked(true);
            } else if ("Medium".equals(difficulty)) {
                radioButtonMedium.setChecked(true);
            } else if ("Hard".equals(difficulty)) {
                radioButtonHard.setChecked(true);
            }

            // Check the appropriate radio button based on parking available
            boolean parkingAvailable = selectedHike.isParkingAvailable();
            if (parkingAvailable) {
                radioButtonYes.setChecked(true);
            } else {
                radioButtonNo.setChecked(true);
            }

            hikeDescriptionEditText.setText(selectedHike.getDescription());

            if (selectedHike.getImageBlob() != null) {
                editImageButton.setText("Edit Image");
            }
        }
        buttonChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        editImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
        saveHikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    String hikeName = hikeNameEditText.getText().toString();
                    String hikeLocation = hikeLocationEditText.getText().toString();
                    String hikeLength = hikeLengthEditText.getText().toString();

                    int selectedDifficultyId = radioGroupDifficulty.getCheckedRadioButtonId();
                    RadioButton selectedDifficultyRadioButton = findViewById(selectedDifficultyId);
                    String hikeDifficulty = selectedDifficultyRadioButton.getText().toString();

                    String hikeDescription = hikeDescriptionEditText.getText().toString();

                    int selectedParkingId = radioGroupParkingAvailable.getCheckedRadioButtonId();
                    boolean parkingAvailable = selectedParkingId == R.id.radioButtonYes;

                    int year = datePickerDate.getYear();
                    int month = datePickerDate.getMonth();
                    int day = datePickerDate.getDayOfMonth();
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day);
                    Date date = calendar.getTime();

                    updateHike(hikeId, hikeName, hikeLocation, hikeLength, hikeDifficulty, hikeDescription, parkingAvailable, date, imageBlob);
                    Toast.makeText(EditHike.this, "Edited the hike!" , Toast.LENGTH_SHORT).show();
                    Intent hikeDetailIntent = new Intent(EditHike.this, HikeDetail.class);
                    hikeDetailIntent.putExtra("hikeId", hikeId);
                    startActivity(hikeDetailIntent);
                }
            }
        });


    }

    private boolean validateInput() {
        String hikeName = hikeNameEditText.getText().toString().trim();
        String location = hikeLocationEditText.getText().toString().trim();
        String date = datePickerDate.getYear() + "-" +
                (datePickerDate.getMonth() + 1) + "-" +
                datePickerDate.getDayOfMonth();

        int selectedParkingRadioButtonId = radioGroupParkingAvailable.getCheckedRadioButtonId();

        String length = hikeLengthEditText.getText().toString().trim();

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

    private void updateHike(int hikeId, String name, String location, String length, String difficulty, String description, boolean parkingAvailable, Date date, byte[] imageBlob) {
        Hike hike = hikeRepository.getHike(hikeId);
        if (hike != null) {
            hike.setName(name);
            hike.setLocation(location);
            hike.setLength(length);
            hike.setDifficulty(difficulty);
            hike.setDescription(description);
            hike.setParkingAvailable(parkingAvailable);
            hike.setDate(date);
            hike.setImageBlob(
                    imageBlob
            );
            hikeRepository.updateHike(hike);
        }
    }

    private void showDatePicker() {
        int year = datePickerDate.getYear();
        int month = datePickerDate.getMonth();
        int day = datePickerDate.getDayOfMonth();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                imageBlob = getBytes(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        return byteBuffer.toByteArray();
    }
}
