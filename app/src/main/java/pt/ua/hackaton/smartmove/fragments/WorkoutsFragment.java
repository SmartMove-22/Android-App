package pt.ua.hackaton.smartmove.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

import pt.ua.hackaton.smartmove.CameraActivity;
import pt.ua.hackaton.smartmove.R;
import pt.ua.hackaton.smartmove.data.Exercise;
import pt.ua.hackaton.smartmove.data.mocks.ExercisesMocks;
import pt.ua.hackaton.smartmove.utils.SharedPreferencesHandler;
import pt.ua.hackaton.smartmove.viewmodels.WorkoutsViewModel;
import pt.ua.hackaton.smartmove.recyclers.SuggestedExercisesRecyclerViewAdapter;
import pt.ua.hackaton.smartmove.recyclers.WorkoutPlanRecyclerViewAdapter;

public class WorkoutsFragment extends Fragment {

    private WorkoutPlanRecyclerViewAdapter workoutPlanRecyclerAdapter;

    private WorkoutsViewModel workoutsViewModel;

    public WorkoutsFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workoutsViewModel = new ViewModelProvider(this).get(WorkoutsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_workouts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        
        super.onViewCreated(view, savedInstanceState);

        setupSuggestedExerciseRecyclerView(view, ExercisesMocks.getExercisesList());
        setDashboardInformation(view);
    }

    private void setDashboardInformation(View view) {

        DecimalFormat decimalFormat = new DecimalFormat("##.##");
        SharedPreferencesHandler sharedPreferencesHandler = new SharedPreferencesHandler(view.getContext());

        TextView exerciseTimePlaceholder = view.findViewById(R.id.userFragmentBMIPlaceholder);
        TextView caloriesBurnPlaceholder = view.findViewById(R.id.userFragmentWeightPlaceholder);
        TextView mainDashboardCaloriesLeft = view.findViewById(R.id.mainDashboardCaloriesLeftPlaceholder);
        ProgressBar caloriesGoalProgressBar = view.findViewById(R.id.progressBarCaloriesLeft);

        int caloriesGoal = sharedPreferencesHandler.getPreferenceInteger(getString(R.string.calories_goal_preference), 200);

        workoutsViewModel.getTodayExerciseSeconds().observe(getViewLifecycleOwner(),
                exerciseTime -> exerciseTimePlaceholder.setText(decimalFormat.format(exerciseTime != null ? exerciseTime : 0))
        );

        workoutsViewModel.getTodayExerciseCaloriesBurn().observe(getViewLifecycleOwner(), caloriesBurn -> {

            int caloriesLeft = 0;
            if (caloriesBurn != null) {
                caloriesLeft = caloriesGoal > caloriesBurn ? (int) Math.round(caloriesGoal-caloriesBurn) : 0;
            }

            mainDashboardCaloriesLeft.setText(String.valueOf(caloriesLeft));
            caloriesGoalProgressBar.setMax(caloriesGoal);
            caloriesGoalProgressBar.setProgress(caloriesBurn != null ? caloriesBurn.intValue() : 0);
            caloriesBurnPlaceholder.setText(decimalFormat.format(caloriesBurn != null ? caloriesBurn : 0));

        });

    }

    private void setupSuggestedExerciseRecyclerView(View view, List<? extends Exercise> data) {

        RecyclerView recyclerView = view.findViewById(R.id.suggestedExercisesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        SuggestedExercisesRecyclerViewAdapter suggestedExercisesRecyclerAdapter = new SuggestedExercisesRecyclerViewAdapter(getContext(), data);
        suggestedExercisesRecyclerAdapter.setClickListener((view1, position) -> {

            if (getActivity() != null) {

                Intent myIntent = new Intent(getActivity(), CameraActivity.class);

                myIntent.putExtra("exercise_id", data.get(position).getId());
                myIntent.putExtra("exercise_name", data.get(position).getName());
                myIntent.putExtra("exercise_category_name", data.get(position).getCategory().getCategory().name());

                getActivity().startActivity(myIntent);

            }

        });

        recyclerView.setAdapter(suggestedExercisesRecyclerAdapter);

    }

}