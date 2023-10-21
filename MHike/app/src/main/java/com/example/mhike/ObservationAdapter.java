package com.example.mhike;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mhike.models.Observation;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ObservationAdapter extends RecyclerView.Adapter<ObservationAdapter.ObservationViewHolder> {
    private List<Observation> observations;

    @NonNull
    @Override
    public ObservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_observation, parent, false);
        return new ObservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ObservationViewHolder holder, int position) {
        Observation observation = observations.get(position);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd", Locale.US);
        String formattedDate = sdf.format(observation.getDate());
        holder.nameTextView.setText(observation.getName());
        holder.dateTextView.setText(formattedDate);
        holder.commentsTextView.setText(observation.getComments().isEmpty() ? "No comments" :  observation.getComments() );

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(),ObservationDetail.class);
            intent.putExtra("observationId", observation.getId());
            intent.putExtra("hikeId", observation.getHikeId());
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return observations != null ? observations.size() : 0;
    }

    public void setObservations(List<Observation> observations) {
        this.observations = observations;
        Log.i("ObservationAdapter", "Observations: " + observations.size());
    }

    public static class ObservationViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView dateTextView;
        TextView commentsTextView;

        public ObservationViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textObservationName);
            dateTextView = itemView.findViewById(R.id.textObservationDate);
            commentsTextView = itemView.findViewById(R.id.textObservationComments);
        }
    }



}
