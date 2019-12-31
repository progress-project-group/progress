package Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;

import com.google.android.material.textview.MaterialTextView;
import com.progress_android.MonthlyPlan.EventAddActivity;
import com.progress_android.MonthlyPlan.MonthlyPlanActivity;
import com.progress_android.R;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.RecyclerView;

enum State{
    NOTSTARTED, DOING, COMPLETE, DISCARD
}

public class MonthlyPlanAdapter extends RecyclerView.Adapter<MonthlyPlanAdapter.ViewHolder>
        implements ItemChangedHelper, Filterable {
    List<MonthlyCard> cards;
    List<MonthlyCard> sourceCards;
    Context context;
    public MonthlyPlanAdapter(List<MonthlyCard> cards, Context context){
        this.cards = cards;
        this.sourceCards = cards;
        this.context = context;
    }
    
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)     {
        Log.d("on", "onCreateViewHolder:"+ parent.getContext().toString());
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.monthly_plan_card, parent,false);
        ViewHolder holder = new ViewHolder(view);
        //设置点击事件
        holder.monthlyCardView.setOnClickListener(v -> {
            //将要编辑的卡片送到编辑页面
            int position = holder.getAdapterPosition();
            MonthlyCard monthlyCard = cards.get(position);
            Intent intent = new Intent(context, EventAddActivity.class);
            intent.putExtra("mode", 1);
            intent.putExtra("card", monthlyCard);
            intent.putExtra("position", position);
            ((MonthlyPlanActivity)context).startActivityForResult(intent, 2);
        });
        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MonthlyCard monthlyCard = cards.get(position);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String date = simpleDateFormat.format(monthlyCard.getDue_date());
        holder.title.setText(monthlyCard.getTitle());
        holder.date.setText(date);
        holder.progress_bar.setProgress(monthlyCard.getProgress());
        holder.progress_text.setText("进度："+monthlyCard.getN_complete()+"/"+monthlyCard.getN_target());
    }
    @Override
    public int getItemCount() {
        return cards.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(cards, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public boolean onItemRemove(int removePosition) {
        cards.remove(removePosition);
        notifyItemRemoved(removePosition);
        return true;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String searchString = constraint.toString();
                if (!searchString.isEmpty()){
                    List<MonthlyCard> tempCards = new ArrayList<>();
                    for (MonthlyCard card : sourceCards){
                        if (card.getTitle().contains(searchString)){
                            tempCards.add(card);
                        }
                    }
                    cards = tempCards;
                }
                else {
                    cards = sourceCards;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = cards;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                cards = (ArrayList<MonthlyCard>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        View monthlyCardView;
        MaterialTextView title, date, remark, type, progress_text;
        ProgressBar progress_bar;
        public ViewHolder(View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.monthly_plan_title);
            date = itemView.findViewById(R.id.card_date);
            progress_text = itemView.findViewById(R.id.card_progress_text);
            progress_bar = itemView.findViewById(R.id.card_progress_bar);
            monthlyCardView = itemView;
        }
    }

    public static class MonthlyCard implements Serializable {
        private String title;
        private Date due_date;
        private String remark;
        private boolean type;
        private int n_target;
        private int n_complete;
        private State state;
        public MonthlyCard(String title ,Date due_date, String remark, boolean type ,int n_target){
            this.title = title;
            this.due_date = due_date;
            this.remark = remark;
            this.type = type;
            this.n_target = n_target;
            this.n_complete = 0;
            state = State.NOTSTARTED;
        }

        public String getTitle() {

            return title;
        }
        public int getProgress() {

            return (int)((float)n_complete/(float)n_target * 100);
        }
        public Date getDue_date() {
            return due_date;
        }
        public int getN_complete() {
            return n_complete;
        }
        public int getN_target() {
            return n_target;
        }
        public String getRemark() {
            return remark;
        }
        public boolean getType(){
            return type;
        }
        public State getState() {
            return state;
        }

        public void setDue_date(Date due_date) {
            this.due_date = due_date;
        }
        public void setN_target(int n_target) {
            this.n_target = n_target;
        }
        public void setN_complete(int n_complete) {
            this.n_complete = n_complete;
        }
        public void setRemark(String remark) {
            this.remark = remark;
        }
        public void setTitle(String title) {
            this.title = title;
        }
        public void setType(boolean type) {
            this.type = type;
        }
        public void setState(State state) {
            this.state = state;
        }

//        public void setAllExceptState(String title ,Date due_date, String remark, boolean type ,int progress){
//            setDue_date(due_date);
//            setProgress(progress);
//            setDue_date(due_date);
//            setRemark(remark);
//            setTitle(title);
//            setType(type);
//        }
    }
}


