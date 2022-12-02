package pt.ua.hackaton.smartmove;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.common.util.concurrent.ListenableFuture;

import java.text.DecimalFormat;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import pt.ua.hackaton.smartmove.api.service.ExerciseDataService;
import pt.ua.hackaton.smartmove.data.database.AppDatabase;
import pt.ua.hackaton.smartmove.data.database.dao.ExerciseReportDao;
import pt.ua.hackaton.smartmove.data.database.entities.ExerciseReportEntity;
import pt.ua.hackaton.smartmove.data.responses.ExerciseAnalysisResponse;
import pt.ua.hackaton.smartmove.handlers.CurrentExerciseHandler;
import pt.ua.hackaton.smartmove.handlers.PoseDetectorHandler;
import pt.ua.hackaton.smartmove.permissions.CameraPermission;
import pt.ua.hackaton.smartmove.utils.CameraUtils;
import pt.ua.hackaton.smartmove.utils.ExerciseCategory;
import pt.ua.hackaton.smartmove.utils.PoseLandmarkTypeMapping;
import pt.ua.hackaton.smartmove.viewmodels.PoseDetectorViewModel;

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

        observerPoseDetectorVM();

        findViewById(R.id.cameraSaveExerciseBtn).setOnClickListener(view -> onSaveButtonClick());
        findViewById(R.id.cameraHideShowDotsImageView).setOnClickListener(view -> onLandmarksStatusChangedClick());

    }

    private void observerPoseDetectorVM() {

        DecimalFormat decimalFormat = new DecimalFormat("###.##");

        poseDetectorViewModel.getExerciseRepetitions().observe(this, value -> {
            TextView exerciseRepetitionsTextView = findViewById(R.id.cameraRepetitionsCounterTextView);
            exerciseRepetitionsTextView.setText(value + " reps");
        });

        poseDetectorViewModel.getExerciseCorrectness().observe(this, value -> {
            TextView exerciseCorrectnessTextView = findViewById(R.id.cameraCorrectnessCardText);
            exerciseCorrectnessTextView.setText(decimalFormat.format(value) + "%");
        });

        poseDetectorViewModel.getExerciseProgress().observe(this, value -> {
            TextView exerciseProgressTextView = findViewById(R.id.cameraProgressCardText);
            exerciseProgressTextView.setText(decimalFormat.format(value) + "%");
        });

        poseDetectorViewModel.getExerciseTip().observe(this, tip -> {
            TextView exerciseTips = findViewById(R.id.exerciseTips);
            exerciseTips.setText(tip);
        });

        poseDetectorViewModel.getExerciseReadyToStart().observe(this, isReadyToStart -> {

            TextView cameraExerciseStatus = findViewById(R.id.cameraExerciseStatusPlaceholder);

            if (isReadyToStart) {
                cameraExerciseStatus.setText("Ready to start!");
                cameraExerciseStatus.setTextColor(0xFF4CAF50);
            } else {
                cameraExerciseStatus.setText("Not ready to start!");
                cameraExerciseStatus.setTextColor(0xFFC80F0F);
            }

        });

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
                    Log.e("SmartMove", "Null response body in Update Pose View Model Callback - CameraActivity.class");
                    return;
                }

                PoseLandmarkTypeMapping poseLandmarkTypeMapping = PoseLandmarkTypeMapping.fromPoseLandmarkId(poseDetectorHandler.getWorstLandmark().getLandmarkType());
                double correctnessPercentage = exerciseAnalysisResponseBody.getCorrectness() * 100;
                double progressPercentage = exerciseAnalysisResponseBody.getProgress() * 100;

                poseDetectorHandler.setFirstHalf(exerciseAnalysisResponseBody.isFirstHalf());
                poseDetectorHandler.setWorstLandmark(exerciseAnalysisResponseBody.getWorstMiddleLandmark());

                if (correctnessPercentage > 50 && progressPercentage < 30 && !poseDetectorHandler.isReadyToStart()) {
                    poseDetectorHandler.setReadyToStart(true);
                    poseDetectorViewModel.getExerciseReadyToStart().postValue(poseDetectorHandler.isReadyToStart());
                }

                poseDetectorViewModel.getCurrentPose().postValue(poseDetectorHandler.getCurrentPose());
                poseDetectorViewModel.getExerciseCorrectness().postValue(correctnessPercentage);
                poseDetectorViewModel.getExerciseProgress().postValue(progressPercentage);

                poseDetectorViewModel.getExerciseTip().postValue(String.format("Please, try to adjust your %s!", poseLandmarkTypeMapping != null ? poseLandmarkTypeMapping.getLandmarkText() : "Unknown"));

                if (exerciseAnalysisResponseBody.isFinishedRepetition() && poseDetectorHandler.isReadyToStart()) {
                    int repetitions = Optional.ofNullable(poseDetectorViewModel.getExerciseRepetitions().getValue()).orElse(0);
                    poseDetectorViewModel.getExerciseRepetitions().postValue(repetitions+1);
                }

            }

        });

        // Add data to Current Exercise singleton. Used to store temporarily the data of the current exercise.
        exerciseDataService.addCallback(exerciseAnalysisResponse -> {

            if (exerciseAnalysisResponse.isSuccessful()) {

                ExerciseAnalysisResponse exerciseAnalysisResponseBody = exerciseAnalysisResponse.body();

                if (exerciseAnalysisResponseBody == null) {
                    Log.e("SmartMove", "Null response body in Current Exercise Singleton Callback - CameraActivity.class");
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
        if (exerciseReportEntity == null) return;
        AsyncTask.execute(() -> exerciseReportDao.insertReport(exerciseReportEntity));
    }

    private void onSaveButtonClick() {

        CurrentExerciseHandler currentExerciseHandler = CurrentExerciseHandler.getInstance();
        ExerciseReportEntity exerciseReport = currentExerciseHandler.toExerciseReportEntity();

        // If the exercise was initiated
        if (currentExerciseHandler.getExerciseId() != -1) {
            storeExerciseReport(exerciseReport);
        }

        Intent myIntent = new Intent(this, ExerciseReportActivity.class);
        this.startActivity(myIntent);

        finish();

    }

    private void onLandmarksStatusChangedClick() {

        PoseDetectorHandler poseDetectorHandler = PoseDetectorHandler.getInstance();
        poseDetectorHandler.setShowLandmarks(!poseDetectorHandler.isShowingLandmarks());

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