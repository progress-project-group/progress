package DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Item.DaliyPlan.EventItem;
import Item.DaliyPlan.ExecutedItem;
import Item.DaliyPlan.TimeLineItem;
import Item.Time.MyTime;
import Item.Time.Pomodoro;
import Item.Time.TimeAmount;

public class DailyPlanDbOperator {
    public static List<EventItem> getDailyEventListData(Context context, Date date){
        List<EventItem> eventItemList = new ArrayList<>();
        DailyPlanDataBaseHelper dbHelper = new DailyPlanDataBaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query(
                DailyPlanDbSchema.EventListData.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,
                null // don't filter by row groups
        );

        while(cursor.moveToNext()) {
            String content = cursor.getString(cursor.getColumnIndexOrThrow(DailyPlanDbSchema.EventListData.COLUMN_CONTENT));
            int type = cursor.getInt(cursor.getColumnIndexOrThrow(DailyPlanDbSchema.EventListData.COLUMN_TYPE));
            int porNums = cursor.getInt(cursor.getColumnIndexOrThrow(DailyPlanDbSchema.EventListData.COLUMN_PORNUMS));
            int work = cursor.getInt(cursor.getColumnIndexOrThrow(DailyPlanDbSchema.EventListData.COLUMN_WORK));
            int relax = cursor.getInt(cursor.getColumnIndexOrThrow(DailyPlanDbSchema.EventListData.COLUMN_RELAX));

            EventItem item = new EventItem( content,new TimeAmount(new Pomodoro(work, relax), porNums), type);
            eventItemList.add(item);
        }

        return eventItemList;
    }

    public static List<ExecutedItem> getExecutedEventListData(Context context){
        List<ExecutedItem> itemList = new ArrayList<>();
        Map<Integer, ExecutedItem> itemMap = new HashMap<>();
        DailyPlanDataBaseHelper dbHelper = new DailyPlanDataBaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(
                DailyPlanDbSchema.EventListData.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        while(cursor.moveToNext()) {
            String content = cursor.getString(cursor.getColumnIndexOrThrow(DailyPlanDbSchema.EventListData.COLUMN_CONTENT));
            int type = cursor.getInt(cursor.getColumnIndexOrThrow(DailyPlanDbSchema.EventListData.COLUMN_TYPE));
            int porNums = cursor.getInt(cursor.getColumnIndexOrThrow(DailyPlanDbSchema.EventListData.COLUMN_PORNUMS));
            int work = cursor.getInt(cursor.getColumnIndexOrThrow(DailyPlanDbSchema.EventListData.COLUMN_WORK));
            int relax = cursor.getInt(cursor.getColumnIndexOrThrow(DailyPlanDbSchema.EventListData.COLUMN_RELAX));

            EventItem item = new EventItem( content,new TimeAmount(new Pomodoro(work, relax), porNums), type);
            int status = cursor.getInt(cursor.getColumnIndexOrThrow(DailyPlanDbSchema.EventListData.COLUMN_STATUS));
            String startTimeData = cursor.getString(cursor.getColumnIndexOrThrow(DailyPlanDbSchema.EventListData.COLUMN_STARTTIME));
            String endTimeData = cursor.getString(cursor.getColumnIndexOrThrow(DailyPlanDbSchema.EventListData.COLUMN_ENDTIME));
            String nextItemData = cursor.getString(cursor.getColumnIndexOrThrow(DailyPlanDbSchema.EventListData.COLUMN_NEXT));

            ExecutedItem executedItem = new ExecutedItem(item);
            executedItem.setStatus(status);
            Gson gson = new Gson();
            Type dataListType = new TypeToken<ArrayList<MyTime>>(){}.getType();
            List<MyTime> startTime = gson.fromJson(startTimeData, dataListType);
            List<MyTime> endTime = gson.fromJson(endTimeData, dataListType);

            Type itemListType = new TypeToken<ArrayList<Integer>>(){}.getType();
            List<Integer> nextID = gson.fromJson(nextItemData, itemListType);

            executedItem.setTimeData(startTime, endTime);
            executedItem.setNextID(nextID);

            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DailyPlanDbSchema.EventListData._ID));
            executedItem.setId(id);
            itemMap.put(id, executedItem);
            itemList.add(executedItem);
        }

        for(ExecutedItem executedItem: itemList){
            List<ExecutedItem> nextList = new ArrayList<>();
            for(Integer nextID :executedItem.getNextID()){
                assert itemMap.containsKey(nextID);
                nextList.add(itemMap.get(nextID));
            }
            executedItem.setNext(nextList);
        }

