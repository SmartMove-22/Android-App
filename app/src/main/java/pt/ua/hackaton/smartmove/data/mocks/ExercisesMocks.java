package pt.ua.hackaton.smartmove.data.mocks;

import java.util.ArrayList;
import java.util.List;

import pt.ua.hackaton.smartmove.R;
import pt.ua.hackaton.smartmove.data.Category;
import pt.ua.hackaton.smartmove.data.Exercise;
import pt.ua.hackaton.smartmove.utils.ExerciseCategory;

public class ExercisesMocks {

    public static List<Exercise> getExercisesList() {

        List<Exercise> exercises = new ArrayList<>();

        exercises.add(new Exercise(1, null, "Legs Muscles", new Category(1, ExerciseCategory.SQUAT, ""), 1,1,300, R.drawable.lift_weight));
        exercises.add(new Exercise(2, null, "Abdominal Muscles", new Category(1, ExerciseCategory.SQUAT, ""), 1,1,300, R.drawable.lift_weight));
        exercises.add(new Exercise(3, null, "Push Ups", new Category(1, ExerciseCategory.SQUAT, ""), 1,1,300, R.drawable.lift_weight));

        return exercises;

    }

    public static Exercise getExerciseById(int exerciseId) {
        return getExercisesList().get(exerciseId);
    }

}
