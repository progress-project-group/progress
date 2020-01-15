package Item.DaliyPlan;

import Item.Item;
import Item.Time.TimeAmount;

public class EventItem extends Item{
    private TimeAmount timeAmount;

    private int priority;
    public int id;
    private int completedNum;
    static int idnum = 0;

    public TimeAmount getTimeAmount() {
        return timeAmount;
    }

    public void setTimeAmount(TimeAmount timeAmount) {
        this.timeAmount = timeAmount;
    }

    public EventItem(String content){
        super(content);
        timeAmount = null;
        id = idnum;
        ++idnum;
    }

    public EventItem(String content, TimeAmount timeAmount){
        super(content);
        completedNum = 0;
        id = idnum;
        ++idnum;
        this.timeAmount = timeAmount;
    }

    public EventItem(String content, TimeAmount timeAmount, int variety){
        super(content, variety);
        completedNum = 0;
        id = idnum;
        ++idnum;
        this.timeAmount = timeAmount;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public void addCompletedNum(){
        ++completedNum;
    }

    public int getCompletedNum() {
        return completedNum;
    }

    public void setCompletedNum(int completedNum){
        this.completedNum = completedNum;
    }
}
