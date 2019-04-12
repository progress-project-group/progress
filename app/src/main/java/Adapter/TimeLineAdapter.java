package Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.progress_android.R;

import java.util.List;

import Item.ItemInTimeline;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by Yamaa on 2019/3/20.
 */

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.ViewHolder>{
    private List<ItemInTimeline> mItemInTimelineList;
    private static String TAG = "TimeLineAdapter";
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView matterContent;
        TextView startTime;
        ImageView timePointImage;
        View timeAmount;
        View ambTimeline;

        public ViewHolder(View view){
            super(view);
            matterContent = (TextView) view.findViewById(R.id.matter_content);
            startTime = (TextView) view.findViewById(R.id.start_time);
            timePointImage = (ImageView) view.findViewById(R.id.time_point_image);
            timeAmount = (View) view.findViewById(R.id.time_amount);
            ambTimeline = (View) view.findViewById(R.id.ambiguous_timeline);
        }
    }

    public TimeLineAdapter(List<ItemInTimeline> itemInTimelineList, Context context){
        this.context = context;
        mItemInTimelineList = itemInTimelineList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG,"onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timelineitem,parent,false);
        ViewHolder holder = new ViewHolder(view);
        Log.d(TAG,"construct ViewHolder");
        return holder;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemInTimeline itemInTimeline = mItemInTimelineList.get(position);
        int imageId = itemInTimeline.getTimePointImageId();
        holder.matterContent.setText(itemInTimeline.getContent());
        holder.startTime.setText(itemInTimeline.getStartTime());
        holder.timePointImage.setImageResource(imageId);

        /*修改时间轴颜色、长度
        LinearLayout.LayoutParams timeAmountLayoutParams = (LinearLayout.LayoutParams) holder.timeAmount.getLayoutParams();
        timeAmountLayoutParams.height = itemInTimeline.getTimeAmount().getMinutes();
        if(imageId == R.drawable.blue_time_point){
            Log.d(TAG, "blueID "+ ContextCompat.getColor(context,R.color.blue) + "length = " +timeAmountLayoutParams.height);
            holder.timeAmount.setBackgroundColor(ContextCompat.getColor(context,R.color.blue));
            timeAmountLayoutParams.height *= 5;
        }else if(imageId == R.drawable.green_time_point){
            Log.d(TAG,"setFreeColor, length = "+ timeAmountLayoutParams.height);
            timeAmountLayoutParams.height *= 3;
            holder.timeAmount.setBackgroundColor(ContextCompat.getColor(context,R.color.green));
        }else if(imageId == R.drawable.yellow_time_point){
            holder.timeAmount.setBackgroundColor(ContextCompat.getColor(context,R.color.yellow));
            LinearLayout.LayoutParams ambTimelineParams = (LinearLayout.LayoutParams) holder.ambTimeline.getLayoutParams();
            ambTimelineParams.height = timeAmountLayoutParams.height*4;
            holder.ambTimeline.setLayoutParams(ambTimelineParams);
            holder.ambTimeline.setVisibility(View.VISIBLE);
        }
        if(timeAmountLayoutParams.height<100){
            timeAmountLayoutParams.height = 100;
        }*/


        //holder.timeAmount.setLayoutParams(timeAmountLayoutParams);
    }

    @Override
    public int getItemCount() {
        return mItemInTimelineList.size();
    }
}
