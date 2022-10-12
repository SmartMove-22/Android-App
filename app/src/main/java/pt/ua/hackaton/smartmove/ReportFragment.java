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
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pt.ua.hackaton.smartmove.data.Exercise;
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

        List<LocalDateTime> next7DaysDates = new ArrayList<>();

        for (int i = 0; i < 7; i++) {

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, i);

            next7DaysDates.add(LocalDateTime.ofInstant(calendar.getTime().toInstant(), ZoneId.systemDefault()));

        }

        setupDaysRecyclerView(view, next7DaysDates);

    }

    private void setupDaysRecyclerView(View view, List<LocalDateTime> data) {

        RecyclerView recyclerView = view.findViewById(R.id.reportDaysOfWeekRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        DaysOfWeekRecyclerViewAdapter adapter = new DaysOfWeekRecyclerViewAdapter(getContext(), data);

        adapter.setClickListener((view1, position) -> {
            Toast.makeText(getContext(), "Clicked On item " + position, Toast.LENGTH_SHORT).show();
        });

        recyclerView.setAdapter(adapter);

    }

}