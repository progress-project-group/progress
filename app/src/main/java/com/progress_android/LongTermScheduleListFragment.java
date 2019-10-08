package com.progress_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.ItemTouchHelper.Callback;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import Item.LongTermSchedule;
import Item.LongTermScheduleList;
import Item.subLongTermSchedule;


public class LongTermScheduleListFragment extends Fragment {
    // private static final String ARG_Long_Term = "long_term";
    // waiting for agrs in further usage
    private RecyclerView mScheduleRecyclerView;
    private LongTermScheduleAdapter mAdapter;

    private void updateUI(){
        LongTermScheduleList longTermScheduleList = LongTermScheduleList.get(getActivity());
        List<LongTermSchedule> longTermSchedules = longTermScheduleList.getLongTermSchedules();

        if(mAdapter==null){
            mAdapter = new LongTermScheduleAdapter(longTermSchedules);
            mScheduleRecyclerView.setAdapter(mAdapter);
            Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(mScheduleRecyclerView);
        }
        else {
            mAdapter.setLongTermSchedules(longTermSchedules);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class LongTermScheduleHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener
    {

        private LongTermSchedule mSchedule;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private TextView mPercentTextView;
        private ProgressBar mProgressBar;

        public void bind(LongTermSchedule longTermSchedule){
            mSchedule = longTermSchedule;
            int percent = 0;
            List<subLongTermSchedule> subLongTermSchedules = mSchedule.getSubSchedules();
            for (subLongTermSchedule sub :
                    subLongTermSchedules) {
                if(sub.isFinished()){
                    percent+=sub.getPercent();
                }
            }
            mTitleTextView.setText(mSchedule.getName());
            mDateTextView.setText(mSchedule.getBeginDate().toString());
            mPercentTextView.setText(String.format("%d%%",percent));
            mProgressBar.setProgress(percent);
        }

        public LongTermScheduleHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_long_term_schedule,parent,false));
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.longTermListTitle);
            mDateTextView = (TextView) itemView.findViewById(R.id.longTermListDate);
            mPercentTextView = (TextView) itemView.findViewById(R.id.LongTErmPercent);
            mProgressBar = (ProgressBar)itemView.findViewById(R.id.LongTermProgressBar);
        }

        @Override
        public void onClick(View v) {
            Intent intent = LongTermSchedulePagerActivity.newIntent(getActivity(),mSchedule.getScheduleID());
            startActivity(intent);
        }
    }

    private class LongTermScheduleAdapter extends  RecyclerView.Adapter<LongTermScheduleHolder>
    {
        private List<LongTermSchedule> mLongTermSchedules;

        public LongTermScheduleAdapter(List<LongTermSchedule> longTermSchedules){
            mLongTermSchedules = longTermSchedules;
        }

        public void setLongTermSchedules(List<LongTermSchedule> longTermSchedules){
            mLongTermSchedules = longTermSchedules;
        }

        @NonNull
        @Override
        public LongTermScheduleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new LongTermScheduleHolder(layoutInflater,parent);
        }

        @Override
        public void onBindViewHolder(@NonNull LongTermScheduleHolder holder, int position) {
            System.out.println(mLongTermSchedules.size());
            LongTermSchedule longTermSchedule = mLongTermSchedules.get(position);
            holder.bind(longTermSchedule);
        }

        @Override
        public int getItemCount() {
            return mLongTermSchedules.size();
        }

        public void onItemDismiss(int position) {
            //mLongTermSchedules.remove(position);
            LongTermScheduleList.get(getActivity()).removeLongTermSchedule(position);
            setLongTermSchedules(LongTermScheduleList.get(getActivity()).getLongTermSchedules());
            notifyItemRemoved(position);
            //notifyDataSetChanged();
        }

        public boolean onItemMove(int fromPosition, int toPosition) {
            LongTermScheduleList.get(getActivity()).swapLongTermSchedule(fromPosition,toPosition);
            setLongTermSchedules(LongTermScheduleList.get(getActivity()).getLongTermSchedules());
            notifyItemMoved(fromPosition,toPosition);
            //notifyDataSetChanged();
            return true;
        }
    }

    private class SimpleItemTouchHelperCallback extends Callback {

        private final LongTermScheduleAdapter mAdapter;

        public SimpleItemTouchHelperCallback(LongTermScheduleAdapter adapter) {
            mAdapter = adapter;
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                              RecyclerView.ViewHolder target) {
            mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
        }

    }


    public static LongTermScheduleListFragment newInstance(){
        Bundle args = new Bundle();

        // args.putSerializable(ARG_Long_Term,schedule_id);

        LongTermScheduleListFragment fragment = new LongTermScheduleListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View view = inflater.inflate(R.layout.fragment_long_term_schedule_list,container,false);
        mScheduleRecyclerView = (RecyclerView)view.findViewById(R.id.LongTermScheduleListRecyclerView);
        mScheduleRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.fragment_long_term_schedule_list,menu);
    }

    @Override
    public void onCreate(Bundle savedInstances){
        super.onCreate(savedInstances);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.newLongTermSchedule:
                LongTermSchedule longTermSchedule = new LongTermSchedule(LongTermScheduleList.get(getActivity()).getDatabase());
                LongTermScheduleList.get(getActivity()).addLongTermSchedule(longTermSchedule);
                Intent intent = LongTermSchedulePagerActivity.newIntent(getActivity(),longTermSchedule.getScheduleID());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
