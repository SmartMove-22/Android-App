package pt.ua.hackaton.smartmove.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import pt.ua.hackaton.smartmove.data.database.AppDatabase;
import pt.ua.hackaton.smartmove.data.database.dao.ExerciseReportDao;

public class WorkoutsViewModel extends AndroidViewModel {

    private final LiveData<Long> todayExerciseSeconds;
    private final LiveData<Double> todayCaloriesBurn;

    public WorkoutsViewModel(Application application) {
        super(application);

        ExerciseReportDao exerciseReportDao = AppDatabase.getInstance(application).exerciseReportDao();

        this.todayExerciseSeconds = exerciseReportDao.getTodayExerciseTime();
        this.todayCaloriesBurn = exerciseReportDao.getTodayCaloriesBurn();

    }

    public LiveData<Long> getTodayExerciseSeconds() {
        return todayExerciseSeconds;
    }

    public LiveData<Double> getTodayExerciseCaloriesBurn() {
        return todayCaloriesBurn;
    }

}
