package com.example.sl_project.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.expensetracker.R;
import com.example.sl_project.database.UserDatabaseHelper;
import com.example.sl_project.home.HomeActivity;

public class SignupActivity extends AppCompatActivity {

    EditText nameInput, emailInput, passwordInput;
    TextView nameError, emailError, passwordError;
    Button signupButton;
    TextView loginRedirect;
    UserDatabaseHelper dbHelper;

    boolean isNameValid = false, isEmailValid = false, isPasswordValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        dbHelper = new UserDatabaseHelper(this); // Initialize DB

        nameInput = findViewById(R.id.signupNameInput);
        emailInput = findViewById(R.id.signupEmailInput);
        passwordInput = findViewById(R.id.signupPasswordInput);

        nameError = findViewById(R.id.signupNameError);
        emailError = findViewById(R.id.signupEmailError);
        passwordError = findViewById(R.id.signupPasswordError);

        signupButton = findViewById(R.id.signupBtn);
        loginRedirect = findViewById(R.id.loginRedirectText);

        // Name Validation
        nameInput.addTextChangedListener(new TextWatcher() {
            @Override public void afterTextChanged(Editable s) {
                String name = s.toString().trim();
                if (name.isEmpty()) {
                    nameError.setText("Name is required");
                    isNameValid = false;
                } else if (!name.matches("^[a-zA-Z ]+$")) {
                    nameError.setText("Only letters and spaces allowed");
                    isNameValid = false;
                } else {
                    nameError.setText("");
                    isNameValid = true;
                }
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        // Email Validation
        emailInput.addTextChangedListener(new TextWatcher() {
            @Override public void afterTextChanged(Editable s) {
                if (!Patterns.EMAIL_ADDRESS.matcher(s.toString().trim()).matches()) {
                    emailError.setText("Enter a valid email address");
                    isEmailValid = false;
                } else {
                    emailError.setText("");
                    isEmailValid = true;
                }
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        // Password Validation
        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override public void afterTextChanged(Editable s) {
                if (s.length() < 6) {
                    passwordError.setText("Password must be at least 6 characters");
                    isPasswordValid = false;
                } else {
                    passwordError.setText("");
                    isPasswordValid = true;
                }
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        signupButton.setOnClickListener(view -> {
            String name = nameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            } else if (!isNameValid || !isEmailValid || !isPasswordValid) {
                Toast.makeText(this, "Please correct the errors above", Toast.LENGTH_SHORT).show();
            } else {
                if (dbHelper.checkUserExists(email)) {
                    Toast.makeText(this, "Email already registered", Toast.LENGTH_SHORT).show();
                } else {
                    boolean success = dbHelper.insertUser(name, email, password);
                    if (success) {
                        Toast.makeText(this, "Signup Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignupActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Signup Failed. Try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        loginRedirect.setOnClickListener(view ->
                startActivity(new Intent(SignupActivity.this, LoginActivity.class))
        );
    }
}
