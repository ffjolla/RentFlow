package com.example.taskflow;

public class Car {
    private int id;
    private String name;
    private double pricePerHour;
    private int seats;
    private String fuelType;
    private String transmission;
    private String imagePath;
    private boolean available;

    public Car(int id, String name, double pricePerHour, int seats, String fuelType, String transmission, String imagePath, boolean available) {
        this.id = id;
        this.name = name;
        this.pricePerHour = pricePerHour;
        this.seats = seats;
        this.fuelType = fuelType;
        this.transmission = transmission;
        this.imagePath = imagePath;
        this.available = available;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