        return itemList;
    }

    public static List<ExecutedItem> getExecutedTimeLine(Context context){
        List<ExecutedItem> itemList = new ArrayList<>();
        Map<Integer, ExecutedItem> itemMap = new HashMap<>();

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
            int status = cursor.getInt(cursor.getColumnIndexOrThrow(DailyPlanDbSchema.TimeLineData.COLUMN_STATUS));
            String startTimeData = cursor.getString(cursor.getColumnIndexOrThrow(DailyPlanDbSchema.TimeLineData.COLUMN_STARTTIME));
            String endTimeData = cursor.getString(cursor.getColumnIndexOrThrow(DailyPlanDbSchema.TimeLineData.COLUMN_ENDTIME));
            String nextItemData = cursor.getString(cursor.getColumnIndexOrThrow(DailyPlanDbSchema.TimeLineData.COLUMN_NEXT));

            ExecutedItem executedItem = new ExecutedItem(item);

            executedItem.setStatus(status);
            Gson gson = new Gson();
            Type dataListType = new TypeToken<ArrayList<MyTime>>(){}.getType();
            List<MyTime> startTime = gson.fromJson(startTimeData, dataListType);
            List<MyTime> endTime = gson.fromJson(endTimeData, dataListType);

            Type itemListType = new TypeToken<ArrayList<Integer>>(){}.getType();
            List<Integer> nextID = gson.fromJson(nextItemData, itemListType);

            executedItem.setTimeData(startTime, endTime);
            executedItem.setNextID(nextID);

            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DailyPlanDbSchema.EventListData._ID));
            itemMap.put(id, executedItem);
            itemList.add(executedItem);
        }

        for(ExecutedItem executedItem: itemList){
            List<ExecutedItem> nextList = new ArrayList<>();
            for(Integer nextID :executedItem.getNextID()){
                assert itemMap.containsKey(nextID);
                nextList.add(itemMap.get(nextID));
            }
            executedItem.setNext(nextList);
        }
        return itemList;
    }

    public static List<TimeLineItem> getTimeLineListData(Context context){
        DailyPlanDataBaseHelper dbHelper = new DailyPlanDataBaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<TimeLineItem> itemList = new ArrayList<>();

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
        return itemList;
    }

    public static boolean checkDate(Context context){
        String TAG = "checkDate";
        DailyPlanDataBaseHelper dbHelper = new DailyPlanDataBaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(
                DailyPlanDbSchema.Time.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        //判断数据库中是否有数据
        if(cursor.moveToFirst()){
            Log.d("TAG", "judge date");
            //判断数据库中的数据是否是本日的数据
            String date;
            date = cursor.getString(cursor.getColumnIndex(DailyPlanDbSchema.Time.COLUMN_TIME));
            Date dCurrentDate = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String sCurrentDate = sdf.format(dCurrentDate);
            Log.d(TAG,"StoredDate=" + sCurrentDate+" Date="+date);
            if(sCurrentDate.equals(date)){
                Log.d("TAG", "read daily plan data");
                return true;
            } else{
                Log.d(TAG,"日期不符");
            }
        } else{
            Log.d(TAG, "日期为空");
        }
        return false;
    }

    public static void saveEventListData(List<EventItem> eventItemList, Context context){
        DailyPlanDataBaseHelper dbHelper = new DailyPlanDataBaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for(int i=0;i<eventItemList.size();++i){
            EventItem item = eventItemList.get(i);
            ContentValues values = new ContentValues();
            values.put(DailyPlanDbSchema.EventListData.COLUMN_PRIORITY, item.getPriority());
            values.put(DailyPlanDbSchema.EventListData.COLUMN_CONTENT,item.getContent());
            values.put(DailyPlanDbSchema.EventListData.COLUMN_TYPE, item.getVariety());
            values.put(DailyPlanDbSchema.EventListData.COLUMN_PORNUMS,item.getTimeAmount().getPomodoroNums());
            values.put(DailyPlanDbSchema.EventListData.COLUMN_WORK,item.getTimeAmount().getPomodoro().getWork());
            values.put(DailyPlanDbSchema.EventListData.COLUMN_RELAX,item.getTimeAmount().getPomodoro().getRelax());
            Log.d("save eventList", "saveData" + values);
            db.insert(DailyPlanDbSchema.EventListData.TABLE_NAME, null,values);
        }
    }

    public static void saveTimeLineData(List<TimeLineItem> timeLine, Context context){
        DailyPlanDataBaseHelper dbHelper = new DailyPlanDataBaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for(int i=0;i<timeLine.size();++i){
            TimeLineItem item = timeLine.get(i);
            ContentValues values = new ContentValues();
            values.put(DailyPlanDbSchema.TimeLineData.COLUMN_CONTENT,item.getContent());
            values.put(DailyPlanDbSchema.TimeLineData.COLUMN_TYPE, item.getVariety());
            values.put(DailyPlanDbSchema.TimeLineData.COLUMN_HOUR,item.getStartTimeHour());
            values.put(DailyPlanDbSchema.TimeLineData.COLUMN_MINS,item.getStartTimeMins());

            db.insert(DailyPlanDbSchema.TimeLineData.TABLE_NAME, null,values);
        }
    }

    public static void saveExecutedEventData(List<ExecutedItem> executedItemList, Context context){
        DailyPlanDataBaseHelper dbHelper = new DailyPlanDataBaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for(int i=0;i<executedItemList.size();++i){
            ExecutedItem item = executedItemList.get(i);
            ContentValues values = new ContentValues();
            values.put(DailyPlanDbSchema.EventListData.COLUMN_PRIORITY, item.getPriority());
            values.put(DailyPlanDbSchema.EventListData.COLUMN_CONTENT,item.getContent());
            values.put(DailyPlanDbSchema.EventListData.COLUMN_TYPE, item.getVariety());
            values.put(DailyPlanDbSchema.EventListData.COLUMN_PORNUMS,item.getPlanedTimeAmount().getPomodoroNums());
            values.put(DailyPlanDbSchema.EventListData.COLUMN_WORK,item.getPlanedTimeAmount().getPomodoro().getWork());
            values.put(DailyPlanDbSchema.EventListData.COLUMN_RELAX,item.getPlanedTimeAmount().getPomodoro().getRelax());

            Gson gson = new Gson();
            String startTimeData = gson.toJson(item.getStartTimeData());
            String endTimeData = gson.toJson(item.getEndTimeData());
            String nextID = gson.toJson(item.generateNextID());
            values.put(DailyPlanDbSchema.EventListData.COLUMN_STATUS,item.getStatus());
            values.put(DailyPlanDbSchema.EventListData.COLUMN_STARTTIME,startTimeData);
            values.put(DailyPlanDbSchema.EventListData.COLUMN_ENDTIME,endTimeData);
            values.put(DailyPlanDbSchema.EventListData.COLUMN_NEXT,nextID);

            Log.d("save eventList", "saveData" + values);
            db.insert(DailyPlanDbSchema.EventListData.TABLE_NAME, null,values);
        }
    }

    public static void saveExecutedTimeLineData(List<ExecutedItem> executedItemList, Context context){
        DailyPlanDataBaseHelper dbHelper = new DailyPlanDataBaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for(int i=0;i<executedItemList.size();++i){
            ExecutedItem item = executedItemList.get(i);
            ContentValues values = new ContentValues();
            values.put(DailyPlanDbSchema.TimeLineData.COLUMN_CONTENT,item.getContent());
            values.put(DailyPlanDbSchema.TimeLineData.COLUMN_TYPE, item.getVariety());
            values.put(DailyPlanDbSchema.TimeLineData.COLUMN_HOUR,item.getPlanedStartTime().getHours());
            values.put(DailyPlanDbSchema.TimeLineData.COLUMN_MINS,item.getPlanedStartTime().getMins());

            Gson gson = new Gson();
            String startTimeData = gson.toJson(item.getStartTimeData());
            String endTimeData = gson.toJson(item.getEndTimeData());
            String nextID = gson.toJson(item.generateNextID());
            values.put(DailyPlanDbSchema.TimeLineData.COLUMN_STATUS,item.getStatus());
            values.put(DailyPlanDbSchema.TimeLineData.COLUMN_STARTTIME,startTimeData);
            values.put(DailyPlanDbSchema.TimeLineData.COLUMN_ENDTIME,endTimeData);
            values.put(DailyPlanDbSchema.TimeLineData.COLUMN_NEXT,nextID);

            db.insert(DailyPlanDbSchema.TimeLineData.TABLE_NAME, null,values);
        }
    }

    public static void clearDailyData(Context context){
        DailyPlanDataBaseHelper dbHelper = new DailyPlanDataBaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from "+ DailyPlanDbSchema.Time.TABLE_NAME);
        db.execSQL("delete from "+ DailyPlanDbSchema.TimeLineData.TABLE_NAME);
        db.execSQL("delete from "+ DailyPlanDbSchema.EventListData.TABLE_NAME);
    }

    public static void updateDateData(Context context){
        DailyPlanDataBaseHelper dbHelper = new DailyPlanDataBaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        Date dCurrentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sCurrentDate = sdf.format(dCurrentDate);
        values.put("Date", sCurrentDate);
        db.insert(DailyPlanDbSchema.Time.TABLE_NAME, null,values);
    }
}
