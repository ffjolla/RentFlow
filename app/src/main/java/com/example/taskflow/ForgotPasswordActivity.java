package com.example.taskflow;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailField, newPasswordField, confirmPasswordField;
    private Button sendCodeButton;
    private String generatedCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Initialize UI components
        emailField = findViewById(R.id.emailField);
        newPasswordField = findViewById(R.id.newPasswordField);
        confirmPasswordField = findViewById(R.id.confirmPasswordField);
        sendCodeButton = findViewById(R.id.sendCodeButton);

        // Set up button click listener
        sendCodeButton.setOnClickListener(view -> sendVerificationCode());
    }

    private void sendVerificationCode() {
        String email = emailField.getText().toString().trim();
        String newPassword = newPasswordField.getText().toString().trim();
        String confirmPassword = confirmPasswordField.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email is required!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Password fields cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate a verification code and send an email
        generatedCode = generateRandomCode();
        MailSend.sendEmail(email, generatedCode); // Assumes a MailSend utility is available

        Toast.makeText(this, "Verification code sent to your email!", Toast.LENGTH_SHORT).show();

        // Navigate to VerificationActivity
        Intent intent = new Intent(ForgotPasswordActivity.this, VerificationActivityForgetPWD.class);
        intent.putExtra("email", email);
        intent.putExtra("newPassword", newPassword);
        intent.putExtra("code", generatedCode);
        startActivity(intent);
        finish();
    }

    private String generateRandomCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }
}
