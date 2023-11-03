package com.example.imagecw;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    int[] imageArray;
    int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button_next = findViewById(R.id.button_next);
        Button button_prev = findViewById(R.id.button_prev);
        final ImageView imageView = findViewById(R.id.imageView);

        Field[] drawables = R.drawable.class.getFields();
        List<Integer> drawableResourceIds = new ArrayList<>();

        for (Field field : drawables) {
            if (field.getName().startsWith("image_")) {
                try {
                    drawableResourceIds.add(field.getInt(null));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        imageArray = new int[drawableResourceIds.size()];
        for (int i = 0; i < drawableResourceIds.size(); i++) {
            imageArray[i] = drawableResourceIds.get(i);
        }

        button_next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                currentIndex++;
                if (currentIndex == imageArray.length) currentIndex = 0;
                imageView.setImageResource(imageArray[currentIndex]);
            }
        });

        button_prev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                currentIndex--;
                if (currentIndex < 0) currentIndex = imageArray.length - 1;
                imageView.setImageResource(imageArray[currentIndex]);
            }
        });
    }
}

