package pt.ua.hackaton.smartmove.utils;

import java.util.List;

import okhttp3.ResponseBody;
import pt.ua.hackaton.smartmove.data.Exercise;
import pt.ua.hackaton.smartmove.data.LoginRequest;
import pt.ua.hackaton.smartmove.data.LoginResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("trainee/exercises")
    Call<List<Exercise>> getExercises(@Header("Authorization") String authorization);
}
