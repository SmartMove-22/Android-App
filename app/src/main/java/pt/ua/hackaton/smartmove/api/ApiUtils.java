package pt.ua.hackaton.smartmove.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import pt.ua.hackaton.smartmove.R;
import pt.ua.hackaton.smartmove.api.ApiService;
import pt.ua.hackaton.smartmove.data.Exercise;
import pt.ua.hackaton.smartmove.data.requests.ExerciseDataRequest;
import pt.ua.hackaton.smartmove.data.requests.LoginRequest;
import pt.ua.hackaton.smartmove.data.responses.ExerciseAnalysisResponse;
import pt.ua.hackaton.smartmove.data.responses.LoginResponse;
import pt.ua.hackaton.smartmove.data.Report;
import pt.ua.hackaton.smartmove.data.requests.ReportRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtils {

    private static final String API_BASE_URI = "http://20.229.92.93";
    private static final Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(API_BASE_URI)
            .build();

    private static final ApiService apiService = retrofit.create(ApiService.class);

    public static void authenticate(Context context, String username, String password, Runnable successCallback, Runnable failureCallback) {
        // TODO correct response type
        Call<LoginResponse> call = apiService.login(new LoginRequest(username, password));
        // TODO do something with response

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                try {
                    if (response.isSuccessful()) {

                        SharedPreferences prefs = context.getSharedPreferences(
                                context.getString(R.string.preference_file_key),
                                Context.MODE_PRIVATE);

                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("token", response.body().getAuthorization());
                        editor.putString("type", response.body().getType());
                        editor.apply();

                        successCallback.run();
                    }
                    else
                        failureCallback.run();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public static Call<List<Exercise>> getExercises(String auth_token) {
        return apiService.getExercises(auth_token);
    }

    public static Call<Report> getReportForDay(String auth_token, String timestamp) {
        return apiService.getReportForDay(auth_token, new ReportRequest(timestamp));
    }

    public static Call<ExerciseAnalysisResponse> submitExerciseDataForAnalysis(ExerciseDataRequest exerciseDataRequest) {
        return apiService.submitExerciseDataForAnalysis(exerciseDataRequest);
    }

    public static Response<ExerciseAnalysisResponse> sendExerciseAnalysis(ExerciseDataRequest exerciseDataRequest) {

        Call<ExerciseAnalysisResponse> call = ApiUtils.submitExerciseDataForAnalysis(exerciseDataRequest);

        try {
            return call.execute();
        } catch (Exception exception) {
            Log.e("SmartMove", "An error has occurred while executing the API endpoint." + exception.getMessage());
            return null;
        }

    }

}
