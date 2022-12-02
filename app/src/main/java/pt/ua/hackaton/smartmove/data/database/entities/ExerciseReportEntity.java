package pt.ua.hackaton.smartmove.data.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

import pt.ua.hackaton.smartmove.data.database.converters.DateConverter;

@Entity(tableName = "exercise_report")
@TypeConverters(DateConverter.class)
public class ExerciseReportEntity {

    @PrimaryKey
    public Long id;

    @ColumnInfo(name = "timestamp")
    public Date timestamp;

    @ColumnInfo(name = "exercise_id")
    public int exerciseId;

    @ColumnInfo(name = "exercise_duration_seconds")
    public long exerciseDuration;

    @ColumnInfo(name = "correctness")
    public double exerciseCorrectness;

    @ColumnInfo(name = "calories_burn")
    public double caloriesBurn;

    @ColumnInfo(name = "average_heart_rate")
    public double averageHeartRate;

    public ExerciseReportEntity(Date timestamp, int exerciseId, long exerciseDuration, double exerciseCorrectness, double caloriesBurn, double averageHeartRate) {
        this.timestamp = timestamp;
        this.exerciseId = exerciseId;
        this.exerciseDuration = exerciseDuration;
        this.exerciseCorrectness = exerciseCorrectness;
        this.caloriesBurn = caloriesBurn;
        this.averageHeartRate = averageHeartRate;
    }

}
