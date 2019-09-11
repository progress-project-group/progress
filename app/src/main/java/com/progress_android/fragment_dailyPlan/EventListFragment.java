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
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.progress_android.DailyPlanActivity;
import com.progress_android.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.EventListAdapter;
import DataBase.DataBaseHelper;
import DataBase.FeedReaderContract;
import Dialog.AddEventDialogFragment;
import Item.EventItem;
import Item.Time.Pomodoro;
import Item.Time.TimeAmount;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EventListFragment extends Fragment{

    List<EventItem> eventList = new ArrayList<>();
    RecyclerView eventListRecyclerView;
    EventListAdapter adapter;
    String TAG = "EventListFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_event_list,container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        eventListRecyclerView = view.findViewById(R.id.eventList_RecyclerView);
        RecyclerViewDragDropManager dragDropManager = new RecyclerViewDragDropManager();
        //RecyclerViewSwipeManager swipeManager = new RecyclerViewSwipeManager();

        dragDropManager.setInitiateOnLongPress(true);
        dragDropManager.setInitiateOnMove(false);
        if(getArguments().getBoolean("initList")) {
            initEventList(getActivity());
        }else{
            initListWithDefaultInfor();
        }

        adapter = new EventListAdapter(eventList, getActivity());
        RecyclerView.Adapter wrappedAdapter = dragDropManager.createWrappedAdapter(adapter);
        //wrappedAdapter = swipeManager.createWrappedAdapter(wrappedAdapter);

        eventListRecyclerView.setAdapter(wrappedAdapter);
        eventListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        dragDropManager.attachRecyclerView(eventListRecyclerView);
        //swipeManager.attachRecyclerView(eventListRecyclerView);

        FloatingActionButton addEventButton = (FloatingActionButton) view.findViewById(R.id.add_event_button);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddEventDialog();
            }
        });
    }

    private void showAddEventDialog(){
        DialogFragment dialog = new AddEventDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("FragmentTag", DailyPlanActivity.FragmentTag_EventList);
        dialog.setArguments(bundle);
        dialog.setCancelable(false);
        dialog.show(getChildFragmentManager(), "AddEventDialogFragment");
    }

    public void addEventItem(EventItem addedItem){
        adapter.addEvent(eventList.size(),addedItem);
    }

    public void initEventList(Context context){
        Log.d(TAG,"initEventList");
        DataBaseHelper dbHelper = new DataBaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query(
                FeedReaderContract.EventListData.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        while(cursor.moveToNext()) {
            String content = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.EventListData.COLUMN_CONTENT));
            int type = cursor.getInt(cursor.getColumnIndexOrThrow(FeedReaderContract.EventListData.COLUMN_TYPE));
            int porNums = cursor.getInt(cursor.getColumnIndexOrThrow(FeedReaderContract.EventListData.COLUMN_PORNUMS));
            int work = cursor.getInt(cursor.getColumnIndexOrThrow(FeedReaderContract.EventListData.COLUMN_WORK));
            int relax = cursor.getInt(cursor.getColumnIndexOrThrow(FeedReaderContract.EventListData.COLUMN_RELAX));

            EventItem item = new EventItem( content,new TimeAmount(new Pomodoro(work, relax), porNums), type);
            eventList.add(item);
        }
    }

    public boolean checkData(){
        for(int i=0;i<eventList.size();++i){
            if(eventList.get(i).getContent().equals("")||eventList.get(i).getTimeAmount()==null){
                return false;
            }
        }
        return true;
    }

    public void saveData(SQLiteDatabase db){
        for(int i=0;i<eventList.size();++i){
            EventItem item = eventList.get(i);
            ContentValues values = new ContentValues();
            values.put(FeedReaderContract.EventListData.COLUMN_CONTENT,item.getContent());
            values.put(FeedReaderContract.EventListData.COLUMN_TYPE, item.getVariety());
            values.put(FeedReaderContract.EventListData.COLUMN_PORNUMS,item.getTimeAmount().getPomodoroNums());
            values.put(FeedReaderContract.EventListData.COLUMN_WORK,item.getTimeAmount().getPomodoro().getWork());
            values.put(FeedReaderContract.EventListData.COLUMN_RELAX,item.getTimeAmount().getPomodoro().getRelax());
            Log.d(TAG, "saveData" + values);
            db.insert(FeedReaderContract.EventListData.TABLE_NAME, null,values);
        }
    }

    public void initListWithDefaultInfor(){
        EventItem item1 = new EventItem("写代码，完成eventList界面");
        EventItem item2 = new EventItem("哑铃、仰卧起坐、俯卧撑");
        EventItem item3 = new EventItem("读诗经");
        EventItem item4 = new EventItem("传火");
        EventItem item5 = new EventItem("查英语资料");
        EventItem item6 = new EventItem("跑步");
        EventItem item7 = new EventItem("英雄联盟");

        eventList.add(item1); eventList.add(item2); eventList.add(item3);
        eventList.add(item4); eventList.add(item5); eventList.add(item6);
        eventList.add(item7);
    }

    public void setItemType(int position, int type){
        adapter.setItemType(position, type);
    }
}
