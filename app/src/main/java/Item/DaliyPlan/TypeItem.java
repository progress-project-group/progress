package Item.DaliyPlan;

import java.util.ArrayList;
import java.util.List;

import Item.Item;
import Item.Time.TimeAmount;

public class TypeItem{
    //同种类的事件list
    List<ExecutedItem> executedItemList = new ArrayList<>();
    //事件种类id
    int type;
    //事件种类
    String content;
    //事件的代表颜色
    int colorId;

    public TypeItem(int type){
        this.type = type;
        content = Item.typeName[type];
        colorId = Item.colorId[type];
    }

    public void addExecutedItem(ExecutedItem executedItem){
        executedItemList.add(executedItem);
    }

    public TimeAmount getTimeAmount(){
        TimeAmount timeAmount = new TimeAmount();
        for(int i=0; i<executedItemList.size(); ++i){
            if(executedItemList.get(i).getTimeAmount().getMinutes()>0){
                timeAmount.add(executedItemList.get(i).getTimeAmount());
            }
        }
        return timeAmount;
    }

    public String getContent(){
        return content;
    }

    public int getColor(){return colorId;}

    public List<ExecutedItem> getExecutedItemList() {
        return executedItemList;
    }
}
