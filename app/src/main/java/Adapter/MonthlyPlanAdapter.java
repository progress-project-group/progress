package Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.progress_android.MonthlyPlan.MonthlyCard;
import com.progress_android.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class MonthlyPlanAdapter extends RecyclerView.Adapter<MonthlyPlanAdapter.ViewHolder> {
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
        holder.textView.setText(monthlyCard.title);
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public ViewHolder(View itemView){
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.monthly_plan_title);
        }
    }
}
