package pt.ua.hackaton.smartmove.utils;

import android.content.Context;
import android.content.SharedPreferences;

import pt.ua.hackaton.smartmove.R;

public class SharedPreferencesHandler {

    private final SharedPreferences sharedPreferences;

    public SharedPreferencesHandler(Context context) {
        this.sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
    }

    public void setPreferenceString(String key, String value) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();

    }

    public String getPreferenceString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

}
