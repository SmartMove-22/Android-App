package pt.ua.hackaton.smartmove.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import pt.ua.hackaton.smartmove.data.Exercise;
import pt.ua.hackaton.smartmove.data.mocks.ExercisesMocks;
import pt.ua.hackaton.smartmove.recyclers.WorkoutPlanRecyclerViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CoachingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoachingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CoachingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CoachingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CoachingFragment newInstance(String param1, String param2) {
        CoachingFragment fragment = new CoachingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_coaching, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        setupWorkoutPlanRecyclerView(view, ExercisesMocks.getExercisesList());

    }

    private void setupWorkoutPlanRecyclerView(View view, List<Exercise> data) {

        RecyclerView recyclerView = view.findViewById(R.id.coachingFragWorkoutPlanRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        WorkoutPlanRecyclerViewAdapter adapter = new WorkoutPlanRecyclerViewAdapter(getContext(), data);
        adapter.setClickListener(new WorkoutPlanRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (getActivity() != null) {
                    Intent myIntent = new Intent(getActivity(), CameraActivity.class);

                    myIntent.putExtra("exercise_id", data.get(position).getId());
                    myIntent.putExtra("exercise_name", data.get(position).getName());
                    myIntent.putExtra("exercise_category_name", data.get(position).getCategory().getCategory().name());

                    getActivity().startActivity(myIntent);
                }
            }
        });
        recyclerView.setAdapter(adapter);

    }

}