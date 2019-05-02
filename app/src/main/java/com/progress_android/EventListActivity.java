package com.progress_android;

import Adapter.EventListAdapter;
import Dialog.AddEventDialogFragment;
import Item.EventItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;

import java.util.ArrayList;
import java.util.List;

public class EventListActivity extends AppCompatActivity
        implements AddEventDialogFragment.NoticeDialogListener{

    List<EventItem> eventList = new ArrayList<>();
    RecyclerView eventListRecyclerView;
    EventListAdapter adapter;
    String TAG = "EventListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        Log.d(TAG,"setContentView");
        setContentView(R.layout.activity_event_list);

        eventListRecyclerView = findViewById(R.id.eventList_RecyclerView);
        RecyclerViewDragDropManager dragDropManager = new RecyclerViewDragDropManager();
        //RecyclerViewSwipeManager swipeManager = new RecyclerViewSwipeManager();

        dragDropManager.setInitiateOnLongPress(true);
        dragDropManager.setInitiateOnMove(false);

        initEventList();

        adapter = new EventListAdapter(eventList, this);
        RecyclerView.Adapter wrappedAdapter = dragDropManager.createWrappedAdapter(adapter);
        //wrappedAdapter = swipeManager.createWrappedAdapter(wrappedAdapter);

        eventListRecyclerView.setAdapter(wrappedAdapter);
        eventListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        dragDropManager.attachRecyclerView(eventListRecyclerView);
        //swipeManager.attachRecyclerView(eventListRecyclerView);

        FloatingActionButton addEventButton = (FloatingActionButton) findViewById(R.id.add_event_button);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddEventDialog();
            }
        });
    }

    @Override
    public void onDialogPositiveClick_add(AddEventDialogFragment dialog) {
        EventItem addedItem = new EventItem(dialog.eventContent.getText().toString());
        adapter.addEvent(eventList.size(),addedItem);
    }

    @Override
    public void onDialogNegativeClick_add(AddEventDialogFragment dialog) { }

    private void showAddEventDialog(){
        DialogFragment dialog = new AddEventDialogFragment();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), "AddEventDialogFragment");
    }

    private void initEventList(){
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
}
