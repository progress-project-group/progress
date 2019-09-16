package Item;

import com.progress_android.R;

import java.util.ArrayList;
import java.util.List;

import Item.Time.TimeAmount;

public class TypeItem{
    List<ExecutedItem> executedItemList = new ArrayList<>();
    int type;
    String content;
    int colorId;

    public TypeItem(int type){
        this.type = type;
        switch (type){
            case Item.STUDY: content = Item.sStudy; colorId = R.color.blue; break;
            case Item.SPORT: content = Item.sSPORT; colorId = R.color.red;break;
            case Item.RELAX: content = Item.sRELAX; colorId = R.color.green;break;
            case Item.OTHER: content = Item.sOTHER; colorId = R.color.yellow;break;
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

    public int getColor(){return colorId;}

    public List<ExecutedItem> getExecutedItemList() {
        return executedItemList;
    }
}
