package pt.ua.hackaton.smartmove;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

import pt.ua.hackaton.smartmove.permissions.CameraPermission;
import pt.ua.hackaton.smartmove.utils.CameraUtils;

public class CameraActivity extends AppCompatActivity {

    private boolean recordingExercise = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // Hide the top bar
        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().hide();
        }

        CameraPermission cameraPermission = new CameraPermission(this);

        if (!cameraPermission.isGranted()) {
            cameraPermission.askPermission();
        }

        setupCamera();

        findViewById(R.id.quitCameraBtn).setOnClickListener(view -> finish());

    }

    private void setupCamera() {

        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        ProcessCameraProvider cameraProvider = null;

        try {
            cameraProvider = cameraProviderFuture.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        if (cameraProvider == null) {
            Toast.makeText(this, "Camera not found.", Toast.LENGTH_SHORT).show();
            return;
        }

        ProcessCameraProvider finalCameraProvider = cameraProvider;
        ImageView cameraFrame = findViewById(R.id.cameraPreview);
        Button changeRecordingStatusBtn = findViewById(R.id.changeRecordingStatusBtn);

        findViewById(R.id.changeRecordingStatusBtn).setOnClickListener(view -> cameraProviderFuture.addListener(() -> {

            if (!recordingExercise) {
                CameraUtils.startCameraX(this, cameraFrame, finalCameraProvider);
                changeRecordingStatusBtn.setText("Stop");
            } else {
                CameraUtils.stopCameraX(finalCameraProvider);
                changeRecordingStatusBtn.setText("Start");
            }

            recordingExercise = !recordingExercise;

        }, ContextCompat.getMainExecutor(this)));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CameraPermission.CAMERA_PERMISSION_REQUEST_CODE) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Permission Denied for Camera", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }

}