package Item;

import Item.Time.TimeAmount;

/**
 * Created by Yamaa on 2019/3/20.
 */

public abstract class Item {

    private String content;

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {

        return content;
    }

    public Item(String content)
    {
        this.content = content;
    }

}
