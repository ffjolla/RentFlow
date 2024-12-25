package com.example.taskflow;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class BrowseCarsActivity extends AppCompatActivity {
    private ListView carListView;
    private DataBase dataBase;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_cars);

        carListView = findViewById(R.id.carListView);
        dataBase = new DataBase(this);

        // Get the user email from the intent
        userEmail = getIntent().getStringExtra("email");

        if (userEmail == null) {
            Toast.makeText(this, "Error: User email not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Fetch available cars from the database
        ArrayList<Car> cars = dataBase.getAvailableCars();

        if (cars.isEmpty()) {
            Toast.makeText(this, "No cars available at the moment.", Toast.LENGTH_SHORT).show();
        } else {
            // Set up the adapter
            CarAdapter carAdapter = new CarAdapter(this, cars, car -> {
                // Handle car booking
                boolean success = dataBase.bookCar(userEmail, car.getName(), String.valueOf(System.currentTimeMillis()));
                if (success) {
                    Toast.makeText(this, "Car booked successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to book the car.", Toast.LENGTH_SHORT).show();
                }
            });
            carListView.setAdapter(carAdapter);
        }
    }
}
