package pt.ua.hackaton.smartmove.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import pt.ua.hackaton.smartmove.data.database.AppDatabase;
import pt.ua.hackaton.smartmove.data.database.dao.ExerciseReportDao;

public class ReportsViewModel extends AndroidViewModel {

    private final LiveData<Double> todayCaloriesBurn;
    private final LiveData<Double> todayCorrectnessAverage;
    private final LiveData<Long> todayExerciseTimeSum;

    private final ExerciseReportDao exerciseReportDao;

    public ReportsViewModel(@NonNull Application application) {

        super(application);

        exerciseReportDao = AppDatabase.getInstance(application).exerciseReportDao();

        this.todayCaloriesBurn = exerciseReportDao.getTodayCaloriesBurn();
        this.todayCorrectnessAverage = exerciseReportDao.getTodayCaloriesBurn();
        this.todayExerciseTimeSum = exerciseReportDao.getTodayExerciseTime();

    }

    public LiveData<Double> getTodayCaloriesBurn() {
        return todayCaloriesBurn;
    }

    public LiveData<Double> getTodayCorrectnessAverage() {
        return todayCorrectnessAverage;
    }

    public LiveData<Long> getTodayExerciseTimeSum() {
        return todayExerciseTimeSum;
    }

    public LiveData<Long> getDailyExerciseTimeSum(int offset) {
        if (offset <= 0) {
            return exerciseReportDao.getTodayExerciseTime();
        }
        return exerciseReportDao.getTotalDailyExerciseTime(offset, offset-1);
    }

    public LiveData<Double> getDailyCaloriesSum(int offset) {
        if (offset <= 0) {
            return exerciseReportDao.getTodayCaloriesBurn();
        }
        return exerciseReportDao.getTotalDailyCalories(offset, offset-1);
    }

    public LiveData<Double> getDailyCorrectnessAvg(int offset) {
        if (offset <= 0) {
            return exerciseReportDao.getTodayCorrectnessAverage();
        }
        return exerciseReportDao.getDailyAverageCorrectness(offset, offset-1);
    }

}
