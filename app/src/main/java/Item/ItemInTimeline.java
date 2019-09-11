package Item;


import com.progress_android.R;

import Item.Time.MyTime;
import Item.Time.TimeAmount;

/**
 * Created by Yamaa on 2019/3/20.
 */

public class ItemInTimeline extends Item {
    private MyTime startTime;
    private int timePointImageId;


    public int getTimePointImageId() {

        return timePointImageId;
    }

    public void setTimePointImageId(int timePointImageId) {
        this.timePointImageId = timePointImageId;
    }

    /*public static ItemInTimeline getFreeItem(ItemInTimeline item1, ItemInTimeline item2){
        TimeAmount gap = MyTime.getGap(item2.startTime, item1.endTime);
        if(gap.getMinutes()!=0) {
            ItemInTimeline freeItem = new ItemInTimeline("free", item1.endTime, gap);
            freeItem.timePointImageId = R.drawable.green_time_point;
            return freeItem;
        }else{
            return null;
        }
    }*/

    public void setStartTime(MyTime startTime) {
        this.startTime = startTime;
    }

    public int getStartTimeHour(){
        return startTime.getHours();
    }

    public int getStartTimeMins(){
        return startTime.getMins();
    }

    public String getStarttimeText() {
        return startTime.toString();
    }

    public MyTime getStartTime(){return  startTime;}

    public boolean later(ItemInTimeline item){
        return this.startTime.later(item.startTime);
    }

    public boolean equals(ItemInTimeline item) {return this.startTime.equals(item.startTime);}

    public ItemInTimeline(String content, MyTime startTime){
        super(content);
        this.startTime = startTime;
        timePointImageId = R.drawable.blue_time_point;
    }

    public ItemInTimeline(String content, MyTime startTime, int type){
        super(content,type);
        this.startTime = startTime;
        timePointImageId = R.drawable.blue_time_point;
    }
}
