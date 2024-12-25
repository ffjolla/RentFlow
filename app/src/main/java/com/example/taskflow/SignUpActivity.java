package com.example.taskflow;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private EditText nameFieldEdit, surnameFieldEdit, emailFieldEdit, passwordFieldEdit, confirmPasswordFieldEdit;
    DataBase DataBase;
    private Button signUpButtonEdit;
    private Button loginRedirectEdit;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        nameFieldEdit = findViewById(R.id.nameField);
        surnameFieldEdit = findViewById(R.id.surnameField);
        emailFieldEdit = findViewById(R.id.emailField);
        passwordFieldEdit = findViewById(R.id.passwordField);
        confirmPasswordFieldEdit = findViewById(R.id.confirmPasswordField);
        loginRedirectEdit = findViewById(R.id.loginRedirect);
        signUpButtonEdit=findViewById(R.id.signUpButton);


        DataBase =new DataBase(this);

        signUpButtonEdit.setOnClickListener(v -> {
            if (validateFields()) {
                String name = nameFieldEdit.getText().toString().trim();
                String email = emailFieldEdit.getText().toString().trim();
                String password = passwordFieldEdit.getText().toString().trim();
                String surname = surnameFieldEdit.getText().toString().trim();

                if (DataBase.checkAdminEmail(email)) {
                    Toast.makeText(this, "User already exists.", Toast.LENGTH_SHORT).show();
                } else if (DataBase.insertAdminUser(name, surname, email, password)) {
                    String verificationCode = generateRandomCode();
                    MailSend.sendEmail(email, verificationCode);

                    Intent intent = new Intent(SignUpActivity.this, VerificationActivity.class);
                    intent.putExtra("email", email);
                    intent.putExtra("code", verificationCode);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Failed to register user.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        loginRedirectEdit.setOnClickListener(view->{
            Intent intent=new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private String generateRandomCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(999999));
    }


    private boolean validateFields() {

        String name = nameFieldEdit.getText().toString().trim();
        String surname = surnameFieldEdit.getText().toString().trim();
        String email = emailFieldEdit.getText().toString().trim();
        String password = passwordFieldEdit.getText().toString().trim();
        String confirmPassword = confirmPasswordFieldEdit.getText().toString().trim();

        if (TextUtils.isEmpty(name) || !name.matches("[a-zA-Z]+")) {
            nameFieldEdit.setError("Name must contain only letters and not be empty.");
            return false;
        }


        if (TextUtils.isEmpty(surname) || !surname.matches("[a-zA-Z]+")) {
            surnameFieldEdit.setError("Surname must contain only letters and not be empty.");
            return false;
        }

        if (TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailFieldEdit.setError("Enter a valid email address.");
            return false;
        }

        // 1 lowercase, 1 uppercase, 1 digit, 1 special character, 6-20 characters, no spaces
        if (TextUtils.isEmpty(password) ||
                !Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=]).{6,20}$").matcher(password).matches()) {
            passwordFieldEdit.setError("Password must contain 1 lowercase, 1 uppercase, 1 digit, 1 special character, and be 6-20 characters long.");
            return false;
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            confirmPasswordFieldEdit.setError("Please confirm your password.");
            return false;
        } else if (!password.equals(confirmPassword)) {
            confirmPasswordFieldEdit.setError("Passwords do not match.");
            return false;
        }

        return true;
    }
}
