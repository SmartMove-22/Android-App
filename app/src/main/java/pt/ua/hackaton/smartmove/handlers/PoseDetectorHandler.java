package pt.ua.hackaton.smartmove.handlers;

import android.util.Log;

import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseLandmark;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import pt.ua.hackaton.smartmove.api.service.ExerciseDataService;
import pt.ua.hackaton.smartmove.data.LandmarkPoint;
import pt.ua.hackaton.smartmove.data.requests.ExerciseDataRequest;
import pt.ua.hackaton.smartmove.utils.ExerciseCategory;
import pt.ua.hackaton.smartmove.viewmodels.PoseDetectorViewModel;

public class PoseDetectorHandler {

    private static PoseDetectorHandler instance;

    private Pose currentPose;
    private ExerciseCategory exerciseCategory;
    private ScheduledExecutorService scheduler;
    private Long initialTime;
    private boolean firstHalf;
    private boolean showLandmarks;
    private int worstLandmark;
    private boolean isReadyToStart;

    private PoseDetectorViewModel viewModel;

    private PoseDetectorHandler() {
        this.currentPose = null;
        this.exerciseCategory = null;
        this.scheduler = null;
        this.initialTime = null;
        this.firstHalf = true;
        this.showLandmarks = false;
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

    public Long getElapsedTime() {
        return System.currentTimeMillis() - initialTime;
    }

    public boolean isShowingLandmarks() {
        return showLandmarks;
    }

    public void setShowLandmarks(boolean showLandmarks) {
        this.showLandmarks = showLandmarks;
    }

    private void clearHandler() {
        this.currentPose = null;
        this.exerciseCategory = null;
        this.scheduler = null;
        this.initialTime = null;
        this.firstHalf = false;
        this.showLandmarks = false;
        this.worstLandmark = 0;
    }

    public PoseLandmark getWorstLandmark() {
        return currentPose.getPoseLandmark(worstLandmark);
    }

    public void setWorstLandmark(int worstLandmark) {
        this.worstLandmark = worstLandmark;
    }

    public boolean isReadyToStart() {
        return isReadyToStart;
    }

    public void setReadyToStart(boolean readyToStart) {
        isReadyToStart = readyToStart;
    }

    public static PoseDetectorHandler getInstance() {
        if (instance == null)
            instance = new PoseDetectorHandler();
        return instance;
    }

}
