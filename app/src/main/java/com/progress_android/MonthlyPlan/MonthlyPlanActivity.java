package com.progress_android.MonthlyPlan;

import Adapter.MonthlyPlanAdapter;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.progress_android.MainActivity;
import com.progress_android.R;

import java.util.ArrayList;
import java.util.List;

public class MonthlyPlanActivity extends AppCompatActivity {

    private static final String TAG = "Monthly";
    private Context context = this;
    private RecyclerView recyclerView;
    List<MonthlyCard> monthlyCards = new ArrayList<>();

    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_plan);
        initDrawerLayout();
        //添加按键
        initFAB();
        //initRecycleView
        initRecycle();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void initDrawerLayout(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.monthly_drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        toolbar.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()){
                    case R.id.search:
                        Toast.makeText(MonthlyPlanActivity.this,"search",Toast.LENGTH_SHORT).show();
                }
                return false;
        });
        toolbar.setNavigationOnClickListener(v -> {
                drawerLayout.openDrawer(GravityCompat.START);
                Toast.makeText(MonthlyPlanActivity.this,"nav",Toast.LENGTH_SHORT).show();
        });
    }

    public void initRecycle(){
        recyclerView = findViewById(R.id.month_recycler_view);
        init();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        MonthlyPlanAdapter adapter = new MonthlyPlanAdapter(monthlyCards);
        recyclerView.setAdapter(adapter);
    }

    public void initFAB(){
        FloatingActionButton MAB = findViewById(R.id.monthly_add_button);
        MAB.setOnClickListener(v -> {
                Intent intent = new Intent(MonthlyPlanActivity.this, EventAddActivity.class);
                startActivity(intent);
        });
    }

    public void init()
    {
        MonthlyCard a = new MonthlyCard("a");
        MonthlyCard b = new MonthlyCard("b");
        MonthlyCard c = new MonthlyCard("c");
        MonthlyCard d = new MonthlyCard("d");
        MonthlyCard e = new MonthlyCard("e");
        monthlyCards.add(a);
        monthlyCards.add(b);
        monthlyCards.add(c);
        monthlyCards.add(d);
        monthlyCards.add(e);
        for(int i =0;i<100;i++)
        {
            monthlyCards.add(a);
        }
    }
}
