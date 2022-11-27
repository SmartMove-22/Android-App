package pt.ua.hackaton.smartmove.data.requests;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import pt.ua.hackaton.smartmove.data.LandmarkPoint;

public class ExerciseDataRequest {

    private final long time;

    @SerializedName("first_half")
    private final boolean firstHalf;

    @SerializedName("exercise_category")
    private final String exerciseCategory;

    private final List<LandmarkPoint> landmarks;

    public ExerciseDataRequest(long time, boolean firstHalf, String exerciseCategory, List<LandmarkPoint> landmarks) {
        this.time = time;
        this.firstHalf = firstHalf;
        this.exerciseCategory = exerciseCategory;
        this.landmarks = landmarks;
    }

    public long getTime() {
        return time;
    }

    public boolean isFirstHalf() {
        return firstHalf;
    }

    public String getExerciseCategory() {
        return exerciseCategory;
    }

    public List<LandmarkPoint> getLandmarks() {
        return landmarks;
    }

}
