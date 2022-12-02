package pt.ua.hackaton.smartmove.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.mlkit.vision.pose.Pose;

public class PoseDetectorViewModel extends AndroidViewModel {

    private final MutableLiveData<Pose> currentPose;
    private final MutableLiveData<Integer> exerciseRepetitions;
    private final MutableLiveData<Double> exerciseCorrectness;
    private final MutableLiveData<Double> exerciseProgress;
    private final MutableLiveData<String> exerciseTip;
    private final MutableLiveData<Boolean> exerciseReadyToStart;

    public PoseDetectorViewModel(@NonNull Application application) {

        super(application);

        this.currentPose = new MutableLiveData<>();
        this.exerciseRepetitions = new MutableLiveData<>();
        this.exerciseCorrectness = new MutableLiveData<>();
        this.exerciseProgress = new MutableLiveData<>();
        this.exerciseTip = new MutableLiveData<>();
        this.exerciseReadyToStart = new MutableLiveData<>();

    }

    public MutableLiveData<Pose> getCurrentPose() {
        return currentPose;
    }

    public MutableLiveData<Integer> getExerciseRepetitions() {
        return exerciseRepetitions;
    }

    public MutableLiveData<Double> getExerciseCorrectness() {
        return exerciseCorrectness;
    }

    public MutableLiveData<Double> getExerciseProgress() {
        return exerciseProgress;
    }

    public MutableLiveData<String> getExerciseTip() {
        return exerciseTip;
    }

    public MutableLiveData<Boolean> getExerciseReadyToStart() {
        return exerciseReadyToStart;
    }

}
