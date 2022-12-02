package pt.ua.hackaton.smartmove.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import pt.ua.hackaton.smartmove.data.AssignedExercise;

public class PerformedExerciseViewModel extends ViewModel {

    MutableLiveData<AssignedExercise> exerciseBeingPerformed = new MutableLiveData<>();

    public MutableLiveData<AssignedExercise> getExerciseBeingPerformed() {
        if (exerciseBeingPerformed == null) {
            exerciseBeingPerformed = new MutableLiveData<>();
        }
        return exerciseBeingPerformed;
    }

    public void setExerciseBeingPerformed(AssignedExercise exerciseBeingPerformed) {
        this.exerciseBeingPerformed.setValue(exerciseBeingPerformed);
    }

}
