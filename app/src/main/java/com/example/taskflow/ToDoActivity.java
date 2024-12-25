package com.example.taskflow;

import static com.example.taskflow.R.*;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ToDoActivity extends AppCompatActivity {
    private EditText taskEditText;
    private Button addTaskButton, backToHomeButton;
    private RecyclerView tasksRecyclerView;
    private TaskAdapter taskAdapter;
    private ArrayList<String> tasks;
    private DataBase DataBase;
    private String email;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        // Initialize views
        taskEditText = findViewById(R.id.taskEditText); // Correct ID
        addTaskButton = findViewById(R.id.addTaskButton); // Correct ID
        backToHomeButton = findViewById(R.id.backToHomeButton); // Correct ID
        tasksRecyclerView = findViewById(R.id.tasksRecyclerView); // Correct ID

        // Initialize RecyclerView
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize other fields
        tasks = new ArrayList<>();
        DataBase = new DataBase(this);

        // Get email from intent
        email = getIntent().getStringExtra("email");
        if (email == null || email.isEmpty()) {
            Toast.makeText(this, "Invalid session, please log in again.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Load tasks from database
        tasks = DataBase.getTasks(email);

        // Setup RecyclerView Adapter
        taskAdapter = new TaskAdapter(this, tasks, DataBase, email);
        tasksRecyclerView.setAdapter(taskAdapter);

        // Add Task Button Listener
        addTaskButton.setOnClickListener(view -> {
            String task = taskEditText.getText().toString();
            if (!task.isEmpty()) {
                DataBase.insertTask(email, task); // Insert into database
                tasks.add(task); // Add to task list
                taskAdapter.notifyDataSetChanged(); // Notify adapter of changes
                taskEditText.setText(""); // Clear input field
                Toast.makeText(this, "Task added successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Task cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        // Back to Home Button Listener
        backToHomeButton.setOnClickListener(view -> {
            Intent intent = new Intent(ToDoActivity.this, HomeActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
            finish();
        });
    }
}
