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

}