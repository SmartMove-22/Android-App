package pt.ua.hackaton.smartmove;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import pt.ua.hackaton.smartmove.utils.ApiUtils;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Hide the top bar
        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().hide();
        }

        ImageView imageView = (ImageView)findViewById(R.id.loginMainCardImageView);
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

            login(username, password);
        });


    }

    private void login(final String username, final String password) {
        if (ApiUtils.authenticate(username, password)) {

            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();

            Intent myIntent = new Intent(this, MainActivity.class);
            this.startActivity(myIntent);
            finish();

        } else {
            Toast.makeText(this, "Wrong Credentials!", Toast.LENGTH_SHORT).show();
        }
    }



}