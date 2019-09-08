package com.progress_android;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.progress_android.LongTermSchedule.LongTermScheduleListActivity;
import com.progress_android.MonthlyPlan.MonthlyPlanActivity;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    String TAG = "main_activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");
        setContentView(R.layout.activity_main);
        Log.d(TAG,"setContentView");

        Button TimeLineTestButton = (Button) findViewById(R.id.TimeLineTestButton);
        TimeLineTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,TimelineActivity.class);
                Log.d(TAG,"startTimeLineActivity");
                startActivity(intent);
            }
        });

        Button EventListTestButton = (Button) findViewById(R.id.EventListTestButton);
        EventListTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,EventListActivity.class);
                Log.d(TAG,"startEventListActivity");
                startActivity(intent);
            }
        });

        Button FragmentTestButton = (Button) findViewById(R.id.FragmentTestButton);
        FragmentTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,DailyPlanActivity.class);
                Log.d(TAG,"startFragmentTestActivity");
                startActivity(intent);
            }
        });

        //月计划
        Button MonthlyPlanTestButton = (Button) findViewById(R.id.MonthlyPlanTestButton);
        FragmentTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,DailyPlanActivity.class);
                startActivity(intent);
            }
        });

        Button LongTermTestButton = (Button) findViewById(R.id.LongTermScheduleTestButton);
        LongTermTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LongTermScheduleListActivity.class);
                startActivity(intent);
            }
        });

        //月计划
        Button MonthlyPlanTestButton = (Button) findViewById(R.id.MonthlyPlanTestButton);
        MonthlyPlanTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MonthlyPlanActivity.class);
                startActivity(intent);
            }
        });
    }
}
