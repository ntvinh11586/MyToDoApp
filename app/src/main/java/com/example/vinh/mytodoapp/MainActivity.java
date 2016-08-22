package com.example.vinh.mytodoapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.vinh.mytodoapp.Data.Date;
import com.example.vinh.mytodoapp.Data.Task;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TaskDialogFragment.TaskDialogListener {

    private TaskAdapter adapter;
    private ArrayList<Task> tasks;

    private int changedPosition;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tasks = new ArrayList<Task>();

        adapter = new TaskAdapter(this, tasks);

        listView = (ListView)findViewById(R.id.list_task_item);

        listView.setAdapter(adapter);

        readFile();

        // handle events
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long arg3) {
                tasks.remove(position);
                adapter.notifyDataSetChanged();

                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
                changedPosition = position;

                FragmentManager fm = getSupportFragmentManager();
                TaskDialogFragment editNameDialogFragment = TaskDialogFragment.newInstance(tasks.get(position));
                editNameDialogFragment.show(fm, "fragment_edit_name");
            }
        });
    }


    // ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_add:
                showEditDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // TaskDialog handle
    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        TaskDialogFragment editNameDialogFragment = TaskDialogFragment.newInstance(null);
        editNameDialogFragment.show(fm, "fragment_edit_name");
    }

    @Override
    public void onFinishTaskDialog(int isChanged, final int id, final String taskName, final String priority, final Date dueDate) {
        if (isChanged == 0) {
            Task newTask = new Task(id, taskName, priority, dueDate);
            adapter.add(newTask);
        } else {
            Task task = tasks.get(changedPosition);
            task.name = taskName;
            task.priority = priority;
            task.date = dueDate;

            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        writeFile();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        writeFile();
    }

    private void readFile() {
        try {
            BufferedReader bReader = new BufferedReader(new InputStreamReader(openFileInput("tester.txt")));
            String line;
            StringBuffer text = new StringBuffer();
            while ((line = bReader.readLine()) != null) {
                text.append(line);
            }

            String temp = text.toString();
            String[] seq = temp.split("-");

            int count = Integer.valueOf(seq[0]);

            for (int i = 0; i < count; i++) {
                adapter.add(new Task(1, seq[i*5 + 1], seq[i*5 + 2],
                        new Date(1, Integer.valueOf(seq[i*5 + 3]),
                                Integer.valueOf(seq[i*5 + 4]),
                                Integer.valueOf(seq[i*5 + 5]))));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeFile() {
        String someText;

        someText = Integer.toString(tasks.size());

        for (int i = 0; i < tasks.size(); i++) {
            someText += "-" + tasks.get(i).name;
            someText += "-" + tasks.get(i).priority;
            someText += "-" + Integer.toString(tasks.get(i).date.day);
            someText += "-" + Integer.toString(tasks.get(i).date.month);
            someText += "-" + Integer.toString(tasks.get(i).date.year);
        }

        String FILE_NAME = "tester.txt";
        try {
            FileOutputStream fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fos.write(someText.toString().getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
