package pt.ua.hackaton.smartmove;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class ExerciseReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_report);

        // Hide the top bar
        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().hide();
        }

        double calories = getIntent().getLongExtra("calories", -1);
        double correctness = getIntent().getDoubleExtra("correctness", 0);

        Log.d("TESTEE", String.valueOf(correctness));

        findViewById(R.id.exerciseReportQuitBtn).setOnClickListener(view -> {
            finish();
        });

    }

}