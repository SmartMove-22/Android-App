package pt.ua.hackaton.smartmove;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import pt.ua.hackaton.smartmove.fragments.CoachTraineesFragment;
import pt.ua.hackaton.smartmove.fragments.CoachingFragment;
import pt.ua.hackaton.smartmove.fragments.ReportFragment;
import pt.ua.hackaton.smartmove.fragments.UserProfileFragment;
import pt.ua.hackaton.smartmove.fragments.WorkoutsFragment;
import pt.ua.hackaton.smartmove.utils.SharedPreferencesHandler;
import pt.ua.hackaton.smartmove.utils.UserType;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hide the top bar
        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().hide();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            SharedPreferencesHandler sharedPreferencesHandler = new SharedPreferencesHandler(getApplicationContext());
            UserType userType = UserType.valueOf(sharedPreferencesHandler.getPreferenceString(getString(R.string.user_type_preference), UserType.TRAINEE.name()));

            if (userType == UserType.TRAINEE) {

                if (item.getItemId() == R.id.workouts_menu_item) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView, WorkoutsFragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("workout_fragment")
                            .commit();
                    return true;
                } else if (item.getItemId() == R.id.coaching_menu_item) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView, CoachingFragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("coaching_fragment")
                            .commit();
                    return true;
                } else if (item.getItemId() == R.id.statistics_menu_item) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView, ReportFragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("report_fragment")
                            .commit();
                    return true;
                } else if (item.getItemId() == R.id.profile_menu_item) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView, UserProfileFragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("user_profile_fragment")
                            .commit();
                    return true;
                }

            } else {

                if (item.getItemId() == R.id.workouts_menu_item) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView, WorkoutsFragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("workout_fragment")
                            .commit();
                    return true;
                } else if (item.getItemId() == R.id.coaching_menu_item) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView, CoachTraineesFragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("coaching_fragment")
                            .commit();
                    return true;
                }

            }

            return false;

        });

        // Set Initial Fragment
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, WorkoutsFragment.class, null)
                .setReorderingAllowed(true)
                .addToBackStack("report_fragment")
                .commit();

    }


}