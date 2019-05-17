package Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.progress_android.R;
import Dialog.StartTimeSettingDialog;

import java.util.List;

import Item.ItemInTimeline;
import Item.Time.MyTime;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by Yamaa on 2019/3/20.
 */

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.ViewHolder>{
    private List<ItemInTimeline> mItemInTimelineList;
    private static String TAG = "TimeLineAdapter";
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder{
        EditText matterContent;
        TextView startTime;
        ImageView timePointImage;
        View timeAmount;
        View ambTimeline;
        Button timeLineItemDeleteButton;
        Button timeLineItemSettingButton;

        public ViewHolder(View view){
            super(view);
            matterContent = (EditText) view.findViewById(R.id.matter_content);
            startTime = (TextView) view.findViewById(R.id.start_time);
            timePointImage = (ImageView) view.findViewById(R.id.time_point_image);
            timeAmount = (View) view.findViewById(R.id.time_amount);
            ambTimeline = (View) view.findViewById(R.id.ambiguous_timeline);
            timeLineItemDeleteButton = (Button) view.findViewById(R.id.timeLineItem_delete_button);
            timeLineItemSettingButton = (Button) view.findViewById(R.id.timeLineItem_setting_button);
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
        final ViewHolder holder = new ViewHolder(view);

        holder.timeLineItemDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.timeLineItemDeleteButton.setClickable(false);
                int position = holder.getAdapterPosition();
                mItemInTimelineList.remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
            }
        });

        holder.timeLineItemSettingButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                StartTimeSettingDialog newFragment = new StartTimeSettingDialog();
                newFragment.position = holder.getAdapterPosition();
                newFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "startTimePicker");
            }
        });

        holder.matterContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int position = holder.getAdapterPosition();
                ItemInTimeline eventItem = mItemInTimelineList.get(position);
                eventItem.setContent(s.toString());
            }
        });

        Log.d(TAG,"construct ViewHolder");
        return holder;
    }

    public void addItem(int position, ItemInTimeline item){
        mItemInTimelineList.add(position,item);
        notifyItemInserted(position);
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

    public void updateItem(MyTime myTime, int position){
        Log.d(TAG,"updateItem");
        for(int i=0; i<mItemInTimelineList.size(); ++i){
            //Log.d(TAG, "StartTime: " + mItemInTimelineList.get(i).getStartTime())
            if(mItemInTimelineList.get(i).getStartTime().equals(myTime.toString())){
                Toast.makeText(context, "该时间点已有事件", Toast.LENGTH_SHORT).show();
                Log.d(TAG,"Item startTime collide");
                return;
            }
        }
        ItemInTimeline item = mItemInTimelineList.get(position);
        mItemInTimelineList.remove(position);

        notifyItemRemoved(position);
        item.setStartTime(myTime);
        int i = 0;
        while(item.later(mItemInTimelineList.get(i))){
            ++i;
        }
        mItemInTimelineList.add(i,item);
        //notifyItemMoved(position,i);
        notifyItemInserted(i);
    }
}
