package com.example.sl_project.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.expensetracker.R;
import com.example.sl_project.home.HomeActivity;

public class FirstActivity extends AppCompatActivity {

    Button loginButton;
    TextView signupLink;

    // SharedPreferences constants
    private static final String PREF_NAME = "LoginPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // Check if user is already logged in
        if (sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)) {
            // User is already logged in, redirect to HomeActivity
            startActivity(new Intent(FirstActivity.this, HomeActivity.class));
            finish(); // Close this activity so user can't go back
            return; // Exit the method early
        }

        setContentView(R.layout.login_start); // XML: first page

        loginButton = findViewById(R.id.loginBtn);
        signupLink = findViewById(R.id.signupText);

        loginButton.setOnClickListener(view -> {
            Intent intent = new Intent(FirstActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        signupLink.setOnClickListener(view -> {
            Intent intent = new Intent(FirstActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }

    // Method to clear login state (for logout functionality)
    public static void clearLoginState(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}