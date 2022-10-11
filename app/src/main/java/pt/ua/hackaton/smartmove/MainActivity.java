package pt.ua.hackaton.smartmove;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseDetection;
import com.google.mlkit.vision.pose.PoseDetector;
import com.google.mlkit.vision.pose.PoseLandmark;
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions;
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

import pt.ua.hackaton.smartmove.utils.BitmapUtils;

public class MainActivity extends AppCompatActivity {

    public static final int PICK_IMAGE = 1;

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        findViewById(R.id.startCameraBtn).setOnClickListener(view -> cameraProviderFuture.addListener(() -> {
            Intent myIntent = new Intent(MainActivity.this, CameraActivity.class);
            MainActivity.this.startActivity(myIntent);
        }, ContextCompat.getMainExecutor(this)));

    }

    private void stopCameraX(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();
    }

    private void startCameraX(ProcessCameraProvider cameraProvider) {

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

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), new ImageAnalysis.Analyzer() {
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
                                            markPoint(rotatedMutableBitmap, poseLandmark);
                                        });

                                        ImageView imageView = findViewById(R.id.imageView);
                                        imageView.setImageBitmap(rotatedMutableBitmap);

                                    })
                            .addOnFailureListener(e -> {
                                Log.d("SmartMove_Image_Analysis", "Failed to recognize points");
                            }).addOnCompleteListener(runnable -> imageProxy.close());

                }

            }

        });

        cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, imageAnalysis);

    }

    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {

            try {
                InputStream inputStream = getApplicationContext().getContentResolver().openInputStream(data.getData());

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                InputImage image = InputImage.fromBitmap(bitmap, 0);

                AccuratePoseDetectorOptions options = new AccuratePoseDetectorOptions.Builder()
                        .setDetectorMode(AccuratePoseDetectorOptions.SINGLE_IMAGE_MODE)
                        .build();

                PoseDetector poseDetector = PoseDetection.getClient(options);

                Task<Pose> result = poseDetector.process(image)
                        .addOnSuccessListener(
                                pose -> {

                                    Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

                                    Toast.makeText(getApplicationContext(), String.format("Points found: %s", pose.getAllPoseLandmarks().size()), Toast.LENGTH_LONG)
                                            .show();

                                    pose.getAllPoseLandmarks().forEach(poseLandmark -> {
                                        Toast.makeText(getApplicationContext(), String.format("Point found with confidence: %s", poseLandmark.getInFrameLikelihood()), Toast.LENGTH_LONG).show();
                                        markPoint(mutableBitmap, poseLandmark);
                                    });

                                    ImageView imageView = (ImageView) findViewById(R.id.imageView);
                                    imageView.setImageBitmap(mutableBitmap);

                                })
                        .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show());


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

    }

    private void markPoint(Bitmap mutableBitmap, PoseLandmark poseLandmark) {

        if (mutableBitmap == null) {
            return;
        }

        Canvas tempCanvas = new Canvas(mutableBitmap);

        Paint paint = new Paint(Paint.DITHER_FLAG);
        paint.setColor(Color.RED);

        tempCanvas.drawBitmap(mutableBitmap, 0, 0, null);
        tempCanvas.drawCircle(poseLandmark.getPosition().x, poseLandmark.getPosition().y, 20, paint);

    }

}