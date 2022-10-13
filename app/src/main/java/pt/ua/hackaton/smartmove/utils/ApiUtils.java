package pt.ua.hackaton.smartmove.utils;

import java.util.List;

import pt.ua.hackaton.smartmove.data.Exercise;
import pt.ua.hackaton.smartmove.data.LoginRequest;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiUtils {

    private static final String API_BASE_URI = "";
    private static final Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .baseUrl(API_BASE_URI)
            .build();

    private static final ApiService apiService = retrofit.create(ApiService.class);

    public static boolean authenticate(String username, String password) {
        // TODO correct response type
        Call response = apiService.login(new LoginRequest(username, password));
        // TODO do something with response
        return true;
    }

    public static Call<List<Exercise>> getExercises() {
        return apiService.getExercises();
    }

}
