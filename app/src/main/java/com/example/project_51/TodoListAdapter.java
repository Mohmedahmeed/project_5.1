package com.example.project_51;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class TodoListAdapter extends ArrayAdapter<TodoList> {

    // Member variables
    private ArrayList<TodoList> todoListArrayList;
    private DatabaseManager databaseManager;

    // Constructor
    public TodoListAdapter(Context context, ArrayList<TodoList> todoListArrayList) {
        super(context, 0, todoListArrayList);
        this.todoListArrayList = todoListArrayList;
        this.databaseManager = new DatabaseManager(context);
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    // Method to initialize the view for each item in the list
    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.todolist_list_view_row, parent, false
            );
        }

        // Find views in the layout
        TextView textViewTodoListName = convertView.findViewById(R.id.text_view_name_todolist_row);
        CheckBox checkBox = convertView.findViewById(R.id.checkbox_for_selected_items_todolist);
        checkBox.setTag(position);
        ImageButton editImageButton = convertView.findViewById(R.id.edit_name_todoList_row);
        ImageButton checkImageButton = convertView.findViewById(R.id.edit_text_name_todolist_row);
        EditText nameEditText = convertView.findViewById(R.id.edit_text_name_todolist_row);

        // Get the current TodoList from the adapter
        TodoList currentTodoList = getItem(position);

        // Set TodoList name to the TextView
        if (currentTodoList != null) {
            textViewTodoListName.setText(currentTodoList.getName());
        }

        // Set position as a tag for the checkbox
        checkBox.setTag(position);

        // Set visibility based on action mode status
        if (MainActivity.isActionMode) {
            checkBox.setVisibility(View.VISIBLE);
            editImageButton.setVisibility(View.VISIBLE);
        } else {
            checkBox.setVisibility(View.GONE);
            editImageButton.setVisibility(View.GONE);
        }

        // CheckBox listener for handling selection
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                int pos = (int) compoundButton.getTag();

                if (MainActivity.UserSelectionListTodosList.contains(todoListArrayList.get(pos))) {
                    MainActivity.UserSelectionListTodosList.remove(todoListArrayList.get(pos));
                } else {
                    MainActivity.UserSelectionListTodosList.add(todoListArrayList.get(pos));
                }
                MainActivity.actionModeListTodosLists.setTitle(MainActivity.UserSelectionListTodosList.size() + "todolistes selectionnées...");
            }
        });

        // Edit button click listener
        editImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox.setVisibility(View.GONE);
                editImageButton.setVisibility(View.GONE);
                textViewTodoListName.setVisibility(View.GONE);

                nameEditText.setVisibility(View.VISIBLE);
                nameEditText.setText(currentTodoList.getName());
                checkImageButton.setVisibility(View.VISIBLE);
            }
        });

        // Check button click listener for updating TodoList name
        checkImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setVisibility(View.VISIBLE);
                editImageButton.setVisibility(View.VISIBLE);
                textViewTodoListName.setVisibility(View.VISIBLE);

                String newText = nameEditText.getText().toString();
                nameEditText.setVisibility(View.GONE);
                checkImageButton.setVisibility(View.GONE);
                textViewTodoListName.setText(newText);

                // Update the TodoList name in the database
                databaseManager.updateTodoList(newText, currentTodoList.getId());
            }
        });

        return convertView;
    }

    // Method to update the list with a new filtered list
    public void updateList(ArrayList<TodoList> filteredList) {
        clear();
        addAll(filteredList);
        notifyDataSetChanged();
    }
}
