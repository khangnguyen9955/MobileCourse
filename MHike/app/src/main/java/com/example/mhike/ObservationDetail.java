package com.example.mhike;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mhike.database.ObservationRepository;
import com.example.mhike.models.Hike;
import com.example.mhike.models.Observation;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ObservationDetail extends AppCompatActivity {

        private int hikeId;
        private int observationId;
        private ObservationRepository observationRepository;

        private TextView observationNameTextView;
        private TextView observationDateTextView;
        private TextView observationCommentsTextView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_observation_detail);
            setTitle("Observation Details");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            hikeId = getIntent().getIntExtra("hikeId", -1);
            observationId = getIntent().getIntExtra("observationId", -1);
            Log.i("ObservationDetail", "HikeId: " + hikeId);

            observationRepository = new ObservationRepository(this);

            observationNameTextView = findViewById(R.id.textViewObservationName);
            observationDateTextView = findViewById(R.id.textViewObservationDate);
            observationCommentsTextView = findViewById(R.id.textViewObservationComments);

            loadObservationDetails(observationId);
        }

        private void loadObservationDetails(int observationId) {
            Observation observation = observationRepository.getObservation(observationId);
            if (observation != null) {
                observationNameTextView.setText(observation.getName());
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MMM dd", Locale.US);
                String formattedDate = dateFormat.format(observation.getDate());
                observationDateTextView.setText(formattedDate);
                observationCommentsTextView.setText(observation.getComments().isEmpty() ? "No comments": observation.getComments());
            }
            else{
                Toast.makeText(this, "Observation not found", Toast.LENGTH_SHORT).show();
            }
        }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
        if(id == android.R.id.home){
                if (hikeId != -1) {
                    Log.i("ObservationDetail", "HikeId: " + hikeId);
                    Intent intent = new Intent(this, HikeDetail.class);
                    intent.putExtra("hikeId", hikeId);
                    intent.putExtra("observationId", observationId);
                    startActivity(intent);
                }
                return true;
        }
        else if (id == R.id.action_edit){
                Intent intent = new Intent(this, EditObservation.class);
                intent.putExtra("observationId", observationId);
                intent.putExtra("hikeId", hikeId);
                startActivity(intent);
                return true;
        }
        else if (id == R.id.action_delete) {
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Deletion")
                    .setMessage("Are you sure you want to delete this observation?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            observationRepository.deleteObservation(observationId);
                            Toast.makeText(ObservationDetail.this, "Observation deleted", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ObservationDetail.this, HikeDetail.class);
                            intent.putExtra("hikeId", hikeId);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        }

                return super.onOptionsItemSelected(item);
        }
    }

