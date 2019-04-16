package Adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;

import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultAction;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionDefault;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionDoNothing;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionMoveToSwipedDirection;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableSwipeableItemViewHolder;
import com.progress_android.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Item.EventItem;
import Item.Time.Pomodoro;
import Item.Time.TimeAmount;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder>
        implements DraggableItemAdapter<EventListAdapter.ViewHolder>{

    private List<EventItem> eventList;
    private String TAG = "EventListAdapter";
    private Context context;
    private Button tomato_setting_cancel_button;
    private Button tomato_setting_confirm_button;

    public EventListAdapter(List<EventItem> eventList, Context context){
        setHasStableIds(true);
        this.eventList = eventList;
        this.context = context;
    }


    //dragdropFunction
    @Override
    public boolean onCheckCanStartDrag(@NonNull ViewHolder holder, int position, int x, int y) {
        View dragHandle = holder.dragHandle;

        Log.d(TAG,"onCheckCanStartDrag");

        int handleWidth = dragHandle.getWidth();
        int handleHeight = dragHandle.getHeight();
        int handleLeft = dragHandle.getLeft();
        int handleTop = dragHandle.getTop();

        //Log.d(TAG,"x = " + x + " y = " + y );
        //Log.d(TAG,"left = "+ handleLeft + " right = " + handleLeft")

        Boolean result = (x >= handleLeft) && (x < handleLeft + handleWidth) &&
                (y >= handleTop) && (y < handleTop + handleHeight);
        //Log.d(TAG,"result = "+ result);
        return result;

    }

    @Nullable
    @Override
    public ItemDraggableRange onGetItemDraggableRange(@NonNull ViewHolder holder, int position) {
        return null;
    }

    @Override
    public void onMoveItem(int fromPosition, int toPosition) {
        Log.d(TAG,"onMoveItem");
        EventItem removed = eventList.remove(fromPosition);
        eventList.add(toPosition, removed);
    }

    @Override
    public boolean onCheckCanDrop(int draggingPosition, int dropPosition) {
        Log.d(TAG,"onCheckCanDrop");
        return true;
    }

    @Override
    public void onItemDragStarted(int position) {
        Log.d(TAG,"onMoveItem");
        notifyDataSetChanged();
    }

    @Override
    public void onItemDragFinished(int fromPosition, int toPosition, boolean result) {
        Log.d(TAG,"onItemDragFinished");
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return eventList.get(position).id;
    }


    //SwipeFunction
    /*
    @Override
    public int onGetSwipeReactionType(@NonNull ViewHolder holder, int position, int x, int y) {
        return SwipeableItemConstants.REACTION_CAN_SWIPE_RIGHT;
    }

    @Override
    public void onSwipeItemStarted(@NonNull ViewHolder holder, int position) {
        notifyDataSetChanged();
    }

    @Override
    public void onSetSwipeBackground(@NonNull ViewHolder holder, int position, int type) {
    }

    @Nullable
    @Override
    public SwipeResultAction onSwipeItem(@NonNull ViewHolder holder, int position, int result) {
        if(result == SwipeableItemConstants.RESULT_SWIPED_RIGHT){
            return new SwipeResultActionDefault();
        }else{
            return new SwipeResultActionDoNothing();
        }
    }*/


    //ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.eventitem,parent,false);
        final ViewHolder holder = new ViewHolder(view);

        setTextWatch(holder.eventContent, holder.getAdapterPosition());

        holder.delete_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                holder.delete_button.setClickable(false);
                int position = holder.getAdapterPosition();
                eventList.remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
            }
        });

        holder.tomato_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showTomatoSettingDialog(holder.getAdapterPosition());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EventItem eventItem = eventList.get(position);

        holder.eventContent.setText(eventItem.getContent().toString());
        if(eventItem.getTimeAmount()==null) {
            holder.eventPriority.setText(" " + position);
        }else{
            holder.eventPriority.setText(" " + position + "  " + eventItem.getTimeAmount().getRealtime());
            for(int i=0;i<eventItem.getTimeAmount().getPomodoroNums();++i){
                holder.tomato[i].setVisibility(View.VISIBLE);
            }
        }
    }


    static class ViewHolder extends AbstractDraggableItemViewHolder {
        public EditText eventContent;
        public TextView eventPriority;
        public View dragHandle;
        public Button delete_button;
        public Button tomato_button;
        public ImageView[] tomato = new ImageView[6];

        //public FrameLayout containerView;

        public ViewHolder(View view){
            super(view);
            eventContent = (EditText) view.findViewById(R.id.event_content);
            eventPriority = (TextView) view.findViewById(R.id.event_priority);
            dragHandle = (View) view.findViewById(R.id.drag_handle);
            delete_button = (Button) view.findViewById(R.id.event_delete_button);
            tomato_button = (Button) view.findViewById(R.id.event_tomato_button);
            tomato[0] = (ImageView) view.findViewById(R.id.tomato1);
            tomato[1] = (ImageView) view.findViewById(R.id.tomato2);
            tomato[2] = (ImageView) view.findViewById(R.id.tomato3);
            tomato[3] = (ImageView) view.findViewById(R.id.tomato4);
            tomato[4] = (ImageView) view.findViewById(R.id.tomato5);
            tomato[5] = (ImageView) view.findViewById(R.id.tomato6);

            //containerView = (FrameLayout) view.findViewById(R.id.container);
        }

        /*@NonNull
        @Override
        public View getSwipeableContainerView() {
            return containerView;
        }*/
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public void addEvent(int position, EventItem eventItem){
        eventList.add(position,eventItem);
        notifyItemInserted(position);
    }

    private void showTomatoSettingDialog(final int position){
        EventItem eventItem = eventList.get(position);
        TimeAmount timeAmount = eventItem.getTimeAmount();
        Calendar selectedTime = Calendar.getInstance();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d H:m:s");
            if (timeAmount != null) {
                Log.d(TAG, "timeAmount second = " + timeAmount.getPomodoro().getRelax());
                Date date = dateFormat.parse("2014-1-1 " + timeAmount.getPomodoroNums() + ":" + timeAmount.getPomodoro().getWork() + ":" + timeAmount.getPomodoro().getRelax());
                selectedTime.setTime(date);
            }
            // 按特定格式显示刚设置的时间
            String str = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")).format(selectedTime.getTime());
            Log.d(TAG, str);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar startDate = Calendar.getInstance();
        startDate.set(2014, 1, 1, 1,1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2014, 1, 1, 5, 59);
        //时间选择器 ，自定义布局
        final TimePickerView timePickerDialog = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                Log.d(TAG,"onTimeSelected");
                String data = getTime(date);
                EventItem eventItem = eventList.get(position);
                eventItem.setTimeAmount(getTimeAmount(data));
                notifyItemChanged(position);

                Toast.makeText(context, data, Toast.LENGTH_SHORT).show();
                }
        })
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.tomatotimesettingdialog, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        tomato_setting_cancel_button = v.findViewById(R.id.tomato_setting_cancel_button);
                        tomato_setting_confirm_button = v.findViewById(R.id.tomato_setting_confirm_button);
                        }
                })
                .setContentTextSize(25)
                .setType(new boolean[]{false, false, false, true, true, true})
                .setLineSpacingMultiplier(1.2f)
                .setTextXOffset(0, 0, 0, 0, 0, 0)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。(这个函数有问题)
                .setDividerColor(0xFF24AD9D)
                .isDialog(true)
                .setLabel(null,null,"","","分","分")
                //.setTextXOffset(10,10,10,10,10,10)
                .setDate(selectedTime)
                .build();

        tomato_setting_cancel_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                timePickerDialog.dismiss();
            }
        });

        tomato_setting_confirm_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                timePickerDialog.returnData();
                timePickerDialog.dismiss();
            }
        });

        timePickerDialog.show();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d(TAG, "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    private TimeAmount getTimeAmount(String data){
        int num = Integer.parseInt(data.substring(8,10));
        int workingTime = Integer.parseInt(data.substring(14,16));
        int relaxingTime = Integer.parseInt(data.substring(17,19));
        Log.d(TAG,"relaxingTime = "+ relaxingTime);
        return new TimeAmount(new Pomodoro(workingTime,relaxingTime), num);
    }

    private void setTextWatch(final EditText eventContent, final int position){
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                EventItem eventItem = eventList.get(position);
                eventItem.setContent(s.toString());
            }
        };
    }
}
