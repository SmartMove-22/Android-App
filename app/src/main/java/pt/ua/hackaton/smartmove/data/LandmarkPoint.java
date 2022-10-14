package pt.ua.hackaton.smartmove.data;

import com.google.mlkit.vision.pose.PoseLandmark;

public class LandmarkPoint {

    private final int id;
    private final double x;
    private final double y;
    private final double z;

    public LandmarkPoint(int id, double x, double y, double z) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public static LandmarkPoint fromPoseLandmark(PoseLandmark poseLandmark) {
        return new LandmarkPoint(poseLandmark.getLandmarkType(), poseLandmark.getPosition3D().getX(), poseLandmark.getPosition3D().getY(), poseLandmark.getPosition3D().getZ());
    }

}
