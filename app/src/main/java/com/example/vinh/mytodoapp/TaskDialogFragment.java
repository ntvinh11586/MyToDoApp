package com.example.vinh.mytodoapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.vinh.mytodoapp.Data.Date;
import com.example.vinh.mytodoapp.Data.Task;

import java.util.Calendar;

public class TaskDialogFragment extends DialogFragment {

    private EditText edTaskName;
    private LinearLayout llTaskDialog;
    private Button btnDiscard;
    private Button btnSave;
    private RadioGroup rgPriority;
    private DatePicker tpDueDate;
    private RadioButton rbPriorityLow;
    private Task changedTask;
    private RadioButton rbPriorityMedium;
    private RadioButton rbPriorityHigh;
    private int isChanged;

    public interface TaskDialogListener {
        void onFinishTaskDialog(int isChanged, final int id, final String taskName, final String priority, final Date dueDate);
    }

    public TaskDialogFragment() {

    }

    public static TaskDialogFragment newInstance(Task task) {
        TaskDialogFragment frag = new TaskDialogFragment();

        Bundle args = new Bundle();

        if (task != null) {
            args.putInt("available", 1);
            args.putInt("id", task.id);
            args.putString("name", task.name);
            args.putString("priority", task.priority);
            args.putInt("day", task.date.day);
            args.putInt("month", task.date.month);
            args.putInt("year", task.date.year);
        } else {
            args.putInt("avaiable", 0);
        }

        frag.setArguments(args);

        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return inflater.inflate(R.layout.fragment_task_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edTaskName = (EditText)view.findViewById(R.id.text_input_text);
        llTaskDialog = (LinearLayout)view.findViewById(R.id.layout_task_description);
        btnDiscard = (Button)view.findViewById(R.id.button_discard);
        btnSave = (Button)view.findViewById(R.id.button_save);
        rgPriority = (RadioGroup)view.findViewById(R.id.radio_group_priority_1);
        tpDueDate = (DatePicker)view.findViewById(R.id.date_picker_due_date);
        rbPriorityLow = (RadioButton)view.findViewById(R.id.radio_low);
        rbPriorityMedium = (RadioButton)view.findViewById(R.id.radio_medium);
        rbPriorityHigh = (RadioButton)view.findViewById(R.id.radio_high);

        isChanged = getArguments().getInt("available");

        if (isChanged == 1) {
            edTaskName.setText(getArguments().getString("name"));
            if (!edTaskName.getText().toString().equals(""))
                btnSave.setEnabled(true);

            switch (getArguments().getString("priority")) {
                case "Low":
                    rbPriorityLow.setChecked(true);
                    rbPriorityMedium.setChecked(false);
                    rbPriorityHigh.setChecked(false);
                    break;
                case "Medium":
                    rbPriorityLow.setChecked(false);
                    rbPriorityMedium.setChecked(true);
                    rbPriorityHigh.setChecked(false);
                    break;
                case "High":
                    rbPriorityLow.setChecked(false);
                    rbPriorityMedium.setChecked(false);
                    rbPriorityHigh.setChecked(true);
                    break;
            }

            int day = getArguments().getInt("day");
            int month = getArguments().getInt("month") - 1;
            int year = getArguments().getInt("year");

            tpDueDate.init(year, month, day, null);
        } else {
            tpDueDate.init(
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH) - 1,
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                    null);
        }

        edTaskName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edTaskName.getText().toString().equals(""))
                    btnSave.setEnabled(false);
                else
                    btnSave.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // handle events
        edTaskName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        llTaskDialog.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                return false;
            }
        });

        
        btnDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = rgPriority.getCheckedRadioButtonId();
                RadioButton rbPriority = (RadioButton)getView().findViewById(selectedId);
                String priority = rbPriority.getText().toString();

                Date dueDate = new Date(1, tpDueDate.getDayOfMonth(), tpDueDate.getMonth() + 1, tpDueDate.getYear());

                String taskName = edTaskName.getText().toString();

                TaskDialogListener listener = (TaskDialogListener) getActivity();
                listener.onFinishTaskDialog(isChanged, 1, taskName, priority, dueDate);

                getDialog().dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        super.onResume();
    }
}











