package pt.ua.hackaton.smartmove.data;

import java.util.List;

public class Trainee extends User {

    private final Coach coach;
    private final List<AssignedExercise> exercises;
    private final List<Report> reports;

    public Trainee(String username, String email, String password, String image, Coach coach, List<AssignedExercise> exercises, List<Report> reports) {
        super(username, email, password, image);
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
