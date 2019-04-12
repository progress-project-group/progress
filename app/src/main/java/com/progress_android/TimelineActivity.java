package com.progress_android;

import Adapter.TimeLineAdapter;
import Item.ItemInTimeline;
import Item.Time.MyTime;
import Item.Time.TimeAmount;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class TimelineActivity extends AppCompatActivity {

    String TAG = "TimelineActivity";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<ItemInTimeline> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        recyclerView = (RecyclerView) findViewById(R.id.timelineitem_RecyclerView);
        Log.d(TAG,"setContentView");

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        initItemList();
        Log.d(TAG,"initItemList");

        mAdapter = new TimeLineAdapter(itemList,this);
        recyclerView.setAdapter(mAdapter);
    }

    private void initItemList() {
        ItemInTimeline moyu = new ItemInTimeline("摸鱼", new MyTime(8,0), new TimeAmount(150));
        moyu.setTimePointImageId(R.drawable.completed_point);
        ItemInTimeline chuanhuo = new ItemInTimeline("传火", new MyTime(13,0), new TimeAmount(100));
        chuanhuo.setTimePointImageId(R.drawable.completed_point);
        //ItemInTimeline free1 = ItemInTimeline.getFreeItem(moyu,chuanhuo);
        ItemInTimeline dushu = new ItemInTimeline("读书", new MyTime(15,0), new TimeAmount(180));
        dushu.setTimePointImageId(R.drawable.doing_point);
        //ItemInTimeline free2 = ItemInTimeline.getFreeItem(chuanhuo, dushu);
        ItemInTimeline duanlian = new ItemInTimeline("锻炼", new MyTime(20,0), new TimeAmount(80));
        //ItemInTimeline free3 = ItemInTimeline.getFreeItem(dushu,duanlian);
        ItemInTimeline xizao = new ItemInTimeline("洗澡", new MyTime(22,0), new TimeAmount(30));
        //ItemInTimeline free4 = ItemInTimeline.getFreeItem(duanlian, xizao);

        itemList.add(moyu);
        itemList.add(chuanhuo);
        itemList.add(dushu);
        itemList.add(duanlian);
        itemList.add(xizao);
    }
}
