package com.example.project_51;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class TodoAdapter extends ArrayAdapter<Todo> {

    // ArrayAdapter for todos
    public ArrayAdapter<Todo> todos;

    // DatabaseManager instance
    public DatabaseManager databaseManager;

    // Constructor for TodoAdapter
    public TodoAdapter(@NonNull Context context, ArrayList<Todo> todos){
        super(context, 0, todos);
        this.todos = todos;
        this.databaseManager = new DatabaseManager(context);
    }

    // Override method to get the View for each item in the Adapter
    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    // Method to initialize the View for each item in the Adapter
    private View initView(int position, View convertView, ViewGroup parent) {
        // If the convertView is null, inflate the layout
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.todo_listview_row, parent, false
            );
        }

        // Find Views in the layout
        TextView textViewTodoName = convertView.findViewById(R.id.textview_name_todo_row);
        CheckBox checkBox = convertView.findViewById(R.id.checkbox_for_task_todo);

        // Get the current Todo item
        Todo currentTodo = getItem(position);

        // Set the Todo name to the TextView
        if (currentTodo != null) {
            textViewTodoName.setText(currentTodo.getName());
        }

        // Set the CheckBox state based on Todo's "done" status
        checkBox.setChecked(currentTodo.isDone());

        // Set a listener for CheckBox changes
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Update the task status in the database based on CheckBox state
                if (isChecked) {
                    databaseManager.updateTaskStatus(1, currentTodo.getId());
                } else {
                    databaseManager.updateTaskStatus(0, currentTodo.getId());
                }
            }
        });

        // Return the initialized View
        return convertView;
    }

    // Method to update the list with a new set of Todos
    public void updateList(ArrayList<TodoList> filteredList) {
        clear();
        addAll(filteredList);
        notifyDataSetChanged();
    }
}
