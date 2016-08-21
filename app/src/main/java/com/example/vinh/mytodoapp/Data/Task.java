package com.example.vinh.mytodoapp.Data;

/**
 * Created by Vinh on 8/21/2016.
 */
public class Task {
    public int id;

    public String name;

    public int priority;

    public Date date;

    public Task(int id, String name, int priority, Date date) {
        this.id = id;
        this.name = name;
        this.priority = priority;
        this.date = date;
    }
}
