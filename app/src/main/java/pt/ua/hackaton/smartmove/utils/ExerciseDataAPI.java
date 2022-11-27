package pt.ua.hackaton.smartmove.utils;

import android.util.Log;

import pt.ua.hackaton.smartmove.data.requests.ExerciseDataRequest;
import pt.ua.hackaton.smartmove.data.responses.ExerciseAnalysisResponse;
import retrofit2.Call;
import retrofit2.Response;

public class ExerciseDataAPI {

    public Response<ExerciseAnalysisResponse> sendExerciseAnalysis(ExerciseDataRequest exerciseDataRequest) {

        Call<ExerciseAnalysisResponse> call = ApiUtils.submitExerciseDataForAnalysis(1, exerciseDataRequest);

        try {
            return Response.success(new ExerciseAnalysisResponse(0, 0, true, false));
            //return call.execute();
        } catch (Exception exception) {
            Log.e("SmartMove", "An error as occurred while executing the API endpoint.");
            return null;
        }

    }

}
