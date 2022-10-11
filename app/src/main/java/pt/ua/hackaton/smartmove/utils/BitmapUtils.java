package pt.ua.hackaton.smartmove.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.Image;

import com.google.mlkit.vision.pose.PoseLandmark;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class BitmapUtils {

    public static Bitmap toBitmap(Image image) {

        Image.Plane[] planes = image.getPlanes();
        ByteBuffer yBuffer = planes[0].getBuffer();
        ByteBuffer uBuffer = planes[1].getBuffer();
        ByteBuffer vBuffer = planes[2].getBuffer();

        int ySize = yBuffer.remaining();
        int uSize = uBuffer.remaining();
        int vSize = vBuffer.remaining();

        byte[] nv21 = new byte[ySize + uSize + vSize];
        //U and V are swapped
        yBuffer.get(nv21, 0, ySize);
        vBuffer.get(nv21, ySize, vSize);
        uBuffer.get(nv21, ySize + vSize, uSize);

        YuvImage yuvImage = new YuvImage(nv21, ImageFormat.NV21, image.getWidth(), image.getHeight(), null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, yuvImage.getWidth(), yuvImage.getHeight()), 75, out);

        byte[] imageBytes = out.toByteArray();
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

    }

    public static Bitmap toMutableBitmap(Bitmap bitmap) {
        if (bitmap != null)
            return bitmap.copy(Bitmap.Config.ARGB_8888, true);
        return null;
    }

    public static Bitmap rotateBitmap(Bitmap mutableBitmap, int rotationInDegrees) {

        if (mutableBitmap == null) {
            return null;
        }

        Matrix matrix = new Matrix();
        matrix.postRotate(rotationInDegrees);
        return Bitmap.createBitmap(mutableBitmap, 0, 0, mutableBitmap.getWidth(), mutableBitmap.getHeight(), matrix, true);

    }

    public static void markPoint(Bitmap mutableBitmap, PoseLandmark poseLandmark) {

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
