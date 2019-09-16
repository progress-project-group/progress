package com.progress_android.MonthlyPlan;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import Adapter.MonthlyPlanAdapter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.progress_android.MainActivity;
import com.progress_android.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EventAddActivity extends AppCompatActivity {
    private String title;
    private Date due_date;
    private String remark;
    private boolean type;
    private int progress;
    SimpleDateFormat simpleDateFormat;
    private TextInputEditText title_text, due_date_text, remark_text;
    private MaterialTextView progress_text;
    private RadioGroup type_radio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_add);
        Toolbar toolbar = findViewById(R.id.event_toolbar);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            new MaterialAlertDialogBuilder(EventAddActivity.this)
                    .setTitle("是否保存编辑信息")
                    .setPositiveButton("确定",(dialog, which) -> {
                        //待完成
                        finish();
                    })
                    .setNegativeButton("取消", (dialog, which) -> {
                        finish();
                    }).show();
        });
        initEditText();
        initRadioGroup();
        initProgressSlider();
        initEditButton();
    }

    private void initEditText(){
        title_text = findViewById(R.id.title);
        due_date_text = findViewById(R.id.due_date);
        remark_text = findViewById(R.id.remark);
        //为日期输入框添加日期选择器
        due_date_text.setOnTouchListener((v,event)->{
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                //关闭软键盘
                hideSoftInput();
                showDatePicker(due_date_text);
                return true;
            }
            return false;
        });
        due_date_text.setOnFocusChangeListener((v,hasFocus)->{
            if (hasFocus){
                //关闭软键盘
                hideSoftInput();
                showDatePicker(due_date_text);
            }
        });
    }
    private void initRadioGroup(){
        type_radio = findViewById(R.id.type_radio);
        type_radio.setOnCheckedChangeListener(((group, checkedId) -> {
            LinearLayout addition = findViewById(R.id.addition);
            if (checkedId == R.id.separable){
                addition.setVisibility(View.VISIBLE);
            }
            else {
                addition.setVisibility(View.GONE);
            }
        }));
    }
    private void initProgressSlider(){
        progress_text = findViewById(R.id.progress_text);
        SeekBar progress = findViewById(R.id.progress_slider);
        progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress_text.setText(String.valueOf(progress).concat("%"));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }
    private void initEditButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            title = title_text.getText().toString();
            try {
                due_date = simpleDateFormat.parse(due_date_text.getText().toString());
            } catch (ParseException e) {
                //改成其他提示
                Toast.makeText(EventAddActivity.this,"date wrong",Toast.LENGTH_LONG).show();
            }
            remark = remark_text.getText().toString();
            int selectedType = type_radio.getCheckedRadioButtonId();
            type = (selectedType == R.id.separable);
            String percent = progress_text.getText().toString();
            progress = type? Integer.parseInt(percent.substring(0,percent.length()-1)): 0;
            //返回给月计划一个行的计划
            MonthlyPlanAdapter.MonthlyCard newMonthlyCard = new MonthlyPlanAdapter.MonthlyCard
                    (title, due_date, remark, type, progress);
            Intent intent = new Intent();
            intent.putExtra("new_plan",newMonthlyCard);
            setResult(RESULT_OK,intent);
            finish();
        });
    }

    private void hideSoftInput(){
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus().getWindowToken() != null){
            manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    protected void showDatePicker(TextInputEditText date){
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(EventAddActivity.this,
                ((view, year, month, dayOfMonth) -> {
                    //月份少一，暂时先手动添加
                    month++;
                    date.setText(year + "-" + month + "-" + dayOfMonth);
                }), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}
