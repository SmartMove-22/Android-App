package pt.ua.hackaton.smartmove.handlers;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import pt.ua.hackaton.smartmove.data.database.entities.ExerciseReportEntity;
import pt.ua.hackaton.smartmove.utils.ExerciseCategory;

public class CurrentExerciseHandler {

    private static CurrentExerciseHandler instance;

    private int exerciseId;
    private ExerciseCategory exerciseCategory;
    private long exerciseStartTimestamp;
    private int exerciseRepetitions;
    private List<Double> exerciseCorrectnessMeasures;
    private List<Double> exerciseHeartRateMeasures;

    private CurrentExerciseHandler() {
        cleanData();
    }

    public void addCorrectnessMeasure(Double correctness) {
        exerciseCorrectnessMeasures.add(correctness);
    }

    public void addExerciseHeartRate(Double exerciseHeartRate) {
        exerciseHeartRateMeasures.add(exerciseHeartRate);
    }

    public void addExerciseRepetition() {
        exerciseRepetitions += 1;
    }

    public Double calculateCaloriesBurn() {

        switch (exerciseCategory) {
            case SQUAT:
                return (2.9*3.5*70/200)*(getExerciseTimeInSeconds()/60d);
            case PUSH_UP:
                return (2.6*3.5*70/200)*(getExerciseTimeInSeconds()/60d);
            case ABDOMINAL:
                return (2.8*3.5*70/200)*(getExerciseTimeInSeconds()/60d);
            default:
                return 0d;
        }

    }

    public Double calculatePacing() {
        return (double) exerciseRepetitions/getExerciseTimeInSeconds();
    }

    public Double getAverageCorrectness() {
        return exerciseCorrectnessMeasures
                .stream()
                .reduce(0d, Double::sum);
    }

    public Double getAverageHeartRate() {
        return exerciseCorrectnessMeasures
                .stream()
                .reduce(0d, Double::sum);
    }

    public void startExerciseTimer() {
        this.exerciseStartTimestamp = System.currentTimeMillis();
    }

    public long getExerciseTimeInSeconds() {
        final int initializationTime = 5;
        return Math.round((System.currentTimeMillis() - this.exerciseStartTimestamp)/1000d) - initializationTime;
    }

    public ExerciseReportEntity toExerciseReportEntity() {
        return new ExerciseReportEntity(Date.from(Instant.now()), exerciseId, getExerciseTimeInSeconds(), getAverageCorrectness(), calculateCaloriesBurn(), getAverageHeartRate());
    }

    public void cleanData() {
        this.exerciseId = -1;
        this.exerciseCategory = null;
        this.exerciseStartTimestamp = 0;
        this.exerciseRepetitions = 0;
        this.exerciseCorrectnessMeasures = new ArrayList<>();
        this.exerciseHeartRateMeasures = new ArrayList<>();
    }

    // Getters

    public int getExerciseId() {
        return exerciseId;
    }

    public List<Double> getExerciseCorrectnessMeasures() {
        return exerciseCorrectnessMeasures;
    }

    public List<Double> getExerciseHeartRateMeasures() {
        return exerciseHeartRateMeasures;
    }

    public long getExerciseStartTimestamp() {
        return exerciseStartTimestamp;
    }

    public int getExerciseRepetitions() {
        return exerciseRepetitions;
    }

    // Setters

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public void setExerciseCategory(ExerciseCategory exerciseCategory) {
        this.exerciseCategory = exerciseCategory;
    }

    public void setExerciseStartTimestamp(long exerciseStartTimestamp) {
        this.exerciseStartTimestamp = exerciseStartTimestamp;
    }

    public static CurrentExerciseHandler getInstance() {
        if (instance == null)
            instance = new CurrentExerciseHandler();
        return instance;
    }

}
