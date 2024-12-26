package com.example.taskflow;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    private TextView emailView, nameView, joinDateView;
    private Button changePasswordButton, backToHomeButton;
    private LinearLayout profileHeader, profileDetails;
    DataBase DataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize views
        emailView = findViewById(R.id.emailView);
        nameView = findViewById(R.id.nameView);
        joinDateView = findViewById(R.id.joinDateView);
        changePasswordButton = findViewById(R.id.changePasswordButton);
        backToHomeButton = findViewById(R.id.backToHomeButton);
        profileHeader = findViewById(R.id.profileHeader);
        profileDetails = findViewById(R.id.profileDetails);

        DataBase = new DataBase(this);

        // Retrieve email from intent
        String email = getIntent().getStringExtra("email");

        if (email != null) {
            // Fetch user details
            String userName = DataBase.getUserNameByEmail(email);
            // Placeholder join date - update if join date is stored in the database
            String joinDate = "December 25, 2024";

            // Display user details
            emailView.setText(email);
            nameView.setText(userName != null ? userName : "User");
            joinDateView.setText(joinDate);
        }

        // Add animations
        applyFadeInAnimation(profileHeader);
        applyFadeInAnimation(profileDetails);

        // Navigate to Change Password Activity
        changePasswordButton.setOnClickListener(view -> {
            animateButtonPress(changePasswordButton);
            Intent intent = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
        });

        // Navigate back to Home Activity
        backToHomeButton.setOnClickListener(view -> {
            animateButtonPress(backToHomeButton);
            Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
            finish(); // Trigger custom transition
        });
    }

    private void applyFadeInAnimation(LinearLayout layout) {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new AccelerateDecelerateInterpolator());
        fadeIn.setDuration(800);
        layout.startAnimation(fadeIn);
    }

    private void animateButtonPress(Button button) {
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                button,
                android.animation.PropertyValuesHolder.ofFloat("scaleX", 0.9f),
                android.animation.PropertyValuesHolder.ofFloat("scaleY", 0.9f)
        );
        scaleDown.setDuration(200);
        scaleDown.setInterpolator(new AccelerateDecelerateInterpolator());

        scaleDown.setRepeatCount(1);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);

        scaleDown.start();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}
