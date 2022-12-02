package pt.ua.hackaton.smartmove.utils;

public enum PoseLandmarkTypeMapping {

    NOSE(0, "Head"),
    LEFT_EYE_INNER(1, "Head"),
    LEFT_EYE(2, "Head"),
    LEFT_EYE_OUTER(3, "Head"),
    RIGHT_EYE_INNER(4, "Head"),
    RIGHT_EYE(5, "Head"),
    RIGHT_EYE_OUTER(6, "Head"),
    LEFT_EAR(7, "Head"),
    RIGHT_EAR(8, "Head"),
    LEFT_MOUTH(9, "Head"),
    RIGHT_MOUTH(10, "Head"),
    LEFT_SHOULDER(11, "Left Shoulder"),
    RIGHT_SHOULDER(12, "Right Shoulder"),
    LEFT_ELBOW(13, "Left Elbow"),
    RIGHT_ELBOW(14, "Right Elbow"),
    LEFT_WRIST(15, "Left Wrist"),
    RIGHT_WRIST(16, "Right Wrist"),
    LEFT_PINKY(17, "Left Hand"),
    RIGHT_PINKY(18, "Right Hand"),
    LEFT_INDEX(19, "Left Hand"),
    RIGHT_INDEX(20, "Right Hand"),
    LEFT_THUMB(21, "Left Hand"),
    RIGHT_THUMB(22, "Right Hand"),
    LEFT_HIP(23, "Hip to the left"),
    RIGHT_HIP(24, "Hip to the right"),
    LEFT_KNEE(25, "Left Knee"),
    RIGHT_KNEE(26, "Right Knee"),
    LEFT_ANKLE(27, "Left Ankle"),
    RIGHT_ANKLE(28, "Right Ankle"),
    LEFT_HEEL(29, "Left Heel"),
    RIGHT_HEEL(30, "Right Heel"),
    LEFT_FOOT_INDEX(31, "Left Foot"),
    RIGHT_FOOT_INDEX(32, "Right Foot");

    private final int landmarkId;
    private final String landmarkText;

    PoseLandmarkTypeMapping(int landmarkId, String landmarkText) {
        this.landmarkId = landmarkId;
        this.landmarkText = landmarkText;
    }

    public int getLandmarkId() {
        return landmarkId;
    }

    public String getLandmarkText() {
        return landmarkText;
    }

    public static PoseLandmarkTypeMapping fromPoseLandmarkId(int poseLandmarkId) {
        for (PoseLandmarkTypeMapping poseLandmarkTypeMapping : PoseLandmarkTypeMapping.values()) {
            if (poseLandmarkTypeMapping.getLandmarkId() == poseLandmarkId) {
                return poseLandmarkTypeMapping;
            }
        }
        return null;
    }


}
