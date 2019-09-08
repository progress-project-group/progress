package com.progress_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

public class LongTermScheduleListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return LongTermScheduleListFragment.newInstance();
    }
}
