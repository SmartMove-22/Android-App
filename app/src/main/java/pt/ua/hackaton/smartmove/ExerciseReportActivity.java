package pt.ua.hackaton.smartmove;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import pt.ua.hackaton.smartmove.data.Exercise;
import pt.ua.hackaton.smartmove.data.mocks.ExercisesMocks;
import pt.ua.hackaton.smartmove.handlers.CurrentExerciseHandler;

public class ExerciseReportActivity extends AppCompatActivity {

    private final List<Entry> chartData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_report);

        // Hide the top bar
        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().hide();
        }

        setUpDataInView();

        initHRChart();
        setupHRChart(getWindow().getDecorView().getRootView());

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
        String correctnessStr = decimalFormat.format(currentExerciseHandler.getAverageCorrectness());

        exerciseReportExerciseName.setText(exercise.map(Exercise::getName).orElse("Unknown Exercise"));
        exerciseReportCalories.setText(caloriesStr);
        exerciseReportCorrectness.setText(correctnessStr);
        exerciseReportTotalTime.setText(String.valueOf(currentExerciseHandler.getExerciseTimeInSeconds()));
        exerciseReportPacing.setText(pacingStr);

    }

    private void setupHRChart(View view) {

        LineChart barChart = view.findViewById(R.id.correctnessBarChart);
        LineDataSet barDataSet = new LineDataSet(chartData, "Heart Rate Average");
        LineData barData = new LineData(barDataSet);

        barChart.setClickable(false);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setPinchZoom(false);
        barChart.setScaleEnabled(false);

        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);

        barChart.getXAxis().setDrawAxisLine(true);
        barChart.getAxisLeft().setDrawAxisLine(true);
        barChart.getAxisRight().setDrawAxisLine(false);

        barChart.getXAxis().setDrawLabels(true);
        barChart.getAxisLeft().setDrawLabels(true);
        barChart.getAxisRight().setDrawLabels(false);

        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        barChart.setData(barData);
        barChart.setDrawGridBackground(false);

        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);

        barDataSet.setColor(ContextCompat.getColor(view.getContext(), R.color.yellow));
        barDataSet.setDrawValues(true);
        barDataSet.setLineWidth(2);
        barDataSet.setDrawCircleHole(false);
        barDataSet.setDrawCircles(false);
        barDataSet.setDrawValues(false);

        barChart.notifyDataSetChanged();
        barChart.invalidate();

    }

    private void initHRChart() {

        CurrentExerciseHandler currentExerciseHandler = CurrentExerciseHandler.getInstance();
        int correctnessMeasures = currentExerciseHandler.getExerciseCorrectnessMeasures().size();

        for (int correctnessMeasureIdx = 1; correctnessMeasureIdx < correctnessMeasures; correctnessMeasureIdx++) {
            chartData.add(new BarEntry((float) correctnessMeasureIdx,
                    currentExerciseHandler.getExerciseCorrectnessMeasures().get(correctnessMeasureIdx-1).floatValue()));
        }

    }

}