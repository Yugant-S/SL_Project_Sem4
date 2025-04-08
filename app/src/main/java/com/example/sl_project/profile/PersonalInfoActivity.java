package com.example.sl_project.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.expensetracker.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalInfoActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private CircleImageView profileImage;
    private EditText etName, etEmail, etLocation;
    private Button btnSave;
    private ImageView backButton;
    private TextView editPicture;

    // Consistent SharedPreferences keys
    private static final String PREF_NAME = "LoginPrefs";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_NAME = "userName";
    private static final String PROFILE_PREF_NAME = "UserProfile"; // For profile-specific data

    private SharedPreferences loginPrefs;
    private SharedPreferences profilePrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_personal_info);

        // Initialize UI components
        profileImage = findViewById(R.id.profileImage);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etLocation = findViewById(R.id.etLocation);
        btnSave = findViewById(R.id.btnSave);
        backButton = findViewById(R.id.backButton);
        editPicture = findViewById(R.id.editPicture);

        // Initialize both SharedPreferences - one for login and one for profile details
        loginPrefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        profilePrefs = getSharedPreferences(PROFILE_PREF_NAME, MODE_PRIVATE);

        // Load existing user data
        loadUserInfo();

        // Back Button Click
        backButton.setOnClickListener(v -> finish());

        // Edit Profile Picture Click
        editPicture.setOnClickListener(v -> openGallery());

        // Save Button Click
        btnSave.setOnClickListener(v -> saveUserInfo());
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            profileImage.setImageURI(imageUri);

            // Save image URI to profile SharedPreferences
            profilePrefs.edit().putString("profileImageUri", imageUri.toString()).apply();
        }
    }

    private void saveUserInfo() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String location = etLocation.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else {
            // Save user data to both SharedPreferences

            // Update login SharedPreferences
            SharedPreferences.Editor loginEditor = loginPrefs.edit();
            loginEditor.putString(KEY_USER_NAME, name);
            loginEditor.putString(KEY_USER_EMAIL, email);
            loginEditor.apply();

            // Update profile SharedPreferences
            SharedPreferences.Editor profileEditor = profilePrefs.edit();
            profileEditor.putString("name", name);
            profileEditor.putString("email", email);
            profileEditor.putString("location", location);
            profileEditor.apply();

            Toast.makeText(this, "Profile Updated!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadUserInfo() {
        // First try to get name and email from login preferences
        String name = loginPrefs.getString(KEY_USER_NAME, "");
        String email = loginPrefs.getString(KEY_USER_EMAIL, "");

        // If not found in login prefs, try profile prefs
        if (name.isEmpty()) {
            name = profilePrefs.getString("name", "User");
        }

        if (email.isEmpty()) {
            email = profilePrefs.getString("email", "user@example.com");
        }

        // Get location and image from profile prefs
        String location = profilePrefs.getString("location", "United States");
        String profileImageUri = profilePrefs.getString("profileImageUri", null);

        etName.setText(name);
        etEmail.setText(email);
        etLocation.setText(location);

        if (profileImageUri != null) {
            try {
                profileImage.setImageURI(Uri.parse(profileImageUri));
            } catch (Exception e) {
                // In case the saved URI is no longer valid
                profileImage.setImageResource(R.drawable.profile_placeholder);
            }
        }
    }
}