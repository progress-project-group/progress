package Adapter;

public interface ItemChangedHelper {
    //实现对数据库的更改
    boolean onItemMove(int fromPosition, int toPosition);
    boolean onItemRemove(int removePosition);
}
