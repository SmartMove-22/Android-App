package pt.ua.hackaton.smartmove.utils;

import java.util.List;

import pt.ua.hackaton.smartmove.data.Exercise;
import pt.ua.hackaton.smartmove.data.LoginRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @POST("auth/login")
    Call login(@Body LoginRequest loginRequest);

    @GET("exercises")
    Call<List<Exercise>> getExercises();
}
