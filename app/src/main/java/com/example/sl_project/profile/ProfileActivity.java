package com.example.sl_project.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.expensetracker.R;
import com.example.sl_project.home.HomeActivity;
import com.example.sl_project.login.LoginActivity;
import com.example.sl_project.profile.PersonalInfoActivity;
import com.example.sl_project.stats.StatisticsActivity;
import com.example.sl_project.transactions.AddTransactions;
import com.example.sl_project.transactions.TransactionListActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import de.hdodenhof.circleimageview.CircleImageView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ProfileActivity extends AppCompatActivity {

    private ImageView backButton;
    private CircleImageView profileImage;
    private TextView username, email;
    private LinearLayout personalInfoLayout, changePasswordLayout, settingsLayout, inviteLayout;
    private LinearLayout contactUsLayout, logoutLayout;
    private BottomNavigationView bottomNavigation;
    private FloatingActionButton fabAddTransaction;
    private BottomNavigationView bottomNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        // Initialize UI components
        initViews();
        setupToolbar();
        loadUserData();
        setupClickListeners();
        setupBottomNavigation();
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        profileImage = findViewById(R.id.profileImage);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        personalInfoLayout = findViewById(R.id.personalInfoItem);
        changePasswordLayout = findViewById(R.id.changePasswordItem);
        settingsLayout = findViewById(R.id.settingsItem);
        inviteLayout = findViewById(R.id.inviteFriendsItem);
        contactUsLayout = findViewById(R.id.contactUsItem);
        logoutLayout = findViewById(R.id.logOutItem);
        bottomNav = findViewById(R.id.bottomNav);
    }

    private void setupToolbar() {
        backButton.setOnClickListener(v -> onBackPressed());
    }

    private void loadUserData() {
        // Load user profile image using Glide
        // Replace "profileImageUrl" with the actual URL from your user data
        String profileImageUrl = "https://example.com/profile_image.jpg";

        // Example with placeholder and error images
        Glide.with(this)
                .load(profileImageUrl)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.profile_placeholder)
                        .error(R.drawable.profile_placeholder))
                .into(profileImage);

        // Set username from user data
        // Replace with actual username from your user data
        username.setText("Naveena");
        email.setText("naveena@example.com");
    }

    private void setupClickListeners() {
        // Personal Information
        personalInfoLayout.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, PersonalInfoActivity.class);
            startActivity(intent);
        });

        // Change password
        changePasswordLayout.setOnClickListener(v -> showChangePasswordDialog());

        // Settings
//        settingsLayout.setOnClickListener(v -> {
//            Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
//            startActivity(intent);
//        });

        // Invite Friends
        inviteLayout.setOnClickListener(v -> shareInvite());

        // Contact Us
        contactUsLayout.setOnClickListener(v -> contactUs());

        // Logout
        logoutLayout.setOnClickListener(v -> logout());

        fabAddTransaction.setOnClickListener(v -> {
            // Open add transaction activity
            // startActivity(new Intent(StatisticsActivity.this, AddTransactionActivity.class));
        });
    }

    private void showChangePasswordDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.profile_change_password, null);

        new MaterialAlertDialogBuilder(this)
                .setTitle("Change Password")
                .setView(dialogView)
                .setPositiveButton("Change", (dialog, which) -> {
                    // Process password change
                    // In a real app, validate inputs and submit to backend
                    Toast.makeText(ProfileActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void shareInvite() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));

        String shareMessage = "Hey! Check out this awesome app: ";

        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        startActivity(Intent.createChooser(shareIntent, "Invite via"));
    }

    private void contactUs() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@example.com"});

        try {
            startActivity(Intent.createChooser(intent, "Contact Us via Email"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No email client found.", Toast.LENGTH_SHORT).show();
        }
    }

    private void logout() {
        // Show confirmation dialog
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    Toast.makeText(ProfileActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();

                    // Redirect to login
                    Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void setupBottomNavigation() {
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Intent home = new Intent(this, HomeActivity.class);
            Intent allTransactions = new Intent(this, TransactionListActivity.class);
            Intent addTransaction = new Intent(this, AddTransactions.class);
            Intent stats = new Intent(this, StatisticsActivity.class);
            Intent profile = new Intent(this, ProfileActivity.class);
            // Declare intent here

            if (itemId == R.id.nav_home) {
                startActivity(home);
                return true;
            }else if (itemId == R.id.nav_add) {
                startActivity(addTransaction);
                return true;
            } else if (itemId == R.id.nav_transactions) {
                startActivity(allTransactions);
                return true;
            } else if (itemId == R.id.nav_statistics) {
                startActivity(stats);
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(profile);
                return true;
            } else {
                return false; // Unknown item
            }

            //startActivity(intent); // Start the activity
            //return true;
        });
    }
}