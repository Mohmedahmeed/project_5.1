package com.example.project_51;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.PublicKey;
import java.util.ArrayList;

public class DatabaseManager extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "TodoListApp.db";
    private static final int DATABASE_VERSION = 2;

    // Constructor
    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create tables when the database is created
    @Override
    public void onCreate(SQLiteDatabase db) {
        String strSql = "create table TodoList ("
                + "id integer primary key autoincrement,"
                + "name text not null"
                + ")";
        db.execSQL(strSql);

        String strSql2 = "create table Todo ("
                + "id integer primary key autoincrement,"
                + "name text not null,"
                + "isDone integer not null,"  // Use integer for boolean (0 for false, 1 for true)
                + "idTodoList int not null"
                + ")";
        db.execSQL(strSql2);
    }

    // Handle database upgrades
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrade
        String strSql = "DROP TABLE Todo";
        db.execSQL(strSql);

        String strSql2 = "create table Todo ("
                + "id integer primary key autoincrement,"
                + "name text not null,"
                + "isDone integer not null,"  // Use integer for boolean (0 for false, 1 for true)
                + "idTodoList int not null"
                + ")";
        db.execSQL(strSql2);
    }

    // Get all TodoLists from the database
    public ArrayList<TodoList> getAllTodoLists() {
        ArrayList<TodoList> listTodoLists = new ArrayList<>();

        String strSql = "select * from TodoList";
        Cursor cursor = this.getWritableDatabase().rawQuery(strSql, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String todoListName = cursor.getString(cursor.getColumnIndex("name"));
            todoListName = todoListName.replace("((%))", "'");

            TodoList todoListObj = new TodoList();
            todoListObj.setId(id);
            todoListObj.setName(todoListName);

            listTodoLists.add(todoListObj);
        }

        cursor.close(); // Close the cursor after use

        return listTodoLists;
    }

    // Add a new TodoList to the database
    public long addTodoList(TodoList todoList) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", todoList.getName());
        return db.insert("TodoList", null, values);
    }

    // Insert a new TodoList with a given name
    public void insertTodoList(String todoListName) {
        String name = todoListName.replace("'", "((%))");
        String strSql = "INSERT INTO TodoList"
                + "(name) VALUES ('"
                + name + "')";
        this.getWritableDatabase().execSQL(strSql);
    }

    // Update the name of a TodoList
    public void updateTodoList(String newText, int idTodoList){
        newText = newText.replace("'", "((%))");
        String strSql ="UPDATE TodoList SET name = '" + newText + "' WHERE id =" + idTodoList;
        this.getWritableDatabase().execSQL(strSql);
    }

    // Delete multiple TodoLists along with associated Todos
    public void deleteTodoLists(ArrayList<TodoList> todoListArrayList){
        for (int i = 0; i< todoListArrayList.size(); i++){
            String strSql = "DELETE FROM TodoList WHERE id = " + todoListArrayList.get(i).getId();
            this.getWritableDatabase().execSQL(strSql);

            String strSql2= "DELETE FROM Todo WHERE idTodoList = " + todoListArrayList.get(i).getId();
            this.getWritableDatabase().execSQL(strSql2);
        }
    }

    // Get all Todos for a specific TodoList
    public ArrayList<Todo> getAllTodos(int todoListId){
        ArrayList<Todo> todos = new ArrayList<>();
        String strSql = "SELECT * FROM Todo WHERE idTodoList =" + todoListId;
        Cursor cursor = this.getWritableDatabase().rawQuery(strSql, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()){
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                boolean isDone = cursor.getInt(cursor.getColumnIndex("isDone")) > 0;

                Todo todo = new Todo();
                todo.setId(id);
                todo.setName(name);
                todo.isDone(isDone);

                todos.add(todo);
                cursor.moveToNext();
            }
        }
        cursor.close(); // Close the cursor after use
        return todos;
    }

    // Insert a new Todo with a given name and associated TodoList
    public void insertTodo(String todoName, int todoListId){
        todoName = todoName.replace("'", "((%))");
        String strSql = "INSERT INTO Todo(name , isDone , idTodoList) VALUES ('" + todoName + "'," + 0 +  "," + todoListId + ")";
        this.getWritableDatabase().execSQL(strSql);
    }

    // Update the status of a Todo
    public void updateTaskStatus(int newIsDone, int todoId){
        String strSql = "UPDATE Todo SET isDone=" + newIsDone + " WHERE id =" + todoId;
        this.getWritableDatabase().execSQL(strSql);
    }
}
