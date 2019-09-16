package com.progress_android;

import Item.ExecutedItemList;
import Item.Item;
import Item.ExecutedItem;
import Item.Time.MyTime;
import Item.Time.TimeAmount;
import Item.TypeItem;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Typeface;
import android.os.Bundle;

import com.progress_android.fragment_summary.SpecificTAFragment;
import com.progress_android.fragment_summary.TimeAllocationFragment;

import java.util.ArrayList;
import java.util.List;

public class DailySummaryActivity extends AppCompatActivity {

    ViewPager viewPager;
    int NUM_ITEMS = 2;
    public static final int typeNum = 4;
    ExecutedItemList executedItemList = new ExecutedItemList();
    List<String> typeLabel = new ArrayList<>();
    List<TypeItem> typeItemList = new ArrayList<>();

    //used for test
    TypeItem studyItemList = new TypeItem(Item.STUDY);
    TypeItem sportItemList = new TypeItem(Item.SPORT);
    TypeItem relaxItemList = new TypeItem(Item.RELAX);
    TypeItem otherItemList = new TypeItem(Item.OTHER);

    List<Fragment> fragmentList = new ArrayList<>();
    List<String> pageTitleList = new ArrayList<>();

    public static Typeface tfRegular, tfLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_summary);
        initDefaultList();
        SummaryPagerAdapter adapter = new SummaryPagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.summary_pager);
        viewPager.setAdapter(adapter);
        tfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");
        tfLight = Typeface.createFromAsset(getAssets(),"OpenSans-Light.ttf");
    }


    public class SummaryPagerAdapter extends FragmentPagerAdapter {
        public SummaryPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return pageTitleList.get(position);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }
    }
    private void initDefaultList(){
        ExecutedItem item1 = new ExecutedItem("写代码", Item.STUDY, new MyTime(8,0), new MyTime(9,30), new TimeAmount(5*60));
        studyItemList.addExecutedItem(item1);
        executedItemList.addExecutedItem(item1);

        ExecutedItem item2 = new ExecutedItem("写英语卷子", Item.STUDY, new MyTime(10,0), new MyTime(11,0), new TimeAmount(210));
        studyItemList.addExecutedItem(item2);
        executedItemList.addExecutedItem(item2);

        ExecutedItem item3 = new ExecutedItem("吃饭", Item.OTHER, new MyTime(11,0), new MyTime(12,0));
        otherItemList.addExecutedItem(item3);
        executedItemList.addExecutedItem(item3);

        ExecutedItem item4 = new ExecutedItem("休息", Item.RELAX, new MyTime(12,0), new MyTime(13,0));
        relaxItemList.addExecutedItem(item4);
        executedItemList.addExecutedItem(item4);

        item1.addStartTime(new MyTime(13,0)); item1.addEndTime(new MyTime(15,0));
        executedItemList.addOrderPoint(item1);
        item2.addStartTime(new MyTime(15,0)); item2.addEndTime(new MyTime(16,25));
        executedItemList.addOrderPoint(item2);
        item3.addStartTime(new MyTime(16, 40)); item3.addEndTime(new MyTime(17,30));
        executedItemList.addOrderPoint(item3);

        ExecutedItem item5 = new ExecutedItem("看书", Item.RELAX, new MyTime(18,0), new MyTime(19,0));
        relaxItemList.addExecutedItem(item5);
        executedItemList.addExecutedItem(item5);

        item2.addStartTime(new MyTime(19,0)); item2.addEndTime(new MyTime(21, 0));
        executedItemList.addOrderPoint(item2);

        ExecutedItem item6 = new ExecutedItem("锻炼", Item.SPORT, new MyTime(21,30), new MyTime(23,0));
        sportItemList.addExecutedItem(item6);
        executedItemList.addExecutedItem(item6);

        List<TypeItem> itemList = new ArrayList<>();
        itemList.add(studyItemList); itemList.add(sportItemList); itemList.add(relaxItemList); itemList.add(otherItemList);

        TimeAllocationFragment timeAllocationFragment = new TimeAllocationFragment();
        timeAllocationFragment.setList(executedItemList,itemList);
        fragmentList.add(timeAllocationFragment);

        SpecificTAFragment specificTAFragment = new SpecificTAFragment();
        specificTAFragment.setTypeItem(itemList, executedItemList.getItemCount());
        fragmentList.add(specificTAFragment);
    }
}
