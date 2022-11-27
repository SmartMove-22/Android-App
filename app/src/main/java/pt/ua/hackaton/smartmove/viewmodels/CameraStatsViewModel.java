package pt.ua.hackaton.smartmove.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CameraStatsViewModel extends ViewModel {

    private final MutableLiveData<Long> totalMeasurementsCount;
    private final MutableLiveData<Double> totalCorrectness;
    private final MutableLiveData<Double> totalCalories;
    private final MutableLiveData<Integer> repetitionsCount;
    private final MutableLiveData<Boolean> exerciseHalf;

    public CameraStatsViewModel() {
        this.totalMeasurementsCount = new MutableLiveData<>(0L);
        this.totalCorrectness = new MutableLiveData<>(0d);
        this.totalCalories = new MutableLiveData<>(0d);
        this.repetitionsCount = new MutableLiveData<>(0);
        this.exerciseHalf = new MutableLiveData<>(false);
    }

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
