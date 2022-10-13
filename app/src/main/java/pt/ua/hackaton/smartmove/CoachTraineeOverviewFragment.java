package pt.ua.hackaton.smartmove;

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

import pt.ua.hackaton.smartmove.data.AssignedExercise;
import pt.ua.hackaton.smartmove.data.Trainee;
import pt.ua.hackaton.smartmove.recyclers.TraineesOverviewRecyclerViewAdapter;
import pt.ua.hackaton.smartmove.recyclers.TraineesRecyclerViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CoachTraineeOverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoachTraineeOverviewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CoachTraineeOverviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CoachTraineeOverviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CoachTraineeOverviewFragment newInstance(String param1, String param2) {
        CoachTraineeOverviewFragment fragment = new CoachTraineeOverviewFragment();
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
        return inflater.inflate(R.layout.fragment_coach_trainee_overview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // data to populate the RecyclerView with
        List<AssignedExercise> trainees = new ArrayList<>();
        trainees.add(new AssignedExercise(1, null, "Chest Exercise", null, 5, 5, 200, null, true, .5, .5, .5,200,0));
        trainees.add(new AssignedExercise(1, null, "Abs Exercise", null, 5, 5, 200, null, false, .25, .4, .5,90,0));

        setupTraineeExercisesOverviewRecyclerView(view, trainees);

    }

    private void setupTraineeExercisesOverviewRecyclerView(View view, List<AssignedExercise> data) {

        RecyclerView recyclerView = view.findViewById(R.id.coachTraineeOverviewExercisesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        TraineesOverviewRecyclerViewAdapter adapter = new TraineesOverviewRecyclerViewAdapter(getContext(), data);
        adapter.setClickListener((view1, position) -> {
            if (getActivity() != null) {
                Intent myIntent = new Intent(getActivity(), CameraActivity.class);
                getActivity().startActivity(myIntent);
            }
        });

        recyclerView.setAdapter(adapter);

    }

}