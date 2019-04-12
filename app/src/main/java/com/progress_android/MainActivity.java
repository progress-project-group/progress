package com.progress_android;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
                Log.d(TAG,"startActivity");
                startActivity(intent);
            }
        });
    }
}
