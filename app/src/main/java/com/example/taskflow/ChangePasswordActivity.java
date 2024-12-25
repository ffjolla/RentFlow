package com.example.taskflow;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText oldPasswordEdit, newPasswordEdit, confirmNewPasswordEdit;
    private Button updatePasswordButton, backToHomeButton;
    DataBase DataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // Initialize UI elements
        oldPasswordEdit = findViewById(R.id.oldPassword);
        newPasswordEdit = findViewById(R.id.newPassword);
        confirmNewPasswordEdit = findViewById(R.id.confirmNewPassword);
        updatePasswordButton = findViewById(R.id.updatePasswordButton);
        backToHomeButton = findViewById(R.id.backToHomeButton);

        DataBase = new DataBase(this);

        // Get the user email from intent
        String email = getIntent().getStringExtra("email");
        if (email == null) {
            Toast.makeText(this, "Invalid user session", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Update Password Button Listener
        updatePasswordButton.setOnClickListener(view -> {
            String oldPassword = oldPasswordEdit.getText().toString();
            String newPassword = newPasswordEdit.getText().toString();
            String confirmNewPassword = confirmNewPasswordEdit.getText().toString();

            // Validate inputs
            if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if new password matches confirm password
            if (!newPassword.equals(confirmNewPassword)) {
                Toast.makeText(this, "New Password and Confirm Password do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate old password and update new password
            if (DataBase.validateUser(email, oldPassword)) {
                if (DataBase.updatePassword(email, newPassword)) {
                    Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close activity
                } else {
                    Toast.makeText(this, "Failed to update password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Incorrect old password", Toast.LENGTH_SHORT).show();
            }
        });

        // Back to Home Button Listener
        backToHomeButton.setOnClickListener(view -> {
            Intent intent = new Intent(ChangePasswordActivity.this, HomeActivity.class);
            intent.putExtra("email", email); // Pass user email to HomeActivity
            startActivity(intent);
            finish();
        });
    }
}
