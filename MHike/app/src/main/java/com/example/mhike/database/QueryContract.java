package com.example.mhike.database;

import com.example.mhike.HikeQueryOptions;
import com.example.mhike.models.Hike;
import com.example.mhike.models.Observation;

import java.util.List;

public final class QueryContract {
    private QueryContract() {} // Make it non-instantiable

    public interface HikeRepository {
        long insertHike(Hike hike);
        Hike getHike(int hikeId);
        List<Hike> getAllHikes();
        int updateHike(Hike hike);
        void deleteHike(int hikeId);
        List<Hike> getFilteredHikes(HikeQueryOptions queryOptions, String query);
    }

    public interface ObservationRepository {
        long insertObservation(Observation observation);
        Observation getObservation(int observationId);
        List<Observation> getObservationsForHike(int hikeId);
        int updateObservation(Observation observation);
        void deleteObservation(int observationId);
    }
}
