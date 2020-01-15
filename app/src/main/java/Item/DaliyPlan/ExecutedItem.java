package Item.DaliyPlan;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import Item.Item;
import Item.Time.MyTime;
import Item.Time.TimeAmount;

//计划执行中，对于每个content的记录
public class ExecutedItem extends Item{
    //制定计划时的Item
    private EventItem eventItem;
    private TimeLineItem timeLineItem;
    private boolean eventFlag;
    private boolean timeFlag;

    //本日实际花费时间
    TimeAmount timeAmount = new TimeAmount();
    //该计划的预期时间

    //该计划的开始结束时间，可以有多段
    List<MyTime> startTime = new ArrayList<>();
    List<MyTime> endTime = new ArrayList<>();

    //打断理由（多次）
    //List<String> stopReason = new ArrayList<>();        //可为空

    //用于时间轴展示
    //时间上，紧随本次时间段之后的事件
    List<ExecutedItem> next = new ArrayList<>();
    //当前时间段，根据该项获取特定的startTime和endTime
    int currentItem = 0;

    public ExecutedItem(EventItem eventItem){
        super(eventItem);
        this.eventFlag = true;
        this.eventItem = eventItem;
    }

    public ExecutedItem(TimeLineItem itemInTimeline){
        super(itemInTimeline);
        this.timeFlag = true;
        this.timeLineItem = itemInTimeline;
    }


    public void addEndTime(MyTime time){
        endTime.add(time);
        timeAmount.add(MyTime.getGap(time, startTime.get(startTime.size()-1)));
//        timeLineItem.getStatus() = COMPLETED;
    }

    public void addStartTime(MyTime time){
        startTime.add(time);
//        status = WAITING;
    }

    //public void addReason(String reason){
//        stopReason.add(reason);
//    }

    public void addNext(ExecutedItem executedItem){
        next.add(executedItem);
    }

    public TimeAmount getTimeAmount(){
//        if(status != WAITING) {
            return timeAmount;
//        }
//        else{
//            return new TimeAmount(-1);
//        }
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

    public boolean isEventItem() {
        return eventFlag;
    }

    public boolean isTimeItem(){
        return timeFlag;
    }

    public TimeAmount getPlanedTimeAmount(){
        return eventItem.getTimeAmount();
    }

    public int getPriority(){
        return eventItem.getPriority();
    }

    public MyTime getPlanedStartTime(){
        return timeLineItem.getStartTime();
    }

    public int getCompletedNum(){ return eventItem.getCompletedNum();}

    public void setCompletedNum(int completedNum){eventItem.setCompletedNum(completedNum);}

    public void setTimeLineItem(TimeLineItem timeLineItem) {
        this.timeLineItem = timeLineItem;
    }

    public void setTimeData(List<MyTime> startTime, List<MyTime> endTime){
        Log.d("EventItem", "startTime.size:"+startTime.size()+" endTime,size："+endTime.size());
        this.startTime = startTime;
        this.endTime = endTime;
        if(startTime==null){
            return ;
        }
        if(startTime.size()==0||startTime.size()!=endTime.size()){
            return ;
        }
        for(int i=0; i<startTime.size(); ++i){
            timeAmount.add(MyTime.getGap(endTime.get(i), startTime.get(i)));
        }
    }

    public List<MyTime> getStartTimeData(){
        return startTime;
    }

    public List<MyTime> getEndTimeData(){
        return endTime;
    }

    public void setNext(List<ExecutedItem> next){
        this.next = next;
    }

    public void setPriority(int priority){
        eventItem.setPriority(priority);
    }

    private List<Integer> nextID = new ArrayList<>();
    private int id;

    public List<Integer> getNextID() {
        if(nextID == null){
            nextID = new ArrayList<>();
        }
        return nextID;
    }

    public List<Integer> generateNextID(){
        nextID = new ArrayList<>();
        for(ExecutedItem executedItem: next){
            nextID.add(executedItem.getId());
        }

        return nextID;
    }

    public void setNextID(List<Integer> nextID) {
        this.nextID = nextID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
