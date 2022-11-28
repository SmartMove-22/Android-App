package pt.ua.hackaton.smartmove;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Optional;

import pt.ua.hackaton.smartmove.data.Exercise;
import pt.ua.hackaton.smartmove.data.mocks.ExercisesMocks;
import pt.ua.hackaton.smartmove.handlers.CurrentExerciseHandler;
import pt.ua.hackaton.smartmove.utils.ExerciseCategory;

public class ExerciseReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_report);

        // Hide the top bar
        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().hide();
        }

        setUpDataInView();

        findViewById(R.id.exerciseReportQuitBtn).setOnClickListener(view -> {
            CurrentExerciseHandler.getInstance().cleanData();
            finish();
        });

    }

    private void setUpDataInView() {

        CurrentExerciseHandler currentExerciseHandler = CurrentExerciseHandler.getInstance();
        DecimalFormat decimalFormat = new DecimalFormat("##.##");
        Optional<Exercise> exercise = ExercisesMocks.getExercisesList()
                .stream()
                .filter(exercise1 -> exercise1.getId() == currentExerciseHandler.getExerciseId())
                .findFirst();

        TextView exerciseReportExerciseName = findViewById(R.id.exerciseReportExerciseNamePlaceholder);
        TextView exerciseReportCalories = findViewById(R.id.exerciseReportCaloriesPlaceholder);
        TextView exerciseReportCorrectness = findViewById(R.id.exerciseReportCorrectnessPlaceholder);
        TextView exerciseReportTotalTime = findViewById(R.id.exerciseReportTotalTimePlaceholder);
        TextView exerciseReportPacing = findViewById(R.id.exerciseReportAvgPerSetPlaceholder);

        String pacingStr = decimalFormat.format(currentExerciseHandler.calculatePacing());
        String caloriesStr = decimalFormat.format(currentExerciseHandler.calculateCaloriesBurn());

        exerciseReportExerciseName.setText(exercise.map(Exercise::getName).orElse("Unknown Exercise"));
        exerciseReportCalories.setText(caloriesStr);
        exerciseReportCorrectness.setText(String.valueOf(currentExerciseHandler.getAverageCorrectness()));
        exerciseReportTotalTime.setText(String.valueOf(currentExerciseHandler.getExerciseTimeInSeconds()));
        exerciseReportPacing.setText(pacingStr);

    }

}