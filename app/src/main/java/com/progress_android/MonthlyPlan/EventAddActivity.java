package com.progress_android.MonthlyPlan;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.progress_android.MainActivity;
import com.progress_android.R;

import java.util.Calendar;
import java.util.Date;

public class EventAddActivity extends AppCompatActivity {

    TextInputEditText date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_add);
        Toolbar toolbar = findViewById(R.id.event_toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EventAddActivity.this,"back",Toast.LENGTH_LONG).show();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initEditText();
    }

    public void initEditText(){
        date = findViewById(R.id.due_date);
        date.setOnTouchListener((v,event)->{
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                //关闭软键盘
                hideSoftInput();
                showDatePicker();
                return true;
            }
            return false;
        });
        date.setOnFocusChangeListener((v,hasFocus)->{
            if (hasFocus){
                //关闭软键盘
                hideSoftInput();
                showDatePicker();
            }
        });
//        date.setOnClickListener(v -> {
//            Toast.makeText(EventAddActivity.this,"date",Toast.LENGTH_SHORT).show();
//        });
    }
    public void hideSoftInput(){
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus().getWindowToken() != null){
            manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    protected void showDatePicker(){
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(EventAddActivity.this,
                ((view, year, month, dayOfMonth) -> {
                    date.setText(year + "-" + month + "-" + dayOfMonth);
                }), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}
