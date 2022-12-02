package pt.ua.hackaton.smartmove.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import pt.ua.hackaton.smartmove.CameraActivity;
import pt.ua.hackaton.smartmove.R;
import pt.ua.hackaton.smartmove.data.AssignedExercise;
import pt.ua.hackaton.smartmove.data.database.entities.ExerciseReportEntity;
import pt.ua.hackaton.smartmove.recyclers.ExerciseReportsViewAdapter;
import pt.ua.hackaton.smartmove.recyclers.TraineesOverviewRecyclerViewAdapter;
import pt.ua.hackaton.smartmove.recyclers.utils.DayOfWeekRecyclerItem;
import pt.ua.hackaton.smartmove.viewmodels.ReportsViewModel;
import pt.ua.hackaton.smartmove.recyclers.DaysOfWeekRecyclerViewAdapter;
import pt.ua.hackaton.smartmove.recyclers.WorkoutPlanRecyclerViewAdapter;

public class ReportFragment extends Fragment {

    private ReportsViewModel reportsViewModel;
    private WorkoutPlanRecyclerViewAdapter workoutPlanRecyclerViewAdapter;
    private ExerciseReportsViewAdapter exerciseReportsViewAdapter;

    public ReportFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        reportsViewModel = new ViewModelProvider(this).get(ReportsViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        setupDaysRecyclerView(view, getLastDays());

        // setupTraineeExercisesOverviewRecyclerView(view, exercisesNames);
        updateViewWithAggregations(view, 0);

        reportsViewModel.getDailyExerciseReports(1).observe(getViewLifecycleOwner(), exerciseReportEntities -> {
            setupTraineeExercisesOverviewRecyclerView(view, exerciseReportEntities);
        });


        // setupTraineeExercisesOverviewRecyclerView(view, 0);

        TextView exerciseReportMainCardSubtitle = view.findViewById(R.id.exerciseReportMainCardSubtitle);
        exerciseReportMainCardSubtitle.setText("Check your progress on " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM")));

    }

    private void setupTraineeExercisesOverviewRecyclerView(View view, List<ExerciseReportEntity> data) {

        RecyclerView recyclerView = view.findViewById(R.id.coachTraineeOverviewExercisesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        ExerciseReportsViewAdapter adapter = new ExerciseReportsViewAdapter(getContext(), data);
        adapter.setClickListener((view1, position) -> {
            if (getActivity() != null) {
                Intent myIntent = new Intent(getActivity(), CameraActivity.class);
                getActivity().startActivity(myIntent);
            }
        });

        recyclerView.setAdapter(adapter);

    }

    private void setupDaysRecyclerView(View view, List<LocalDateTime> dateData) {

        List<DayOfWeekRecyclerItem> data = dateData.stream()
                .map(localDateTime -> new DayOfWeekRecyclerItem(
                        localDateTime, false,
                        ResourcesCompat.getColor(getResources(), R.color.yellow_secondary, null),
                        ResourcesCompat.getColor(getResources(), R.color.white, null)))
                .collect(Collectors.toList());

        RecyclerView recyclerView = view.findViewById(R.id.reportDaysOfWeekRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        DaysOfWeekRecyclerViewAdapter adapter = new DaysOfWeekRecyclerViewAdapter(getContext(), data);

        TextView exerciseReportMainCardSubtitle = view.findViewById(R.id.exerciseReportMainCardSubtitle);

        adapter.setClickListener((view1, position) -> {

            DayOfWeekRecyclerItem dayOfWeekRecyclerCurrentItem = data.get(position);

            exerciseReportMainCardSubtitle.setText("Check your progress on " + dayOfWeekRecyclerCurrentItem.getLocalDateTime().format(DateTimeFormatter.ofPattern("dd MMM")));

            for (int dataItemIdx = 0; dataItemIdx < data.size(); dataItemIdx++) {
                data.get(dataItemIdx).setActive(false);
                adapter.notifyItemChanged(dataItemIdx);
            }

            dayOfWeekRecyclerCurrentItem.setActive(true);
            adapter.notifyItemChanged(position);

            reportsViewModel.getDailyExerciseReports(6-position).observe(getViewLifecycleOwner(), exerciseReportEntities -> {
                setupTraineeExercisesOverviewRecyclerView(view, exerciseReportEntities);
            });

            updateViewWithAggregations(view, 6-position);

        });

        recyclerView.setAdapter(adapter);

    }

    private void updateViewWithAggregations(View view, int transformedPosition) {

        DecimalFormat decimalFormat = new DecimalFormat("##.##");

        reportsViewModel.getDailyExerciseTimeSum(transformedPosition).observe(getViewLifecycleOwner(), exerciseTimeSum -> {

            TextView dailyReportExerciseTime = view.findViewById(R.id.dailyReportExerciseTimePlaceholder);

            if (exerciseTimeSum == null) {
                dailyReportExerciseTime.setText("N/A");
            } else {
                dailyReportExerciseTime.setText(decimalFormat.format(exerciseTimeSum));
            }

        });

        reportsViewModel.getDailyCaloriesSum(transformedPosition).observe(getViewLifecycleOwner(), caloriesSum -> {

            TextView dailyReportCalories = view.findViewById(R.id.dailyReportCaloriesPlaceholder);

            if (caloriesSum == null) {
                dailyReportCalories.setText("N/A");
            } else {
                dailyReportCalories.setText(decimalFormat.format(caloriesSum));
            }

        });

        reportsViewModel.getDailyCorrectnessAvg(transformedPosition).observe(getViewLifecycleOwner(), correctnessAvg -> {

            TextView dailyCorrectnessAvg = view.findViewById(R.id.dailyReportCorrectnessPlaceholder);
            TextView dailyReportImprovement = view.findViewById(R.id.dailyReportImprovementPlaceholder);

            if (correctnessAvg == null) {
                dailyCorrectnessAvg.setText("N/A");
                dailyReportImprovement.setText("N/A");
            } else {

                reportsViewModel.getDailyCorrectnessAvg(transformedPosition-1).observe(getViewLifecycleOwner(), correctnessAvgBefore ->
                    dailyReportImprovement.setText(decimalFormat.format((correctnessAvg-correctnessAvgBefore)*100))
                );

                dailyCorrectnessAvg.setText(decimalFormat.format(correctnessAvg*100));

            }

        });

    }

    private List<LocalDateTime> getLastDays() {

        List<LocalDateTime> daysRange = new ArrayList<>();

        for (int i = 6; i > 0; i--) {

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -i);
            daysRange.add(LocalDateTime.ofInstant(calendar.getTime().toInstant(), ZoneId.systemDefault()));

        }

        return daysRange;

    }

}