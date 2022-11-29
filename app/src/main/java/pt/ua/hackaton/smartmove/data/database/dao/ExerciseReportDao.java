package pt.ua.hackaton.smartmove.data.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;

import java.util.List;

import pt.ua.hackaton.smartmove.data.database.converters.DateConverter;
import pt.ua.hackaton.smartmove.data.database.entities.ExerciseReportEntity;

@Dao
@TypeConverters(DateConverter.class)
public interface ExerciseReportDao {

    @Insert
    void insertReport(ExerciseReportEntity exerciseReportEntity);

    @Query("SELECT * FROM exercise_report WHERE timestamp BETWEEN strftime('%s', 'now', 'start of day', '-' || :howManyDaysBefore || ' day') AND strftime('%s', 'now', 'start of day', '-' || :howManyDaysBefore-1 || ' day');")
    LiveData<List<ExerciseReportEntity>> getDailyReportsData(int howManyDaysBefore);

    @Query("SELECT sum(exercise_duration_seconds) FROM exercise_report WHERE timestamp BETWEEN strftime('%s', 'now', 'start of day', '-' || :firstDay || ' day') AND strftime('%s', 'now', 'start of day', '-' || :dayBefore || ' day');")
    LiveData<Long> getTotalDailyExerciseTime(int firstDay, int dayBefore);

    @Query("SELECT sum(calories_burn) FROM exercise_report WHERE timestamp BETWEEN strftime('%s', 'now', 'start of day', '-' || :firstDay || ' day') AND strftime('%s', 'now', 'start of day', '-' || :dayBefore || ' day');")
    LiveData<Double> getTotalDailyCalories(int firstDay, int dayBefore);

    @Query("SELECT avg(correctness) FROM exercise_report WHERE timestamp BETWEEN strftime('%s', 'now', 'start of day', '-' || :firstDay || ' day') AND strftime('%s', 'now', 'start of day', '-' || :dayBefore || ' day');")
    LiveData<Double> getDailyAverageCorrectness(int firstDay, int dayBefore);

    @Query("SELECT sum(exercise_duration_seconds) FROM exercise_report WHERE timestamp BETWEEN strftime('%s', 'now', 'start of day') AND strftime('%s', 'now');")
    LiveData<Long> getTodayExerciseTime();

    @Query("SELECT sum(calories_burn) FROM exercise_report WHERE timestamp BETWEEN strftime('%s', 'now', 'start of day') AND strftime('%s', 'now');")
    LiveData<Double> getTodayCaloriesBurn();

    @Query("SELECT avg(correctness) FROM exercise_report WHERE timestamp BETWEEN strftime('%s', 'now', 'start of day') AND strftime('%s', 'now');")
    LiveData<Double> getTodayCorrectnessAverage();

    @Query("SELECT avg(correctness) FROM exercise_report;")
    LiveData<Double> getTotalCorrectnessAverage();

    @Query("SELECT sum(calories_burn) FROM exercise_report;")
    LiveData<Double> getTotalCaloriesBurn();

    @Query("SELECT sum(exercise_duration_seconds) FROM exercise_report;")
    LiveData<Double> getTotalExerciseTime();

}