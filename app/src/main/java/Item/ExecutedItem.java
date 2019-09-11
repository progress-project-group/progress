package Item;

import com.github.mikephil.charting.utils.EntryXComparator;

import java.util.ArrayList;
import java.util.List;

import Item.Time.MyTime;
import Item.Time.TimeAmount;

public class ExecutedItem extends Item {
    static public final boolean FINISHED = true;
    static public final boolean WAITING = false;

    boolean status;
    TimeAmount timeAmount = new TimeAmount();
    TimeAmount planedTimeAmount;    //可为空
    List<MyTime> startTime = new ArrayList<>();
    List<MyTime> endTime = new ArrayList<>();
    List<String> stopReason = new ArrayList<>();        //可为空
    List<ExecutedItem> next = new ArrayList<>();

    public ExecutedItem(EventItem eventItem){
        super(eventItem);
        planedTimeAmount = eventItem.getTimeAmount();
    }

    public ExecutedItem(ItemInTimeline itemInTimeline){
        super(itemInTimeline);
        startTime.add(itemInTimeline.getStartTime());
    }

    //used for test
    public ExecutedItem(String content, int type, MyTime startTime, MyTime endTime, TimeAmount planedTimeAmount){
        super(content, type);
        addStartTime(startTime);
        addEndTime(endTime);
        this.planedTimeAmount = planedTimeAmount;
    }
    public ExecutedItem(String content, int type, MyTime startTime, MyTime endTime){
        super(content, type);
        addStartTime(startTime);
        addEndTime(endTime);
    }


    public void addEndTime(MyTime time){
        endTime.add(time);
        timeAmount.add(MyTime.getGap(time, startTime.get(startTime.size()-1)));
        status = FINISHED;
    }

    public void addStartTime(MyTime time){
        startTime.add(time);
        status = WAITING;
    }

    public void addReason(String reason){
        stopReason.add(reason);
    }

    public void addNext(ExecutedItem executedItem){
        next.add(executedItem);
    }

    public TimeAmount getTimeAmount(){
        if(status == FINISHED) {
            return timeAmount;
        }
        else{
            return new TimeAmount(-1);
        }
    }

}
