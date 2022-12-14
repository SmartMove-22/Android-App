package pt.ua.hackaton.smartmove.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import pt.ua.hackaton.smartmove.data.database.dao.ExerciseReportDao;
import pt.ua.hackaton.smartmove.data.database.dao.UserDao;
import pt.ua.hackaton.smartmove.data.database.entities.ExerciseReportEntity;
import pt.ua.hackaton.smartmove.data.database.entities.UserEntity;

@Database(entities = {ExerciseReportEntity.class, UserEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract ExerciseReportDao exerciseReportDao();
    public abstract UserDao userDao();

    public static AppDatabase getInstance(Context context) {
        if (instance == null)
            instance = Room.databaseBuilder(context, AppDatabase.class, "smart-move").build();
        return instance;
    }

}
