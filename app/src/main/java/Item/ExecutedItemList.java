package Item;

import java.util.ArrayList;
import java.util.List;

public class ExecutedItemList {
    List<ExecutedItem> executedItemList = new ArrayList<>();
    ExecutedItem currentItem = null;
    ExecutedItem firstItem = null;

    public ExecutedItemList(){

    }

    //该函数在为一已有项添加开始与结束时间时需要用到
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
