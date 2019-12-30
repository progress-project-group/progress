package Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.progress_android.R;

import java.util.List;

import Item.DaliySummary.TimeLineItemInSummary;
import Item.Item;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class TimeLineSummaryAdapter extends RecyclerView.Adapter<TimeLineSummaryAdapter.ViewHolder>{
    private List<TimeLineItemInSummary> itemList;
    private static String TAG = "TimeLineAdapter";
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView matterContent;
        TextView startTime;
        TextView endTime;
        View timeLine;
        ImageView endPoint;

        public ViewHolder(View view){
            super(view);
            matterContent = view.findViewById(R.id.summary_matter_content);
            startTime =  view.findViewById(R.id.start_time);
            endTime = view.findViewById(R.id.end_time);
            timeLine = view.findViewById(R.id.timeLine);
            endPoint = view.findViewById(R.id.end_point);
        }
    }

    public TimeLineSummaryAdapter(List<TimeLineItemInSummary> itemInTimelineList, Context context){
        this.context = context;
        itemList = itemInTimelineList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG,"onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.summary_timelineitem,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TimeLineItemInSummary timeLineItemInSummary = itemList.get(position);
        holder.matterContent.setText(timeLineItemInSummary.getContent());
        if(timeLineItemInSummary.getVariety() == Item.NONE){
            holder.timeLine.setBackgroundColor(ContextCompat.getColor(context,R.color.gray));
        }
        holder.startTime.setText(timeLineItemInSummary.getStartTime().toString());
        if(position == itemList.size()-1){
            holder.endPoint.setVisibility(View.VISIBLE);
            holder.endTime.setVisibility(View.VISIBLE);
            holder.endTime.setText(timeLineItemInSummary.getEndTime().toString());
        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
