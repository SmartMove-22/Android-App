package pt.ua.hackaton.smartmove.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.pose.PoseDetection;
import com.google.mlkit.vision.pose.PoseDetector;
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions;


public class CameraUtils {

    public static void startCameraX(Context context, ImageView cameraFrame, ProcessCameraProvider cameraProvider) {

        cameraProvider.unbindAll();

        // Camera Selector Use Case
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build();

        // Preview Use Case
        // Preview preview = new Preview.Builder().build();
        // preview.setSurfaceProvider(previewView.getSurfaceProvider());

        // Image Analysis Use Case
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

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

                    poseDetector.process(image)
                            .addOnSuccessListener(
                                    pose -> {

                                        Log.d("SmartMove_Image_Analysis", "Recognized image.");
                                        pose.getAllPoseLandmarks().forEach(poseLandmark -> {
                                            BitmapUtils.markPoint(rotatedMutableBitmap, poseLandmark);
                                        });

                                        cameraFrame.setImageBitmap(rotatedMutableBitmap);

                                    })
                            .addOnFailureListener(e -> {
                                Log.d("SmartMove_Image_Analysis", "Failed to recognize points");
                            }).addOnCompleteListener(runnable -> imageProxy.close());

                }

            }

        });

        cameraProvider.bindToLifecycle((LifecycleOwner) context, cameraSelector, imageAnalysis);

    }

    public static void stopCameraX(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();
    }

}
