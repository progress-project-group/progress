package Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.progress_android.R;
import com.progress_android.activity_implement.ImplementActivity;

import java.util.List;

import Item.DaliyPlan.ExecutedItem;
import Item.Item;
import androidx.recyclerview.widget.RecyclerView;

public class ImplementELAdapter extends RecyclerView.Adapter<ImplementELAdapter.ViewHolder>{
    private List<ExecutedItem> eventItemList;
    private static String TAG = "ImplementEventListAdapter";
    private Context context;
    private boolean nextMatter = true;
    private static final int showedItemNum = 3;
    private int waitingItemSize;
    private int completedItemSize;

    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView eventContent;
        public TextView eventPriority;
        public Button start_button;
        public ImageView[] tomato = new ImageView[6];

        public ViewHolder(View view){
            super(view);
            eventContent = view.findViewById(R.id.iEvent_content);
            eventPriority = view.findViewById(R.id.iEvent_priority);
            start_button = view.findViewById(R.id.event_start_button);
            tomato[0] = view.findViewById(R.id.iTomato1);
            tomato[1] = view.findViewById(R.id.iTomato2);
            tomato[2] = view.findViewById(R.id.iTomato3);
            tomato[3] = view.findViewById(R.id.iTomato4);
            tomato[4] = view.findViewById(R.id.iTomato5);
            tomato[5] = view.findViewById(R.id.iTomato6);

        }
    }

    public ImplementELAdapter(List<ExecutedItem> eventItemList, Context context){
        this.eventItemList = eventItemList;
        this.context = context;
    }

    @Override
    public ImplementELAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG,"onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_element,parent,false);
        final ImplementELAdapter.ViewHolder holder = new ImplementELAdapter.ViewHolder(view);
        holder.start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!nextMatter){
                    Toast.makeText(context,"已有正在执行的任务！", Toast.LENGTH_SHORT).show();
                    return;
                }
                //判断之前是否有任务没有完成
                //更新activity的当前任务与时间线的数据
                if(((ImplementActivity) context).startMatter(eventItemList.get(holder.getAdapterPosition()), holder.getAdapterPosition())){
                    nextMatter = false;
                    eventItemList.get(holder.getAdapterPosition()).setStatus(Item.DOING);
                    holder.start_button.setBackgroundResource(R.drawable.pause);
                }
            }
        });


        Log.d(TAG,"construct ViewHolder");
        return holder;
    }

    @Override
    public void onBindViewHolder(ImplementELAdapter.ViewHolder holder, int position) {
        ExecutedItem eventItem = eventItemList.get(position);
        Log.d(TAG, "content:"+ eventItem.getContent());
        holder.eventContent.setText(eventItem.getContent());

        if(eventItem.getTimeAmount()==null) {
            Log.d(TAG, "error element" + position + " timeAmount is null");
            holder.eventPriority.setText(" " + eventItem.getPriority());
        }else{
            holder.eventPriority.setText(" " + eventItem.getPriority() + "  " + eventItem.getPlanedTimeAmount().getRealtime());
            for(int i=0;i<eventItem.getPlanedTimeAmount().getPomodoroNums();++i){
                holder.tomato[i].setVisibility(View.VISIBLE);
            }
        }

        if(eventItem.getStatus()==Item.DOING){
            holder.start_button.setBackgroundResource(R.drawable.pause);
            holder.start_button.setClickable(false);
        }else if(eventItem.getStatus()==Item.CANCEL){
            holder.start_button.setBackgroundResource(R.drawable.cancel);
            holder.start_button.setClickable(false);
        }else{
            holder.start_button.setBackgroundResource(R.drawable.start);
        }
    }

    @Override
    public int getItemCount() {
        return eventItemList.size();
    }

    public void cancelMatter(ExecutedItem eventItem, int position){
        eventItemList.remove(position);
        --waitingItemSize;
        eventItemList.add(waitingItemSize+completedItemSize, eventItem);
        notifyItemMoved(position, waitingItemSize+completedItemSize);
    }

    public void pauseMatter(ExecutedItem eventItem, int position){

    }

    public void completeMatter(ExecutedItem eventItem, int position){
        eventItemList.remove(position);
        --waitingItemSize;
        eventItemList.add(waitingItemSize, eventItem);
        ++completedItemSize;
        notifyItemMoved(position, waitingItemSize);
    }
}
