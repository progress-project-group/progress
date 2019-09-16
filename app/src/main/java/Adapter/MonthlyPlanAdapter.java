package Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.textview.MaterialTextView;
import com.progress_android.R;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class MonthlyPlanAdapter extends RecyclerView.Adapter<MonthlyPlanAdapter.ViewHolder> implements ItemChangedHelper{
    List<MonthlyCard> cards;
    public MonthlyPlanAdapter(List<MonthlyCard> cards){
        this.cards = cards;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Log.d("on", "onCreateViewHolder:"+ parent.getContext().toString());
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.monthly_plan_card, parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MonthlyCard monthlyCard = cards.get(position);
        holder.title.setText(monthlyCard.getTitle());
        holder.progress.setText(monthlyCard.getProgress()+"%");
        holder.type.setText(monthlyCard.getType() ? "可分段":"完整");
        holder.remark.setText(monthlyCard.getRemark());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        holder.due_date.setText(simpleDateFormat.format(monthlyCard.getDue_date()));
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

    class ViewHolder extends RecyclerView.ViewHolder{
        MaterialTextView title, due_date, remark, type, progress;
        public ViewHolder(View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.monthly_plan_title);
            due_date = itemView.findViewById(R.id.monthly_plan_due_date);
            remark = itemView.findViewById(R.id.monthly_plan_remark);
            type = itemView.findViewById(R.id.monthly_plan_type);
            progress = itemView.findViewById(R.id.monthly_plan_progress);
        }
    }

    public static class MonthlyCard implements Serializable {
        private String title;
        private Date due_date;
        private String remark;
        private boolean type;
        private int progress;
        public MonthlyCard(String title ,Date due_date, String remark, boolean type ,int progress){
            this.title = title;
            this.due_date = due_date;
            this.remark = remark;
            this.type = type;
            this.progress = progress;
        }

        public String getTitle() {

            return title;
        }

        public int getProgress() {
            return progress;
        }

        public Date getDue_date() {
            return due_date;
        }

        public String getRemark() {
            return remark;
        }

        public boolean getType(){
            return type;
        }
    }
}


