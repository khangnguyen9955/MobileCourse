package com.example.mhike;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
    private TextView hikeRatingTextView;
    private TextView hikeNameTextView;
    private TextView hikeLocationTextView;
    private TextView hikeDateTextView;
    private TextView hikeLengthTextView;
    private TextView hikeDifficultyTextView;
    private TextView hikeDescriptionTextView;
    private float hikeRating;
    private TextView hikeParkingStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Hike Details");

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
        hikeRatingTextView = findViewById(R.id.textViewHikeRating);

        Intent intent = getIntent();
        hikeId = intent.getIntExtra("hikeId", -1);

        recyclerView = findViewById(R.id.recyclerViewObservations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        observationAdapter = new ObservationAdapter();
        recyclerView.setAdapter(observationAdapter);
        loadObservationsForHike(hikeId);

        if (hikeId != -1) {
            Hike selectedHike = hikeRepository.getHike(hikeId);
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
           Toast.makeText(this, "Hike not found", Toast.LENGTH_SHORT).show();
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
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Deletion")
                    .setMessage("Are you sure you want to delete this hike and its observations?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            hikeRepository.deleteHike(hikeId);
                            Toast.makeText(HikeDetail.this, "Hike deleted", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(HikeDetail.this,Feed.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void updateUI(Hike selectedHike) {
        Log.i("HikeDetail", "updateUI");
        if (selectedHike != null) {
            hikeNameTextView.setText(selectedHike.getName());
            hikeLocationTextView.setText(selectedHike.getLocation());
            hikeDateTextView.setText(formatDate(selectedHike.getDate()));
            hikeLengthTextView.setText(selectedHike.getLength() + " km");
            hikeDifficultyTextView.setText(selectedHike.getDifficulty());
            hikeParkingStatus.setText(selectedHike.isParkingAvailable() ? "Yes" : "No");
            hikeRatingTextView.setText(String.valueOf(selectedHike.getRating()));
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
                ImageView hikeImageView = findViewById(R.id.imageViewBackground);
                hikeImageView.setImageResource(R.drawable.default_image_background);
            }
        } else {
            Toast.makeText(this, "Hike not found", Toast.LENGTH_SHORT).show();
        }
    }

    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(date);
    }

    private void createObservation(int hikeId) {
        Intent observationIntent = new Intent(HikeDetail.this, CreateObservation.class);
        observationIntent.putExtra("hikeId", hikeId);
        startActivity(observationIntent);
    }

    private void loadObservationsForHike(int hikeId) {
        List<Observation> observations = observationRepository.getObservationsForHike(hikeId);
        observationAdapter.setObservations(observations);
        observationAdapter.notifyDataSetChanged();
    }
}
