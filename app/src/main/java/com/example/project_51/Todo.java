package com.example.project_51;

// Class representing a Todo item
public class Todo {

    // Member variables
    private int id;
    private String name;
    private boolean isDone;

    // Getter method for Todo ID
    public int getId() {
        return id;
    }

    // Setter method for Todo ID
    public void setId(int id) {
        this.id = id;
    }

    // Getter method for Todo name
    public String getName() {
        return name;
    }

    // Setter method for Todo name
    public void setName(String name) {
        this.name = name;
    }

    // Getter method for Todo completion status
    public boolean isDone() {
        return isDone;
    }

    // Setter method for Todo completion status
    public void setDone(boolean done) {
        isDone = done;
    }
}
