package com.example.taskflow;


import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ViewBookingsActivity extends AppCompatActivity {
    private ListView bookingsListView;
    private DataBase dataBase;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bookings);

        bookingsListView = findViewById(R.id.bookingsListView);
        dataBase = new DataBase(this);

        // Get the user email from the intent
        userEmail = getIntent().getStringExtra("email");
        if (userEmail == null) {
            Toast.makeText(this, "Error: User email not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Fetch bookings
        ArrayList<String> bookings = dataBase.getCarBookings(userEmail);

        if (bookings.isEmpty()) {
            Toast.makeText(this, "You have no bookings.", Toast.LENGTH_SHORT).show();
        } else {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bookings);
            bookingsListView.setAdapter(adapter);
        }
    }
}
