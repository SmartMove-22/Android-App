package pt.ua.hackaton.smartmove.models;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import pt.ua.hackaton.smartmove.data.AssignedExercise;
import pt.ua.hackaton.smartmove.utils.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoachTraineeOverviewViewModel extends ViewModel {

    private MutableLiveData<String> trainee;
    private MutableLiveData<List<AssignedExercise>> assignedExercises;

    private SharedPreferences sharedPreferences;

    public void setSharedPreferences(SharedPreferences prefs) {
        sharedPreferences = prefs;
    }

    public LiveData<List<AssignedExercise>> getAssignedExercises() {
        if (assignedExercises == null) {
            assignedExercises = new MutableLiveData<List<AssignedExercise>>();
        }
        return assignedExercises;
    }

    public LiveData<String> getTrainee() {
        if (trainee == null) {
            trainee = new MutableLiveData<String>();
        }
        return trainee;
    }

    private void loadAssignedExercises() {
        String auth_token = sharedPreferences.getString("token", "");
        ApiUtils.getTraineeAssignedExercises(auth_token, trainee.getValue()).enqueue(new Callback<List<AssignedExercise>>() {
            @Override
            public void onResponse(Call<List<AssignedExercise>> call, Response<List<AssignedExercise>> response) {
                assignedExercises.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<AssignedExercise>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
