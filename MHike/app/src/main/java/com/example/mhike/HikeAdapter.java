package com.example.mhike;

import android.content.Context;
import android.content.Intent;
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

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

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
        holder.textViewHikeTitle.setText(currentHike.getName());
        holder.textViewHikeLocation.setText(currentHike.getLocation());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String formattedDate = dateFormat.format(currentHike.getDate());
        holder.textViewHikeDate.setText(formattedDate);
        holder.textViewLength.setText(currentHike.getLength() + " km");
        holder.textViewDifficulty.setText(currentHike.getDifficulty());

        // Check if there is an image associated with the hike
        byte[] imageBlob = currentHike.getImageBlob();
        if (imageBlob != null) {
            // Load the image from the byte array
            Glide.with(context)
                    .load(imageBlob)
                    .into(holder.imageViewBackground);
        } else {
            // Load a default image when imageBlob is null
            holder.imageViewBackground.setImageResource(R.drawable.default_image_background);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HikeDetail.class);
                intent.putExtra("hikeId", currentHike.getId());
                context.startActivity(intent);
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
        TextView textViewHikeDate;
        TextView textViewLength;
        TextView textViewDifficulty;

        ImageView imageViewBackground;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewHikeTitle = itemView.findViewById(R.id.textViewHikeTitle);
            textViewHikeLocation = itemView.findViewById(R.id.textViewHikeLocation);
            textViewLength = itemView.findViewById(R.id.textViewHikeLength);
            textViewDifficulty = itemView.findViewById(R.id.textViewHikeDifficulty);
            textViewHikeDate = itemView.findViewById(R.id.textViewHikeDate);
            imageViewBackground = itemView.findViewById(R.id.imageViewBackground);
            // Initialize other views here...
        }
    }
}
