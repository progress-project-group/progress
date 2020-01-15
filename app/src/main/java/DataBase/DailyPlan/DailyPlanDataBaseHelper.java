package DataBase.DailyPlan;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DailyPlanDataBaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DailyPlan.db";
    private static final String TIMELINE_TABLE_CREATE = "CREATE TABLE " + DailyPlanDbSchema.TimeLineData.TABLE_NAME + " (" +
            DailyPlanDbSchema.TimeLineData._ID + " INTEGER PRIMARY KEY," +
            DailyPlanDbSchema.TimeLineData.COLUMN_CONTENT + " TEXT," +
            DailyPlanDbSchema.TimeLineData.COLUMN_TYPE + " INTEGER,"+
            DailyPlanDbSchema.TimeLineData.COLUMN_HOUR + " INTEGER,"+
            DailyPlanDbSchema.TimeLineData.COLUMN_MINS + " INTEGER," +
            DailyPlanDbSchema.EventListData.COLUMN_STATUS + " INTEGER," +
            DailyPlanDbSchema.TimeLineData.COLUMN_STARTTIME + " TEXT," +
            DailyPlanDbSchema.TimeLineData.COLUMN_ENDTIME + " TEXT," +
            DailyPlanDbSchema.TimeLineData.COLUMN_NEXT + " TEXT)";
    private static final String EVENTLIST_TABLE_CREATE = "CREATE TABLE " + DailyPlanDbSchema.EventListData.TABLE_NAME + " (" +
            DailyPlanDbSchema.EventListData._ID + " INTEGER PRIMARY KEY," +
            DailyPlanDbSchema.EventListData.COLUMN_PRIORITY + " INTEGER,"+
            DailyPlanDbSchema.EventListData.COLUMN_CONTENT + " TEXT," +
            DailyPlanDbSchema.EventListData.COLUMN_TYPE + " INTEGER,"+
            DailyPlanDbSchema.EventListData.COLUMN_PORNUMS + " INTEGER," +
            DailyPlanDbSchema.EventListData.COLUMN_WORK + " INTEGER," +
            DailyPlanDbSchema.EventListData.COLUMN_RELAX + " INTEGER," +
            DailyPlanDbSchema.EventListData.COLUMN_COMPLETED + " INTEGER," +
            DailyPlanDbSchema.EventListData.COLUMN_STATUS + " INTEGER," +
            DailyPlanDbSchema.TimeLineData.COLUMN_STARTTIME + " TEXT," +
            DailyPlanDbSchema.TimeLineData.COLUMN_ENDTIME + " TEXT," +
            DailyPlanDbSchema.TimeLineData.COLUMN_NEXT + " TEXT)";
    private static final String Time_TABLE_CREATE = "CREATE TABLE " + DailyPlanDbSchema.Time.TABLE_NAME + "(" +
            DailyPlanDbSchema.Time._ID + "INITEGER PRIMARY KEY," +
            DailyPlanDbSchema.Time.COLUMN_TIME + " TEXT)";


    public DailyPlanDataBaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Time_TABLE_CREATE);
        db.execSQL(TIMELINE_TABLE_CREATE);
        db.execSQL(EVENTLIST_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
