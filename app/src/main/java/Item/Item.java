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
    public static final String sStudy = "学习";
    public static final String sSPORT = "运动";
    public static final String sRELAX = "休息";
    public static final String sOTHER = "其他";


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
    public Item(Item item){this.content = item.content; this.type = item.type;}
}
