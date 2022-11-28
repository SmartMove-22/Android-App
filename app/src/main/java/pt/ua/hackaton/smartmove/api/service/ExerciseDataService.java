package pt.ua.hackaton.smartmove.api.service;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import pt.ua.hackaton.smartmove.api.ApiUtils;
import pt.ua.hackaton.smartmove.data.requests.ExerciseDataRequest;
import pt.ua.hackaton.smartmove.data.responses.ExerciseAnalysisResponse;
import retrofit2.Response;

public class ExerciseDataService {

    private static ExerciseDataService instance;

    private final List<Consumer<Response<ExerciseAnalysisResponse>>> callbacks;

    private ExerciseDataService() {
        this.callbacks = new ArrayList<>();
    }

    public Response<ExerciseAnalysisResponse> sendExerciseAnalysis(ExerciseDataRequest exerciseDataRequest) {

        Log.d("SmartMove", "Debug 0");

        Response<ExerciseAnalysisResponse> exerciseAnalysisResponseResponse = ApiUtils.sendExerciseAnalysis(exerciseDataRequest);
        callbacks.forEach(responseFunction -> responseFunction.accept(exerciseAnalysisResponseResponse));
        return exerciseAnalysisResponseResponse;

    }

    public void addCallback(Consumer<Response<ExerciseAnalysisResponse>> callback) {
        this.callbacks.add(callback);
    }

    public static ExerciseDataService getInstance() {
        if (instance == null)
            instance = new ExerciseDataService();
        return instance;
    }

}
