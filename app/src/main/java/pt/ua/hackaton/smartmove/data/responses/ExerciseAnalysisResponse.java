package pt.ua.hackaton.smartmove.data.responses;

import com.google.gson.annotations.SerializedName;

public class ExerciseAnalysisResponse {

    private double correctness;
    private double progress;

    @SerializedName("finished_repetition")
    private boolean finishedRepetition;

    @SerializedName("first_half")
    private boolean firstHalf;

    @SerializedName("most_divergent_angle_landmark_middle")
    private int worstMiddleLandmark;

    @SerializedName("most_divergent_angle_value")
    private double worstAngle;

    public ExerciseAnalysisResponse(double correctness, double progress, boolean finishedRepetition, boolean firstHalf) {
        this.correctness = correctness;
        this.progress = progress;
        this.finishedRepetition = finishedRepetition;
        this.firstHalf = firstHalf;
    }

    public double getCorrectness() {
        return correctness;
    }

    public double getProgress() {
        return progress;
    }

    public boolean getFinishedRepetition() {
        return finishedRepetition;
    }

    public boolean isFirstHalf() {
        return firstHalf;
    }

    public boolean isFinishedRepetition() {
        return finishedRepetition;
    }

    public int getWorstMiddleLandmark() {
        return worstMiddleLandmark;
    }

    public double getWorstAngle() {
        return worstAngle;
    }

}
