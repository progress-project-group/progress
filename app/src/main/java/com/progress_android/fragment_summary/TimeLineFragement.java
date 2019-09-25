package com.progress_android.fragment_summary;

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
import com.progress_android.DailyPlanActivity;
import com.progress_android.R;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import Item.Item;
import Adapter.TimeLineAdapter;
import Adapter.TimeLineSummaryAdapter;
import DataBase.DataBaseHelper;
import DataBase.FeedReaderContract;
import Dialog.AddEventDialogFragment;
import Item.ExecutedItem;
import Item.ExecutedItemList;
import Item.ItemInTimeline;
import Item.Time.MyTime;
import Item.Time.TimeAmount;
import Item.TimeLineItemInSummary;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TimeLineFragement extends Fragment {

    String TAG = "TimelineFragement";
    private RecyclerView recyclerView;
    private TimeLineSummaryAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<TimeLineItemInSummary> itemList = new ArrayList<>();
    private ExecutedItemList executedItemList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_time_line_fragement,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.summary_RecyclerView);
        Log.d(TAG,"setContentView");

        recyclerView.setHasFixedSize(true);

        //这个用来读取长期计划的数据
//        if(getArguments().getBoolean("initList")) {
//            initItemList(getActivity());
//        }else{
//            //initListWithDefaultInfor();
//        }
        initData();

        mAdapter = new TimeLineSummaryAdapter(itemList, getActivity());
        recyclerView.setAdapter(mAdapter);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        Log.d(TAG,"initItemList");

    }

    public void setList(ExecutedItemList executedItemList){
        this.executedItemList = executedItemList;

    }

    public void initData(){
        ExecutedItem executedItem = executedItemList.getFirstItem();
        ExecutedItem frontItem, endItem;
        frontItem = executedItem;
        while(executedItem!=null){
            endItem = frontItem.getNext();
            itemList.add(new TimeLineItemInSummary(frontItem.getContent(),frontItem.getVariety(), frontItem.getStartTime(), frontItem.getEndTime()));
            if(endItem == null){
                break;
            }
            if(!frontItem.getEndTime().equals(endItem.getStartTime())){
                itemList.add(new TimeLineItemInSummary(Item.none, Item.NONE, frontItem.getEndTime(), endItem.getStartTime()));
            }
            frontItem.toNext();
            frontItem = endItem;
        }
    }
}
