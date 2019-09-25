package com.progress_android.fragment_summary;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import Item.Item;
import Item.Time.TimeAmount;
import Item.ExecutedItemList;
import Item.TypeItem;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.progress_android.DailySummaryActivity;
import com.progress_android.R;

import java.util.ArrayList;
import java.util.List;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;
import static com.progress_android.DailySummaryActivity.tfLight;
import static com.progress_android.DailySummaryActivity.tfRegular;


public class TimeAllocationFragment extends Fragment {

    PieChart timeAllocationChart;
    ExecutedItemList executedItemList;
    List<TypeItem> typeItemList = new ArrayList<>();
    SpecificTAFragment specificTAFragment;
    String TAG = "TimeAllocationFragment";

    public TimeAllocationFragment() {
        // Required empty public constructor
    }

//    // TODO: Rename and change types and number of parameters
//    public static TimeAllocationFragment newInstance(String param1, String param2) {
//        TimeAllocationFragment fragment = new TimeAllocationFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time_allocation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {
        timeAllocationChart = view.findViewById(R.id.time_allocate_chart);

        timeAllocationChart.setUsePercentValues(true);
        timeAllocationChart.getDescription().setEnabled(false);
        timeAllocationChart.setExtraOffsets(5, 10, 5, 5);

        timeAllocationChart.setDragDecelerationFrictionCoef(0.95f);

        //timeAllocationChart.setCenterTextTypeface(tfLight);
        //timeAllocationChart.setCenterText(generateCenterSpannableText());

        timeAllocationChart.setDrawHoleEnabled(true);
        timeAllocationChart.setHoleColor(Color.WHITE);

        timeAllocationChart.setTransparentCircleColor(Color.WHITE);
        timeAllocationChart.setTransparentCircleAlpha(110);

        timeAllocationChart.setHoleRadius(58f);
        timeAllocationChart.setTransparentCircleRadius(61f);

        timeAllocationChart.setDrawCenterText(true);

        timeAllocationChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        timeAllocationChart.setRotationEnabled(false);
        timeAllocationChart.setHighlightPerTapEnabled(true);

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
        timeAllocationChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null)
                    return;
                Log.d(TAG, "selectedc begin");
//                PieEntry entry = (PieEntry) e;
//
//                SpecificTAFragment specificTAFragment = new SpecificTAFragment();
//                specificTAFragment.setTypeItem(typeItemList);
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager()
//                        .beginTransaction();
//                transaction.replace(R.id.summary_pager, specificTAFragment);
//                transaction.addToBackStack(null);
//                transaction.commit();
//                Log.d(TAG, "selected end");
            }

            @Override
            public void onNothingSelected() {

            }
        });

        timeAllocationChart.animateY(1400, Easing.EaseInOutQuad);
        // chart.spin(2000, 0, 360);

        Legend l = timeAllocationChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        timeAllocationChart.setEntryLabelColor(Color.WHITE);
        timeAllocationChart.setEntryLabelTypeface(tfRegular);
        timeAllocationChart.setEntryLabelTextSize(12f);

        setData();

    }

    public void setData(){
        List<PieEntry> entries = new ArrayList<>();
        for(int i=0; i< Item.typeNum; ++i){
            TypeItem currentTypeItem = typeItemList.get(i);
            entries.add(new PieEntry(currentTypeItem.getTimeAmount().getMinutes(), currentTypeItem.getContent()));
        }

        PieDataSet set = new PieDataSet(entries,"本日时间分配");
        set.setSliceSpace(3f);
        set.setSelectionShift(5f);
        set.setColors(getResources().getColor(R.color.blue),getResources().getColor(R.color.red), getResources().getColor(R.color.green), getResources().getColor(R.color.yellow));

        PieData data = new PieData(set);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(tfLight);

        timeAllocationChart.setData(data);
        timeAllocationChart.invalidate();
    }

    public void setList(ExecutedItemList executedItemList,
            List<TypeItem> typeItemList){
        this.executedItemList = executedItemList;
        this.typeItemList = typeItemList;
    }
}
