package pt.ua.hackaton.smartmove;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ExerciseReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_report);

        // Hide the top bar
        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().hide();
        }

        findViewById(R.id.exerciseReportQuitBtn).setOnClickListener(view -> {
            finish();
        });

    }



}