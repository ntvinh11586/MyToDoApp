package com.example.vinh.mytodoapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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
//                Toast.makeText(MainActivity.this, "hallo", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        TaskDialogFragment editNameDialogFragment = TaskDialogFragment.newInstance();
        editNameDialogFragment.show(fm, "fragment_edit_name");
    }
}
