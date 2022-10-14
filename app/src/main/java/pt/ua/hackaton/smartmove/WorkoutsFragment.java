package pt.ua.hackaton.smartmove;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

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

        workoutsViewModel.getWorkouts().observe(getActivity(), new Observer<List<Exercise>>() {
            @Override
            public void onChanged(List<Exercise> exercises) {
                if (workoutPlanRecyclerAdapter != null)
                    workoutPlanRecyclerAdapter.setData(exercises);
                if (suggestedExercisesRecyclerAdapter != null)
                    suggestedExercisesRecyclerAdapter.setData(exercises);
            }
        });

        /*
         data to populate the RecyclerView with
        List<Exercise> exercisesNames = new ArrayList<>();
        exercisesNames.add(new Exercise(1, null, "Chest Muscles", null, 1,1,300));
        exercisesNames.add(new Exercise(1, null, "Abdominal Muscles", null, 1,1,300));
        exercisesNames.add(new Exercise(1, null, "Push Ups", null, 1,1,300));
        */

        setupWorkoutPlanRecyclerView(view);
        setupSuggestedExerciseRecyclerView(view);

    }

    private void setupWorkoutPlanRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.workoutPlanRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        workoutPlanRecyclerAdapter = new WorkoutPlanRecyclerViewAdapter(getContext());
        workoutPlanRecyclerAdapter.setClickListener(new WorkoutPlanRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (getActivity() != null) {
                    Intent myIntent = new Intent(getActivity(), CameraActivity.class);
                    getActivity().startActivity(myIntent);
                }
            }
        });
        recyclerView.setAdapter(workoutPlanRecyclerAdapter);
    }

    private void setupSuggestedExerciseRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.suggestedExercisesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        suggestedExercisesRecyclerAdapter = new SuggestedExercisesRecyclerViewAdapter(getContext());
        suggestedExercisesRecyclerAdapter.setClickListener(new SuggestedExercisesRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (getActivity() != null) {
                    Intent myIntent = new Intent(getActivity(), CameraActivity.class);
                    getActivity().startActivity(myIntent);
                }
            }
        });
        recyclerView.setAdapter(suggestedExercisesRecyclerAdapter);
    }

}