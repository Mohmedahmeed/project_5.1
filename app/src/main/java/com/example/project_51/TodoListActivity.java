package com.example.project_51;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class TodoListActivity extends AppCompatActivity {

    // Member variables
    private TodoList todoList;
    private int todoListId;
    private DatabaseManager databaseManager;

    private ArrayList<Todo> todos;
    private ListView listViewTodos;

    private TextView todoListNameTextView;
    private Button addTodoBtn;
    private TodoAdapter adapter; // Corrected class name to TodoAdapter

    private ActivityResultLauncher<Intent> launchAddTodoActivity; // Corrected variable name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        // Find views by their IDs
        todoListNameTextView = findViewById(R.id.todoListNameTextView);
        listViewTodos = findViewById(R.id.listViewTodos);
        addTodoBtn = findViewById(R.id.addTodoButton);

        // Initialize DatabaseManager and ArrayList
        databaseManager = new DatabaseManager(getApplicationContext());
        todos = new ArrayList<>();

        // Get TodoList information from the Intent
        todoList = getIntent().getParcelableExtra("TodoList");
        todoListId = todoList.getId();

        // Set TodoList name to the TextView
        todoListNameTextView.setText(todoList.getName());

        // Initialize the display
        updateDisplay();

        // Register the result handler for AddTodoActivity
        launchAddTodoActivity = registerForActivityResult(
                new ActivityResultContract<Intent, Integer>() {
                    @Override
                    public Intent createIntent(Intent input) {
                        return input;
                    }

                    @Override
                    public Integer parseResult(int resultCode, Intent intent) {
                        if (resultCode == Activity.RESULT_OK) {
                            String message = intent.getStringExtra("RESULT_ADD_TODO");
                            updateDisplay();
                        }
                        return resultCode;
                    }
                }
        );

        // Set OnClickListener for the "Add Todo" button
        addTodoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startAddTodoActivity = new Intent(getApplicationContext(), AddTodoActivity.class);
                startAddTodoActivity.putExtra("TodoListId", todoListId);
                launchAddTodoActivity.launch(startAddTodoActivity);
            }
        });
    }

    // Update the display with the current list of Todos
    private void updateDisplay(){
        todos = databaseManager.getAllTodos(todoListId);
        adapter = new TodoAdapter(getApplicationContext(), todos);
        listViewTodos.setAdapter(adapter);
    }
}
