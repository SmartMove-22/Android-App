package pt.ua.hackaton.smartmove;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import pt.ua.hackaton.smartmove.data.database.AppDatabase;
import pt.ua.hackaton.smartmove.data.database.dao.UserDao;
import pt.ua.hackaton.smartmove.data.database.entities.UserEntity;
import pt.ua.hackaton.smartmove.utils.SharedPreferencesHandler;
import pt.ua.hackaton.smartmove.utils.UserType;

public class LoginActivity extends AppCompatActivity {

    UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Hide the top bar
        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().hide();
        }

        ImageView imageView = (ImageView)findViewById(R.id.exerciseCardImageView);
        imageView.setColorFilter(Color.GRAY, PorterDuff.Mode.DARKEN);

        findViewById(R.id.loginBtn).setOnClickListener(view -> {

            EditText usernameEditText = findViewById(R.id.editTextTextUsername);
            EditText passwordEditText = findViewById(R.id.editTextTextPassword);

            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (username.trim().isEmpty() || password.trim().isEmpty()) {
                Toast.makeText(
                        this,
                        "Please enter all credentials!",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            userDao = AppDatabase.getInstance(getApplicationContext()).userDao();

            AsyncTask.execute(() -> userDao.insertUser(new UserEntity("Hugo1307", "Hugo", "Gonçalves", "hugogoncalves13@ua.pt", "boatarde", 58d, 163, 21.4, 0)));

            login(username, password);
        });


    }

    private void login(final String username, final String password) {

        if (username.equals("Hugo1307") && password.equals("boatarde")) {

            Intent mainActivityIntent = new Intent(this, MainActivity.class);
            SharedPreferencesHandler sharedPreferencesHandler = new SharedPreferencesHandler(getApplicationContext());

            sharedPreferencesHandler.setPreferenceString(getString(R.string.username_preference), username);
            sharedPreferencesHandler.setPreferenceString(getString(R.string.user_type_preference), UserType.TRAINEE.name());

            startActivity(mainActivityIntent);
            finish();

        } else {
            Toast.makeText(this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
        }

    }



}