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

import pt.ua.hackaton.smartmove.data.Exercise;
import pt.ua.hackaton.smartmove.data.Trainee;
import pt.ua.hackaton.smartmove.recyclers.TraineesRecyclerViewAdapter;
import pt.ua.hackaton.smartmove.recyclers.WorkoutPlanRecyclerViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CoachTraineesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoachTraineesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CoachTraineesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CoachTraineesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CoachTraineesFragment newInstance(String param1, String param2) {
        CoachTraineesFragment fragment = new CoachTraineesFragment();
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
        return inflater.inflate(R.layout.fragment_coach_trainees, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // data to populate the RecyclerView with
        List<Trainee> trainees = new ArrayList<>();
        trainees.add(new Trainee("Hugo", "hugogoncalves13@ua.pt", "", null, null,null,null));
        trainees.add(new Trainee("Daniela", "daniela@ua.pt", "", null, null,null,null));
        trainees.add(new Trainee("Martinho", "martinho@ua.pt", "", null, null,null,null));

        setupTraineePlanRecyclerView(view, trainees);

    }

    private void setupTraineePlanRecyclerView(View view, List<Trainee> data) {

        RecyclerView recyclerView = view.findViewById(R.id.coachTraineesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        TraineesRecyclerViewAdapter adapter = new TraineesRecyclerViewAdapter(getContext(), data);
        adapter.setClickListener((view1, position) -> {
            if (getActivity() != null) {
                Intent myIntent = new Intent(getActivity(), CameraActivity.class);
                getActivity().startActivity(myIntent);
            }
        });

        recyclerView.setAdapter(adapter);

    }

}