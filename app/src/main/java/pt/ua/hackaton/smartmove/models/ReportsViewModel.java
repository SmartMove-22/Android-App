package pt.ua.hackaton.smartmove.models;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.List;

import pt.ua.hackaton.smartmove.data.Exercise;
import pt.ua.hackaton.smartmove.data.Report;
import pt.ua.hackaton.smartmove.utils.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportsViewModel extends ViewModel {

    private MutableLiveData<String> timestamp;
    private MutableLiveData<Report> report;

    private SharedPreferences sharedPreferences;

    public void setSharedPreferences(SharedPreferences prefs) {
        sharedPreferences = prefs;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp.setValue(timestamp);
    }

    public LiveData<String> getTimestamp() {
        if (timestamp == null) {
            timestamp = new MutableLiveData<>();
        }
        return timestamp;
    }

    public LiveData<Report> getReport() {
        if (report == null) {
            report = new MutableLiveData<Report>();
            loadReport();
        }
        return report;
    }

    public void fetchReport() {
        loadReport();
    }

    private void loadReport() {
        String auth_token = sharedPreferences.getString("token", "");
        ApiUtils.getReportForDay(auth_token, timestamp.getValue()).enqueue(new Callback<Report>() {
            @Override
            public void onResponse(Call<Report> call, Response<Report> response) {
                report.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Report> call, Throwable t) {
                // uhhhhh...
            }
        });
    }

}
