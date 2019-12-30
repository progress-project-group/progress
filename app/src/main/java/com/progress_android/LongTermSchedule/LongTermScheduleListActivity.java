package com.progress_android.LongTermSchedule;

import androidx.fragment.app.Fragment;

public class LongTermScheduleListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return LongTermScheduleListFragment.newInstance();
    }
}
