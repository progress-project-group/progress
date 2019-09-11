package Item;

import java.util.ArrayList;
import java.util.List;

import Item.Time.TimeAmount;

public class TypeItem{
    List<ExecutedItem> executedItemList = new ArrayList<>();
    int type;
    String content;

    public TypeItem(int type){
        this.type = type;
        switch (type){
            case Item.STUDY: content = Item.sStudy; break;
            case Item.SPORT: content = Item.sSPORT; break;
            case Item.RELAX: content = Item.sRELAX; break;
            case Item.OTHER: content = Item.sOTHER; break;
        }
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
}
