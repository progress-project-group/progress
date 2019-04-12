package Item;

import Item.Time.TimeAmount;

/**
 * Created by Yamaa on 2019/3/20.
 */

public abstract class Item {

    private String content;
    private TimeAmount timeAmount;

    public void setTimeAmount(TimeAmount timeAmount) {
        this.timeAmount = timeAmount;
    }

    public TimeAmount getTimeAmount() {

        return timeAmount;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {

        return content;
    }

    public Item(String content, TimeAmount timeAmount){
        this.content = content;
        this.timeAmount = timeAmount;
    }
}
