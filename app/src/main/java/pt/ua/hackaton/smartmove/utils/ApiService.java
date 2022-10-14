package pt.ua.hackaton.smartmove.utils;

import java.util.List;

import okhttp3.ResponseBody;
import pt.ua.hackaton.smartmove.data.AssignedExercise;
import pt.ua.hackaton.smartmove.data.Exercise;
import pt.ua.hackaton.smartmove.data.requests.ExerciseDataRequest;
import pt.ua.hackaton.smartmove.data.requests.LoginRequest;
import pt.ua.hackaton.smartmove.data.responses.ExerciseAnalysisResponse;
import pt.ua.hackaton.smartmove.data.LoginResponse;
import pt.ua.hackaton.smartmove.data.Report;
import pt.ua.hackaton.smartmove.data.ReportRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @PUT("auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("exercises")
    Call<List<Exercise>> getExercises();

    @POST("trainee/exercise/{exerciseId}/data")
    Call<ExerciseAnalysisResponse> submitExerciseDataForAnalysis(@Path("exerciseId") int exerciseId, @Body ExerciseDataRequest exerciseDataRequest);

    @GET("trainee/exercises")
    Call<List<Exercise>> getTraineeExercises(@Header("Authorization") String authorization);

    @GET("trainee/exercises/report")
    Call<Report> getReportForDay(@Header("Authorization") String authorization, @Body ReportRequest body);

    @GET("coach/trainees/{traineeId}/exercises")
    Call<List<AssignedExercise>> getTraineeAssignedExercises(
            @Header("Authorization") String authorization,
            @Path("traineeId") String traineeId);

}
