package pt.ua.hackaton.smartmove.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pt.ua.hackaton.smartmove.R;
import pt.ua.hackaton.smartmove.data.AssignedExercise;
import pt.ua.hackaton.smartmove.data.Category;
import pt.ua.hackaton.smartmove.models.ReportsViewModel;
import pt.ua.hackaton.smartmove.recyclers.DaysOfWeekRecyclerViewAdapter;
import pt.ua.hackaton.smartmove.recyclers.WorkoutPlanRecyclerViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ReportFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ReportsViewModel reportsViewModel;
    private WorkoutPlanRecyclerViewAdapter workoutPlanRecyclerViewAdapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportFragment newInstance(String param1, String param2) {
        ReportFragment fragment = new ReportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ReportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // reportsViewModel = new ViewModelProvider(this).get(ReportsViewModel.class);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        /*
        reportsViewModel.getTimestamp().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                reportsViewModel.fetchReport();
            }
        });

        reportsViewModel.getReport().observe(this, new Observer<Report>() {
            @Override
            public void onChanged(Report report) {
                if (workoutPlanRecyclerViewAdapter != null)
                    workoutPlanRecyclerViewAdapter.setData(report.getExercises());
            }
        });
         */

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

        List<LocalDateTime> range7Days = new ArrayList<>();

        List<AssignedExercise> exercisesNames = new ArrayList<>();
        exercisesNames.add(new AssignedExercise(1, null, "Chest Muscles", new Category(1, "squat", ""), 1,1,300, null, false, 0, 0, 0, 0, 0));
        exercisesNames.add(new AssignedExercise(1, null, "Abdominal Muscles", new Category(1, "squat", ""), 1,1,300, null, false, 0, 0, 0, 0, 0));
        exercisesNames.add(new AssignedExercise(1, null, "Push Ups", new Category(1, "squat", ""), 1,1,300, null, false, 0, 0, 0, 0, 0));

        for (int i = -7; i < 7; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, i);

            range7Days.add(LocalDateTime.ofInstant(calendar.getTime().toInstant(), ZoneId.systemDefault()));
        }

        setupDaysRecyclerView(view, range7Days);
        setupWorkoutPlanRecyclerView(view, exercisesNames);

    }

    private void setupDaysRecyclerView(View view, List<LocalDateTime> data) {

        RecyclerView recyclerView = view.findViewById(R.id.reportDaysOfWeekRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        DaysOfWeekRecyclerViewAdapter adapter = new DaysOfWeekRecyclerViewAdapter(getContext(), data);

        /*
        adapter.setClickListener((view1, position) -> {
            //Toast.makeText(getContext(), "Clicked On item " + position, Toast.LENGTH_SHORT).show();
            reportsViewModel.setTimestamp(data.get(position).toString());
        });

         */

        recyclerView.setAdapter(adapter);
        // reportsViewModel.setTimestamp(data.get(7).toString());
    }

    private void setupWorkoutPlanRecyclerView(View view, List<AssignedExercise> data) {

        RecyclerView recyclerView = view.findViewById(R.id.reportExercisesProgramRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        workoutPlanRecyclerViewAdapter = new WorkoutPlanRecyclerViewAdapter(getContext(), data);

        workoutPlanRecyclerViewAdapter.setClickListener((view1, position) -> {
            Toast.makeText(getContext(), "Clicked On item " + position, Toast.LENGTH_SHORT).show();
        });

        recyclerView.setAdapter(workoutPlanRecyclerViewAdapter);
    }

}