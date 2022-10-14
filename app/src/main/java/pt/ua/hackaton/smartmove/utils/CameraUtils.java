package pt.ua.hackaton.smartmove.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.pose.PoseDetection;
import com.google.mlkit.vision.pose.PoseDetector;
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import pt.ua.hackaton.smartmove.R;
import pt.ua.hackaton.smartmove.data.LandmarkPoint;
import pt.ua.hackaton.smartmove.data.requests.ExerciseDataRequest;
import pt.ua.hackaton.smartmove.data.responses.ExerciseAnalysisResponse;
import pt.ua.hackaton.smartmove.models.CameraStatsViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CameraUtils {

    public static List<Double> startCameraX(Context context, ImageView cameraFrame, ProcessCameraProvider cameraProvider) {

        cameraProvider.unbindAll();

        // Camera Selector Use Case
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build();

        // Image Analysis Use Case
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        long initialTime = System.currentTimeMillis();

        double caloriesBurn = 0;
        final double[] correctness = {0};
        final long[] frameCount = {0};

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context), new ImageAnalysis.Analyzer() {
            @Override
            public void analyze(@NonNull ImageProxy imageProxy) {

                @SuppressLint("UnsafeOptInUsageError")
                Image mediaImage = imageProxy.getImage();

                if (mediaImage != null) {

                    Bitmap mediaImageBitmap = BitmapUtils.toBitmap(mediaImage);
                    Bitmap mutableBitmap = BitmapUtils.toMutableBitmap(mediaImageBitmap);
                    Bitmap rotatedMutableBitmap = BitmapUtils.rotateBitmap(mutableBitmap, 270);

                    InputImage image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());

                    PoseDetectorOptions options = new PoseDetectorOptions.Builder()
                            .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
                            .build();

                    PoseDetector poseDetector = PoseDetection.getClient(options);

                    final long[] timeElapsed = {System.currentTimeMillis()};

                    TextView correctnessTextView = ((AppCompatActivity) context).findViewById(R.id.cameraCorrectnessCardText);
                    TextView progressTextView = ((AppCompatActivity) context).findViewById(R.id.cameraProgressCardText);
                    TextView cameraRepetitionsCounterTextView = ((AppCompatActivity) context).findViewById(R.id.cameraRepetitionsCounterTextView);

                    AtomicInteger repetitionsCounter = new AtomicInteger(0);

                    poseDetector.process(image)
                            .addOnSuccessListener(
                                    pose -> {

                                        Log.d("SmartMove_Image_Analysis", "Recognized image.");
                                        pose.getAllPoseLandmarks().forEach(poseLandmark -> BitmapUtils.markPointBlue(rotatedMutableBitmap, poseLandmark));

                                        List<LandmarkPoint> landmarkPointList = pose.getAllPoseLandmarks()
                                                .stream()
                                                .map(LandmarkPoint::fromPoseLandmark)
                                                .collect(Collectors.toList());

                                        Log.d("EXERCISE_DATA", "Sending request with landmarks " + String.valueOf(landmarkPointList));

                                        CameraStatsViewModel cameraStatsViewModel = new ViewModelProvider((AppCompatActivity) context).get(CameraStatsViewModel.class);

                                        timeElapsed[0] = System.currentTimeMillis();

                                        AtomicBoolean isInFirstHalt = new AtomicBoolean(false);

                                        long totalTime = System.currentTimeMillis() - initialTime;
                                        ExerciseDataRequest exerciseDataRequest = new ExerciseDataRequest(totalTime, cameraStatsViewModel.getExerciseHalf().getValue(), "squat", landmarkPointList);

                                        Call<ExerciseAnalysisResponse> call = ApiUtils.submitExerciseDataForAnalysis(1, exerciseDataRequest);

                                        if (landmarkPointList.size() > 0) {

                                            Log.d("EXERCISE_DATA", "Executing request");
                                            call.enqueue(new Callback<ExerciseAnalysisResponse>() {
                                                @Override
                                                public void onResponse(Call<ExerciseAnalysisResponse> call, Response<ExerciseAnalysisResponse> response) {

                                                    if (response.body() != null) {
                                                        // Toast.makeText(context.getApplicationContext(), String.valueOf(response.body().getCorrectness()), Toast.LENGTH_SHORT).show();


                                                        cameraStatsViewModel.addMeasurementCount(1);
                                                        cameraStatsViewModel.addMeasurementToTotalCorrectness(response.body().getCorrectness());
                                                        cameraStatsViewModel.getTotalTime().setValue((double)totalTime);

                                                        cameraStatsViewModel.setExerciseHalf(response.body().isFirstHalf());

                                                        if (response.body().isFinishedRepetition()) {
                                                            Toast.makeText(context, "Finished Repetition", Toast.LENGTH_SHORT).show();
                                                            cameraStatsViewModel.incrementReps();
                                                            //cameraStatsViewModel.updateRepTimes((double)System.currentTimeMillis());

                                                            cameraStatsViewModel.addRepetition(1);
                                                            cameraRepetitionsCounterTextView.setText(cameraStatsViewModel.getRepetitionsCount().getValue() + " reps");

                                                            Toast.makeText(context, String.format("Correctness: %s; Progress: %s", response.body().getCorrectness(), response.body().getProgress()), Toast.LENGTH_SHORT).show();

                                                        }

                                                        double correctnessValue = Math.round(response.body().getCorrectness() * 100);

                                                        BitmapUtils.markPointRed(rotatedMutableBitmap, pose.getPoseLandmark(response.body().getWorstMiddleLandmark()));

                                                        correctnessTextView.setText(String.valueOf(correctnessValue));

                                                        if (correctnessValue >= 30) {

                                                            double progress = response.body().getProgress()/2;

                                                            if (!cameraStatsViewModel.getExerciseHalf().getValue()) {
                                                                progress += 0.5;
                                                            }

                                                            double progressNormalized = Math.round(progress * 100);
                                                            progressTextView.setText(progressNormalized + "%");

                                                        }

                                                        Log.d("EXERCISE_DATA", "Returned Response " + response.body());
                                                    }

                                                }

                                                @Override
                                                public void onFailure(Call<ExerciseAnalysisResponse> call, Throwable t) {
                                                    Log.d("EXERCISE_DATA", "Request failed " + t.getMessage());
                                                }
                                            });

                                        }

                                        cameraFrame.setImageBitmap(rotatedMutableBitmap);

                                    })
                            .addOnFailureListener(e -> {
                                Log.d("SmartMove_Image_Analysis", "Failed to recognize points");
                            }).addOnCompleteListener(runnable -> imageProxy.close());

                }

            }

        });

        cameraProvider.bindToLifecycle((LifecycleOwner) context, cameraSelector, imageAnalysis);

        return Arrays.asList(caloriesBurn, correctness[0]);

    }

    public static void stopCameraX(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();
    }

}
