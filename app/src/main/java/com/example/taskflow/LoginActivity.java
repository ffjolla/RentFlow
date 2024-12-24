package com.example.taskflow;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditField;
    private EditText passwordEditField;
    private Button logingButtonEdit;
    private Button signUpButtonEdit;

    private String generatedCode;
    private boolean isCodeValid = false;
    DataBase DataBase;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        emailEditField = findViewById(R.id.emailField);
        passwordEditField = findViewById(R.id.passwordField);
        logingButtonEdit = findViewById(R.id.loginButton);
        signUpButtonEdit = findViewById(R.id.signUpButton);
        DataBase = new DataBase(this);

        View rootView = findViewById(R.id.main);
        if (rootView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        } else {
            Toast.makeText(this, "Root layout not found! Check activity_login.xml.", Toast.LENGTH_SHORT).show();
        }

        logingButtonEdit.setOnClickListener(view -> validateFields());
        signUpButtonEdit.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    private void validateFields() {
        String email = emailEditField.getText().toString();
        String password = passwordEditField.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your email!", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password!", Toast.LENGTH_SHORT).show();
        } else {
            if (DataBase == null) {
                Toast.makeText(this, "Database not initialized!", Toast.LENGTH_SHORT).show();
                return;
            }

            Boolean validateUser = DataBase.validateUser(email, password);
            if (validateUser) {
                sendVerificationCode(email);
            } else {
                Toast.makeText(this, "Wrong Credentials!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void sendVerificationCode(String email) {
        generatedCode = generateRandomCode();
        isCodeValid = true;

        MailSend.sendEmail(email, generatedCode);

        Toast.makeText(LoginActivity.this, "Verification code sent!", Toast.LENGTH_SHORT).show();


        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                isCodeValid = false;
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Code expired!", Toast.LENGTH_SHORT).show());
            }
        }, 20000);

        Intent intent = new Intent(LoginActivity.this, VerificationActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("code", generatedCode);

        startActivity(intent);


    }

    private String generateRandomCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(999999));
    }
}