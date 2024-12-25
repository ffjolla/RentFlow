package com.example.taskflow;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditField, passwordEditField;
    private Button loginButtonEdit, signUpButtonEdit;
    private TextView forgotPasswordText;
    DataBase DataBase;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI elements
        emailEditField = findViewById(R.id.emailField);
        passwordEditField = findViewById(R.id.passwordField);
        loginButtonEdit = findViewById(R.id.loginButton);
        signUpButtonEdit = findViewById(R.id.signUpButton);
        forgotPasswordText = findViewById(R.id.forgotPassword); // Forgot Password TextView
        DataBase = new DataBase(this);

        // Login button listener
        loginButtonEdit.setOnClickListener(view -> validateFields());

        // Sign Up button listener
        signUpButtonEdit.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        forgotPasswordText.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

    }

    private void validateFields() {
        String email = emailEditField.getText().toString().trim();
        String password = passwordEditField.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your email!", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password!", Toast.LENGTH_SHORT).show();
        } else {
            Boolean validateUser = DataBase.validateUser(email, password);
            if (validateUser) {
                // Navigate to verification or home activity
                sendVerificationCode(email);
            } else {
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendVerificationCode(String email) {
        String generatedCode = generateRandomCode();
        MailSend.sendEmail(email, generatedCode);

        Toast.makeText(LoginActivity.this, "Verification code sent!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(LoginActivity.this, VerificationActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("code", generatedCode);
        startActivity(intent);
    }

    private String generateRandomCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }
}
