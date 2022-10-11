package pt.ua.hackaton.smartmove.permissions;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

public class CameraPermission extends AppPermission {

    public static final int CAMERA_PERMISSION_REQUEST_CODE = 1;

    public CameraPermission(Activity activity) {
        super(activity, Manifest.permission.CAMERA, CAMERA_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void askPermission() {
        if (!isGranted()) {
            activity.requestPermissions(new String[] {permission}, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public boolean isGranted() {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

}
