package com.progress_android.fragment_summary;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.progress_android.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimeLineFragement extends Fragment {


    public TimeLineFragement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time_line_fragement, container, false);
    }

}
