package pt.ua.hackaton.smartmove.data;

import java.util.List;

public class Coach extends User {

    private final List<Trainee> trainees;
    private final List<Exercise> availableExercises;
    private final List<AssignedExercise> assignedExercises;

    public Coach(String username, String email, String password, String image, List<Trainee> trainees, List<Exercise> availableExercises, List<AssignedExercise> assignedExercises) {
        super(username, email, password, image);
        this.trainees = trainees;
        this.availableExercises = availableExercises;
        this.assignedExercises = assignedExercises;
    }

    public List<Trainee> getTrainees() {
        return trainees;
    }

    public List<Exercise> getAvailableExercises() {
        return availableExercises;
    }

    public List<AssignedExercise> getAssignedExercises() {
        return assignedExercises;
    }

}
