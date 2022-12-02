package pt.ua.hackaton.smartmove.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import pt.ua.hackaton.smartmove.data.database.AppDatabase;
import pt.ua.hackaton.smartmove.data.database.dao.ExerciseReportDao;
import pt.ua.hackaton.smartmove.data.database.dao.UserDao;

public class UserViewModel extends AndroidViewModel {

    private final UserDao userDao;
    private final ExerciseReportDao exerciseReportDao;

    public UserViewModel(@NonNull Application application) {

        super(application);

        userDao = AppDatabase.getInstance(application).userDao();
        exerciseReportDao = AppDatabase.getInstance(application).exerciseReportDao();

    }

    public LiveData<Double> getTotalCaloriesBurned() {
        return exerciseReportDao.getTotalCaloriesBurn();
    }

    public LiveData<Double> getTotalExerciseTime() {
        return exerciseReportDao.getTotalExerciseTime();
    }

    public LiveData<Double> getTotalCorrectnessAverage() {
        return exerciseReportDao.getTotalCorrectnessAverage();
    }

    public LiveData<String> getUserFirstName(String username) {
        return userDao.getUserFirstName(username);
    }

    public LiveData<String> getUserLastName(String username) {
        return userDao.getUserLastName(username);
    }

    public LiveData<String> getUserEmail(String username) {
        return userDao.getUserEmail(username);
    }

    public LiveData<Double> getUserPotential(String username) {
        return userDao.getUserPotential(username);
    }

    public LiveData<Integer> getUserHeight(String username) {
        return userDao.getUserHeight(username);
    }

    public LiveData<Double> getUserWeight(String username) {
        return userDao.getUserWeight(username);
    }

    public LiveData<Double> getUserIMC(String username) {
        return userDao.getUserIMC(username);
    }

    public void setUserPotential(String username, double newPotential) {
        userDao.setUserPotential(username, newPotential);
    }

}
