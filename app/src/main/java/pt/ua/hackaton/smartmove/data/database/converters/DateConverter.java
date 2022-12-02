package pt.ua.hackaton.smartmove.data.database.converters;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConverter {

    @TypeConverter
    public static Date toDate(Long dateLong){
        return dateLong == null ? null: new Date(dateLong*1000);
    }

    @TypeConverter
    public static Long fromDate(Date date){
        return date == null ? null : date.getTime()/1000;
    }

}
