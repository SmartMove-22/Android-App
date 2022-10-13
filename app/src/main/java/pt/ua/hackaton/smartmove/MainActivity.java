package pt.ua.hackaton.smartmove;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hide the top bar
        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().hide();
        }

        /*
        findViewById(R.id.startCameraBtn).setOnClickListener(view -> {
            Intent myIntent = new Intent(MainActivity.this, CameraActivity.class);
            MainActivity.this.startActivity(myIntent);
        });
        */

        FragmentManager fragmentManager = getSupportFragmentManager();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.workouts_menu_item) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, WorkoutsFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("workout_fragment")
                        .commit();
                return true;
            } else if (item.getItemId() == R.id.statistics_menu_item) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, ReportFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("report_fragment")
                        .commit();
                return true;
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