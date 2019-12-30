package Item;

import com.progress_android.R;

/**
 * Created by Yamaa on 2019/3/20.
 */

public abstract class Item {
    public static final int NONE = -1;
    public static final int STUDY = 0;
    public static final int SPORT = 1;
    public static final int RELAX = 2;
    public static final int OTHER = 3;

    public static final int typeNum = 4;

    public static final String[] typeName = new String[]{"学习", "运动", "休息", "其他"};
    public static final int[] colorId = new int[]{R.color.blue, R.color.red, R.color.green, R.color.yellow};
    public static final int[] iconId = new int[]{R.drawable.study, R.drawable.sport, R.drawable.relax, R.drawable.other, R.drawable.type_button};

    public static final int typeIconId = R.drawable.type_button;
    public static final String none = "无";

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
        type = -1;
    }
    public Item(String content, int variaty) { this.content = content; this.type = variaty;}
    public Item(Item item){this.content = item.content; this.type = item.type;}

    public int getIcon(int type){
        if(type == -1||type>=iconId.length){
            return typeIconId;
        }
        return iconId[type];
    }
}
