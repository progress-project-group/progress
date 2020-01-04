package com.progress_android.activity_implement;

import Adapter.ImplementELAdapter;
import Adapter.ImplementTLAdapter;
import DataBase.DailyPlanDataBaseHelper;
import DataBase.DailyPlanDbOperator;
import DataBase.DailyPlanDbSchema;
import Item.DaliyPlan.EventItem;
import Item.DaliyPlan.ExecutedItem;
import Item.Item;
import Item.DaliyPlan.ExecutedItemList;
import Item.DaliyPlan.TimeLineItem;
import Item.Time.MyTime;
import Item.Time.Pomodoro;
import Item.Time.TimeAmount;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.progress_android.R;
import com.progress_android.activity_implement.util.CardScaleHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static Item.Item.CANCEL;
import static Item.Item.COMPLETED;
import static Item.Item.DOING;
import static Item.Item.PAUSE;
import static Item.Item.WAITING;

public class ImplementActivity extends AppCompatActivity {

    String TAG = "ImplementActivity";
    ExecutedItemList executedItemList;
    ExecutedItem currentItem;

    Button cancelButton;
    Button pauseButton;
    Button confirmButton;
    RecyclerView eventListRecyclerView;
    RecyclerView timeLineRecyclerView;
    ImplementELAdapter eventListAdapter;
    ImplementTLAdapter timeLineAdapter;
    List<ExecutedItem> eventList = new ArrayList<>();
    List<ExecutedItem> timeLine = new ArrayList<>();

    int timeLinePosition = 0;
    int eventPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_implement);
        cancelButton = findViewById(R.id.Implement_Cancel);
        pauseButton = findViewById(R.id.Implement_Pause);
        confirmButton = findViewById(R.id.Implement_Finish);
        eventListRecyclerView = findViewById(R.id.IEventListRecycleView);
        timeLineRecyclerView = findViewById(R.id.ITimeLineRecycleView);

        initData();
        //点击取消时更新eventList显示信息，更新时间轴记录信息，更新执行时间轴
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishMatter(CANCEL, true);
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishMatter(PAUSE, false);
            }
        });

        //开始按钮响应函数
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMatter(currentItem);
            }
        });

        eventListAdapter = new ImplementELAdapter(eventList, this);
        timeLineAdapter = new ImplementTLAdapter(timeLine, this);
        LinearLayoutManager l1 = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        LinearLayoutManager l2 = new LinearLayoutManager(this);
        eventListRecyclerView.setAdapter(eventListAdapter); eventListRecyclerView.setLayoutManager(l2);
        timeLineRecyclerView.setAdapter(timeLineAdapter); timeLineRecyclerView.setLayoutManager(l1);
        new CardScaleHelper().attachToRecyclerView(timeLineRecyclerView);

        boolean flag = false;
        int currentPosition = -1;
        for(int i=0; i<timeLine.size(); ++i){
            ExecutedItem executedItem = timeLine.get(i);
            if(executedItem.getStatus()==DOING){
                currentPosition = i;
                break ;
            }else{
                if(!flag) {
                    MyTime currentTime = new MyTime(new Date());
                    if(executedItem.getPlanedStartTime().later(currentTime)){
                        flag = true;
                        currentPosition = i;
                    }
                }
            }
        }
        if(!flag){
            currentPosition = timeLine.size()-1;
        }
        if(currentPosition>=0){
            currentItem = timeLine.get(currentPosition);
            timeLineRecyclerView.smoothScrollToPosition(currentPosition);
        }
    }

    //从eventList中加入到timeLine中
    public boolean startMatter(ExecutedItem item, int eventPosition){
        Date date = new Date();
        MyTime startTime = new MyTime(date.getHours(), date.getMinutes());
        if(currentItem.getPlanedStartTime().later(startTime)){
            if(currentItem.getStatus()==DOING||currentItem.getStatus()==WAITING){
                Toast.makeText(this, "还有未完成的任务哦", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        currentItem = item;
        this.eventPosition = eventPosition;
        TimeLineItem timelineItem = new TimeLineItem(item.getContent(), startTime, item.getVariety());
        item.setTimeLineItem(timelineItem);
        item.setStatus(DOING);
        timeLinePosition = timeLineAdapter.startMatter(item);
        eventListAdapter.notifyItemChanged(eventPosition);

        pauseButton.setVisibility(View.VISIBLE);
        confirmButton.setText("完成");
        //
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishMatter(Item.COMPLETED, true);
            }
        });
        timeLineRecyclerView.smoothScrollToPosition(timeLinePosition);
        return true;
    }

    public void startMatter(ExecutedItem item){
        currentItem = item;
        pauseButton.setVisibility(View.VISIBLE);
        confirmButton.setText("完成");
        //
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishMatter(Item.COMPLETED, true);
            }
        });
        timeLineRecyclerView.smoothScrollToPosition(timeLinePosition);
    }

    public void finishMatter(int status, boolean boost){
        currentItem.setStatus(status);
        if(currentItem.isEventItem()){
            if(status==CANCEL) {
                eventListAdapter.cancelMatter(currentItem, eventPosition);
            }else if(status==PAUSE){
                eventListAdapter.pauseMatter(currentItem, eventPosition);
            }else if(status==COMPLETED){
                eventListAdapter.completeMatter(currentItem, eventPosition);
            }
        }
        //timeLineList的更新
        executedItemList.addExecutedItem(currentItem);
        //推进到时间轴的下一个事件，等待用户选择开始或取消
        if(boost){
            ++timeLinePosition;
            timeLineRecyclerView.smoothScrollToPosition(timeLinePosition);
        }
        currentItem.setStatus(Item.WAITING);
        pauseButton.setVisibility(View.GONE);
        confirmButton.setText("开始");
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMatter(currentItem);
            }
        });
    }

    public void initData(){
        if(!DailyPlanDbOperator.checkDate(this)){
            Toast.makeText(this, "还没有制定今日的计划！", Toast.LENGTH_SHORT).show();
        }
        Log.d(TAG, "read daily plan data");
        eventList = DailyPlanDbOperator.getExecutedEventListData(this);
        timeLine = DailyPlanDbOperator.getExecutedTimeLine(this);
        Log.d(TAG, "eventList size: "+ eventList.size());
        Log.d(TAG, "timeLine size: "+ timeLine.size());
        Gson gson = new Gson();
        Log.d(TAG, "eventList: "+ gson.toJson(eventList));
        Log.d(TAG, "timeLine: "+ gson.toJson(timeLine));
    }

    public void saveDate(){
        DailyPlanDbOperator.clearDailyData(this);
        DailyPlanDbOperator.updateDateData(this);
        DailyPlanDbOperator.saveExecutedEventData(eventList, this);
        DailyPlanDbOperator.saveExecutedTimeLineData(timeLine, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveDate();
    }
}
