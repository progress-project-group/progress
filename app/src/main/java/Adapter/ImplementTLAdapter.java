package Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.progress_android.R;
import com.progress_android.activity_implement.GalleryUtil.CardAdapterHelper;
import com.progress_android.activity_implement.ImplementActivity;

import java.util.List;

import Item.DaliyPlan.ExecutedItem;
import Item.Item;
import androidx.recyclerview.widget.RecyclerView;

import static Item.Item.CANCEL;
import static Item.Item.COMPLETED;
import static Item.Item.DOING;
import static Item.Item.PAUSE;
import static Item.Item.WAITING;
import static android.view.View.GONE;

public class ImplementTLAdapter extends RecyclerView.Adapter<ImplementTLAdapter.ViewHolder>{
    private List<ExecutedItem> timeLine;
    private static String TAG = "ImplementTLAdapter";
    private Context context;
    private CardAdapterHelper mCardAdapterHelper = new CardAdapterHelper();

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView matterImage;
        TextView matterContent;
        TextView status;
        Button cancelButton;
        Button pauseButton;
        Button startButton;
        Button confirmButton;

        public ViewHolder(View view){
            super(view);
            matterContent = view.findViewById(R.id.ImplementTimeLineContent);
            matterImage = view.findViewById(R.id.ImplementMatterImage);
            status = view.findViewById(R.id.Implement_Status);
            cancelButton = view.findViewById(R.id.Implement_Cancel);
            startButton = view.findViewById(R.id.Implement_Start);
            pauseButton = view.findViewById(R.id.Implement_StatusImage);
            confirmButton = view.findViewById(R.id.Implement_Confirm);
        }
    }

    public ImplementTLAdapter(List<ExecutedItem> itemInTimelineList, Context context){
        this.context = context;
        timeLine = itemInTimelineList;
    }

    @Override
    public ImplementTLAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG,"onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timeline_element,parent,false);
        mCardAdapterHelper.onCreateViewHolder(parent, view);
        final ImplementTLAdapter.ViewHolder holder = new ImplementTLAdapter.ViewHolder(view);

        Log.d(TAG,"construct ViewHolder");
        return holder;
    }

    @Override
    public void onBindViewHolder(ImplementTLAdapter.ViewHolder holder, int position) {
        if(position==0||position== timeLine.size()-1){
            holder.itemView.setVisibility(View.INVISIBLE);
            return ;
        }
        Log.d(TAG, "for position: "+position);
        mCardAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());
        ExecutedItem item = timeLine.get(position);
        holder.matterContent.setText(item.getContent());
        holder.matterImage.setBackgroundResource(Item.iconId[item.getVariety()]);
        //当任务已经被取消或完成时，只显示类别、内容、与状态图标
        if(item.getStatus()==CANCEL||item.getStatus()==COMPLETED){
            holder.status.setVisibility(GONE);
            holder.confirmButton.setVisibility(GONE);
            holder.startButton.setVisibility(GONE);
            holder.cancelButton.setVisibility(GONE);
            if(item.getStatus()==CANCEL) {
                holder.pauseButton.setBackgroundResource(R.drawable.canceled);
            }else{
                holder.pauseButton.setBackgroundResource(R.drawable.completed);
            }
            holder.pauseButton.setVisibility(View.VISIBLE);
            holder.pauseButton.setClickable(false);
        }else if(item.getStatus()==DOING){
            holder.status.setVisibility(GONE);
            holder.confirmButton.setVisibility(View.VISIBLE);
            holder.startButton.setVisibility(GONE);
            holder.cancelButton.setVisibility(View.VISIBLE);
            holder.pauseButton.setBackgroundResource(R.drawable.pause);
            holder.pauseButton.setVisibility(View.VISIBLE);
            holder.pauseButton.setClickable(true);
        }else if(item.getStatus()==WAITING||item.getStatus()==PAUSE){
            holder.status.setVisibility(View.VISIBLE);
            holder.confirmButton.setVisibility(GONE);
            holder.startButton.setVisibility(View.VISIBLE);
            holder.cancelButton.setVisibility(View.VISIBLE);
            holder.pauseButton.setVisibility(GONE);
            if(item.getStatus()==WAITING) {
                holder.status.setText(item.getPlanedStartTime().toString());
            }else if(item.getStatus()==PAUSE){
                holder.status.setText("暂停");
            }
        }
        holder.pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ImplementActivity) context).finishMatter(PAUSE, false);
            }
        });
        holder.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ImplementActivity) context).finishMatter(Item.CANCEL, true);
            }
        });

        holder.startButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d(TAG, "start time line matter! position = "+position);
                ((ImplementActivity) context).startTimeLineMatter(item, position);
            }
        });

        holder.confirmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ((ImplementActivity) context).finishMatter(COMPLETED, true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return timeLine.size();
    }

    public int startMatter(ExecutedItem item){
        int i = 0;
        ExecutedItem tempItem = timeLine.get(i);
        while(item.getPlanedStartTime().later(tempItem.getPlanedStartTime())){
            if(tempItem.getStatus()==Item.WAITING){

                tempItem.setStatus(Item.CANCEL);
            }
            ++i;
        }
        timeLine.add(i,item);
        //notifyItemMoved(position,i);
        notifyDataSetChanged();
        return i;
    }
}
