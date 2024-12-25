package com.example.taskflow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private Context context;
    private List<String> tasks;
    private DataBase DataBase;
    private String userEmail;

    // Constructor
    public TaskAdapter(Context context, List<String> tasks, DataBase DataBase, String userEmail) {
        this.context = context;
        this.tasks = tasks != null ? tasks : new ArrayList<>();
        this.DataBase = DataBase;
        this.userEmail = userEmail;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        String task = tasks.get(position);
        holder.taskText.setText(task);

        // Delete button functionality
        holder.deleteButton.setOnClickListener(view -> {
            DataBase.deleteTask(userEmail, task); // Delete from database
            tasks.remove(position); // Remove from list
            notifyItemRemoved(position); // Notify adapter
            notifyItemRangeChanged(position, tasks.size()); // Update list
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    // ViewHolder Class
    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskText;
        Button deleteButton;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskText = itemView.findViewById(R.id.taskText);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
