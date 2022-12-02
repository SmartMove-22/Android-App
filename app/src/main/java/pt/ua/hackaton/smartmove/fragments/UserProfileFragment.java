package pt.ua.hackaton.smartmove.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.text.DecimalFormat;

import pt.ua.hackaton.smartmove.R;
import pt.ua.hackaton.smartmove.handlers.PotentialHandler;
import pt.ua.hackaton.smartmove.utils.SharedPreferencesHandler;
import pt.ua.hackaton.smartmove.viewmodels.UserViewModel;

public class UserProfileFragment extends Fragment {

    private UserViewModel userViewModel;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        SharedPreferencesHandler sharedPreferencesHandler = new SharedPreferencesHandler(view.getContext());
        String username = sharedPreferencesHandler.getPreferenceString(getString(R.string.username_preference), "Hugo1307");

        updatePotentialCalculation(username);
        updateDataWithVM(view, username);

        setGoalButtonClickListener(view, sharedPreferencesHandler);

    }

    private void setGoalButtonClickListener(View view, SharedPreferencesHandler sharedPreferencesHandler) {

        view.findViewById(R.id.userFragmentCaloriesGoalAcceptBtn).setOnClickListener(view1 -> {

            EditText userFragmentCaloriesGoalEditText = view.findViewById(R.id.userFragmentCaloriesGoalEditText);
            Integer caloriesGoal = Integer.parseInt(userFragmentCaloriesGoalEditText.getText().toString());

            sharedPreferencesHandler.setPreferenceInt(getString(R.string.calories_goal_preference), caloriesGoal);

            Toast.makeText(getContext(), "Calories Goal updated!", Toast.LENGTH_SHORT).show();

        });


    }

    private void updateDataWithVM(View view, String username) {

        userViewModel.getUserPotential(username).observe(getViewLifecycleOwner(), userPotential -> {

            DecimalFormat decimalFormat = new DecimalFormat("##.##");

            if (userPotential == null) userPotential = 0d;

            TextView userFragmentPotential = view.findViewById(R.id.userFragmentPotentialPlaceholder);
            userFragmentPotential.setText(decimalFormat.format(userPotential));

        });

        userViewModel.getUserIMC(username).observe(getViewLifecycleOwner(), userIMC -> {

            DecimalFormat decimalFormat = new DecimalFormat("##.##");

            if (userIMC == null) userIMC = 0d;

            TextView userFragmentBMI = view.findViewById(R.id.userFragmentBMIPlaceholder);
            userFragmentBMI.setText(decimalFormat.format(userIMC));

        });

        userViewModel.getUserWeight(username).observe(getViewLifecycleOwner(), userWeight -> {

            DecimalFormat decimalFormat = new DecimalFormat("##.##");

            if (userWeight == null) userWeight = 0d;

            TextView userFragmentWeight = view.findViewById(R.id.userFragmentWeightPlaceholder);
            userFragmentWeight.setText(decimalFormat.format(userWeight));

        });

        userViewModel.getUserHeight(username).observe(getViewLifecycleOwner(), userHeight -> {

            if (userHeight == null) userHeight = 0;

            TextView userFragmentHeight = view.findViewById(R.id.userFragmentHeightPlaceholder);
            userFragmentHeight.setText(String.valueOf(userHeight));

        });

        userViewModel.getUserFirstName(username).observe(getViewLifecycleOwner(), firstName -> {
            userViewModel.getUserLastName(username).observe(getViewLifecycleOwner(), lastName -> {

                TextView userFragmentName = view.findViewById(R.id.userFragmentNamePlaceholder);
                userFragmentName.setText(firstName + " " + lastName);

            });
        });

        userViewModel.getUserEmail(username).observe(getViewLifecycleOwner(), userEmail -> {

            TextView userFragmentEmail = view.findViewById(R.id.userFragmentEmailPlaceholder);
            userFragmentEmail.setText(userEmail);

        });

    }

    private void updatePotentialCalculation(String username) {

        userViewModel.getTotalCaloriesBurned().observe(getViewLifecycleOwner(), totalCaloriesBurned -> {

            userViewModel.getUserIMC(username).observe(getViewLifecycleOwner(), userImc -> {

                userViewModel.getTotalCorrectnessAverage().observe(getViewLifecycleOwner(), totalCorrectnessAvg -> {

                    userViewModel.getTotalExerciseTime().observe(getViewLifecycleOwner(), totalExerciseTime -> {

                        AsyncTask.execute(() -> userViewModel.setUserPotential(username,
                                PotentialHandler.getInstance().calculatePotential(
                                        userImc,
                                        totalExerciseTime,
                                        totalCorrectnessAvg,
                                        totalCaloriesBurned
                                ))
                        );

                    });

                });

            });

        });

    }

}