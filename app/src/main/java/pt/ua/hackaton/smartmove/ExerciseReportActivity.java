package pt.ua.hackaton.smartmove;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ExerciseReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_report);

        // Hide the top bar
        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().hide();
        }

        double calories = getIntent().getLongExtra("calories", 0);
        double correctness = getIntent().getDoubleExtra("correctness", 0);
        double pacing = getIntent().getDoubleExtra("pacing", 0);
        double time = getIntent().getDoubleExtra("time", 0);
        double averageTime = getIntent().getDoubleExtra("averageTime", 0);

        Log.d("TESTEE", String.valueOf(correctness));

        findViewById(R.id.exerciseReportQuitBtn).setOnClickListener(view -> {
            finish();
        });

        ((TextView)findViewById(R.id.exerciseReportCaloriesPlaceholder)).setText(String.valueOf(calories));
        ((TextView)findViewById(R.id.exerciseReportCorrectnessPlaceholder)).setText(String.valueOf(correctness));
        ((TextView)findViewById(R.id.exerciseReportPerformancePlaceholder)).setText("N/A");
        ((TextView)findViewById(R.id.exerciseReportPacingPlaceholder)).setText("N/A");
        ((TextView)findViewById(R.id.exerciseReportTotalTimePlaceholder)).setText(String.valueOf(time));
        ((TextView)findViewById(R.id.exerciseReportAvgPerSetPlaceholder)).setText(String.valueOf(averageTime));
    }

}