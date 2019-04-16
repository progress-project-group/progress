package Item;

import Item.Time.TimeAmount;

public class EventItem extends Item{
    public enum varieties{
        xuexi, duanlian, dushu, yule
    }

    private TimeAmount timeAmount;
    varieties variety;
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
        variety = null;
        timeAmount = null;
        id = idnum;
        ++idnum;
    }

    public EventItem(String content, TimeAmount timeAmount){
        super(content);
        id = idnum;
        ++idnum;
        this.variety = null;
        this.timeAmount = timeAmount;
    }

    public EventItem(String content, TimeAmount timeAmount, varieties variety){
        super(content);
        id = idnum;
        ++idnum;
        this.variety = variety;
        this.timeAmount = timeAmount;
    }

    public void setVariety(varieties variety) {
        this.variety = variety;
    }

    public varieties getVariety() {
        return variety;
    }
}
