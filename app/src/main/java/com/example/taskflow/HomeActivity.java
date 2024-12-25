package com.example.taskflow;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    private TextView usersNameEdit;
    private Button profileButton, changePasswordButton, logOutButton, taskManagerButton, browseCarsButton, viewBookingsButton;
    private DataBase DataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize UI elements
        usersNameEdit = findViewById(R.id.welcomeText);
        profileButton = findViewById(R.id.profileButton);
        changePasswordButton = findViewById(R.id.changePasswordButton);
        logOutButton = findViewById(R.id.logoutButton);
        taskManagerButton = findViewById(R.id.taskManagerButton);
        browseCarsButton = findViewById(R.id.browseCarsButton);
        viewBookingsButton = findViewById(R.id.viewBookingsButton);

        // Initialize database instance
        DataBase = new DataBase(this);

        // Get the user email passed from the previous activity
        String email = getIntent().getStringExtra("email");

        // Fetch user name from the database
        String userName = (email != null) ? DataBase.getUserNameByEmail(email) : null;

        if (userName != null) {
            usersNameEdit.setText("Welcome, " + userName);
        } else {
            usersNameEdit.setText("Welcome, User");
            Log.w("HomeActivity", "User name not found for email: " + email);
        }

        // Add sample cars to the database (optional: only call this once to avoid duplicates)
        addSampleCars();

        // Navigate to Profile Activity
        profileButton.setOnClickListener(view -> navigateToActivity(ProfileActivity.class, email));

        // Navigate to Change Password Activity
        changePasswordButton.setOnClickListener(view -> navigateToActivity(ChangePasswordActivity.class, email));

        // Navigate to Task Manager Activity
        taskManagerButton.setOnClickListener(view -> navigateToActivity(ToDoActivity.class, email));

        // Navigate to Browse Cars Activity
        browseCarsButton.setOnClickListener(view -> navigateToActivity(BrowseCarsActivity.class, email));

        // Navigate to View Bookings Activity
        viewBookingsButton.setOnClickListener(view -> navigateToActivity(ViewBookingsActivity.class, email));

        // Log out and navigate to Login Activity
        logOutButton.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close HomeActivity
        });
    }

    // Navigate to a specific activity
    private void navigateToActivity(Class<?> targetActivity, String email) {
        if (email != null) {
            Intent intent = new Intent(HomeActivity.this, targetActivity);
            intent.putExtra("email", email);
            startActivity(intent);
        } else {
            Log.e("HomeActivity", "Email is null. Cannot navigate to " + targetActivity.getSimpleName());
            Toast.makeText(this, "Error: Cannot navigate without email.", Toast.LENGTH_SHORT).show();
        }
    }

    private void addSampleCars() {
        // Check if each car already exists before inserting
        if (!isCarInDatabase("Audi A4")) {
            DataBase.insertCar("Audi A4", 50.0, 4, "Petrol", "Automatic", "audi_a4", true);
        }
        if (!isCarInDatabase("BMW X5")) {
            DataBase.insertCar("BMW X5", 70.0, 5, "Diesel", "Automatic", "bmw_x5", true);
        }
        if (!isCarInDatabase("Tesla Model 3")) {
            DataBase.insertCar("Tesla Model 3", 100.0, 4, "Electric", "Automatic", "tesla_model_3", true);
        }
        if (!isCarInDatabase("Audi RS5")) {
            DataBase.insertCar("Audi RS5", 90.0, 4, "Petrol", "Automatic", "audi_rs5", true);
        }
        if (!isCarInDatabase("Porsche Panamera")) {
            DataBase.insertCar("Porsche Panamera", 120.0, 4, "Petrol", "Automatic", "panamera", true);
        }
        if (!isCarInDatabase("Seat Leon")) {
            DataBase.insertCar("Seat Leon", 40.0, 5, "Diesel", "Manual", "seat_leon", true);
        }
        if (!isCarInDatabase("Mercedes CLS 63")) {
            DataBase.insertCar("Mercedes CLS 63", 200.0, 5, "Petrol", "Automatic", "cls_63", true);
        }
        if (!isCarInDatabase("Volkswagen Golf 7 GTD")) {
            DataBase.insertCar("Volkswagen Golf 7 GTD", 35.0, 5, "Diesel", "Automatic", "golf_7_gtd", true);
        }

        Toast.makeText(this, "Sample cars added to the database!", Toast.LENGTH_SHORT).show();
    }

    // Helper method to check if a car is already in the database
    private boolean isCarInDatabase(String carName) {
        // Query the database to see if the car exists
        SQLiteDatabase db = DataBase.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM cars WHERE name=?", new String[]{carName});
        boolean exists = false;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                exists = cursor.getInt(0) > 0; // Check if count > 0
            }
            cursor.close();
        }
        return exists;
    }

}
