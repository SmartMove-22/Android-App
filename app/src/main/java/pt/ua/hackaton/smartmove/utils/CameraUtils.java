package pt.ua.hackaton.smartmove.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseDetection;
import com.google.mlkit.vision.pose.PoseDetector;
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import pt.ua.hackaton.smartmove.R;
import pt.ua.hackaton.smartmove.data.LandmarkPoint;
import pt.ua.hackaton.smartmove.data.requests.ExerciseDataRequest;
import pt.ua.hackaton.smartmove.data.responses.ExerciseAnalysisResponse;
import pt.ua.hackaton.smartmove.handlers.PoseDetectorHandler;
import pt.ua.hackaton.smartmove.viewmodels.CameraStatsViewModel;
import pt.ua.hackaton.smartmove.viewmodels.PoseDetectorViewModel;
import retrofit2.Call;
import retrofit2.Response;


public class CameraUtils {

    public void startCameraX(Context context, ImageView cameraFrame, ProcessCameraProvider cameraProvider) {

        cameraProvider.unbindAll();

        // Camera Selector Use Case
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build();

        // Image Analysis Use Case
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context), imageProxy -> {

            @SuppressLint("UnsafeOptInUsageError")
            Image mediaImage = imageProxy.getImage();

            if (mediaImage == null) return;

            Bitmap mediaImageBitmap = BitmapUtils.toBitmap(mediaImage);
            Bitmap mutableBitmap = BitmapUtils.toMutableBitmap(mediaImageBitmap);
            Bitmap rotatedMutableBitmap = BitmapUtils.rotateBitmap(mutableBitmap, -90);

            InputImage image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());

            PoseDetectorOptions options = new PoseDetectorOptions.Builder()
                    .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
                    .build();

            PoseDetector poseDetector = PoseDetection.getClient(options);

            poseDetector.process(image)
                    .addOnSuccessListener(pose -> {

                        PoseDetectorHandler poseDetectorHandler = PoseDetectorHandler.getInstance();
                        poseDetectorHandler.setCurrentPose(pose);
                        // poseDetectorHandler.setViewModel(poseDetectorViewModel);

                        // Mark points in image
                        if (poseDetectorHandler.isShowingLandmarks()) {
                            pose.getAllPoseLandmarks().forEach(poseLandmark -> BitmapUtils.markPointBlue(rotatedMutableBitmap, poseLandmark));
                            BitmapUtils.markPointRed(rotatedMutableBitmap, poseDetectorHandler.getWorstLandmark());
                        }

                        cameraFrame.setImageBitmap(rotatedMutableBitmap);

                    })
                    .addOnFailureListener(e -> Log.d("SmartMove_Image_Analysis", "Failed to recognize points")).addOnCompleteListener(runnable -> imageProxy.close());

        });

        cameraProvider.bindToLifecycle((LifecycleOwner) context, cameraSelector, imageAnalysis);

    }

    public void stopCameraX(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();
    }

}
