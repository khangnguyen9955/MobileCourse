package com.example.mhike;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mhike.database.HikeRepository;
import com.example.mhike.models.Hike;

public class EditHike extends AppCompatActivity {

    private EditText hikeNameEditText;
    private EditText hikeLocationEditText;
    private EditText hikeLengthEditText;
    private EditText hikeDifficultyEditText;
    private EditText hikeDescriptionEditText;
    private RadioGroup radioGroupDifficulty;
    private RadioGroup radioGroupParkingAvailable;

    private Button saveHikeButton;
    private Button editImageButton;
    private int hikeId;
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
        radioGroupDifficulty = findViewById(R.id.radioGroupDifficulty);
        radioGroupParkingAvailable = findViewById(R.id.radioGroupParkingAvailable);
        hikeDescriptionEditText = findViewById(R.id.editTextDescription);
        saveHikeButton = findViewById(R.id.buttonSaveHike);
        editImageButton = findViewById(R.id.buttonUploadImage);

        hikeId = getIntent().getIntExtra("hikeId", -1);

        Hike selectedHike = hikeRepository.getHike(hikeId);

        if (selectedHike != null) {
            hikeNameEditText.setText(selectedHike.getName());
            hikeLocationEditText.setText(selectedHike.getLocation());
            hikeLengthEditText.setText(selectedHike.getLength());
            hikeDifficultyEditText.setText(selectedHike.getDifficulty());
            hikeDescriptionEditText.setText(selectedHike.getDescription());

            if (selectedHike.getImageBlob() != null ) {
                editImageButton.setText("Edit Image");
            }
        }
        editImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle image selection/editing here
                // You can open the image gallery or a file picker to allow the user to select or update the image
                // If the button label is "Add Image," it means no image is associated with the hike. Allow the user to add one.
                // If the button label is "Edit Image," it means an image exists, and the user can update it.
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
        saveHikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hikeName = hikeNameEditText.getText().toString();
                String hikeLocation = hikeLocationEditText.getText().toString();
                String hikeLength = hikeLengthEditText.getText().toString();
                String hikeDifficulty = hikeDifficultyEditText.getText().toString();
                String hikeDescription = hikeDescriptionEditText.getText().toString();
                boolean parkingAvailable = radioGroupParkingAvailable.getCheckedRadioButtonId() == R.id.radioButtonYes;

                updateHike(hikeId, hikeName, hikeLocation, hikeLength, hikeDifficulty, hikeDescription, parkingAvailable);

                Intent hikeDetailIntent = new Intent(EditHike.this, HikeDetail.class);
                hikeDetailIntent.putExtra("hikeId", hikeId);
                startActivity(hikeDetailIntent);
            }
        });


    }

    private void updateHike(int hikeId, String name, String location, String length, String difficulty, String description, boolean parkingAvailable) {
        Hike hike = hikeRepository.getHike(hikeId);
        if (hike != null) {
            hike.setName(name);
            hike.setLocation(location);
            hike.setLength(length);
            hike.setDifficulty(difficulty);
            hike.setDescription(description);
            hike.setParkingAvailable(parkingAvailable);
            // You can update the image path in the Hike model if it has changed
            // Example: hike.setImagePath(newImagePath);

            hikeRepository.updateHike(hike);
        }
    }
}
