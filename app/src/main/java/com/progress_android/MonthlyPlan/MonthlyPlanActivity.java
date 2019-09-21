package com.progress_android.MonthlyPlan;

import Adapter.ItemChangedHelper;
import Adapter.MonthlyPlanAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.progress_android.MainActivity;
import com.progress_android.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MonthlyPlanActivity extends AppCompatActivity {

    MonthlyPlanAdapter month_adapter;
    private RecyclerView recyclerView;
    List<MonthlyPlanAdapter.MonthlyCard> monthlyCards = new ArrayList<>();

    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_plan);
        initDrawerLayout();
        //添加按键
        initAddButton();
        //initRecycleView
        initRecycleView();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.monthly_list_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MonthlyPlanAdapter.MonthlyCard newCard = (MonthlyPlanAdapter.MonthlyCard) data
                .getSerializableExtra("new_plan");
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK){
                    //刷新RecycleView
                    monthlyCards.add(newCard);
                    month_adapter.notifyItemInserted(monthlyCards.size() - 1);
                    recyclerView.scrollToPosition(monthlyCards.size() - 1);
                }
                break;
            case 2:
                if (resultCode == RESULT_OK){
                    int position = data.getIntExtra("position" , -2);
                    if (position >= 0 ){
                        monthlyCards.set(position, newCard);
                        month_adapter.notifyItemChanged(position);
                    }
                }
                break;
        }
    }

    private void initDrawerLayout(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.monthly_drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.search) {
                Toast.makeText(MonthlyPlanActivity.this, "search", Toast.LENGTH_SHORT).show();
            }
                return false;
        });
        toolbar.setNavigationOnClickListener(v -> {
                drawerLayout.openDrawer(GravityCompat.START);
                Toast.makeText(MonthlyPlanActivity.this,"nav",Toast.LENGTH_SHORT).show();
        });
    }

    private void initRecycleView(){
        recyclerView = findViewById(R.id.month_recycler_view);
        initCard();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        month_adapter = new MonthlyPlanAdapter(monthlyCards, this);
        ItemTouchHelper itemTouchHelper =
                new ItemTouchHelper(new MonthlyCardTouchCallBack(month_adapter));
        recyclerView.setAdapter(month_adapter);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void initAddButton(){
        FloatingActionButton MAB = findViewById(R.id.monthly_add_button);
        MAB.setOnClickListener(v -> {
                Intent intent = new Intent(MonthlyPlanActivity.this, EventAddActivity.class);
                intent.putExtra("mode", 0);
                startActivityForResult(intent,1);
        });
    }

    private void initCard() {
        MonthlyPlanAdapter.MonthlyCard a = new
                MonthlyPlanAdapter.MonthlyCard("AAAaaa",new Date(),"SDAD",true,50);
        monthlyCards.add(a);
//        MonthlyCard a = new MonthlyCard("a");
//        MonthlyCard b = new MonthlyCard("b");
//        MonthlyCard c = new MonthlyCard("c");
//        MonthlyCard d = new MonthlyCard("d");
//        MonthlyCard e = new MonthlyCard("e");
//        monthlyCards.add(a);
//        monthlyCards.add(b);
//        monthlyCards.add(c);
//        monthlyCards.add(d);
//        monthlyCards.add(e);
//        for(int i =0;i<100;i++)
//        {
//            monthlyCards.add(a);
//        }
    }

    private static class MonthlyCardTouchCallBack extends ItemTouchHelper.Callback{
        private static final int DRAG_FLAGS = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        private static final int SWIPE_FLAGS = ItemTouchHelper.LEFT;
        @Nullable private MaterialCardView dragCardView;
        private final ItemChangedHelper mItemTouchStatus;

        public MonthlyCardTouchCallBack(ItemChangedHelper itemTouchStatus) {
            mItemTouchStatus = itemTouchStatus;
        }
        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            return makeMovementFlags(DRAG_FLAGS,SWIPE_FLAGS);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return mItemTouchStatus.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            mItemTouchStatus.onItemRemove(viewHolder.getAdapterPosition());
        }

        @Override
        public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
            //添加选中效果
            if (actionState == ItemTouchHelper.ACTION_STATE_DRAG && viewHolder != null) {
                dragCardView = (MaterialCardView) viewHolder.itemView;
                dragCardView.setDragged(true);
            } else if (actionState == ItemTouchHelper.ACTION_STATE_IDLE && dragCardView != null) {
                dragCardView.setDragged(false);
                dragCardView = null;
            }
        }
    }
}
