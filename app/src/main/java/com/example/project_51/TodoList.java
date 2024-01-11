package com.example.project_51;

import android.os.Parcel;
import android.os.Parcelable;

// Class representing a TodoList item, implementing Parcelable for passing between activities
public class TodoList implements Parcelable {

    // Member variables
    private int id;
    private String name;

    // Constructor for creating TodoList from a Parcel
    public TodoList(Parcel in){
        id = in.readInt();
        name = in.readString();
    }

    // Default constructor
    public TodoList(){

    }

    // Getter method for TodoList ID
    public int getId() {
        return id;
    }

    // Setter method for TodoList ID
    public void setId(int id) {
        this.id = id;
    }

    // Getter method for TodoList name
    public String getName() {
        return name;
    }

    // Setter method for TodoList name
    public void setName(String name) {
        this.name = name;
    }

    // Parcelable implementation - write object values to the Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }

    // Parcelable implementation - describe the contents of the object
    @Override
    public int describeContents() {
        return 0;
    }

    // Parcelable implementation - create a TodoList object from a Parcel
    public static final Creator<TodoList> CREATOR = new Creator<TodoList>() {
        @Override
        public TodoList createFromParcel(Parcel in) {
            return new TodoList(in);
        }

        @Override
        public TodoList[] newArray(int size) {
            return new TodoList[size];
        }
    };
}
