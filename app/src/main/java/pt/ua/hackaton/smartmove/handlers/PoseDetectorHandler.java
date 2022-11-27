package pt.ua.hackaton.smartmove.handlers;

import android.util.Log;

import com.google.mlkit.vision.pose.Pose;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import pt.ua.hackaton.smartmove.data.LandmarkPoint;
import pt.ua.hackaton.smartmove.data.database.entities.ExerciseReportEntity;
import pt.ua.hackaton.smartmove.data.requests.ExerciseDataRequest;
import pt.ua.hackaton.smartmove.data.responses.ExerciseAnalysisResponse;
import pt.ua.hackaton.smartmove.utils.ExerciseDataAPI;
import pt.ua.hackaton.smartmove.viewmodels.PoseDetectorViewModel;
import retrofit2.Response;

public class PoseDetectorHandler {

    private static PoseDetectorHandler instance;

    private Pose currentPose;
    private String exerciseCategory;
    private ScheduledExecutorService scheduler;
    private Long initialTime;
    private boolean secondHalf;
    private PoseDetectorViewModel viewModel;

    private PoseDetectorHandler() {
        this.currentPose = null;
        this.exerciseCategory = null;
        this.scheduler = null;
        this.initialTime = null;
        this.secondHalf = false;
    }

    public synchronized Pose getCurrentPose() {
        return currentPose;
    }

    public synchronized void setCurrentPose(Pose currentPose) {
        this.currentPose = currentPose;
    }

    public void setExerciseCategory(String exerciseCategory) {
        this.exerciseCategory = exerciseCategory;
    }

    public synchronized boolean isDetectingPoses() {
        return scheduler != null;
    }

    public PoseDetectorViewModel getViewModel() {
        return viewModel;
    }

    public void setViewModel(PoseDetectorViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public synchronized void startScheduler() {
        if (this.scheduler == null) {
            this.scheduler = Executors.newScheduledThreadPool(1);
            this.scheduler.scheduleAtFixedRate(this::handlePoseData, 1, 1, TimeUnit.SECONDS);
            this.initialTime = System.currentTimeMillis();
        }
    }

    public synchronized void stopScheduler() {
        if (this.scheduler != null) {
            this.scheduler.shutdown();
            clearHandler();
        }
    }

    public void handlePoseData() {

        Log.d("SmartMove", "Scheduled task.");

        if (currentPose == null) return;

        List<LandmarkPoint> allLandmarkPoints = currentPose.getAllPoseLandmarks()
                .stream()
                .map(LandmarkPoint::fromPoseLandmark)
                .collect(Collectors.toList());

        ExerciseDataRequest exerciseDataRequest = new ExerciseDataRequest(getElapsedTime(), secondHalf, exerciseCategory, allLandmarkPoints);

        ExerciseDataAPI exerciseDataAPI = new ExerciseDataAPI();
        Response<ExerciseAnalysisResponse> exerciseAnalysisRetrofit = exerciseDataAPI.sendExerciseAnalysis(exerciseDataRequest);

        if (exerciseAnalysisRetrofit.isSuccessful()) {

            ExerciseAnalysisResponse exerciseAnalysisResponse = exerciseAnalysisRetrofit.body();

            if (exerciseAnalysisResponse == null) return;

            viewModel.getCurrentPose().postValue(currentPose);
            viewModel.getExerciseCorrectness().postValue(exerciseAnalysisResponse.getCorrectness());
            viewModel.getExerciseProgress().postValue(exerciseAnalysisResponse.getProgress());

            if (exerciseAnalysisResponse.isFinishedRepetition()) {
                int repetitions = Optional.ofNullable(viewModel.getExerciseRepetitions().getValue()).orElse(0);
                viewModel.getExerciseRepetitions().postValue(repetitions+1);
            }

        }

    }

    private Long getElapsedTime() {
        return System.currentTimeMillis() - initialTime;
    }

    private void clearHandler() {
        this.currentPose = null;
        this.exerciseCategory = null;
        this.scheduler = null;
        this.initialTime = null;
        this.secondHalf = false;
    }

    public ExerciseReportEntity createExerciseReport() {
        return null;
    }

    public static PoseDetectorHandler getInstance() {
        if (instance == null)
            instance = new PoseDetectorHandler();
        return instance;
    }

}
