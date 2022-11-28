package pt.ua.hackaton.smartmove.handlers;

import android.util.Log;

import com.google.mlkit.vision.pose.Pose;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import pt.ua.hackaton.smartmove.api.service.ExerciseDataService;
import pt.ua.hackaton.smartmove.data.LandmarkPoint;
import pt.ua.hackaton.smartmove.data.requests.ExerciseDataRequest;
import pt.ua.hackaton.smartmove.data.responses.ExerciseAnalysisResponse;
import pt.ua.hackaton.smartmove.utils.ExerciseCategory;
import pt.ua.hackaton.smartmove.viewmodels.PoseDetectorViewModel;
import retrofit2.Response;

public class PoseDetectorHandler {

    private static PoseDetectorHandler instance;

    private Pose currentPose;
    private ExerciseCategory exerciseCategory;
    private ScheduledExecutorService scheduler;
    private Long initialTime;
    private boolean firstHalf;
    private List<Consumer<Response<ExerciseAnalysisResponse>>> callbacks;

    private PoseDetectorViewModel viewModel;

    private PoseDetectorHandler() {
        this.currentPose = null;
        this.exerciseCategory = null;
        this.scheduler = null;
        this.initialTime = null;
        this.firstHalf = true;
    }

    public synchronized Pose getCurrentPose() {
        return currentPose;
    }

    public synchronized void setCurrentPose(Pose currentPose) {
        this.currentPose = currentPose;
    }

    public void setExerciseCategory(ExerciseCategory exerciseCategory) {
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

    public void setFirstHalf(boolean firstHalf) {
        this.firstHalf = firstHalf;
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

        ExerciseDataRequest exerciseDataRequest = new ExerciseDataRequest(getElapsedTime(), firstHalf, exerciseCategory.name(), allLandmarkPoints);
        ExerciseDataService exerciseDataService = ExerciseDataService.getInstance();

        exerciseDataService.sendExerciseAnalysis(exerciseDataRequest);

    }

    private Long getElapsedTime() {
        return System.currentTimeMillis() - initialTime;
    }

    private void clearHandler() {
        this.currentPose = null;
        this.exerciseCategory = null;
        this.scheduler = null;
        this.initialTime = null;
        this.firstHalf = false;
    }

    public static PoseDetectorHandler getInstance() {
        if (instance == null)
            instance = new PoseDetectorHandler();
        return instance;
    }

}
