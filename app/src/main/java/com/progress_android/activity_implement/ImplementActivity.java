package com.progress_android.activity_implement;

import Adapter.ImplementELAdapter;
import Adapter.ImplementTLAdapter;
import DataBase.DailyPlanDbOperator;
import Item.DaliyPlan.ExecutedItem;
import Item.Item;
import Item.DaliyPlan.ExecutedItemList;
import Item.DaliyPlan.TimeLineItem;
import Item.Time.MyTime;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.progress_android.R;
import com.progress_android.activity_implement.CenterUtil.CenterLayoutManager;
import com.progress_android.activity_implement.GalleryUtil.CardScaleHelper;

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

    RecyclerView eventListRecyclerView;
    RecyclerView timeLineRecyclerView;
    ImplementELAdapter eventListAdapter;
    ImplementTLAdapter timeLineAdapter;
    List<ExecutedItem> eventList = new ArrayList<>();
    List<ExecutedItem> timeLine = new ArrayList<>();

    int timeLinePosition = 0;
    int eventPosition;
    boolean nextMatter = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_implement);
        eventListRecyclerView = findViewById(R.id.IEventListRecycleView);
        timeLineRecyclerView = findViewById(R.id.ITimeLineRecycleView);

        initData();

        eventListAdapter = new ImplementELAdapter(eventList, this);
        timeLineAdapter = new ImplementTLAdapter(timeLine, this);
        LinearLayoutManager l1 = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        LinearLayoutManager l2 = new LinearLayoutManager(this);
        eventListRecyclerView.setAdapter(eventListAdapter); eventListRecyclerView.setLayoutManager(l2);
        timeLineRecyclerView.setAdapter(timeLineAdapter); timeLineRecyclerView.setLayoutManager(l1);

        boolean flag = false;
        int currentPosition = -1;
        for(int i=1; i<timeLine.size()-1; ++i){
            Gson gson = new Gson();
            Log.d(TAG, "i="+i);
            ExecutedItem executedItem = timeLine.get(i);
            Log.d(TAG, gson.toJson(executedItem));
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
        timeLinePosition = currentPosition;
        if(!flag){
            timeLinePosition = timeLine.size()-2;
        }
        assert timeLinePosition>=0;
        currentItem = timeLine.get(timeLinePosition);
        timeLineRecyclerView.smoothScrollToPosition(timeLinePosition);
        CardScaleHelper cardScaleHelper = new CardScaleHelper();
        cardScaleHelper.setCurrentItemPos(timeLinePosition);
        cardScaleHelper.attachToRecyclerView(timeLineRecyclerView);
        //l1.smoothScrollToPosition(timeLineRecyclerView, );

        if(currentItem.getStatus()==DOING){
            nextMatter = false;
        }
        Log.d(TAG, "当前任务的position："+timeLinePosition);
    }

    // 从eventList中开始任务
    // 从eventList中加入到timeLine中
    public boolean startEventListMatter(ExecutedItem item, int eventPosition){
        if(!nextMatter){
            Toast.makeText(this, "当前任务还未完成哦", Toast.LENGTH_SHORT).show();
            return false;
        }
        //记录统计数据
        Date date = new Date();
        MyTime startTime = new MyTime(date.getHours(), date.getMinutes());
        item.addStartTime(startTime);

        currentItem = item;
        this.eventPosition = eventPosition;
        TimeLineItem timelineItem = new TimeLineItem(item.getContent(), startTime, item.getVariety());
        item.setTimeLineItem(timelineItem);
        item.setStatus(DOING);

        timeLinePosition = timeLineAdapter.startMatter(item);
        Log.d(TAG, "从EventList开始任务："+eventPosition);
        Log.d(TAG, "插入TimeLine位置："+timeLinePosition);
        eventListAdapter.notifyItemChanged(eventPosition);

        timeLineRecyclerView.smoothScrollToPosition(timeLinePosition);
        return true;
    }

    //从timeLine直接开始任务
    public void startTimeLineMatter(ExecutedItem item, int position){
        if(!nextMatter){
            Toast.makeText(this, "当前任务还未完成哦", Toast.LENGTH_SHORT).show();
            return ;
        }
        Log.d(TAG, "当前timeLine position为"+this.timeLinePosition);
        if(position != this.timeLinePosition){
            Log.d(TAG, "开始position为"+position+" 的任务");
            new MaterialAlertDialogBuilder(this, R.style.dialogTheme)
                    .setTitle("提示")
                    .setMessage("还有任务没有完成，确定要直接开始吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            skipMatter(position);
                        }
                    })
                    .show();
        }
        timeLinePosition = position;
        nextMatter = false;
        currentItem = item;
        currentItem.setStatus(DOING);
        currentItem.addStartTime(new MyTime(new Date()));
        timeLineAdapter.notifyItemChanged(timeLinePosition);
        timeLineRecyclerView.smoothScrollToPosition(timeLinePosition);
    }

    private void skipMatter(int position){
        for(int i=1; i<position; ++i){
            ExecutedItem item = timeLine.get(i);
            if(item.getStatus()==WAITING){
                item.setStatus(CANCEL);
            }
        }
        timeLineAdapter.notifyDataSetChanged();
        //eventListAdapter.notifyItemChanged();
    }

    //在timeLine中选择暂停、取消、完成任务
    //更新eventList中的数据显示
    //更新统计数据
    //将timeline推进到下一个任务
    public void finishMatter(int status, boolean boost){
        //更新eventList数据
        nextMatter = true;
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
        //统计数据更新
        currentItem.addEndTime(new MyTime(new Date()));
        executedItemList.addExecutedItem(currentItem);

        timeLineAdapter.notifyItemChanged(timeLinePosition);
        //推进到时间轴的下一个事件，等待用户选择开始或取消
        if(timeLinePosition==timeLine.size()-2){
            return ;
        }
        if(boost) {
            ++timeLinePosition;
            currentItem = timeLine.get(timeLinePosition);
        }
        currentItem.setStatus(Item.WAITING);
        timeLineAdapter.notifyItemChanged(timeLinePosition);
        timeLineRecyclerView.smoothScrollToPosition(timeLinePosition);
    }

    public void initData(){
        if(!DailyPlanDbOperator.checkDate(this)){
            Toast.makeText(this, "还没有制定今日的计划！", Toast.LENGTH_SHORT).show();
        }
        Log.d(TAG, "read daily plan data");
        eventList = DailyPlanDbOperator.getExecutedEventListData(this);
        timeLine = DailyPlanDbOperator.getExecutedTimeLine(this);
        timeLine.add(0, null);
        timeLine.add(timeLine.size(), null);
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
        timeLine.remove(0);
        timeLine.remove(timeLine.size()-1);
        DailyPlanDbOperator.saveExecutedTimeLineData(timeLine, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveDate();
    }
}
