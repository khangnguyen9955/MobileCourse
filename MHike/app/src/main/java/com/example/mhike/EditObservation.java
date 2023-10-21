package com.example.mhike;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mhike.database.ObservationRepository;
import com.example.mhike.models.Observation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditObservation extends AppCompatActivity {

    private EditText observationNameEditText;
    private TextView observationDateEditText;
    private EditText observationCommentsEditText;
    private DatePicker datePickerDate;
    private int observationId;
    private Observation observationToEdit;
    private ObservationRepository observationRepository;
    private Button buttonChooseDate;
    private Button buttonSaveObservation;
    private Button buttonCancelObservation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Edit Observation");
        setContentView(R.layout.activity_edit_observation);

        observationRepository = new ObservationRepository(this);

        observationNameEditText = findViewById(R.id.editTextObservationName);
        observationDateEditText = findViewById(R.id.dateDisplay);
        observationCommentsEditText = findViewById(R.id.editTextAdditionalComments);
        datePickerDate = findViewById(R.id.datePickerObservation);
        buttonChooseDate = findViewById(R.id.buttonChooseObservationDate);
        buttonSaveObservation = findViewById(R.id.buttonSaveObservation);
        buttonCancelObservation = findViewById(R.id.buttonCancel);
        observationId = getIntent().getIntExtra("observationId", -1);

        if (observationId != -1) {
            observationToEdit = observationRepository.getObservation(observationId);

            if (observationToEdit != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MMM dd", Locale.US);
                String formattedDate = dateFormat.format(observationToEdit.getDate());
                observationDateEditText.setText(formattedDate);
                observationNameEditText.setText(observationToEdit.getName());
                observationCommentsEditText.setText(observationToEdit.getComments());
            }
            else{
                Toast.makeText(this, "Observation not found", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Observation not found", Toast.LENGTH_SHORT).show();
        }

        buttonChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        buttonCancelObservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelClicked();
            }
        });
        buttonSaveObservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = observationNameEditText.getText().toString().trim();
                int day = datePickerDate.getDayOfMonth();
                int month = datePickerDate.getMonth();
                int year =datePickerDate.getYear();
                String comment = observationCommentsEditText.getText().toString().trim();
                onSaveClicked(name,day,month,year,comment);
            }
        });
    }
    private void onCancelClicked() {
        Intent observationDetailIntent = new Intent(EditObservation.this,ObservationDetail.class);
        observationDetailIntent.putExtra("observationId",observationId);
        observationDetailIntent.putExtra("hikeId",observationToEdit.getHikeId());
        startActivity(observationDetailIntent);
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
    private void onSaveClicked(String name, int day, int month, int year, String comment) {
        if (validateInput(name)) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            Date date = calendar.getTime();

            observationToEdit.setName(name);
            observationToEdit.setComments(comment);
            observationToEdit.setDate(date);

            observationRepository.updateObservation(observationToEdit);
            Toast.makeText(this, "Saved observation!", Toast.LENGTH_SHORT).show();
            Intent observationDetailIntent = new Intent(EditObservation.this,ObservationDetail.class);
            observationDetailIntent.putExtra("observationId",observationId);
            observationDetailIntent.putExtra("hikeId",observationToEdit.getHikeId());
            startActivity(observationDetailIntent);
        }
    }
    private boolean validateInput(String name) {
        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter the name of the observation", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
