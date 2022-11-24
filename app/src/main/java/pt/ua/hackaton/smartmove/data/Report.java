package pt.ua.hackaton.smartmove.data;

import java.util.Date;
import java.util.List;

public class Report {

    private final long id;
    private final Date date;
    private final List<AssignedExercise> exercises;
    private final double correctness;
    private final double performance;
    private final double improvement;
    private final double caloriesBurn;

    public Report(long id, Date date, List<AssignedExercise> exercises, double correctness, double performance, double improvement, double caloriesBurn) {
        this.id = id;
        this.date = date;
        this.exercises = exercises;
        this.correctness = correctness;
        this.performance = performance;
        this.improvement = improvement;
        this.caloriesBurn = caloriesBurn;
    }

    public long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public List<AssignedExercise> getExercises() {
        return exercises;
    }

    public double getCorrectness() {
        return correctness;
    }

    public double getPerformance() {
        return performance;
    }

    public double getImprovement() {
        return improvement;
    }

    public double getCaloriesBurn() {
        return caloriesBurn;
    }
}
