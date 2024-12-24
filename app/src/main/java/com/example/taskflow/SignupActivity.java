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
public class SignupActivity extends AppCompatActivity {

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
        signUpButtonEdit = findViewById(R.id.signUpButton);


        DataBase = new DataBase(this);
    }
}