package com.example.project_51;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddTodoListActivity extends AppCompatActivity {
    // Variable to store TodoList name
    private String TodoListName;

    // DatabaseManager instance
    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the layout for this activity
        setContentView(R.layout.activity_add_todo_list);

        // Initialize DatabaseManager
        databaseManager = new DatabaseManager(getApplicationContext());

        // Find Views in the layout
        Button confirmBtn = findViewById(R.id.confirmButton);
        EditText todoListNameEditText = findViewById(R.id.TodoListNameEditText);

        // Set OnClickListener for the Confirm button
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get TodoList name from the EditText
                TodoListName = todoListNameEditText.getText().toString();

                // Insert the TodoList into the database
                databaseManager.insertTodoList(TodoListName);

                // Create an Intent to start the MainActivity
                Intent startTodoListActivity = new Intent(getApplicationContext(), MainActivity.class);

                // Start the MainActivity
                startActivity(startTodoListActivity);
            }
        });
    }
}
