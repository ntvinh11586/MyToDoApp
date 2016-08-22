package com.example.vinh.mytodoapp;

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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TaskDialogFragment.TaskDialogListener {

    private TaskAdapter adapter;

    private ArrayList<Task> tasks;

    private ListView listView;

    private int changedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tasks = new ArrayList<Task>();

        adapter = new TaskAdapter(this, tasks);

        listView = (ListView)findViewById(R.id.list_task_item);

        listView.setAdapter(adapter);

        Task newTask1 = new Task(1, "task 1", "Medium", new Date(7, 8, 6, 2016));
        Task newTask2 = new Task(1, "task 2", "High", new Date(7, 8, 6, 2016));

        adapter.add(newTask1);
        adapter.add(newTask2);


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
}
