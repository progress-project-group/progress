package Item.DaliyPlan;

import java.util.ArrayList;
import java.util.List;

import Item.Item;

//用于生成本日ExecuedItem的时间轴
public class ExecutedItemList {
    List<ExecutedItem> executedItemList = new ArrayList<>();
    ExecutedItem currentItem = null;
    ExecutedItem firstItem = null;

    public ExecutedItemList(){
        //该类通过addOrderPoint与addExecutedItem为ExecutedItem添加next项
    }

    //该函数在为一已有项添加时间段时需要用到
    public void addOrderPoint(ExecutedItem executedItem){
        if(currentItem!=null) {
            currentItem.addNext(executedItem);
        }else{
            firstItem = executedItem;
        }
        currentItem = executedItem;

    }

    //添加新项调用
    public void addExecutedItem(ExecutedItem executedItem){
        executedItemList.add(executedItem);
        addOrderPoint(executedItem);
        currentItem = executedItem;
    }

    public ExecutedItem getNext(ExecutedItem executedItem){
        return executedItem.getNext();
    }
    public int getItemCount(){
        return executedItemList.size();
    }

    public ExecutedItem getFirstItem(){
        return firstItem;
    }
}
