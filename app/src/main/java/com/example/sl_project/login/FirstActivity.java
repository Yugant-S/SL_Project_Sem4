package com.example.sl_project.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.expensetracker.R;

public class FirstActivity extends AppCompatActivity {

    Button loginButton;
    TextView signupLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}
