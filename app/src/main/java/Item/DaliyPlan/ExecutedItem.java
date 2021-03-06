package Item.DaliyPlan;

import java.util.ArrayList;
import java.util.List;

import Item.Item;
import Item.Time.MyTime;
import Item.Time.TimeAmount;

//计划执行中，对于每个content的记录
public class ExecutedItem extends Item {
    //表示本日该计划是否结束（完成或不再做了）
    static public final boolean FINISHED = true;
    static public final boolean WAITING = false;
    boolean status;

    //本日实际花费时间
    TimeAmount timeAmount = new TimeAmount();
    //该计划的预期时间
    TimeAmount planedTimeAmount;    //可为空

    //该计划的开始结束时间，可以有多段
    List<MyTime> startTime = new ArrayList<>();
    List<MyTime> endTime = new ArrayList<>();

    //打断理由（多次）
    List<String> stopReason = new ArrayList<>();        //可为空

    //用于时间轴展示
    //时间上，紧随本次时间段之后的事件
    List<ExecutedItem> next = new ArrayList<>();
    //当前时间段，根据该项获取特定的startTime和endTime
    int currentItem = 0;

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

    public ExecutedItem getNext(){
        if(currentItem>=next.size()){
            return null;
        }
        return next.get(currentItem);
    }

    public MyTime getStartTime(){
        return startTime.get(currentItem);
    }

    public MyTime getEndTime(){
        return endTime.get(currentItem);
    }

    public void toNext(){
        currentItem++;
    }
}
