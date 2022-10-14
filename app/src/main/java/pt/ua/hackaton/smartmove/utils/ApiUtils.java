package pt.ua.hackaton.smartmove.utils;

import java.util.List;

import okhttp3.ResponseBody;
import pt.ua.hackaton.smartmove.data.Exercise;
import pt.ua.hackaton.smartmove.data.requests.ExerciseDataRequest;
import pt.ua.hackaton.smartmove.data.requests.LoginRequest;
import pt.ua.hackaton.smartmove.data.responses.ExerciseAnalysisResponse;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtils {

    private static final String API_BASE_URI = "http://20.163.184.110";
    private static final Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(API_BASE_URI)
            .build();

    private static final ApiService apiService = retrofit.create(ApiService.class);

    public static boolean authenticate(String username, String password) {
        // TODO correct response type
        Call<Response<ResponseBody>> response = apiService.login(new LoginRequest(username, password));
        // TODO do something with response
        return true;
    }

    public static Call<List<Exercise>> getExercises() {
        return apiService.getExercises();
    }

    public static Call<ExerciseAnalysisResponse> submitExerciseDataForAnalysis(int exerciseId, ExerciseDataRequest exerciseDataRequest) {
        return apiService.submitExerciseDataForAnalysis(exerciseId, exerciseDataRequest);
    }

}
