package Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.progress_android.R;

import java.util.List;

import Item.DaliyPlan.ExecutedItem;
import Item.Item;
import Item.Time.MyTime;
import androidx.recyclerview.widget.RecyclerView;

public class ImplementTLAdapter extends RecyclerView.Adapter<ImplementTLAdapter.ViewHolder>{
    private List<ExecutedItem> mItemInTimelineList;
    private static String TAG = "ImplementTLAdapter";
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView matterImage;
        TextView matterContent;
        TextView status;

        public ViewHolder(View view){
            super(view);
            matterContent = view.findViewById(R.id.ImplementTimeLineContent);
            matterImage = view.findViewById(R.id.ImplementMatterImage);
            status = view.findViewById(R.id.Implement_Status);
        }
    }

    public ImplementTLAdapter(List<ExecutedItem> itemInTimelineList, Context context){
        this.context = context;
        mItemInTimelineList = itemInTimelineList;
    }

    @Override
    public ImplementTLAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG,"onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timeline_element,parent,false);
        final ImplementTLAdapter.ViewHolder holder = new ImplementTLAdapter.ViewHolder(view);

        Log.d(TAG,"construct ViewHolder");
        return holder;
    }

    @Override
    public void onBindViewHolder(ImplementTLAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "position = "+position);
        ExecutedItem item = mItemInTimelineList.get(position);
        Log.d(TAG, "item: "+(item==null));
        Log.d(TAG, "content = "+item.getContent());
        holder.matterContent.setText(item.getContent());
        holder.matterImage.setBackgroundResource(Item.iconId[item.getVariety()]);
        holder.status.setText(Item.statusText[item.getStatus()]);
    }

    @Override
    public int getItemCount() {
        return mItemInTimelineList.size();
    }

    public int startMatter(ExecutedItem item){
        int i = 0;
        ExecutedItem tempItem = mItemInTimelineList.get(i);
        while(item.getPlanedStartTime().later(tempItem.getPlanedStartTime())){
            if(tempItem.getStatus()==Item.WAITING){

                tempItem.setStatus(Item.CANCEL);
            }
            ++i;
        }
        mItemInTimelineList.add(i,item);
        //notifyItemMoved(position,i);
        notifyDataSetChanged();
        return i;
    }
}
