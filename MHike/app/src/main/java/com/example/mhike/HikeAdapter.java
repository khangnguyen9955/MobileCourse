package com.example.mhike;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mhike.models.Hike;

import java.util.List;

public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.ViewHolder> {
    private Context context;
    private List<Hike> hikeList;

    public HikeAdapter(Context context, List<Hike> hikeList) {
        this.context = context;
        this.hikeList = hikeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i("HikeAdapter", "onCreateViewHolder");
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_hike, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i("HikeAdapter", "onBindViewHolder");
        Hike currentHike = hikeList.get(position);
        // Bind data to the ViewHolder views
        holder.textViewHikeTitle.setText(currentHike.getName());
        holder.textViewHikeLocation.setText(currentHike.getLocation());
        // Bind other hike details here...

        // Check if there is an image associated with the hike
        if (currentHike.getImagePath() != null) {
            // Load the image from the specified path
            Glide.with(context)
                    .load(currentHike.getImagePath())
                    .into(holder.imageViewBackground);
        } else {
            // Load a default image when imagePath is null
            holder.imageViewBackground.setImageResource(R.drawable.default_image_background);
        }

        // You can set click listeners here to handle item click events
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle item click (e.g., open detail activity)
            }
        });
    }

    @Override
    public int getItemCount() {
        return hikeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewHikeTitle;
        TextView textViewHikeLocation;
        ImageView imageViewBackground;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewHikeTitle = itemView.findViewById(R.id.textViewHikeTitle);
            textViewHikeLocation = itemView.findViewById(R.id.textViewHikeLocation);
            imageViewBackground = itemView.findViewById(R.id.imageViewBackground);
            // Initialize other views here...
        }
    }
}
