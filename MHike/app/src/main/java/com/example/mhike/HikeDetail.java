package com.example.mhike;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mhike.database.HikeRepository;
import com.example.mhike.database.ObservationRepository;
import com.example.mhike.models.Hike;
import com.example.mhike.models.Observation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HikeDetail extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ObservationAdapter observationAdapter;
    private ObservationRepository observationRepository;
    int hikeId;
    private HikeRepository hikeRepository;
    private TextView hikeNameTextView;
    private TextView hikeLocationTextView;
    private TextView hikeDateTextView;
    private TextView hikeLengthTextView;
    private TextView hikeDifficultyTextView;
    private TextView hikeDescriptionTextView;
    private TextView hikeParkingStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Hike Detail");

        setContentView(R.layout.item_hike);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize the repository
        hikeRepository = new HikeRepository(this);
        observationRepository = new ObservationRepository(this);

        hikeNameTextView = findViewById(R.id.textViewHikeTitle);
        hikeLocationTextView = findViewById(R.id.textViewHikeLocation);
        hikeDateTextView = findViewById(R.id.textViewHikeDate);
        hikeLengthTextView = findViewById(R.id.textViewHikeLength);
        hikeDifficultyTextView = findViewById(R.id.textViewHikeDifficulty);
        hikeDescriptionTextView = findViewById(R.id.textViewHikeDescription);
        hikeParkingStatus = findViewById(R.id.textViewHikeParkingStatus);
        // Retrieve data passed from HikeAdapter
        Intent intent = getIntent();
        hikeId = intent.getIntExtra("hikeId", -1);

        recyclerView = findViewById(R.id.recyclerViewObservations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        observationAdapter = new ObservationAdapter();
        recyclerView.setAdapter(observationAdapter);
        loadObservationsForHike(hikeId);

        if (hikeId != -1) {
            // Fetch the details of the selected hike based on the hikeId
            Hike selectedHike = hikeRepository.getHike(hikeId);
            Log.i("HikeDetail", "Hike: " + selectedHike.getName());
            updateUI(selectedHike);

            Button addObservationButton = findViewById(R.id.addObservationButton);
            addObservationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle the button click to create an observation
                    createObservation(selectedHike.getId());
                }
            });
        } else {
            // Handle the case where hikeId is not valid
            // For example, show an error message or navigate back to the previous screen
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

        if (id == R.id.action_edit) {
            // Handle the edit action
            Intent editIntent = new Intent(this, EditHike.class);
            editIntent.putExtra("hikeId", hikeId);
            Log.i("HikeEdit", "hikeId: " + hikeId);
            startActivity(editIntent);
            return true;
        } else if (id == R.id.action_delete) {
            // Handle the delete action
            // Implement deletion logic here, such as displaying a confirmation dialog
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    // Update the UI with the selected hike data
    private void updateUI(Hike selectedHike) {
        Log.i("HikeDetail", "updateUI");
        if (selectedHike != null) {
            hikeNameTextView.setText(selectedHike.getName());
            hikeLocationTextView.setText(selectedHike.getLocation());
            hikeDateTextView.setText(formatDate(selectedHike.getDate()));
            hikeLengthTextView.setText(selectedHike.getLength() + " km");
            hikeDifficultyTextView.setText(selectedHike.getDifficulty());
            hikeParkingStatus.setText(selectedHike.isParkingAvailable() ? "Yes" : "No");
            if (selectedHike.getDescription() == null || selectedHike.getDescription().isEmpty()) {
                hikeDescriptionTextView.setText("No description.");
            } else {
                hikeDescriptionTextView.setText(selectedHike.getDescription());
            }

            byte[] imageBlob = selectedHike.getImageBlob();
            if (imageBlob != null) {
                // Load the image from the byte array
                ImageView hikeImageView = findViewById(R.id.imageViewBackground);
                Glide.with(this)
                        .load(imageBlob)
                        .into(hikeImageView);
            } else {
                // Load a default image when imageBlob is null
                ImageView hikeImageView = findViewById(R.id.imageViewBackground);
                hikeImageView.setImageResource(R.drawable.default_image_background);
            }
        } else {
            // Handle the case where the selectedHike is null
        }
    }

    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(date);
    }

    private void createObservation(int hikeId) {
        // Open the ObservationActivity and pass the hikeId if needed
        Intent observationIntent = new Intent(HikeDetail.this, CreateObservation.class);
        observationIntent.putExtra("hikeId", hikeId);
        startActivity(observationIntent);
    }

    private void loadObservationsForHike(int hikeId) {
        Log.i("HikeDetail", "loadObservationsForHike");
        Log.i("HikeDetail", "hikeId: " + hikeId);
        List<Observation> observations = observationRepository.getObservationsForHike(hikeId);
        Log.i("HikeDetail", "observations: " + observations.size());
        observationAdapter.setObservations(observations);
        observationAdapter.notifyDataSetChanged();
    }
}
