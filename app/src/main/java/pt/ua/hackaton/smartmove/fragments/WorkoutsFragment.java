package pt.ua.hackaton.smartmove.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import pt.ua.hackaton.smartmove.CameraActivity;
import pt.ua.hackaton.smartmove.R;
import pt.ua.hackaton.smartmove.data.AssignedExercise;
import pt.ua.hackaton.smartmove.data.Category;
import pt.ua.hackaton.smartmove.data.Exercise;
import pt.ua.hackaton.smartmove.models.WorkoutsViewModel;
import pt.ua.hackaton.smartmove.recyclers.SuggestedExercisesRecyclerViewAdapter;
import pt.ua.hackaton.smartmove.recyclers.WorkoutPlanRecyclerViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkoutsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkoutsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private WorkoutPlanRecyclerViewAdapter workoutPlanRecyclerAdapter;
    private SuggestedExercisesRecyclerViewAdapter suggestedExercisesRecyclerAdapter;

    private WorkoutsViewModel workoutsViewModel;


    public WorkoutsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WorkoutsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WorkoutsFragment newInstance(String param1, String param2) {
        WorkoutsFragment fragment = new WorkoutsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workoutsViewModel = new ViewModelProvider(this).get(WorkoutsViewModel.class);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_workouts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        
        super.onViewCreated(view, savedInstanceState);

        workoutsViewModel.setSharedPreferences(
                getContext().getSharedPreferences(
                        getString(R.string.preference_file_key),
                        Context.MODE_PRIVATE));

        // data to populate the RecyclerView with
        List<AssignedExercise> exercisesNames = new ArrayList<>();
        exercisesNames.add(new AssignedExercise(1, null, "Legs Muscles", new Category(1, "squat", ""), 1,1,300, null, false, 0, 0, 0, 0, 0));
        exercisesNames.add(new AssignedExercise(1, null, "Abdominal Muscles", new Category(1, "squat", ""), 1,1,300, null, false, 0, 0, 0, 0, 0));
        exercisesNames.add(new AssignedExercise(1, null, "Push Ups", new Category(1, "squat", ""), 1,1,300, null, false, 0, 0, 0, 0, 0));

        List<Exercise> exercises = new ArrayList<>();
        exercises.add(new Exercise(1, null, "Legs Muscles", new Category(1, "squat", ""), 1,1,300));
        exercises.add(new Exercise(1, null, "Abdominal Muscles", new Category(1, "squat", ""), 1,1,300));
        exercises.add(new Exercise(1, null, "Push Ups", new Category(1, "squat", ""), 1,1,300));

        // setupWorkoutPlanRecyclerView(view, exercisesNames);
        setupSuggestedExerciseRecyclerView(view, exercises);

    }

    /*private void setupWorkoutPlanRecyclerView(View view, List<? extends Exercise> data) {


        RecyclerView recyclerView = view.findViewById(R.id.workoutPlanRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        workoutPlanRecyclerAdapter = new WorkoutPlanRecyclerViewAdapter(getContext(), data);
        workoutPlanRecyclerAdapter.setClickListener((view1, position) -> {

            if (getActivity() != null) {

                Intent myIntent = new Intent(getActivity(), CameraActivity.class);

                myIntent.putExtra("exercise_id", data.get(position).getId());
                myIntent.putExtra("exercise_name", data.get(position).getName());
                myIntent.putExtra("exercise_category_name", data.get(position).getCategory().getCategory());

                getActivity().startActivity(myIntent);

            }

        });

        recyclerView.setAdapter(workoutPlanRecyclerAdapter);

    }*/

    /*
    private void setupWorkoutPlanRecyclerView(View view) {

        RecyclerView recyclerView = view.findViewById(R.id.workoutPlanRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        workoutPlanRecyclerAdapter = new WorkoutPlanRecyclerViewAdapter(getContext());
        workoutPlanRecyclerAdapter.setClickListener(new WorkoutPlanRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (getActivity() != null) {

                    Intent myIntent = new Intent(getActivity(), CameraActivity.class);

                    myIntent.putExtra("exercise_id", data.get(position).getId());
                    myIntent.putExtra("exercise_name", data.get(position).getName());
                    myIntent.putExtra("exercise_category_name", data.get(position).getCategory().getCategory());

                    getActivity().startActivity(myIntent);

                }

            }
        });
        recyclerView.setAdapter(workoutPlanRecyclerAdapter);
    }
    */

    private void setupSuggestedExerciseRecyclerView(View view, List<? extends Exercise> data) {
        RecyclerView recyclerView = view.findViewById(R.id.suggestedExercisesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        suggestedExercisesRecyclerAdapter = new SuggestedExercisesRecyclerViewAdapter(getContext(), data);
        suggestedExercisesRecyclerAdapter.setClickListener(new SuggestedExercisesRecyclerViewAdapter.ItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {

                if (getActivity() != null) {

                    Intent myIntent = new Intent(getActivity(), CameraActivity.class);

                    myIntent.putExtra("exercise_id", data.get(position).getId());
                    myIntent.putExtra("exercise_name", data.get(position).getName());
                    myIntent.putExtra("exercise_category_name", data.get(position).getCategory().getCategory());

                    getActivity().startActivity(myIntent);

                }

            }

        });
        recyclerView.setAdapter(suggestedExercisesRecyclerAdapter);
    }

}