package com.example.vinh.mytodoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.vinh.mytodoapp.Data.Task;

import java.util.ArrayList;

/**
 * Created by Vinh on 8/21/2016.
 */
public class TaskAdapter extends ArrayAdapter<Task> {
    public TaskAdapter(Context context, ArrayList<Task> tasks) {
        super(context, 0, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Task task = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_task, parent, false);

        TextView tvName = (TextView)convertView.findViewById(R.id.text_task_name);
        TextView tvDate = (TextView)convertView.findViewById(R.id.text_task_date);
        TextView tvPriority = (TextView)convertView.findViewById(R.id.text_task_priority);

        tvName.setText(task.name);
        tvDate.setText(Integer.toString(task.date.day)
                    + " " + Integer.toString(task.date.month)
                    + " " + Integer.toString(task.date.year));
        tvPriority.setText(task.priority);

        return convertView;
    }
}
