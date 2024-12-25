package com.example.taskflow;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;

public class DataBase extends SQLiteOpenHelper {
    public static final String DBNAME = "login.db";

    public DataBase(@Nullable Context context) {
        super(context, DBNAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            // Existing tables
            db.execSQL("CREATE TABLE adminUser (email TEXT PRIMARY KEY, password TEXT, name TEXT, surname TEXT)");
            db.execSQL("CREATE TABLE tasks (id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, task TEXT, FOREIGN KEY(email) REFERENCES adminUser(email))");

            // New tables for Rent a Car
            db.execSQL("CREATE TABLE cars (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price_per_hour REAL, seats INTEGER, fuel_type TEXT, transmission TEXT, image_path TEXT, available BOOLEAN)");
            db.execSQL("CREATE TABLE car_bookings (id INTEGER PRIMARY KEY AUTOINCREMENT, user_email TEXT, car_id INTEGER, booking_date TEXT, FOREIGN KEY(user_email) REFERENCES adminUser(email), FOREIGN KEY(car_id) REFERENCES cars(id))");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            if (oldVersion < 2) {
                // Add new tables for version 2
                db.execSQL("CREATE TABLE cars (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price_per_hour REAL, seats INTEGER, fuel_type TEXT, transmission TEXT, image_path TEXT, available BOOLEAN)");
                db.execSQL("CREATE TABLE car_bookings (id INTEGER PRIMARY KEY AUTOINCREMENT, user_email TEXT, car_id INTEGER, booking_date TEXT, FOREIGN KEY(user_email) REFERENCES adminUser(email), FOREIGN KEY(car_id) REFERENCES cars(id))");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Preserve existing methods (do not delete these)

    public String getUserNameByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT name, surname FROM adminUser WHERE email=?", new String[]{email});
            if (cursor != null && cursor.moveToFirst()) {
                String userName = cursor.getString(0) + " " + cursor.getString(1);
                return userName;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }

    public Boolean validateUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT password FROM adminUser WHERE email=?", new String[]{email});
            if (cursor != null && cursor.moveToFirst()) {
                String storedHashedPassword = cursor.getString(0);
                return BCrypt.checkpw(password, storedHashedPassword);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return false;
    }

    public Boolean insertAdminUser(String name, String surname, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            contentValues.put("name", name);
            contentValues.put("surname", surname);
            contentValues.put("email", email);
            contentValues.put("password", hashedPassword);
            long result = db.insert("adminUser", null, contentValues);
            return result != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean checkAdminEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM adminUser WHERE email=?", new String[]{email});
            return cursor.getCount() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return false;
    }

    public boolean updatePassword(String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            contentValues.put("password", hashedPassword);
            int rowsAffected = db.update("adminUser", contentValues, "email=?", new String[]{email});
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // New methods for Rent a Car

    public boolean insertCar(String name, double pricePerHour, int seats, String fuelType, String transmission, String imagePath, boolean available) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", name);
            contentValues.put("price_per_hour", pricePerHour);
            contentValues.put("seats", seats);
            contentValues.put("fuel_type", fuelType);
            contentValues.put("transmission", transmission);
            contentValues.put("image_path", imagePath);
            contentValues.put("available", available);
            long result = db.insert("cars", null, contentValues);
            return result != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<Car> getAvailableCars() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Car> cars = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM cars WHERE available=1", null);
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                double pricePerHour = cursor.getDouble(cursor.getColumnIndexOrThrow("price_per_hour"));
                int seats = cursor.getInt(cursor.getColumnIndexOrThrow("seats"));
                String fuelType = cursor.getString(cursor.getColumnIndexOrThrow("fuel_type"));
                String transmission = cursor.getString(cursor.getColumnIndexOrThrow("transmission"));
                String imagePath = cursor.getString(cursor.getColumnIndexOrThrow("image_path"));
                boolean available = cursor.getInt(cursor.getColumnIndexOrThrow("available")) == 1;

                cars.add(new Car(id, name, pricePerHour, seats, fuelType, transmission, imagePath, available));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return cars;
    }

    public boolean bookCar(String userEmail, String carName, String bookingDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT id FROM cars WHERE name=?", new String[]{carName});
            if (cursor != null && cursor.moveToFirst()) {
                int carId = cursor.getInt(0);
                ContentValues contentValues = new ContentValues();
                contentValues.put("user_email", userEmail);
                contentValues.put("car_id", carId);
                contentValues.put("booking_date", bookingDate);
                long result = db.insert("car_bookings", null, contentValues);
                if (result != -1) {
                    ContentValues updateValues = new ContentValues();
                    updateValues.put("available", false);
                    db.update("cars", updateValues, "id=?", new String[]{String.valueOf(carId)});
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return false;
    }

    public ArrayList<String> getCarBookings(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> bookings = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(
                    "SELECT c.name, cb.booking_date FROM car_bookings cb " +
                            "JOIN cars c ON cb.car_id = c.id WHERE cb.user_email=?", new String[]{userEmail});
            while (cursor.moveToNext()) {
                String carName = cursor.getString(0);
                String bookingDate = cursor.getString(1);
                bookings.add("Car: " + carName + " | Date: " + bookingDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return bookings;
    }
}
