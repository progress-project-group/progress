package com.progress_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.List;
import java.util.UUID;

import Item.LongTermSchedule;
import Item.LongTermScheduleList;

public class LongTermSchedulePagerActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private List<LongTermSchedule> mLongTermSchedules;
    private static final String EXTRA_SCHEDULE_ID = "com.progress_android.schedule_id";

    public static Intent newIntent(Context context, UUID uuid){
        Intent intent = new Intent(context,LongTermSchedulePagerActivity.class);
        intent.putExtra(EXTRA_SCHEDULE_ID,uuid);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_long_term_schedule_pager);
        UUID uuid = (UUID) getIntent().getSerializableExtra(EXTRA_SCHEDULE_ID);

        mViewPager = (ViewPager) findViewById(R.id.longTermSchedulePager);

        mLongTermSchedules = LongTermScheduleList.get(this).getLongTermSchedules();
        //System.out.println("pager" + mLongTermSchedules.size());
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                LongTermSchedule longTermSchedule = mLongTermSchedules.get(position);
                return LongTermScheduleFragment.newInstance(longTermSchedule.getScheduleID());
            }

            @Override
            public int getCount() {
                return mLongTermSchedules.size();
            }
        });

        for (int i = 0; i < mLongTermSchedules.size(); i++) {
            if(mLongTermSchedules.get(i).getScheduleID().equals(uuid)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }

    }
}
