package com.example.taskflow;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class VerificationActivity extends AppCompatActivity {

    private EditText verificationCodeEdit;
    private Button verifyButtonEdit;
    private Button resendButtonEdit;

    private String receivedCode;
    private String email;
    private boolean isCodeValid = true;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verification);

        verificationCodeEdit = findViewById(R.id.verificationCode);
        verifyButtonEdit = findViewById(R.id.verifyButton);
        resendButtonEdit = findViewById(R.id.resendButton);

        email = getIntent().getStringExtra("email");
        receivedCode = getIntent().getStringExtra("code");
        if (email == null || receivedCode == null) {
            Log.e("VerificationActivity", "Email or code is null!");
            return;
        }
        View rootView = findViewById(R.id.main);
        if (rootView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        } else {
            Toast.makeText(this, "Root layout not found! Check activity_verification.xml.", Toast.LENGTH_SHORT).show();
        }

        verifyButtonEdit.setOnClickListener(view -> verify());
        resendButtonEdit.setOnClickListener(view -> resendCode());
    }

    private void verify() {
        String enteredCode = verificationCodeEdit.getText().toString();

        if (enteredCode.equals(receivedCode) && isCodeValid) {
            Toast.makeText(this, "Verification successful!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(VerificationActivity.this, HomeActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
        } else {
            verifyButtonEdit.setEnabled(false);
            Toast.makeText(this, "Verification code expired or code not correct!", Toast.LENGTH_SHORT).show();
        }
    }

    private void resendCode() {
        String newCode = generateRandomCode();
        MailSend.sendEmail(email, newCode);
        receivedCode = newCode;

        Toast.makeText(this, "Verification code resent!", Toast.LENGTH_SHORT).show();
        verifyButtonEdit.setEnabled(true);
    }

    private String generateRandomCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(999999));
    }



}