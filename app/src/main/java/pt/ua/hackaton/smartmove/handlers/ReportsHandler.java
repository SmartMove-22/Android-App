package pt.ua.hackaton.smartmove.handlers;

import java.util.HashMap;
import java.util.Map;

import pt.ua.hackaton.smartmove.data.Report;
import pt.ua.hackaton.smartmove.data.database.dao.ExerciseReportDao;
import pt.ua.hackaton.smartmove.viewmodels.ReportsViewModel;

public class ReportsHandler {

    private static ReportsHandler instance;

    private ExerciseReportDao exerciseReportDao;

    private ReportsHandler() { }

    public void setDataAccessObject(ExerciseReportDao exerciseReportDao) {
        this.exerciseReportDao = exerciseReportDao;
    }

    public void getReport(ExerciseReportDao exerciseReportDao, ReportsViewModel reportsViewModel, int reportDayIdx) {



    }

    public static ReportsHandler getInstance() {
        if (instance == null)
            instance = new ReportsHandler();
        return instance;
    }

}
