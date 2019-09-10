package DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DailyPlan.db";
    private static final String TIMELINE_TABLE_CREATE = "CREATE TABLE " + FeedReaderContract.TimeLineData.TABLE_NAME + " (" +
            FeedReaderContract.TimeLineData._ID + " INTEGER PRIMARY KEY," +
            FeedReaderContract.TimeLineData.COLUMN_CONTENT + " TEXT," +
            FeedReaderContract.TimeLineData.COLUMN_TYPE + " INTEGER,"+
            FeedReaderContract.TimeLineData.COLUMN_HOUR + " INTEGER,"+
            FeedReaderContract.TimeLineData.COLUMN_MINS + " INTEGER)";
    private static final String EVENTLIST_TABLE_CREATE = "CREATE TABLE " + FeedReaderContract.EventListData.TABLE_NAME + " (" +
            FeedReaderContract.EventListData._ID + " INTEGER PRIMARY KEY," +
            FeedReaderContract.EventListData.COLUMN_CONTENT + " TEXT," +
            FeedReaderContract.TimeLineData.COLUMN_TYPE + " INTEGER,"+
            FeedReaderContract.EventListData.COLUMN_PORNUMS + " INTEGER," +
            FeedReaderContract.EventListData.COLUMN_WORK + " INTEGER," +
            FeedReaderContract.EventListData.COLUMN_RELAX + " INTEGER)";
    private static final String Time_TABLE_CREATE = "CREATE TABLE " + FeedReaderContract.Time.TABLE_NAME + "(" +
            FeedReaderContract.Time._ID + "INITEGER PRIMARY KEY," +
            FeedReaderContract.Time.COLUMN_TIME + " TEXT)";


    public DataBaseHelper(Context context){
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
