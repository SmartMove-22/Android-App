package pt.ua.hackaton.smartmove;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.widget.ImageView;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

import pt.ua.hackaton.smartmove.utils.CameraUtils;

public class CameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        ProcessCameraProvider cameraProvider = null;

        try {
            cameraProvider = cameraProviderFuture.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        if (cameraProvider != null) {

            ProcessCameraProvider finalCameraProvider = cameraProvider;

            ImageView cameraFrame = findViewById(R.id.cameraPreview);

            findViewById(R.id.startRecordingBtn).setOnClickListener(view -> cameraProviderFuture.addListener(() -> {
                CameraUtils.startCameraX(this, cameraFrame, finalCameraProvider);
            }, ContextCompat.getMainExecutor(this)));

            findViewById(R.id.stopRecordingBtn).setOnClickListener(view -> cameraProviderFuture.addListener(() -> {
                CameraUtils.stopCameraX(finalCameraProvider);
            }, ContextCompat.getMainExecutor(this)));

        }

    }

}