package com.example.mhike;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.mhike.database.HikeRepository;
import com.example.mhike.models.Hike;

import java.util.ArrayList;
import java.util.List;

public class Feed extends AppCompatActivity {
    private RecyclerView recyclerView;
    private HikeAdapter hikeAdapter;
    private List<Hike> hikeList;
    private Button createHikeButton;
    private HikeRepository hikeRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        setTitle("Feed");
        recyclerView = findViewById(R.id.recyclerView);
        createHikeButton = findViewById(R.id.buttonCreateHike);

        // Initialize the HikeRepository
        hikeRepository = new HikeRepository(this);

        // Initialize the hikeList
        hikeList = new ArrayList<>();

        // Initialize the HikeAdapter and set it to the RecyclerView
        hikeAdapter = new HikeAdapter(this,hikeList); // Create a custom adapter for your hikes
        recyclerView.setAdapter(hikeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        createHikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Feed.this, CreateHike.class);
                startActivity(intent);
            }
        });

        // Load and display the list of hikes
        loadHikes();
    }

    private void loadHikes() {
        hikeList.clear();
        Log.e("HikeList", "Clear");

        hikeList.addAll(hikeRepository.getAllHikes());
        hikeAdapter.notifyDataSetChanged();
    }
}
