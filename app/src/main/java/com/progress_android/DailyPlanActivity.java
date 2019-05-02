package com.progress_android;

import DataBase.DataBaseHelper;
import DataBase.FeedReaderContract;
import Dialog.AddEventDialogFragment;
import Dialog.StartTimeSettingDialog;
import Item.EventItem;
import Item.ItemInTimeline;
import Item.Time.MyTime;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DailyPlanActivity extends AppCompatActivity implements AddEventDialogFragment.NoticeDialogListener
            , StartTimeSettingDialog.NoticeDialogListener {

    DailyPlanPagerAdapter dailyPlanPagerAdapter;
    ViewPager viewPager;
    static final int NUM_ITEMS = 2;
    List<String> pageTitleList = new ArrayList<>();
    List<Fragment> fragmentList = new ArrayList<>();
    EventListFragment eventListFragment;
    TimeLineFragment timeLineFragment;
    int addItemTag_EventList = 2;
    int addItemTag_TimeLine = 1;
    static String TAG = "DailyPlanActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_plan);
        initList();
        ((ViewPager.LayoutParams) findViewById(R.id.pager_header).getLayoutParams()).isDecor = true;
        dailyPlanPagerAdapter = new DailyPlanPagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.daily_plan_pager);
        viewPager.setAdapter(dailyPlanPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.confirm){
            if(checkData()) {
                saveData();
                Toast.makeText(this,"store success",Toast.LENGTH_SHORT);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveData(){
        Log.d(TAG,"saveData");
        DataBaseHelper dbHelper = new DataBaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        Date dCurrentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sCurrentDate = sdf.format(dCurrentDate);
        values.put("Date", sCurrentDate);
        db.insert(FeedReaderContract.Time.TABLE_NAME, null,values);

        db.execSQL("delete from "+ FeedReaderContract.TimeLineData.TABLE_NAME);
        db.execSQL("delete from "+ FeedReaderContract.EventListData.TABLE_NAME);

        timeLineFragment.saveData(db);
        eventListFragment.saveData(db);
    }

    private boolean checkData(){
        if(!eventListFragment.checkData()) {
            Toast.makeText(this, "清单中有未设置的事件", Toast.LENGTH_SHORT).show();
            return false;
        } else if(!timeLineFragment.checkData()){
            Toast.makeText(this, "时间轴中有未设置的事件", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onDialogPositiveClick_add(AddEventDialogFragment dialog) {
        if(dialog.getAddItemTag()==addItemTag_EventList) {
            EventItem addedItem = new EventItem(dialog.eventContent.getText().toString());
            eventListFragment.addEventItem(addedItem);
        } else{
            ItemInTimeline addedItem = new ItemInTimeline(dialog.eventContent.getText().toString(),new MyTime(0,0));
            timeLineFragment.addItem(addedItem);
        }
    }

    @Override
    public void onDialogNegativeClick_add(AddEventDialogFragment dialog) { }

    @Override
    public void onDialogPositiveClick_start(int hour, int minute, int position) {
        MyTime myTime = new MyTime(hour, minute);
        timeLineFragment.updateItem(myTime, position);
    }

    @Override
    public void onDialogNegativeClick_start(int hour, int minute, int position) {}

    public class DailyPlanPagerAdapter extends FragmentPagerAdapter {
        public DailyPlanPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return pageTitleList.get(position);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }
    }

    private void initList(){
        Log.d(TAG,"initList");
        DataBaseHelper dbHelper = new DataBaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        pageTitleList.add("清单");
        pageTitleList.add("时间线");

        eventListFragment = new EventListFragment();
        timeLineFragment = new TimeLineFragment();

        fragmentList.add(eventListFragment);
        fragmentList.add(timeLineFragment);

        Cursor cursor = db.query(
                FeedReaderContract.Time.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        if(cursor.moveToFirst()){
            Log.d("TAG", "judge date");
            String date;
            date = cursor.getString(cursor.getColumnIndex(FeedReaderContract.Time.COLUMN_TIME));
            Date dCurrentDate = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String sCurrentDate = sdf.format(dCurrentDate);
            Log.d(TAG,"StoredDate=" + sCurrentDate+" Date="+date);
            if(sCurrentDate.equals(date)){
                Log.d("TAG", "read daily plan data");
                Bundle bundle = new Bundle();
                bundle.putBoolean("initList", true);
                eventListFragment.setArguments(bundle);
                timeLineFragment.setArguments(bundle);
                return;
            } else{
                Log.d(TAG,"日期不符");
            }
        } else{
            Log.d(TAG, "日期为空");
        }
        Bundle bundle = new Bundle();
        bundle.putBoolean("initList", false);
        eventListFragment.setArguments(bundle);
        timeLineFragment.setArguments(bundle);
    }
}
