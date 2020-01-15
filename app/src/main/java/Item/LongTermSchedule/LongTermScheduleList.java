package Item.LongTermSchedule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import DataBase.LongTermSchedule.LongTermDataBaseHelper;
import DataBase.LongTermSchedule.LongTermScheduleSchema.LongTermTable;

public class LongTermScheduleList {
    private static LongTermScheduleList sLongTermScheduleList;
    private List<LongTermSchedule> mLongTermSchedules;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public SQLiteDatabase getDatabase() {
        return mDatabase;
    }

    private static ContentValues getContentValues(LongTermSchedule schedule){
        ContentValues contentValues = new ContentValues();
        contentValues.put(LongTermTable.Cols.UUID,schedule.getScheduleID().toString());
        contentValues.put(LongTermTable.Cols.DATE,schedule.getBeginDate().getTime());
        contentValues.put(LongTermTable.Cols.NAME,schedule.getName());
        contentValues.put(LongTermTable.Cols.INDEX,schedule.getIndex());

        return contentValues;
    }

    public static LongTermScheduleList get(Context context){
        if(sLongTermScheduleList == null){
            sLongTermScheduleList = new LongTermScheduleList(context);
        }
        return  sLongTermScheduleList;
    }

    private LongTermScheduleList(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new LongTermDataBaseHelper(mContext).getWritableDatabase();
        mLongTermSchedules = new ArrayList<>();
    }

    public List<LongTermSchedule> getLongTermSchedules(){
        List<LongTermSchedule> schedules = new ArrayList<>();

        LongTermCursorWrapper cursorWrapper = queryLongTermSchedule(null,null,LongTermTable.Cols.INDEX);
        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()){
                schedules.add(cursorWrapper.getSchedule());
                cursorWrapper.moveToNext();
            }
        }finally {
            cursorWrapper.close();
        }
        mLongTermSchedules = schedules;
        return schedules;
    }

    public  LongTermSchedule getLongTermSchedule(UUID uuid){
        LongTermCursorWrapper wrapper = queryLongTermSchedule(
                LongTermTable.Cols.UUID + "=?",
                new String[]{uuid.toString()},null);
        try {
            if(wrapper.getCount()==0){
                return null;
            }
            wrapper.moveToFirst();
            return wrapper.getSchedule();
        }finally {
            wrapper.close();
        }
    }

    public void addLongTermSchedule(LongTermSchedule longTermSchedule){
        int size = mLongTermSchedules.size();
        longTermSchedule.setIndex(size);
        ContentValues contentValues = getContentValues(longTermSchedule);
        mDatabase.insert(LongTermTable.NAME,null,contentValues);

    }

    public void updateLongTermSchedule(LongTermSchedule longTermSchedule){
        String uuid = longTermSchedule.getScheduleID().toString();
        ContentValues values = getContentValues(longTermSchedule);
        mDatabase.update(LongTermTable.NAME,values,
                LongTermTable.Cols.UUID + "=?",
                new String[]{uuid});
    }

    public void removeLongTermSchedule(int index){
        String uuid = mLongTermSchedules.get(index).getScheduleID().toString();
        mDatabase.delete(LongTermTable.NAME,LongTermTable.Cols.UUID+"=?",new String[]{uuid});
        mLongTermSchedules.remove(index);
        reorderIndex();

    }

    private void reorderIndex() {
        for (int i = 0; i < mLongTermSchedules.size(); i++) {
            LongTermSchedule schedule =  mLongTermSchedules.get(i);
            schedule.setIndex(i);
            updateLongTermSchedule(schedule);
        }
    }

    private LongTermCursorWrapper queryLongTermSchedule(String where, String[] whereArgs, String order){
        Cursor cursor = mDatabase.query(
                LongTermTable.NAME,
                null,
                where,
                whereArgs,
                null,
                null,
                order
        );

        return new LongTermCursorWrapper(cursor);
    }

    private class LongTermCursorWrapper extends CursorWrapper{

        /**
         * Creates a cursor wrapper.
         *
         * @param cursor The underlying cursor to wrap.
         */
        public LongTermCursorWrapper(Cursor cursor) {
            super(cursor);
        }

        public LongTermSchedule getSchedule(){
            String uuid = getString(getColumnIndex(LongTermTable.Cols.UUID));
            String name = getString(getColumnIndex(LongTermTable.Cols.NAME));
            long date = getLong(getColumnIndex(LongTermTable.Cols.DATE));
            int index = getInt(getColumnIndex(LongTermTable.Cols.INDEX));

            LongTermSchedule longTermSchedule = new LongTermSchedule(mDatabase,UUID.fromString(uuid));
            longTermSchedule.setIndex(index);
            longTermSchedule.setBeginDate(new Date(date));
            longTermSchedule.setName(name);

            return longTermSchedule;
        }
    }

    public void swapLongTermSchedule(int from,int to){
        if (from < to) {
            for (int i = from; i < to; i++) {
                Collections.swap(mLongTermSchedules, i, i + 1);
            }
        } else {
            for (int i = from; i > to; i--) {
                Collections.swap(mLongTermSchedules, i, i - 1);
            }
        }
        reorderIndex();
    }
}
