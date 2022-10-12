package pt.ua.hackaton.smartmove.data;

import java.util.List;

public class Coach {

    private final List<Trainee> trainees;
    private final List<Exercise> availableExercises;
    private final List<AssignedExercise> assignedExercises;

    public Coach(List<Trainee> trainees, List<Exercise> availableExercises, List<AssignedExercise> assignedExercises) {
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
