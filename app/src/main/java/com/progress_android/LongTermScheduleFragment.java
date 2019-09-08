package com.progress_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import Dialog.DatePickerFragment;
import Dialog.SubLongTermScheduleDialog;
import Item.LongTermSchedule;
import Item.LongTermScheduleList;
import Item.subLongTermSchedule;

public class LongTermScheduleFragment extends Fragment {

    private static final String ARG_SCHEDULE_ID = "schedule_id";
    private static final String DIALOG_DATE = "dialogDate";
    private static final String DIALOG_SUB_SCHEDULE = "subSchedule";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_SUB_SCHEDULE = 1;
    private LongTermSchedule mLongTermSchedule;
    private EditText mScheduleTitle;
    private Button mDateButton;
    private PieChart mPieChart;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode!= Activity.RESULT_OK){
            return;
        }
        if(requestCode==REQUEST_DATE){
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mLongTermSchedule.setBeginDate(date);
        }else if(requestCode == REQUEST_SUB_SCHEDULE){
            boolean deleted = (boolean)data.getBooleanExtra(SubLongTermScheduleDialog.EXTRA_IF_DELETE,false);
            UUID id = (UUID)data.getSerializableExtra(SubLongTermScheduleDialog.EXTRA_ID);
            if(deleted){
                mLongTermSchedule.deleteSubSchedule(id);
            }else {
                String title = (String)data.getStringExtra(SubLongTermScheduleDialog.EXTRA_TITLE);
                boolean finished = (boolean)data.getBooleanExtra(SubLongTermScheduleDialog.EXTRA_FINISHED,false);
                int percent = (int)data.getIntExtra(SubLongTermScheduleDialog.EXTRA_PERCENT,10);
                subLongTermSchedule sub = mLongTermSchedule.getSubSchedule(id);
                sub.setTitle(title);
                sub.setFinished(finished);
                sub.setPercent(percent);
                mLongTermSchedule.setSubSchedule(id,sub);
            }
        }
        update();
    }

    private void update() {
        mDateButton.setText(mLongTermSchedule.getBeginDate().toString());
        Log.d("null",mLongTermSchedule.getScheduleID().toString());
        List<subLongTermSchedule> subLongTermSchedules = mLongTermSchedule.getSubSchedules();
        ArrayList<PieEntry> Entries = new ArrayList<>();
        int totalFinished = 0;
        for (subLongTermSchedule sub :
                subLongTermSchedules) {
                Entries.add(new PieEntry(sub.getPercent(),sub.getTitle()));
        }
        //Entries.add(new PieEntry(totalFinished,"已完成"));
        //Collections.reverse(Entries);
        PieDataSet subScheduleSet = new PieDataSet(Entries,"子任务");
        subScheduleSet.setColors(new int[]{R.color.green,R.color.blue,R.color.red,R.color.yellow},getActivity());
        PieData data = new PieData(subScheduleSet);
        mPieChart.setData(data);
        mPieChart.invalidate(); // refresh
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setHasOptionsMenu(true);
        UUID scheduleID = (UUID) getArguments().getSerializable(ARG_SCHEDULE_ID);
        mLongTermSchedule = LongTermScheduleList.get(getActivity()).getLongTermSchedule(scheduleID);
        System.out.println(mLongTermSchedule.toString());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_long_term_schedule,container,false);


        mScheduleTitle = (EditText)v.findViewById(R.id.longTermTitle);
        mScheduleTitle.setText(mLongTermSchedule.getName());
        mScheduleTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mLongTermSchedule.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mDateButton = (Button)v.findViewById(R.id.LongTermDate);

        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(mLongTermSchedule
                .getBeginDate());
                datePickerFragment.setTargetFragment(LongTermScheduleFragment.this,REQUEST_DATE);
                datePickerFragment.show(fragmentManager,DIALOG_DATE);
            }
        });

        mPieChart = (PieChart)v.findViewById(R.id.longTermSchedulePieChart);
        mPieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener(){

            @Override
            public void onValueSelected(Entry e, Highlight h) {
                int index = (int)h.getX();
                callSubScheduleDialog(index);
            }

            @Override
            public void onNothingSelected() { }
        });
        update();
        return v;
    }

    private void callSubScheduleDialog(int index) {
        subLongTermSchedule sub = mLongTermSchedule.getSubSchedules().get(index);
        FragmentManager fragmentManager = getFragmentManager();
        SubLongTermScheduleDialog dialog = SubLongTermScheduleDialog.newInstance(
                sub.getSubId(),
                sub.getTitle(),
                sub.getPercent(),
                sub.isFinished()
        );
        dialog.setTargetFragment(LongTermScheduleFragment.this,REQUEST_SUB_SCHEDULE);
        dialog.show(fragmentManager,DIALOG_SUB_SCHEDULE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.fragment_long_term_schedule,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.newLongTermSubSchedule:
                subLongTermSchedule schedule = new subLongTermSchedule(mLongTermSchedule.getScheduleID());
                schedule.setPercent(20);
                mLongTermSchedule.addSubSchedule(schedule);
                callSubScheduleDialog(mLongTermSchedule.getSubSchedules().size()-1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static LongTermScheduleFragment newInstance(UUID schedule_id){
        Bundle args = new Bundle();
        args.putSerializable(ARG_SCHEDULE_ID,schedule_id);

        LongTermScheduleFragment fragment = new LongTermScheduleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onPause(){
        super.onPause();

        LongTermScheduleList.get(getActivity()).updateLongTermSchedule(mLongTermSchedule);
    }


}
