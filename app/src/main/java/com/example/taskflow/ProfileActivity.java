package com.example.taskflow;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    private TextView emailView, nameView, joinDateView;
    private Button changePasswordButton, backToHomeButton;
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

        DataBase = new DataBase(this);

        // Retrieve email from intent
        String email = getIntent().getStringExtra("email");

        if (email != null) {
            // Fetch user details
            String userName = DataBase.getUserNameByEmail(email);
            // Placeholder join date - update if join date is stored in the database
            String joinDate = "January 1, 2023";

            // Display user details
            emailView.setText(email);
            nameView.setText(userName != null ? userName : "User");
            joinDateView.setText(joinDate);
        }

        // Navigate to Change Password Activity
        changePasswordButton.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
        });

        // Navigate back to Home Activity
        backToHomeButton.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
            finish();
        });
    }
}
