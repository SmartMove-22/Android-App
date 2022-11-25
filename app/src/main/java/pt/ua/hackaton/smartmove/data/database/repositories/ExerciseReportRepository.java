package pt.ua.hackaton.smartmove.data.database.repositories;

import android.content.Context;

import pt.ua.hackaton.smartmove.data.database.AppDatabase;
import pt.ua.hackaton.smartmove.data.database.dao.ExerciseReportDao;

public class ExerciseReportRepository {

    private static ExerciseReportRepository instance;

    private final ExerciseReportDao exerciseReportDao;

    public ExerciseReportRepository(Context appContext) {
        this.exerciseReportDao = AppDatabase.getInstance(appContext).exerciseReportDao();
    }

    public void getDailyExerciseSeconds() {

    }

    public static ExerciseReportRepository getInstance(Context appContext) {
        if (instance == null)
            instance = new ExerciseReportRepository(appContext);
        return instance;
    }

}
