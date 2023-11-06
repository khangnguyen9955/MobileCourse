package com.example.mhike;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.mhike.database.ObservationRepository;
import com.example.mhike.models.Observation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateObservation extends AppCompatActivity {

    private EditText observationNameEditText;
    private DatePicker datePickerObservation;
    private EditText observationCommentsEditText;
    private Button buttonChooseObservationDate;
    private Button saveObservationButton;
    private Button buttonChooseObservationImage;
    private ObservationRepository observationRepository;
    private int hikeId;private byte[] imageBlob;

    private static final int PICK_IMAGE_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_observation);
        setTitle("Add Observation");

        observationRepository = new ObservationRepository(this);

        observationNameEditText = findViewById(R.id.editTextObservationName);
        datePickerObservation = findViewById(R.id.datePickerObservation);
        observationCommentsEditText = findViewById(R.id.editTextAdditionalComments);
        saveObservationButton = findViewById(R.id.buttonSaveObservation);
        buttonChooseObservationDate = findViewById(R.id.buttonChooseObservationDate);
        buttonChooseObservationImage = findViewById(R.id.buttonChooseObservationImage);

        hikeId = getIntent().getIntExtra("hikeId", -1);

        saveObservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = observationNameEditText.getText().toString().trim();
                int day = datePickerObservation.getDayOfMonth();
                int month = datePickerObservation.getMonth();
                int year = datePickerObservation.getYear();
                String comment = observationCommentsEditText.getText().toString().trim();

                saveObservation(name, day, month, year, comment);
            }
        });
        buttonChooseObservationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        buttonChooseObservationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
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

    private boolean validateInput(String name) {
        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter the name of the observation", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void saveObservation(String name, int day, int month, int year, String comment) {
        if (validateInput(name)) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            Date date = calendar.getTime();

            Observation observation = new Observation();
            observation.setHikeId(hikeId);
            observation.setName(name);
            observation.setComments(comment);
            observation.setDate(date);
            observation.setImageBlob(imageBlob);
            observationRepository.insertObservation(observation);
            Toast.makeText(this, "Saved new observation!", Toast.LENGTH_SHORT).show();
            Intent hikeDetailIntent = new Intent(this, HikeDetail.class);
            hikeDetailIntent.putExtra("hikeId", hikeId);
            startActivity(hikeDetailIntent);
        }
    }

    private void onCancelClicked() {
        finish();
    }

    private void showDatePicker() {
        int year = datePickerObservation.getYear();
        int month = datePickerObservation.getMonth();
        int day = datePickerObservation.getDayOfMonth();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        datePickerObservation.updateDate(selectedYear, selectedMonth, selectedDay);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                imageBlob = getBytes(inputStream);
                ImageView imageViewObservation = findViewById(R.id.imageViewUploaded);
                imageViewObservation.setVisibility(View.VISIBLE);
                imageViewObservation.setImageURI(imageUri);
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

    private String formatDate(int year, int month, int day) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return sdf.format(calendar.getTime());
    }
}
