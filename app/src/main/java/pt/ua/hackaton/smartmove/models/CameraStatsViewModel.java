package pt.ua.hackaton.smartmove.models;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CameraStatsViewModel extends ViewModel {

    MutableLiveData<Long> totalMeasurementsCount = new MutableLiveData<>(0l);
    MutableLiveData<Double> totalCorrectness = new MutableLiveData<>(0d);
    MutableLiveData<Double> totalCalories = new MutableLiveData<>(0d);


    public MutableLiveData<Long> getTotalMeasurementsCount() {
        return totalMeasurementsCount;
    }

    public MutableLiveData<Double> getTotalCorrectness() {
        return totalCorrectness;
    }

    public MutableLiveData<Double> getTotalCalories() {
        return totalCalories;
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
}
