package com.example.sl_project.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.*;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import com.example.expensetracker.R;
import com.example.sl_project.database.UserDatabaseHelper;
import com.example.sl_project.home.HomeActivity;

public class LoginActivity extends AppCompatActivity {

    Button loginButton;
    TextView signupRedirect, emailError, passwordError;
    EditText emailInput, passwordInput;
    UserDatabaseHelper dbHelper;

    boolean isEmailValid = false;
    boolean isPasswordValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        dbHelper = new UserDatabaseHelper(this); // Initialize DB

        loginButton = findViewById(R.id.loginBtn);
        signupRedirect = findViewById(R.id.signupText);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        emailError = findViewById(R.id.emailError);
        passwordError = findViewById(R.id.passwordError);

        //loginButton.setEnabled(false);

        emailInput.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String email = s.toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailError.setText("Invalid email format");
                    isEmailValid = false;
                } else {
                    emailError.setText("");
                    isEmailValid = true;
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        passwordInput.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() < 6) {
                    passwordError.setText("Password must be at least 6 characters");
                    isPasswordValid = false;
                } else {
                    passwordError.setText("");
                    isPasswordValid = true;
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        loginButton.setOnClickListener(view -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all details", Toast.LENGTH_SHORT).show();
            } else if (!isEmailValid || !isPasswordValid) {
                Toast.makeText(this, "Please fix input errors", Toast.LENGTH_SHORT).show();
            } else {
                boolean loginSuccess = dbHelper.validateLogin(email, password);
                if (loginSuccess) {
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                     startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                     finish();
                } else {
                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signupRedirect.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });
    }
}
