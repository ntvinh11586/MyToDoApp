package com.example.vinh.mytodoapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.vinh.mytodoapp.Data.Date;
import com.example.vinh.mytodoapp.Data.Task;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Task> tasks = new ArrayList<Task>();

        TaskAdapter adapter = new TaskAdapter(this, tasks);

        ListView listView = (ListView)findViewById(R.id.list_task_item);

        listView.setAdapter(adapter);

        Task newTask1 = new Task(1, "task 1", 0, new Date(7, 8, 6, 2016));
        Task newTask2 = new Task(1, "task 2", 1, new Date(7, 8, 6, 2016));

        adapter.add(newTask1);
        adapter.add(newTask2);
    }
}
