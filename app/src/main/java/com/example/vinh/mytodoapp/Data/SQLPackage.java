package com.example.vinh.mytodoapp.Data;

/**
 * Created by Vinh on 8/27/2016.
 */
public class SQLPackage {

    public int id;
    public String name;
    public String priority;
    public int day;
    public int month;
    public int year;

    public SQLPackage(String name, String priority, int day, int month, int year) {
        this.name = name;
        this.priority = priority;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public SQLPackage() {

    }
}
