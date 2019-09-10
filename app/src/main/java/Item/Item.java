package Item;

import Item.Time.TimeAmount;

/**
 * Created by Yamaa on 2019/3/20.
 */

public abstract class Item {
    public static final int STUDY = 1;
    public static final int SPORT = 2;
    public static final int RELAX = 3;
    public static final int OTHER = 4;

    private String content;
    int type;

    public void setVariety(int variety) {
        this.type = variety;
    }

    public int getVariety() {
        return type;
    }

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
    public Item(String content, int variaty) { this.content = content; this.type = variaty;}
}
