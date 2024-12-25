package com.example.taskflow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CarAdapter extends ArrayAdapter<Car> {
    private final Context context;
    private final ArrayList<Car> cars; // List of Car objects
    private final OnCarBookListener bookListener;

    public CarAdapter(Context context, ArrayList<Car> cars, OnCarBookListener bookListener) {
        super(context, R.layout.car_list_item, cars); // Initialize superclass with layout and data
        this.context = context;
        this.cars = cars;
        this.bookListener = bookListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.car_list_item, parent, false);
        }

        // Get the current Car object
        Car car = cars.get(position);

        // Bind UI elements
        TextView carName = convertView.findViewById(R.id.carName);
        TextView carPrice = convertView.findViewById(R.id.carPrice);
        TextView carSpecs = convertView.findViewById(R.id.carSpecs);
        ImageView carImage = convertView.findViewById(R.id.carImage);
        Button bookButton = convertView.findViewById(R.id.bookButton);

        // Set car details
        carName.setText(car.getName());
        carPrice.setText("$" + car.getPricePerHour() + "/hour");
        carSpecs.setText(car.getSeats() + " seats | " + car.getFuelType() + " | " + car.getTransmission());

        // Load car image dynamically or fallback to a default image
        if (car.getImagePath() != null && !car.getImagePath().isEmpty()) {
            // Use the imagePath to dynamically load the image from resources
            int resId = context.getResources().getIdentifier(car.getImagePath(), "drawable", context.getPackageName());
            if (resId != 0) {
                carImage.setImageResource(resId);
            } else {
                carImage.setImageResource(R.drawable.add_user); // Fallback if resource not found
            }
        } else {
            carImage.setImageResource(R.drawable.add_user); // Default image if imagePath is null
        }

        // Handle "Book Now" button click
        bookButton.setOnClickListener(v -> bookListener.onCarBook(car));

        return convertView;
    }

    // Interface to handle car booking actions
    public interface OnCarBookListener {
        void onCarBook(Car car);
    }
}
