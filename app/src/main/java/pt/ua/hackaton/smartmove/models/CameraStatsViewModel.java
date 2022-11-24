package pt.ua.hackaton.smartmove.models;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CameraStatsViewModel extends ViewModel {

    MutableLiveData<Long> totalMeasurementsCount = new MutableLiveData<>(0l);
    MutableLiveData<Double> totalCorrectness = new MutableLiveData<>(0d);
    MutableLiveData<Double> totalCalories = new MutableLiveData<>(0d);
    MutableLiveData<Integer> repetitionsCount = new MutableLiveData<>(0);
    MutableLiveData<Boolean> exerciseHalf = new MutableLiveData<>(false);

    public MutableLiveData<Long> getTotalMeasurementsCount() {
        return totalMeasurementsCount;
    }

    public MutableLiveData<Double> getTotalCorrectness() {
        return totalCorrectness;
    }

    public MutableLiveData<Double> getTotalCalories() {
        return totalCalories;
    }

    public MutableLiveData<Integer> getRepetitionsCount() {
        return repetitionsCount;
    }

    public MutableLiveData<Boolean> getExerciseHalf() {
        return exerciseHalf;
    }

    public void addMeasurementCount(int measurementCount) {
        this.totalMeasurementsCount.setValue(this.totalMeasurementsCount.getValue() + measurementCount);
    }

    public void addMeasurementToTotalCorrectness(Double totalCorrectnessMeasurement) {
        this.totalCorrectness.setValue((this.totalMeasurementsCount.getValue() + totalCorrectnessMeasurement)/this.totalMeasurementsCount.getValue());
    }

    public void setTotalCalories(Double totalCalories) {
        this.totalCalories.setValue(totalCalories);
    }

    public void addRepetition(Integer repetition) {
        this.repetitionsCount.setValue(this.repetitionsCount.getValue()+1);
    }

    public void setExerciseHalf(Boolean exerciseHalf) {
        this.exerciseHalf.setValue(exerciseHalf);
    }

}
