package pt.ua.hackaton.smartmove.data.mocks;

import java.util.ArrayList;
import java.util.List;

import pt.ua.hackaton.smartmove.R;
import pt.ua.hackaton.smartmove.data.Category;
import pt.ua.hackaton.smartmove.data.Exercise;

public class ExercisesMocks {

    public static List<Exercise> getExercisesList() {

        List<Exercise> exercises = new ArrayList<>();

        exercises.add(new Exercise(1, null, "Legs Muscles", new Category(1, "squat", ""), 1,1,300, R.drawable.lift_weight));
        exercises.add(new Exercise(1, null, "Abdominal Muscles", new Category(1, "squat", ""), 1,1,300, R.drawable.lift_weight));
        exercises.add(new Exercise(1, null, "Push Ups", new Category(1, "squat", ""), 1,1,300, R.drawable.lift_weight));

        return exercises;

    }

}
