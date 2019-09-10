package Item;

import Item.Time.TimeAmount;

public class EventItem extends Item{
    private TimeAmount timeAmount;

    public int id;
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
        id = idnum;
        ++idnum;
        this.timeAmount = timeAmount;
    }

    public EventItem(String content, TimeAmount timeAmount, int variety){
        super(content, variety);
        id = idnum;
        ++idnum;
        this.timeAmount = timeAmount;
    }
}
