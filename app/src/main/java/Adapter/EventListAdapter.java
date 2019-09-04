package Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
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

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Dialog.TypeChooseDialog;
import Item.EventItem;
import Item.Time.Pomodoro;
import Item.Time.TimeAmount;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder>
        implements DraggableItemAdapter<EventListAdapter.ViewHolder>,
                    SwipeableItemAdapter<EventListAdapter.ViewHolder>{

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

    @Override
    public int onGetSwipeReactionType(@NonNull ViewHolder holder, int position, int x, int y) {
        return SwipeableItemConstants.REACTION_CAN_SWIPE_LEFT;
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
    }


    //ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.eventitem,parent,false);
        final ViewHolder holder = new ViewHolder(view);

        holder.type_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showTypeChooseDialog(holder.getAdapterPosition(), eventList.get(holder.getAdapterPosition()).getVariety());
            }
        });

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

        holder.eventContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int position = holder.getAdapterPosition();
                EventItem eventItem = eventList.get(position);
                eventItem.setContent(s.toString());
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EventItem eventItem = eventList.get(position);

        holder.eventContent.setText(eventItem.getContent());

        if(eventItem.getTimeAmount()==null) {
            holder.eventPriority.setText(" " + position);
        }else{
            holder.eventPriority.setText(" " + position + "  " + eventItem.getTimeAmount().getRealtime());
            for(int i=0;i<eventItem.getTimeAmount().getPomodoroNums();++i){
                holder.tomato[i].setVisibility(View.VISIBLE);
            }
        }
    }


    static class ViewHolder extends AbstractDraggableSwipeableItemViewHolder {
        public EditText eventContent;
        public TextView eventPriority;
        public View dragHandle;
        public Button type_button;
        public Button delete_button;
        public Button tomato_button;
        public ImageView[] tomato = new ImageView[6];

        public FrameLayout containerView;

        public ViewHolder(View view){
            super(view);
            eventContent = (EditText) view.findViewById(R.id.event_content);
            eventPriority = (TextView) view.findViewById(R.id.event_priority);
            dragHandle = (View) view.findViewById(R.id.drag_handle);
            type_button = (Button) view.findViewById(R.id.event_type_button);
            delete_button = (Button) view.findViewById(R.id.event_delete_button);
            tomato_button = (Button) view.findViewById(R.id.event_tomato_button);
            tomato[0] = (ImageView) view.findViewById(R.id.tomato1);
            tomato[1] = (ImageView) view.findViewById(R.id.tomato2);
            tomato[2] = (ImageView) view.findViewById(R.id.tomato3);
            tomato[3] = (ImageView) view.findViewById(R.id.tomato4);
            tomato[4] = (ImageView) view.findViewById(R.id.tomato5);
            tomato[5] = (ImageView) view.findViewById(R.id.tomato6);

            containerView = (FrameLayout) view.findViewById(R.id.container);
        }

        @NonNull
        @Override
        public View getSwipeableContainerView() {
            return containerView;
        }
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public void addEvent(int position, EventItem eventItem){
        eventList.add(position,eventItem);
        notifyItemInserted(position);
    }

    private void showTypeChooseDialog(int position, int type){
        TypeChooseDialog dialog = new TypeChooseDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("POSITION",position);
        bundle.putInt("TYPE", type);
        dialog.setArguments(bundle);
        dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "TypeChoose of EventList");
    }

    private void showTomatoSettingDialog(final int position){

        final OptionsPickerView tomatoSettingDialog = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                EventItem eventItem = eventList.get(position);
                eventItem.setTimeAmount(new TimeAmount(new Pomodoro(option2+1,options3+1),options1+1));
                notifyItemChanged(position);
                Toast.makeText(context,"1="+options1 +" 2="+option2+" 3="+options3,Toast.LENGTH_LONG).show();

            }
        })
                .setLayoutRes(R.layout.tomatotimesettingdialog, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        tomato_setting_cancel_button = (Button) v.findViewById(R.id.tomato_setting_cancel_button);
                        tomato_setting_confirm_button = (Button) v.findViewById(R.id.tomato_setting_confirm_button);
                    }
                })
                .isDialog(true)
                .setOutSideCancelable(false)
                .build();
        TimeAmount timeAmount = eventList.get(position).getTimeAmount();
        if(timeAmount!=null){
            Log.d(TAG,"num=" + timeAmount.getPomodoroNums() +" "+timeAmount.getPomodoro().toString());
            tomatoSettingDialog.setSelectOptions(timeAmount.getPomodoroNums()-1, timeAmount.getPomodoro().getWork()-1,timeAmount.getPomodoro().getRelax()-1);
        }else {
            tomatoSettingDialog.setSelectOptions(0, 0, 0);
        }
        List<Integer> num = Arrays.asList(1,2,3,4);
        List<Integer> mins = new ArrayList<>();
        for(int i=0;i<59;++i){
            mins.add(i+1);
        }
        tomatoSettingDialog.setNPicker(num,mins,mins);

        tomato_setting_cancel_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                tomatoSettingDialog.dismiss();
            }
        });

        tomato_setting_confirm_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                tomatoSettingDialog.returnData();
                tomatoSettingDialog.dismiss();
            }
        });

        tomatoSettingDialog.show();
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

}
