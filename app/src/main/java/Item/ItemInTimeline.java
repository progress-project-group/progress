package Item;


import com.progress_android.R;

import Item.Time.MyTime;
import Item.Time.TimeAmount;

/**
 * Created by Yamaa on 2019/3/20.
 */

public class ItemInTimeline extends Item {
    private MyTime startTime;
    private MyTime endTime;
    private int timePointImageId;

    public void setEndTime(MyTime endTime) {
        this.endTime = endTime;
    }

    public int getTimePointImageId() {

        return timePointImageId;
    }

    public void setTimePointImageId(int timePointImageId) {
        this.timePointImageId = timePointImageId;
    }

    public static ItemInTimeline getFreeItem(ItemInTimeline item1, ItemInTimeline item2){
        TimeAmount gap = MyTime.getGap(item2.startTime, item1.endTime);
        if(gap.getMinutes()!=0) {
            ItemInTimeline freeItem = new ItemInTimeline("free", item1.endTime, gap);
            freeItem.timePointImageId = R.drawable.green_time_point;
            return freeItem;
        }else{
            return null;
        }
    }

    public void setStartTime(MyTime startTime) {
        this.startTime = startTime;
    }

    public String getStartTime() {

        return startTime.toString();
    }

    public ItemInTimeline(String content, MyTime startTime, TimeAmount timeAmount){
        super(content,timeAmount);
        this.startTime = startTime;
        timePointImageId = R.drawable.blue_time_point;
        if(timeAmount.getMinutes()==0){
            timePointImageId = R.drawable.yellow_time_point;
        }
        endTime = MyTime.add(startTime,timeAmount);
    }
}
