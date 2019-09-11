package com.progress_android;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import static com.progress_android.DailyPlanActivity.TODAY;

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
                intent.putExtra("PLANID", TODAY);
                startActivity(intent);
            }
        });

        Button SummaryTestButton = (Button) findViewById(R.id.SummaryTestButton);
        SummaryTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,DailySummaryActivity.class);
                Log.d(TAG,"startSummaryTestActivity");
                startActivity(intent);
            }
        });
    }
}
