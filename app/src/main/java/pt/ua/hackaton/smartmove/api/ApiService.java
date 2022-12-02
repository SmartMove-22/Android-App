package pt.ua.hackaton.smartmove.api;

import java.util.List;

import pt.ua.hackaton.smartmove.data.Exercise;
import pt.ua.hackaton.smartmove.data.requests.ExerciseDataRequest;
import pt.ua.hackaton.smartmove.data.requests.LoginRequest;
import pt.ua.hackaton.smartmove.data.responses.ExerciseAnalysisResponse;
import pt.ua.hackaton.smartmove.data.responses.LoginResponse;
import pt.ua.hackaton.smartmove.data.Report;
import pt.ua.hackaton.smartmove.data.requests.ReportRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.PUT;

public interface ApiService {
    @PUT("auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("exercises")
    Call<List<Exercise>> getExercises();

    @POST("exercise/analysis")
    Call<ExerciseAnalysisResponse> submitExerciseDataForAnalysis(@Body ExerciseDataRequest exerciseDataRequest);

    @GET("trainee/exercises")
    Call<List<Exercise>> getExercises(@Header("Authorization") String authorization);

    @GET("trainee/exercises/report")
    Call<Report> getReportForDay(@Header("Authorization") String authorization, @Body ReportRequest body);

}
