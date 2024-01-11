// Define package and imports for necessary classes and interfaces
package com.example.project_51;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;

// Define the MainActivity class which extends AppCompatActivity for backward-compatible features on older Android versions
public class MainActivity extends AppCompatActivity {

    // Declare member variables for UI components and data management
    private ListView listViewTodoLists;
    private ArrayList<TodoList> listOfTodosLists;
    private ArrayList<TodoList> listOfTodosListsOriginal;
    private DatabaseManager databaseManager;
    private TodoListAdapter todoListAdapter;
    private Button addTodoListButton;
    private EditText searchBarListTodoLists;

    // Static variables to manage the state and selection during action mode
    public static boolean isActionMode = false;
    public static ArrayList<TodoList> UserSelectionListTodosList = new ArrayList<>(); // List of TodoList items to delete
    public static ActionMode actionModeListTodosLists = null;

    // onCreate method is called when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Set the content view to the layout for this activity

        // Initialize the database manager to interact with the SQLite database
        databaseManager = new DatabaseManager(getApplicationContext());

        // Fetch all TodoLists from the database
        listOfTodosLists = databaseManager.getAllTodoLists();

        // Make a copy of the fetched TodoLists for filtering during a search
        listOfTodosListsOriginal = new ArrayList<>(listOfTodosLists);

        // Link UI components to their respective views in the layout
        listViewTodoLists = findViewById(R.id.listViewTodos);
        addTodoListButton = findViewById(R.id.addTodoListButton);
        searchBarListTodoLists = findViewById(R.id.search_bar_list_todolists);

        // Set up the list view for multiple modal selection
        listViewTodoLists.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        // Attach a listener for multi-choice mode on the list view
        listViewTodoLists.setMultiChoiceModeListener(modeListener);

        // Instantiate and set the adapter for the list view using the TodoList data
        todoListAdapter = new TodoListAdapter(this, listOfTodosLists);
        listViewTodoLists.setAdapter(todoListAdapter);

        // Set up the onClickListener for the addTodoList button
        addTodoListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an intent to start the AddTodoListActivity
                Intent startActivityAddTodoList = new Intent(getApplicationContext(), AddTodoListActivity.class);
                // Start the new activity
                startActivity(startActivityAddTodoList);
            }
        });

        // Initialize the search bar functionality
        initSearchBar();
    }

    // Sets up the TextWatcher which handles live search filtering on the TodoList
    private void initSearchBar() {
        searchBarListTodoLists.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action taken before the text changes
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Filter the list with the current text in the search bar
                listOfTodosLists = filterArrayListTodoList(s.toString());
                // Update the adapter and list view with the filtered list
                todoListAdapter = new TodoListAdapter(getApplicationContext(), listOfTodosLists);
                listViewTodoLists.setAdapter(todoListAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Once text has changed, update the adapter's list with the filtered results
                ArrayList<TodoList> filteredList = filterArrayListTodoList(s.toString());
                todoListAdapter.updateList(filteredList);
            }
        });

        listViewTodoLists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TodoList todoList = (TodoList) adapterView.getItemAtPosition(i);

                Intent startActivityTodoList = new Intent(getApplicationContext(), TodoListActivity.class);
                startActivityTodoList.putExtra("TodoList", todoList);
                startActivity(startActivityTodoList);
            }
        });
    }

    // Filters the TodoList based on a text filter, matching TodoList names
    private ArrayList<TodoList> filterArrayListTodoList(String textFilter) {
        ArrayList<TodoList> arrayListTodoListTemp = new ArrayList<>();

        // If filter is not null, loop through the original list and add matching items to the temporary list
        if (textFilter != null) {
            for (TodoList todoList : listOfTodosListsOriginal) {
                if (todoList.getName().toUpperCase().contains(textFilter.toUpperCase())) {
                    arrayListTodoListTemp.add(todoList);
                }
            }
        } else {
            // If filter is null, copy the original list to the temp list
            arrayListTodoListTemp = new ArrayList<>(listOfTodosListsOriginal);
        }
        return arrayListTodoListTemp;
    }

    // Define MultiChoiceModeListener to handle contextual actions in multi-choice mode
    AbsListView.MultiChoiceModeListener modeListener = new AbsListView.MultiChoiceModeListener() {
        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            // Method to handle the state change of items when they are checked or unchecked
        }

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            // Inflate the menu for the contextual action bar when action mode is created
            MenuInflater inflater = actionMode.getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);

            // Set the action mode state to true and store the action mode object
            isActionMode = true;
            actionModeListTodosLists = actionMode;

            return true; // Return true to create the action mode
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            // Here you can do something when action mode is prepared
            return false; // Return false if nothing is done
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            // Handle action item clicks in the contextual action bar
            switch (menuItem.getItemId()){
                case R.id.btn_delete_menu  :
                    // Delete the selected items from the database
                    databaseManager.deleteTodoLists(UserSelectionListTodosList);
                    // Update the list display after deletion
                    UpdateDisplayTodoList();
                    // Close the action mode
                    actionMode.finish();
                    return true;

                default:
                    return false; // If we got here, the user's action was not recognized. Invoke the superclass to handle it.


            return false; // If no case matches, return false
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            // When action mode is destroyed, reset state and clean up selections
            isActionMode = false;
            actionModeListTodosLists = null;
            UserSelectionListTodosList.clear();
            // Update the list view display
            UpdateDisplayTodoList();
        }
    };

    // Method to refresh the display of TodoLists after changes
    private void UpdateDisplayTodoList(){
        // Refresh the original and displayed lists from the database
        listOfTodosListsOriginal = databaseManager.getAllTodoLists();
        listOfTodosLists = databaseManager.getAllTodoLists();
        // Update the adapter and list view
        todoListAdapter = new TodoListAdapter(getApplicationContext(),listOfTodosLists);
        listViewTodoLists.setAdapter(todoListAdapter );
    }
}