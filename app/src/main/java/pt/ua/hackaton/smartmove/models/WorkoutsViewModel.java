package pt.ua.hackaton.smartmove.models;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import pt.ua.hackaton.smartmove.data.Exercise;
import pt.ua.hackaton.smartmove.utils.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkoutsViewModel extends ViewModel {

    private MutableLiveData<List<Exercise>> exercises;

    private SharedPreferences sharedPreferences;

    public void setSharedPreferences(SharedPreferences prefs) {
        sharedPreferences = prefs;
    }

    public LiveData<List<Exercise>> getWorkouts() {
        if (exercises == null) {
            exercises = new MutableLiveData<List<Exercise>>();
            loadExercises();
        }
        return exercises;
    }

    private void loadExercises() {
        String auth_token = sharedPreferences.getString("token", "");
        ApiUtils.getExercises(auth_token).enqueue(new Callback<List<Exercise>>() {
            @Override
            public void onResponse(Call<List<Exercise>> call, Response<List<Exercise>> response) {
                exercises.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Exercise>> call, Throwable t) {
                // uhhhhh...
            }
        });
    }

}
