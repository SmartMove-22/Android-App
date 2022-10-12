package pt.ua.hackaton.smartmove.data;

import java.util.List;

public class Trainee {

    private final Coach coach;
    private final List<AssignedExercise> exercises;
    private final List<Report> reports;

    public Trainee(Coach coach, List<AssignedExercise> exercises, List<Report> reports) {
        this.coach = coach;
        this.exercises = exercises;
        this.reports = reports;
    }

    public Coach getCoach() {
        return coach;
    }

    public List<AssignedExercise> getExercises() {
        return exercises;
    }

    public List<Report> getReports() {
        return reports;
    }

}
