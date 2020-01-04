package com.progress_android.fragment_dailyPlan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.progress_android.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.TimeLineAdapter;
import DataBase.DailyPlanDataBaseHelper;
import DataBase.DailyPlanDbSchema;
import Dialog.AddEventDialogFragment;
import Item.DaliyPlan.TimeLineItem;
import Item.Time.MyTime;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TimeLineFragment extends Fragment {

    String TAG = "TimelineFragment";
    private RecyclerView recyclerView;
    private TimeLineAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<TimeLineItem> itemList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_timeline,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.timelineitem_RecyclerView);
        Log.d(TAG,"setContentView");

        recyclerView.setHasFixedSize(true);

        if(getArguments().getBoolean("initList")) {
            initItemList(getActivity());
        }else{
            initListWithDefaultInfor();
        }

        mAdapter = new TimeLineAdapter(itemList,getActivity());
        recyclerView.setAdapter(mAdapter);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        Log.d(TAG,"initItemList");

        FloatingActionButton addEventButton = (FloatingActionButton) view.findViewById(R.id.add_event_button2);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddEventDialog();
            }
        });
    }

    public void updateItem(MyTime myTime, int position) {
        mAdapter.updateItem(myTime, position);
    }

    private void showAddEventDialog(){
        Log.d(TAG,"onAddClick");
        DialogFragment dialog = new AddEventDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("FragmentTag", DailyPlanActivity.FragmentTag_TimeLine);
        dialog.setArguments(bundle);
        dialog.setCancelable(false);
        dialog.show(getChildFragmentManager(), "AddEventDialogFragment");
    }

    public void addItem(TimeLineItem addedItem){
        mAdapter.addItem(0,addedItem);
    }

    public void initItemList(Context context) {
        Log.d(TAG,"initEventList");
        DailyPlanDataBaseHelper dbHelper = new DailyPlanDataBaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query(
                DailyPlanDbSchema.TimeLineData.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        while(cursor.moveToNext()) {
            String content = cursor.getString(cursor.getColumnIndexOrThrow(DailyPlanDbSchema.TimeLineData.COLUMN_CONTENT));
            int type = cursor.getInt(cursor.getColumnIndexOrThrow(DailyPlanDbSchema.TimeLineData.COLUMN_TYPE));
            int hour = cursor.getInt(cursor.getColumnIndexOrThrow(DailyPlanDbSchema.TimeLineData.COLUMN_HOUR));
            int mins = cursor.getInt(cursor.getColumnIndexOrThrow(DailyPlanDbSchema.TimeLineData.COLUMN_MINS));

            TimeLineItem item = new TimeLineItem(content, new MyTime(hour, mins), type);
            itemList.add(item);
        }
    }

    public boolean checkData(){
        for(int i=0;i<itemList.size();++i){
            if(itemList.get(i).getContent().equals("")){
                return false;
            }
        }
        return true;
    }

    public void saveData(SQLiteDatabase db){
        for(int i=0;i<itemList.size();++i){
            TimeLineItem item = itemList.get(i);
            ContentValues values = new ContentValues();
            values.put(DailyPlanDbSchema.TimeLineData.COLUMN_CONTENT,item.getContent());
            values.put(DailyPlanDbSchema.TimeLineData.COLUMN_TYPE, item.getVariety());
            values.put(DailyPlanDbSchema.TimeLineData.COLUMN_HOUR,item.getStartTimeHour());
            values.put(DailyPlanDbSchema.TimeLineData.COLUMN_MINS,item.getStartTimeMins());

            db.insert(DailyPlanDbSchema.TimeLineData.TABLE_NAME, null,values);
        }
    }

    public void initListWithDefaultInfor(){
        TimeLineItem moyu = new TimeLineItem("摸鱼", new MyTime(8,0));
        moyu.setTimePointImageId(R.drawable.completed_point);
        TimeLineItem chuanhuo = new TimeLineItem("传火", new MyTime(13,0));
        chuanhuo.setTimePointImageId(R.drawable.completed_point);
        //ItemInTimeline free1 = ItemInTimeline.getFreeItem(moyu,chuanhuo);
        TimeLineItem dushu = new TimeLineItem("读书", new MyTime(15,0));
        dushu.setTimePointImageId(R.drawable.doing_point);
        //ItemInTimeline free2 = ItemInTimeline.getFreeItem(chuanhuo, dushu);
        TimeLineItem duanlian = new TimeLineItem("锻炼", new MyTime(20,0));
        //ItemInTimeline free3 = ItemInTimeline.getFreeItem(dushu,duanlian);
        TimeLineItem xizao = new TimeLineItem("洗澡", new MyTime(22,0));
        //ItemInTimeline free4 = ItemInTimeline.getFreeItem(duanlian, xizao);

        itemList.add(moyu);
        itemList.add(chuanhuo);
        itemList.add(dushu);
        itemList.add(duanlian);
        itemList.add(xizao);
    }

    public void setItemType(int position, int type){
        mAdapter.setItemType(position, type);
    }
}
