package pt.ua.hackaton.smartmove;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

import pt.ua.hackaton.smartmove.handlers.PoseDetectorHandler;
import pt.ua.hackaton.smartmove.viewmodels.CameraStatsViewModel;
import pt.ua.hackaton.smartmove.permissions.CameraPermission;
import pt.ua.hackaton.smartmove.utils.CameraUtils;
import pt.ua.hackaton.smartmove.viewmodels.PoseDetectorViewModel;

public class CameraActivity extends AppCompatActivity {

    private boolean recordingExercise = false;
    AtomicReference<List<Double>> values = new AtomicReference<>();
    private PoseDetectorViewModel poseDetectorViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // Hide the top bar
        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().hide();
        }

        CameraPermission cameraPermission = new CameraPermission(this);

        if (!cameraPermission.isGranted()) {
            cameraPermission.askPermission();
        }

        long exerciseId = getIntent().getLongExtra("exercise_id", -1);
        String exerciseName = getIntent().getStringExtra("exercise_name");
        String exerciseCategory = getIntent().getStringExtra("exercise_category_name");

        TextView cameraExerciseName = findViewById(R.id.exerciseCardNameTxt);
        cameraExerciseName.setText(exerciseName);

        poseDetectorViewModel = new ViewModelProvider(this).get(PoseDetectorViewModel.class);

        setupCamera(exerciseCategory);

        poseDetectorViewModel.getExerciseRepetitions().observe(this, value -> {
            TextView exerciseRepetitionsTextView = findViewById(R.id.cameraRepetitionsCounterTextView);
            exerciseRepetitionsTextView.setText(String.valueOf(value));
        });

        findViewById(R.id.cameraSaveExerciseBtn).setOnClickListener(view -> {

            CameraStatsViewModel cameraStatsViewModel = new ViewModelProvider(this).get(CameraStatsViewModel.class);

            Intent myIntent = new Intent(this, ExerciseReportActivity.class);

            myIntent.putExtra("calories", cameraStatsViewModel.getTotalCalories().getValue());
            myIntent.putExtra("correctness", cameraStatsViewModel.getTotalCorrectness().getValue());

            this.startActivity(myIntent);

            finish();

        });

    }

    private void setupCamera(String exerciseCategory) {

        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        ProcessCameraProvider cameraProvider = null;

        try {
            cameraProvider = cameraProviderFuture.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        if (cameraProvider == null) {
            Toast.makeText(this, "Camera not found.", Toast.LENGTH_SHORT).show();
            return;
        }

        ProcessCameraProvider finalCameraProvider = cameraProvider;
        ImageView cameraFrame = findViewById(R.id.cameraPreview);
        Button changeRecordingStatusBtn = findViewById(R.id.changeRecordingStatusBtn);
        CameraUtils cameraUtils = new CameraUtils();
        PoseDetectorHandler poseDetectorHandler = PoseDetectorHandler.getInstance();

        findViewById(R.id.changeRecordingStatusBtn).setOnClickListener(view -> cameraProviderFuture.addListener(() -> {

            if (!recordingExercise) {

                cameraUtils.startCameraX(this, cameraFrame, finalCameraProvider);
                changeRecordingStatusBtn.setText("Stop");

                poseDetectorHandler.startScheduler();
                poseDetectorHandler.setViewModel(poseDetectorViewModel);
                poseDetectorHandler.setExerciseCategory(exerciseCategory);

            } else {

                cameraUtils.stopCameraX(finalCameraProvider);
                changeRecordingStatusBtn.setText("Start");

                poseDetectorHandler.stopScheduler();

            }

            recordingExercise = !recordingExercise;

        }, ContextCompat.getMainExecutor(this)));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CameraPermission.CAMERA_PERMISSION_REQUEST_CODE) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Permission Denied for Camera", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }

}