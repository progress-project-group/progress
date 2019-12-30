package Item.LongTermSchedule;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import DataBase.LongTermScheduleSchema.SubLongTermTable;

public class LongTermSchedule {
    private String mName;
    private UUID mScheduleID;
    private Date mBeginDate;
    private int mIndex;
    private List<subLongTermSchedule> mSubSchedules;
    //private Context mContext;
    private SQLiteDatabase mDatabase;

    public LongTermSchedule(SQLiteDatabase database){
        //mContext = context;
        mDatabase = database;

        mScheduleID = UUID.randomUUID();
        mBeginDate = new Date();
        mSubSchedules = new ArrayList<>();

    }

    public LongTermSchedule(SQLiteDatabase database, UUID uuid){
        mDatabase = database;

        mScheduleID = uuid;
        mBeginDate = new Date();
        mSubSchedules = getSubSchedules(uuid);

    }

    public int getIndex() {
        return mIndex;
    }

    public void setIndex(int index) {
        mIndex = index;
    }

    private static ContentValues getContentValues(subLongTermSchedule subLongTermSchedule){
        ContentValues contentValues = new ContentValues();
        contentValues.put(SubLongTermTable.Cols.UUID,subLongTermSchedule.getFatherID().toString());
        contentValues.put(SubLongTermTable.Cols.TITLE,subLongTermSchedule.getTitle());
        contentValues.put(SubLongTermTable.Cols.FINISHED,subLongTermSchedule.isFinished()?1:0);
        contentValues.put(SubLongTermTable.Cols.PERCENT,subLongTermSchedule.getPercent());
        contentValues.put(SubLongTermTable.Cols.SUB_UUID,subLongTermSchedule.getSubId().toString());

        return contentValues;
    }

    private subScheduleWrapper querySubSchedule(String where,String[] whereArgs){
        Cursor cursor = mDatabase.query(
                SubLongTermTable.NAME,
                null,
                where,
                whereArgs,
                null,
                null,
                null
        );
        return new subScheduleWrapper(cursor);
    }

    private List<subLongTermSchedule> getSubSchedules(UUID fatherID){
        List<subLongTermSchedule> subLongTermSchedules = new ArrayList<>();

        subScheduleWrapper wrapper = querySubSchedule(SubLongTermTable.Cols.UUID+"=?",new String[]{fatherID.toString()});
        try {
            wrapper.moveToFirst();
            while (!wrapper.isAfterLast()){
                subLongTermSchedules.add(wrapper.getSubSchedule());
                wrapper.moveToNext();
            }
        }finally {
            wrapper.close();
        }

        return subLongTermSchedules;

    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public UUID getScheduleID() {
        return mScheduleID;
    }

    public Date getBeginDate() {
        return mBeginDate;
    }

    public void setBeginDate(Date beginDate) {
        mBeginDate = beginDate;
    }

    public List<subLongTermSchedule> getSubSchedules() {
        return mSubSchedules;
    }

    public void deleteSubSchedule(UUID uuid){
        mDatabase.delete(SubLongTermTable.NAME,
                SubLongTermTable.Cols.UUID +"=? AND " +
                            SubLongTermTable.Cols.SUB_UUID +"=?",
                new String[]{mScheduleID.toString(),uuid.toString()}
                );
        for (int i = 0; i < mSubSchedules.size(); i++) {
            if(mSubSchedules.get(i).getSubId().equals(uuid)){
                mSubSchedules.remove(i);
                break;
            }
        }
        adjustPercent();
        for (subLongTermSchedule sub :
                mSubSchedules) {
            updateSubSchedule(sub.getSubId(), sub);
        }
    }

    public void addSubSchedule(subLongTermSchedule schedule){
        mDatabase.insert(SubLongTermTable.NAME,null,getContentValues(schedule));
        mSubSchedules.add(schedule);
        adjustPercent();
        for (subLongTermSchedule sub :
                mSubSchedules) {
            updateSubSchedule(sub.getSubId(), sub);
        }
    }

    public void setSubSchedule(UUID uuid, subLongTermSchedule subSchedule){
        updateSubSchedule(uuid, subSchedule);
        for (int i = 0; i < mSubSchedules.size(); i++) {
            if(mSubSchedules.get(i).getSubId().equals(uuid)){
                mSubSchedules.set(i,subSchedule);
                break;
            }
        }
        adjustPercent();
        for (subLongTermSchedule sub :
                mSubSchedules) {
            updateSubSchedule(sub.getSubId(), sub);
        }
    }

    private void updateSubSchedule(UUID uuid, subLongTermSchedule subSchedule) {
        mDatabase.update(SubLongTermTable.NAME,getContentValues(subSchedule),
                SubLongTermTable.Cols.UUID + " =? AND "+
                SubLongTermTable.Cols.SUB_UUID+" =?",
                new String[]{mScheduleID.toString(),uuid.toString()});
    }

    private void adjustPercent() {
        int totalPercent = 0;
        for (int i = 0; i < mSubSchedules.size(); i++) {
                totalPercent += mSubSchedules.get(i).getPercent();
        }
        int currentTotal = 0;
        for (int i = 0; i < mSubSchedules.size(); i++) {
            if(i == mSubSchedules.size()-1){
                mSubSchedules.get(i).setPercent(100-currentTotal);
            }
            else {
                int newPercent = Math.round(100 * mSubSchedules.get(i).getPercent()/totalPercent);
                mSubSchedules.get(i).setPercent(newPercent);
                currentTotal += newPercent;
            }
        }
    }

    public subLongTermSchedule getSubSchedule(UUID sonID){
        subScheduleWrapper wrapper = querySubSchedule(
                SubLongTermTable.Cols.UUID + " =? AND "+
                        SubLongTermTable.Cols.SUB_UUID + "=?",
                new String[]{mScheduleID.toString(),sonID.toString()}
        );
        try {
            if(wrapper.getCount()==0){
                return null;
            }
            wrapper.moveToFirst();
            return wrapper.getSubSchedule();
        }finally {
            wrapper.close();
        }

    }

    private class subScheduleWrapper extends CursorWrapper{

        /**
         * Creates a cursor wrapper.
         *
         * @param cursor The underlying cursor to wrap.
         */
        public subScheduleWrapper(Cursor cursor) {
            super(cursor);
        }

        public subLongTermSchedule getSubSchedule(){
            String fatherID = getString(getColumnIndex(SubLongTermTable.Cols.UUID));
            String sonID = getString(getColumnIndex(SubLongTermTable.Cols.SUB_UUID));
            String title = getString(getColumnIndex(SubLongTermTable.Cols.TITLE));
            int percent = getInt(getColumnIndex(SubLongTermTable.Cols.PERCENT));
            int isSolved = getInt(getColumnIndex(SubLongTermTable.Cols.FINISHED));
            boolean solved;
            if(isSolved==0){
                solved = false;
            }else {
                solved = true;
            }

            subLongTermSchedule subLongTermSchedule = new subLongTermSchedule(UUID.fromString(fatherID),UUID.fromString(sonID));
            subLongTermSchedule.setPercent(percent);
            subLongTermSchedule.setTitle(title);
            subLongTermSchedule.setFinished(solved);

            return subLongTermSchedule;
        }
    }

}
