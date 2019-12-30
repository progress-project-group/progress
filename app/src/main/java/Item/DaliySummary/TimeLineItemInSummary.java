package Item.DaliySummary;

import Item.Item;
import Item.Time.MyTime;

public class TimeLineItemInSummary extends Item {
    MyTime startTime;
    MyTime endTime;

    public TimeLineItemInSummary(String content, int variety, MyTime startTime, MyTime endTime){
        super(content, variety);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public MyTime getStartTime() {
        return startTime;
    }

    public MyTime getEndTime() {
        return endTime;
    }
}
