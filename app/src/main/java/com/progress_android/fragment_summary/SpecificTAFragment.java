package com.progress_android.fragment_summary;


import android.os.Bundle;

import Item.ExecutedItem;
import Item.ExecutedItemList;
import Item.TypeItem;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.progress_android.DailySummaryActivity;
import com.progress_android.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.progress_android.DailySummaryActivity.tfLight;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpecificTAFragment extends Fragment {
    private HorizontalBarChart barChart;
    static final int typeNum = 4;
    List<TypeItem> typeItemList = new ArrayList<>();
    int labelCount;
    String[] label;
    String TAG = "Specific Fragment";

    public SpecificTAFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_specific_ta, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated");
        barChart = view.findViewById(R.id.specificChart);
        //barChart.setOnChartValueSelectedListener(this);
        // chart.setHighlightEnabled(false);

        setData();

        barChart.setDrawBarShadow(false);

        barChart.setDrawValueAboveBar(true);

        barChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        barChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        barChart.setPinchZoom(false);

        // draw shadows for each bar that show the maximum value
        // chart.setDrawBarShadow(true);

        barChart.setDrawGridBackground(false);
        barChart.setScaleYEnabled(false);
        barChart.setFitBars(true);

        //会缺少末尾两个label
        //final String[] quarters = new String[] { "Q1", "Q2", "Q3", "Q4","Q5","Q6", "Q7","Q8" };
        XAxis xl = barChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setTypeface(tfLight);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        //xl.setGranularity(8f);
        xl.setDrawLabels(true);
        xl.setCenterAxisLabels(true);
        xl.setLabelCount(labelCount+1,true);
        xl.setAvoidFirstLastClipping(true);
        xl.setValueFormatter(new XAxisValueFormatter(label));

        YAxis yl = barChart.getAxisLeft();
        yl.setTypeface(tfLight);
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setDrawGridLinesBehindData(true);
        yl.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        yl.setInverted(true);

        YAxis yr = barChart.getAxisRight();
        yr.setTypeface(tfLight);
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
        yr.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        yr.setInverted(true);

        barChart.setFitBars(true);
        barChart.animateY(2500);

        Legend l = barChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setXEntrySpace(4f);
    }

    public void setTypeItem(List<TypeItem> typeItemList, int itemCount){
         labelCount = itemCount;
         this.typeItemList = typeItemList;
    }
    public void setData(){
        label = new String[labelCount+2];
        float barWidth = 1.3f;
        float spaceForBar = 3f;
        List<IBarDataSet> sets = new ArrayList<>();
        int count = 0;
        for(int i=0; i< DailySummaryActivity.typeNum; ++i){
            List<BarEntry> entries = new ArrayList<>();
            List<ExecutedItem> currentExeItem = typeItemList.get(i).getExecutedItemList();
            for(int j=0; j<currentExeItem.size(); ++j){
                entries.add(new BarEntry(count*spaceForBar, currentExeItem.get(j).getTimeAmount().getMinutes()/60));
                label[count] = currentExeItem.get(j).getContent();
                Log.d(TAG, "count = " + count);
                Log.d(TAG, "content = " + label[count]);
                ++count;
            }

            BarDataSet set = new BarDataSet(entries, typeItemList.get(i).getContent());
            set.setColors(getResources().getColor(typeItemList.get(i).getColor()));
            sets.add(set);
        }
        Log.d(TAG, "outside count = " + count);
        label[count++] = "";
        label[count] = "";

        BarData data = new BarData(sets);

        data.setValueTextSize(10f);
        data.setValueTypeface(tfLight);
        data.setBarWidth(barWidth);
        barChart.setData(data);
        barChart.invalidate();
    }

    public class XAxisValueFormatter extends IndexAxisValueFormatter{
        private String[] values;
        private int mValueCount;

        public XAxisValueFormatter(String[] values) {
            this.values = values;
            mValueCount = values.length;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            double interval = (1.15+16.15)/(labelCount-1);
            Log.d(TAG,"outside value = "+interval);
            int index = (int) ((value+1.15)/2.47);

            return values[index];
        }

    }
}
