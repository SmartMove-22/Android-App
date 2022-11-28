package pt.ua.hackaton.smartmove;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.pose.Pose;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import pt.ua.hackaton.smartmove.api.service.ExerciseDataService;
import pt.ua.hackaton.smartmove.data.database.AppDatabase;
import pt.ua.hackaton.smartmove.data.database.dao.ExerciseReportDao;
import pt.ua.hackaton.smartmove.data.database.entities.ExerciseReportEntity;
import pt.ua.hackaton.smartmove.data.responses.ExerciseAnalysisResponse;
import pt.ua.hackaton.smartmove.handlers.CurrentExerciseHandler;
import pt.ua.hackaton.smartmove.handlers.PoseDetectorHandler;
import pt.ua.hackaton.smartmove.utils.ExerciseCategory;
import pt.ua.hackaton.smartmove.viewmodels.CameraStatsViewModel;
import pt.ua.hackaton.smartmove.permissions.CameraPermission;
import pt.ua.hackaton.smartmove.utils.CameraUtils;
import pt.ua.hackaton.smartmove.viewmodels.PoseDetectorViewModel;
import retrofit2.Response;

public class CameraActivity extends AppCompatActivity {

    private boolean recordingExercise = false;
    private PoseDetectorViewModel poseDetectorViewModel;
    private ExerciseReportDao exerciseReportDao;

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
        ExerciseCategory exerciseCategory = ExerciseCategory.valueOf(getIntent().getStringExtra("exercise_category_name"));

        TextView cameraExerciseName = findViewById(R.id.exerciseCardNameTxt);
        cameraExerciseName.setText(exerciseName);

        poseDetectorViewModel = new ViewModelProvider(this).get(PoseDetectorViewModel.class);
        exerciseReportDao = AppDatabase.getInstance(getApplicationContext()).exerciseReportDao();

        setupCamera((int) exerciseId, exerciseCategory);

        poseDetectorViewModel.getExerciseRepetitions().observe(this, value -> {
            TextView exerciseRepetitionsTextView = findViewById(R.id.cameraRepetitionsCounterTextView);
            exerciseRepetitionsTextView.setText(String.valueOf(value));
        });

        findViewById(R.id.cameraSaveExerciseBtn).setOnClickListener(view -> onSaveButtonClick());

    }

    private void setupCamera(int exerciseId, ExerciseCategory exerciseCategory) {

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

        findViewById(R.id.changeRecordingStatusBtn).setOnClickListener(view -> cameraProviderFuture.addListener(() -> {

            if (!recordingExercise) {
                // Code called when the exercise starts
                startExercise(exerciseId, exerciseCategory, finalCameraProvider);
            } else {
                // Code called when the exercise ends
                stopExercise(finalCameraProvider);
            }

            recordingExercise = !recordingExercise;

        }, ContextCompat.getMainExecutor(this)));

    }

    private void startExercise(int exerciseId, ExerciseCategory exerciseCategory, final ProcessCameraProvider cameraProvider) {

        ImageView cameraFrame = findViewById(R.id.cameraPreview);
        Button changeRecordingStatusBtn = findViewById(R.id.changeRecordingStatusBtn);

        CameraUtils cameraUtils = new CameraUtils();
        PoseDetectorHandler poseDetectorHandler = PoseDetectorHandler.getInstance();
        CurrentExerciseHandler currentExerciseHandler = CurrentExerciseHandler.getInstance();

        ExerciseDataService exerciseDataService = ExerciseDataService.getInstance();

        cameraUtils.startCameraX(this, cameraFrame, cameraProvider);
        changeRecordingStatusBtn.setText("Stop");

        poseDetectorHandler.startScheduler();
        poseDetectorHandler.setViewModel(poseDetectorViewModel);
        poseDetectorHandler.setExerciseCategory(exerciseCategory);

        currentExerciseHandler.setExerciseId(exerciseId);
        currentExerciseHandler.setExerciseCategory(exerciseCategory);
        currentExerciseHandler.startExerciseTimer();

        // Update Pose View Model Callback
        exerciseDataService.addCallback(exerciseAnalysisResponse -> {

            if (exerciseAnalysisResponse.isSuccessful()) {

                ExerciseAnalysisResponse exerciseAnalysisResponseBody = exerciseAnalysisResponse.body();

                if (exerciseAnalysisResponseBody == null) {
                    Log.d("SmartMove", "NULL RESPONSE BODY");
                    return;
                }

                poseDetectorViewModel.getCurrentPose().postValue(poseDetectorHandler.getCurrentPose());
                poseDetectorViewModel.getExerciseCorrectness().postValue(exerciseAnalysisResponseBody.getCorrectness());
                poseDetectorViewModel.getExerciseProgress().postValue(exerciseAnalysisResponseBody.getProgress());

                if (exerciseAnalysisResponseBody.isFinishedRepetition()) {
                    int repetitions = Optional.ofNullable(poseDetectorViewModel.getExerciseRepetitions().getValue()).orElse(0);
                    poseDetectorViewModel.getExerciseRepetitions().postValue(repetitions+1);
                }

                poseDetectorHandler.setFirstHalf(exerciseAnalysisResponseBody.isFirstHalf());

            }

        });

        // Add data to Current Exercise singleton. Used to store temporarily the data of the current exercise.
        exerciseDataService.addCallback(exerciseAnalysisResponse -> {

            if (exerciseAnalysisResponse.isSuccessful()) {

                ExerciseAnalysisResponse exerciseAnalysisResponseBody = exerciseAnalysisResponse.body();

                if (exerciseAnalysisResponseBody == null) {
                    Log.d("SmartMove", "NULL RESPONSE BODY");
                    return;
                }

                // Handle Heart Rate here
                currentExerciseHandler.addExerciseHeartRate(0d);
                currentExerciseHandler.addCorrectnessMeasure(exerciseAnalysisResponseBody.getCorrectness());

                if (exerciseAnalysisResponseBody.isFinishedRepetition()) {
                    currentExerciseHandler.addExerciseRepetition();
                }

            }

        });

    }

    private void stopExercise(final ProcessCameraProvider cameraProvider) {

        Button changeRecordingStatusBtn = findViewById(R.id.changeRecordingStatusBtn);

        CameraUtils cameraUtils = new CameraUtils();
        PoseDetectorHandler poseDetectorHandler = PoseDetectorHandler.getInstance();

        cameraUtils.stopCameraX(cameraProvider);
        changeRecordingStatusBtn.setText("Start");

        poseDetectorHandler.stopScheduler();

    }

    private void storeExerciseReport(ExerciseReportEntity exerciseReportEntity) {
        AsyncTask.execute(() -> exerciseReportDao.insertReport(exerciseReportEntity));
    }

    private void onSaveButtonClick() {

        Intent myIntent = new Intent(this, ExerciseReportActivity.class);
        this.startActivity(myIntent);

        finish();

        CurrentExerciseHandler currentExerciseHandler = CurrentExerciseHandler.getInstance();
        ExerciseReportEntity exerciseReport = currentExerciseHandler.toExerciseReportEntity();

        storeExerciseReport(exerciseReport);

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