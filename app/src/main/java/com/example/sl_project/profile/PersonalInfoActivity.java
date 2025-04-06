package com.example.sl_project.profile;

import android.content.Intent;
import android.content.SharedPreferences;
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

    private SharedPreferences sharedPreferences;

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

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);

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

            // Save image URI to SharedPreferences
            sharedPreferences.edit().putString("profileImageUri", imageUri.toString()).apply();
        }
    }

    private void saveUserInfo() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String location = etLocation.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else {
            // Save user data to SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name", name);
            editor.putString("email", email);
            editor.putString("location", location);
            editor.apply();

            Toast.makeText(this, "Profile Updated!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadUserInfo() {
        String name = sharedPreferences.getString("name", "Naveena J");
        String email = sharedPreferences.getString("email", "naveena@gmail.com");
        String location = sharedPreferences.getString("location", "United States");
        String profileImageUri = sharedPreferences.getString("profileImageUri", null);

        etName.setText(name);
        etEmail.setText(email);
        etLocation.setText(location);

        if (profileImageUri != null) {
            profileImage.setImageURI(Uri.parse(profileImageUri));
        }
    }
}
