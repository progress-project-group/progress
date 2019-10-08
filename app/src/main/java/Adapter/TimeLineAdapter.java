package Adapter;

import android.content.Context;
import android.os.Bundle;
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

import Dialog.TypeChooseDialog;
import Item.Item;
import androidx.appcompat.app.AppCompatActivity;

import com.progress_android.DailyPlanActivity;
import com.progress_android.R;
import Dialog.StartTimeSettingDialog;

import java.util.List;

import Item.ItemInTimeline;
import Item.Time.MyTime;
import androidx.recyclerview.widget.RecyclerView;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;


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
        View frontLine;
        View backLine;
        Button timeLineItemDeleteButton;
        Button timeLineItemSettingButton;
        Button typeChooseButton;

        public ViewHolder(View view){
            super(view);
            matterContent = (EditText) view.findViewById(R.id.matter_content);
            startTime = (TextView) view.findViewById(R.id.start_time);
            timePointImage = (ImageView) view.findViewById(R.id.time_point_image);
            frontLine = (View) view.findViewById(R.id.frontLine);
            backLine = view.findViewById(R.id.backLine);
            timeLineItemDeleteButton = (Button) view.findViewById(R.id.timeLineItem_delete_button);
            timeLineItemSettingButton = (Button) view.findViewById(R.id.timeLineItem_setting_button);
            typeChooseButton = (Button) view.findViewById(R.id.timeLineItem_typeChoose_button);
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

        holder.typeChooseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showTypeChooseDialog(holder.getAdapterPosition(), mItemInTimelineList.get(holder.getAdapterPosition()).getVariety());
            }
        });

        Log.d(TAG, "clickable "+holder.matterContent.getText() + ":"+holder.timeLineItemDeleteButton.isClickable());
        holder.timeLineItemDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "delete in TimeLine");
                v.setClickable(false);
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
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(position == 0){
            holder.frontLine.setVisibility(INVISIBLE);
        }else if(position == getItemCount()-1){
            holder.backLine.setVisibility(INVISIBLE);
        }
        ItemInTimeline itemInTimeline = mItemInTimelineList.get(position);
        int imageId = itemInTimeline.getTimePointImageId();
        holder.matterContent.setText(itemInTimeline.getContent());
        holder.startTime.setText(itemInTimeline.getStarttimeText());
        holder.timePointImage.setImageResource(imageId);

        Log.d(TAG, "setType");
        if(itemInTimeline.getVariety()!=Item.NONE) {
            holder.typeChooseButton.setBackgroundResource(Item.iconId[itemInTimeline.getVariety()]);
        }

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

    private void showTypeChooseDialog(int position, int type){
        TypeChooseDialog dialog = new TypeChooseDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("FragmentTag", DailyPlanActivity.FragmentTag_TimeLine);
        bundle.putInt("POSITION",position);
        bundle.putInt("TYPE", type);
        dialog.setArguments(bundle);
        dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "TypeChoose of TimeLine");
    }

    @Override
    public int getItemCount() {
        return mItemInTimelineList.size();
    }

    public void updateItem(MyTime myTime, int position){
        Log.d(TAG,"updateItem");
        for(int i=0; i<mItemInTimelineList.size(); ++i){
            //Log.d(TAG, "StartTime: " + mItemInTimelineList.get(i).getStarttimeText())
            if(mItemInTimelineList.get(i).getStarttimeText().equals(myTime.toString())){
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
    public void setItemType(int position, int type){
        mItemInTimelineList.get(position).setVariety(type);
        notifyItemChanged(position);
    }
}
