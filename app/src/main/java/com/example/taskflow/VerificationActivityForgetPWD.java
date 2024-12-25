package com.example.taskflow;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class VerificationActivityForgetPWD extends AppCompatActivity {

    private EditText verificationCodeField;
    private Button verifyButton;
    private String email;
    private String newPassword;
    private String receivedCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification_forget);

        // Initialize UI components
        verificationCodeField = findViewById(R.id.verificationCodeField);
        verifyButton = findViewById(R.id.verifyButton);

        // Retrieve data from Intent
        email = getIntent().getStringExtra("email");
        newPassword = getIntent().getStringExtra("newPassword");
        receivedCode = getIntent().getStringExtra("code");

        Log.d("VerificationActivity", "Email: " + email);
        Log.d("VerificationActivity", "New Password: " + newPassword);
        Log.d("VerificationActivity", "Received Code: " + receivedCode);

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(receivedCode)) {
            Log.e("VerificationActivity", "Required data is missing!");
            Toast.makeText(this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Set up button click listener
        verifyButton.setOnClickListener(view -> verifyCode());
    }


    private void verifyCode() {
        String enteredCode = verificationCodeField.getText().toString().trim();

        if (enteredCode.equals(receivedCode)) {
            Log.d("VerificationActivity", "Verification code matched!");

            // Update the password in the database
            DataBase databaseInstance = new DataBase(this);
            Log.d("VerificationActivity", "Attempting to reset password for email: " + email);

            boolean isResetSuccessful = databaseInstance.resetPasswordForForgottenAccount(email, newPassword);

            if (isResetSuccessful) {
                Log.d("VerificationActivity", "Password reset successfully for email: " + email);
                Toast.makeText(this, "Password reset successful!", Toast.LENGTH_SHORT).show();

                // Navigate back to LoginActivity
                Intent intent = new Intent(VerificationActivityForgetPWD.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                Log.e("VerificationActivity", "Password reset failed for email: " + email);
                Toast.makeText(this, "Failed to reset password. Please try again.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("VerificationActivity", "Incorrect verification code entered: " + enteredCode);
            Toast.makeText(this, "Incorrect verification code!", Toast.LENGTH_SHORT).show();
        }
    }

}
