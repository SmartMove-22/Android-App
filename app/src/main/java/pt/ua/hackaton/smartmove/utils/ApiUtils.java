package pt.ua.hackaton.smartmove.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import pt.ua.hackaton.smartmove.R;
import pt.ua.hackaton.smartmove.data.Exercise;
import pt.ua.hackaton.smartmove.data.LoginRequest;
import pt.ua.hackaton.smartmove.data.LoginResponse;
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

    public static boolean authenticate(Context context, String username, String password) {
        // TODO correct response type
        Call<LoginResponse> call = apiService.login(new LoginRequest(username, password));
        // TODO do something with response

        try {
            Response<LoginResponse> response = call.execute();
            if (response.isSuccessful()) {

                SharedPreferences prefs = context.getSharedPreferences(
                        context.getString(R.string.preference_file_key),
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("token", response.body().getAuthorization());
                editor.apply();

                return true;
            }
            else
                return false;
        }
        catch (IOException e) {
            return false;
        }
    }

    public static Call<List<Exercise>> getExercises(String auth_token) {
        return apiService.getExercises(auth_token);
    }

}
