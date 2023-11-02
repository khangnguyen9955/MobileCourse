package com.example.mhike;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

public class CreateHike extends AppCompatActivity {
    private EditText createTextHikeName;
    private EditText createTextLocation;
    private DatePicker datePickerDate;
    private RadioGroup radioGroupParkingAvailable;
    private EditText createTextLength;
    private RadioGroup radioGroupDifficulty;
    private EditText createTextDescription;
    private Button buttonChooseDate;
    private Button buttonUploadImage;
    private byte[] imageBlob;
    private static final int PICK_IMAGE_REQUEST = 1;
    private float hikeRating = 0.0f;
    private ImageView imageViewHike;
    private HikeRepository hikeRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_hike);
        setTitle("Add Hike");

        hikeRepository = new HikeRepository(this);
        RatingBar ratingBarHike = findViewById(R.id.ratingBarHike);
        ratingBarHike.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                hikeRating = rating;
            }
        });
        createTextHikeName = findViewById(R.id.createTextHikeName);
        createTextLocation = findViewById(R.id.createTextLocation);
        datePickerDate = findViewById(R.id.datePickerHike);
        radioGroupParkingAvailable = findViewById(R.id.radioGroupParkingAvailable);
        createTextLength = findViewById(R.id.createTextLength);
        radioGroupDifficulty = findViewById(R.id.radioGroupDifficulty);
        createTextDescription = findViewById(R.id.createTextDescription);
        buttonChooseDate = findViewById(R.id.buttonChooseDate);
        buttonUploadImage = findViewById(R.id.buttonUploadImage);
        imageViewHike = findViewById(R.id.imageViewUploaded);
        buttonUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
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
            onSaveClicked();
            }
        });

        Button buttonCancel = findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelClicked();
            }
        });
    }

    private void onSaveClicked() {
        if (validateInput()) {
            String hikeName = createTextHikeName.getText().toString();
            String location = createTextLocation.getText().toString();
            int year = datePickerDate.getYear();
            int month = datePickerDate.getMonth();
            int day = datePickerDate.getDayOfMonth();
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            Date date = calendar.getTime();
            int selectedParkingRadioButtonId = radioGroupParkingAvailable.getCheckedRadioButtonId();
            boolean parkingAvailable = selectedParkingRadioButtonId == R.id.radioButtonYes;
            String lengthText = createTextLength.getText().toString();
            float length = Float.parseFloat(lengthText);
            int selectedRadioButtonId = radioGroupDifficulty.getCheckedRadioButtonId();
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
            String difficulty = selectedRadioButton.getText().toString();
            String description = createTextDescription.getText().toString();
            Hike newHike = new Hike();
            newHike.setName(hikeName);
            newHike.setLocation(location);
            newHike.setDate(date);
            newHike.setParkingAvailable(parkingAvailable);
            newHike.setLength(length);
            newHike.setDifficulty(difficulty);
            newHike.setDescription(description);
            newHike.setImageBlob(imageBlob);
            newHike.setRating(hikeRating);


            StringBuilder message = new StringBuilder();
            message.append("Hike Name: ").append(hikeName).append("\n");
            message.append("Location: ").append(location).append("\n");
            message.append("Date: ").append(date).append("\n");
            message.append("Parking Available: ").append(parkingAvailable ? "Yes" : "No").append("\n");
            message.append("Length: ").append(length).append("\n");
            message.append("Difficulty: ").append(difficulty).append("\n");
            message.append("Image: ").append(imageBlob != null ? "Yes" : "No").append("\n");
            message.append("Description: ").append(description).append("\n");
            message.append("Rating: ").append(hikeRating);
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Save")
                    .setMessage(message.toString())
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            long hikeId = hikeRepository.insertHike(newHike);
                            if (hikeId > -1) {
                                Toast.makeText(CreateHike.this, "Saved new hike!" , Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CreateHike.this, Feed.class);
                                startActivity(intent);
                            } else {
                                Log.e("HikeInsertion", "Failed to save hike to the database. Hike ID: " + hikeId);
                                Toast.makeText(CreateHike.this, "Failed to save hike", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }

    }
    private void onCancelClicked() {
        Intent intent = new Intent(CreateHike.this, Feed.class);
        startActivity(intent);
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

        datePickerDialog.show();
    }
    private boolean validateInput() {
        String hikeName = createTextHikeName.getText().toString().trim();
        String location = createTextLocation.getText().toString().trim();
        String date = datePickerDate.getYear() + "-" +
                (datePickerDate.getMonth() + 1) + "-" +
                datePickerDate.getDayOfMonth();

        int selectedParkingRadioButtonId = radioGroupParkingAvailable.getCheckedRadioButtonId();

        String length = createTextLength.getText().toString().trim();

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
            Uri imageUri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                imageBlob = getBytes(inputStream);
                imageViewHike.setVisibility(View.VISIBLE);
                imageViewHike.setImageURI(imageUri);
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
