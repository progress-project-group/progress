package Adapter.Implement;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.progress_android.R;
import com.progress_android.activity_implement.ImplementActivity;

import java.util.List;

import Item.DaliyPlan.ExecutedItem;
import Item.Item;
import androidx.recyclerview.widget.RecyclerView;

import static android.view.View.GONE;

public class ImplementELAdapter extends RecyclerView.Adapter<ImplementELAdapter.ViewHolder>{
    private List<ExecutedItem> eventItemList;
    private static String TAG = "ImplementEventListAdapter";
    private Context context;
    private int waitingItemSize;
    private int completedItemSize;

    static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView typeImage;
        private TextView eventContent;
        public TextView eventPriority;
        public Button start_button;
        public Button cancel_button;
        public ImageView statusImage;
        public ImageView[] tomato = new ImageView[6];

        public ViewHolder(View view){
            super(view);
            typeImage = view.findViewById(R.id.typeImage);
            eventContent = view.findViewById(R.id.iEvent_content);
            eventPriority = view.findViewById(R.id.iEvent_priority);
            start_button = view.findViewById(R.id.event_start_button);
            cancel_button = view.findViewById(R.id.event_cancel_button);
            statusImage = view.findViewById(R.id.event_status_image);
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
        completedItemSize = 0;
        waitingItemSize = 0;
        for(int i=0; i<eventItemList.size(); ++i){
            ExecutedItem executedItem = eventItemList.get(i);
            if(executedItem.getStatus()==Item.WAITING){
                ++waitingItemSize;
            }else if(executedItem.getStatus()==Item.COMPLETED){
                ++completedItemSize;
            }
        }
    }

    @Override
    public ImplementELAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG,"onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_element,parent,false);
        final ImplementELAdapter.ViewHolder holder = new ImplementELAdapter.ViewHolder(view);
        Log.d(TAG,"construct ViewHolder");
        return holder;
    }

    @Override
    public void onBindViewHolder(ImplementELAdapter.ViewHolder holder, int position) {
        ExecutedItem eventItem = eventItemList.get(position);
        Log.d(TAG, "content:"+ eventItem.getContent());
        holder.eventContent.setText(eventItem.getContent());
        holder.typeImage.setBackgroundResource(Item.iconId[eventItem.getVariety()]);

        for(int i=0; i<eventItem.getCompletedNum()&&i<6; ++i){
            holder.tomato[i].setBackgroundResource(R.drawable.tomato);
        }

        if(eventItem.getTimeAmount()==null) {
            Log.d(TAG, "error element" + position + " timeAmount is null");
            holder.eventPriority.setText(" " + eventItem.getPriority());
        }else{
            holder.eventPriority.setText(" " + eventItem.getPriority() + "  " + eventItem.getPlanedTimeAmount().getRealtime());
            for(int i=0;i<eventItem.getPlanedTimeAmount().getPomodoroNums();++i){
                holder.tomato[i].setVisibility(View.VISIBLE);
            }
        }

        holder.start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更新activity的当前任务与时间线的数据
                ((ImplementActivity) context).startEventListMatter(eventItem, position);
            }
        });
        holder.cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelMatter(eventItem, position);
            }
        });

        if(eventItem.getStatus()==Item.DOING){
            holder.start_button.setVisibility(GONE);
            holder.cancel_button.setVisibility(GONE);
            holder.statusImage.setVisibility(View.VISIBLE);
            holder.statusImage.setBackgroundResource(R.drawable.pause);
        }else if(eventItem.getStatus()==Item.CANCEL){
            holder.start_button.setVisibility(GONE);
            holder.cancel_button.setVisibility(GONE);
            holder.statusImage.setVisibility(View.VISIBLE);
            holder.statusImage.setBackgroundResource(R.drawable.canceled);
        }else if(eventItem.getStatus()==Item.COMPLETED){
            holder.start_button.setVisibility(GONE);
            holder.cancel_button.setVisibility(GONE);
            holder.statusImage.setVisibility(View.VISIBLE);
            holder.statusImage.setBackgroundResource(R.drawable.completed);
        }else{
            holder.start_button.setVisibility(View.VISIBLE);
            holder.cancel_button.setVisibility(View.VISIBLE);
            holder.statusImage.setVisibility(GONE);
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
        Log.d(TAG,"从"+position+"移动到"+waitingItemSize+completedItemSize);
        notifyItemMoved(position, waitingItemSize+completedItemSize);
        notifyItemChanged(waitingItemSize+completedItemSize);
    }

    public void pauseMatter(ExecutedItem eventItem, int position){
        notifyItemChanged(position);
    }

    public void completeMatter(ExecutedItem eventItem, int position){
        eventItemList.remove(position);
        --waitingItemSize;
        eventItemList.add(waitingItemSize, eventItem);
        ++completedItemSize;
        notifyItemMoved(position, waitingItemSize);
    }
}
