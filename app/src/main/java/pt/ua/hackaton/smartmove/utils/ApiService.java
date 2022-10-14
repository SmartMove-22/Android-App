package pt.ua.hackaton.smartmove.utils;

import java.util.List;

import okhttp3.ResponseBody;
import pt.ua.hackaton.smartmove.data.Exercise;
import pt.ua.hackaton.smartmove.data.requests.ExerciseDataRequest;
import pt.ua.hackaton.smartmove.data.requests.LoginRequest;
import pt.ua.hackaton.smartmove.data.responses.ExerciseAnalysisResponse;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("auth/login")
    Call<Response<ResponseBody>> login(@Body LoginRequest loginRequest);

    @GET("exercises")
    Call<List<Exercise>> getExercises();

    @POST("trainee/exercise/{exerciseId}/data")
    Call<ExerciseAnalysisResponse> submitExerciseDataForAnalysis(@Path("exerciseId") int exerciseId, @Body ExerciseDataRequest exerciseDataRequest);

}
