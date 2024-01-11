package com.example.project_51;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddTodoActivity extends AppCompatActivity {

    // Variables to store Todo information
    private String TodoName;
    private DatabaseManager databaseManager;
    private int IdTodoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);

        // Initialize DatabaseManager
        databaseManager = new DatabaseManager(getApplicationContext());

        // Get TodoListId from the Intent
        IdTodoList = getIntent().getIntExtra("TodoListId", 0);

        // Find Views in the layout
        Button confirmBtn = findViewById(R.id.confirmAddTodoButton);
        EditText todoNameEditText = findViewById(R.id.TodoNameEditText);

        // Set OnClickListener for the Confirm button
        confirmBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // Get the Todo name from the EditText
                TodoName = todoNameEditText.getText().toString();

                // Insert the Todo into the database
                databaseManager.insertTodo(TodoName);

                // Create an Intent to pass the result back to the calling activity
                Intent intent = new Intent();
                intent.putExtra("RESULT_ADD_TODO", "OK");

                // Set the result code as RESULT_OK and pass the intent
                setResult(Activity.RESULT_OK, intent);

                // Finish the activity
                finish();
            }
        });
    }
}
