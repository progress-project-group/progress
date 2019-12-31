package DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static DataBase.LongTermScheduleSchema.*;

public class LongTermDataBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "longTermSchedule.db";
    private static final String LONG_TERM_TABLE_CREATE = "CREATE TABLE "+ LongTermTable.NAME + "(" +
            "_id integer primary key autoincrement, "+
            LongTermTable.Cols.DATE + ","+
            LongTermTable.Cols.INDEX+ ","+
            LongTermTable.Cols.NAME+","+
            LongTermTable.Cols.UUID +
            ")";
    private static final String SUB_LONG_TERM_TABLE_CREATE = "CREATE TABLE " + SubLongTermTable.NAME +"("+
            "_id integer primary key autoincrement, "+
            SubLongTermTable.Cols.UUID + "," +
            SubLongTermTable.Cols.FINISHED + "," +
            SubLongTermTable.Cols.PERCENT + "," +
            SubLongTermTable.Cols.SUB_UUID + "," +
            SubLongTermTable.Cols.TITLE +
            ")";


    public LongTermDataBaseHelper(Context context){
        super(context,DATABASE_NAME,null,VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LONG_TERM_TABLE_CREATE);
        db.execSQL(SUB_LONG_TERM_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
