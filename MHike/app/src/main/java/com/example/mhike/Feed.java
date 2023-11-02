package com.example.mhike;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.mhike.database.HikeRepository;
import com.example.mhike.models.Hike;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Feed extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText inputSearch;
    private ImageView filterIcon;
    private HikeAdapter hikeAdapter;
    private List<Hike> hikeList;
    private Button createHikeButton;
    private HikeRepository hikeRepository;
    private boolean isDateAscending = false;
    private boolean isLengthAscending = false;
    private boolean isRatingAscending = false;
    private boolean isNameChecked = false;
    private boolean isLocationChecked = false;
    private LinearLayout linearLayoutDate;
    private LinearLayout linearLayoutLength;
    private LinearLayout linearLayoutRating;
    private boolean isPopupShowing = false;
    private HikeQueryOptions queryOptions;
    private String currentFilterOption = "Name";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        setTitle("Your Hikes");
        recyclerView = findViewById(R.id.recyclerView);
        createHikeButton = findViewById(R.id.buttonCreateHike);
        inputSearch = findViewById(R.id.inputSearch);
        filterIcon = findViewById(R.id.filterIcon);
        linearLayoutDate = findViewById(R.id.linearLayoutDate);
        linearLayoutLength = findViewById(R.id.linearLayoutLength);
        linearLayoutRating = findViewById(R.id.linearLayoutRating);
        hikeRepository = new HikeRepository(this);

        hikeList = new ArrayList<>();
        queryOptions = new HikeQueryOptions();
        hikeAdapter = new HikeAdapter(this,hikeList);
        recyclerView.setAdapter(hikeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadInitialCheckboxState();
        loadHikes();
        createHikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Feed.this, CreateHike.class);
                startActivity(intent);
            }
        });
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                delaySearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        filterIcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!isPopupShowing) {
                    showFilterOptions();
                }
            }
        });

        linearLayoutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageView arrowDate = findViewById(R.id.arrowDate);
                isDateAscending = !isDateAscending;
                if (isDateAscending) {
                    arrowDate.setImageResource(R.drawable.baseline_arrow_drop_up_24);
                    queryOptions.pushSortingField("date asc");
                } else {
                    arrowDate.setImageResource(R.drawable.baseline_arrow_drop_down_24);
                    queryOptions.pushSortingField("date desc");
                }
                filterAndDisplayHikes(inputSearch.getText().toString());

            }
        });

        linearLayoutLength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView arrowLength = findViewById(R.id.arrowLength);
                isLengthAscending = !isLengthAscending;
                if (isLengthAscending) {
                    arrowLength.setImageResource(R.drawable.baseline_arrow_drop_up_24);
                    queryOptions.pushSortingField("length asc");
                } else {
                    arrowLength.setImageResource(R.drawable.baseline_arrow_drop_down_24);
                    queryOptions.pushSortingField("length desc");
                }

                queryOptions.setLengthAscending(isLengthAscending);
                filterAndDisplayHikes(inputSearch.getText().toString());

            }
        });

        linearLayoutRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView arrowRating = findViewById(R.id.arrowRating);
                isRatingAscending = !isRatingAscending;
                if (isRatingAscending) {
                    arrowRating.setImageResource(R.drawable.baseline_arrow_drop_up_24);
                    queryOptions.pushSortingField("rating asc");
                } else {
                    arrowRating.setImageResource(R.drawable.baseline_arrow_drop_down_24);
                    queryOptions.pushSortingField("rating desc");
                }

                queryOptions.setRatingAscending(isRatingAscending);
                filterAndDisplayHikes(inputSearch.getText().toString());

            }
        });

    }

    private android.os.Handler searchHandler = new android.os.Handler();
    private Runnable searchRunnable;

    private void delaySearch(final String query) {
        searchHandler.removeCallbacks(searchRunnable);
        searchRunnable = new Runnable() {
            @Override
            public void run() {
                searchHikes(query);
            }
        };

        searchHandler.postDelayed(searchRunnable, 2000);
    }

    private void searchHikes(String query) {
        if (query.isEmpty()) {
            loadHikes();
        } else {
            filterAndDisplayHikes(query);
        }
    }
    private void loadInitialCheckboxState() {
        View filterMenuView = getLayoutInflater().inflate(R.layout.filter_menu, null);

        CheckBox checkBoxName = filterMenuView.findViewById(R.id.checkBoxName);
        CheckBox checkBoxLocation = filterMenuView.findViewById(R.id.checkBoxLocation);

        checkBoxName.setChecked(isNameChecked);
        checkBoxLocation.setChecked(isLocationChecked);

    }

    private void showFilterOptions() {
        isPopupShowing = true;
        View filterMenuView = getLayoutInflater().inflate(R.layout.filter_menu, null);

        CheckBox checkBoxName = filterMenuView.findViewById(R.id.checkBoxName);
        CheckBox checkBoxLocation = filterMenuView.findViewById(R.id.checkBoxLocation);

        checkBoxName.setChecked(isNameChecked);
        checkBoxLocation.setChecked(isLocationChecked);

        checkBoxName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isNameChecked = isChecked;
                queryOptions.setFilterByName(isChecked);
                filterAndDisplayHikes(inputSearch.getText().toString());
            }
        });

        checkBoxLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isLocationChecked = isChecked;
                queryOptions.setFilterByLocation(isChecked);
                filterAndDisplayHikes(inputSearch.getText().toString());
            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        PopupWindow popupWindow = new PopupWindow(filterMenuView,width/2, height / 10, true);
        popupWindow.setFocusable(true);

        filterMenuView.setBackgroundResource(R.drawable.popup_background);
        popupWindow.showAsDropDown(filterIcon, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                isPopupShowing = false; // Reset the flag when the popup is dismissed
            }
        });
    }

    private void filterAndDisplayHikes(String query) {
        hikeList.clear();
        List<Hike> filteredHikes = hikeRepository.getFilteredHikes(queryOptions, query);
        hikeList.addAll(filteredHikes);
        hikeAdapter.notifyDataSetChanged();
    }

    private void loadHikes() {
        hikeList.clear();
        Log.e("HikeList", "Clear");

        hikeList.addAll(hikeRepository.getAllHikes());
        hikeAdapter.notifyDataSetChanged();
    }
}
