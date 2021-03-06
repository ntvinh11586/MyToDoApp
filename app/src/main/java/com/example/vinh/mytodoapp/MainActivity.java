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
import com.example.vinh.mytodoapp.Data.SQLPackage;
import com.example.vinh.mytodoapp.Data.Task;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TaskDialogFragment.TaskDialogListener {

    private TaskAdapter adapter;
    private ArrayList<Task> tasks;

    private int changedPosition;

    private ListView lvTasks;

    DatabaseHandler dbTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tasks = new ArrayList<Task> ();

        // get dbTasks
        dbTasks = new DatabaseHandler(this);

        ArrayList<SQLPackage> sqlPackages = dbTasks.getAllPackages();

        for (SQLPackage pk : sqlPackages) {
            tasks.add(new Task(pk.name, pk.priority,
                    new Date(pk.day, pk.month, pk.year)));
        }

        adapter = new TaskAdapter(this, tasks);

        lvTasks = (ListView)findViewById(R.id.list_task_item);

        lvTasks.setAdapter(adapter);

        // handle events
        lvTasks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long arg3) {
                tasks.remove(position);
                adapter.notifyDataSetChanged();

                return false;
            }
        });

        lvTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
    protected void onPause() {
        super.onPause();

        dbTasks.deleteAllPackages();

        for (Task task : tasks) {
            dbTasks.addPackage(new SQLPackage(task.name, task.priority,
                    task.date.day, task.date.month, task.date.year));
        }
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
    public void onFinishTaskDialog(int isChanged, final String taskName, final String priority, final Date dueDate) {
        if (isChanged == 0) {
            // add on app
            Task newTask = new Task(taskName, priority, dueDate);
            adapter.add(newTask);
        } else {
            // update on app
            Task task = tasks.get(changedPosition);
            task.name = taskName;
            task.priority = priority;
            task.date = dueDate;
            adapter.notifyDataSetChanged();
        }
    }
}
