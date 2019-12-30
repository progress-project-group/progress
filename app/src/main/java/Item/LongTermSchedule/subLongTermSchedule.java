package Item.LongTermSchedule;

import java.util.UUID;

import Item.Item;

public class subLongTermSchedule {
    private String mTitle;
    private int mPercent;
    private boolean mFinished;
    private UUID mSubId;
    private UUID mFatherID;

    public subLongTermSchedule(UUID fatherID){
        mTitle = "";
        mPercent = 0;
        mFinished = false;
        mSubId = UUID.randomUUID();
        mFatherID = fatherID;
    }

    public subLongTermSchedule(UUID fatherID,UUID sonID){
        mTitle = "";
        mPercent = 0;
        mFinished = false;
        mSubId = sonID;
        mFatherID = fatherID;
    }

    public UUID getFatherID() {
        return mFatherID;
    }

    public void setFatherID(UUID fatherID) {
        mFatherID = fatherID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public int getPercent() {
        return mPercent;
    }

    public void setPercent(int percent) {
        mPercent = percent;
    }

    public boolean isFinished() {
        return mFinished;
    }

    public void setFinished(boolean finished) {
        mFinished = finished;
    }

    public UUID getSubId() {
        return mSubId;
    }
}
