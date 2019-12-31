package com.progress_android.fragment_summary;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import Item.Item;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.progress_android.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class LongTermCompareFragment extends Fragment {

    //该数据需要从数据库中读取
    int typeNum = 4;
    int showedNum = 7;
    LineChart longTermChart;

    public LongTermCompareFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_long_term_compare, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        longTermChart = view.findViewById(R.id.longTermChart);
        setDefaultData();
        longTermChart.getDescription().setEnabled(false);

        XAxis xl = longTermChart.getXAxis();
        String[] Xlabel = new String[]{"", "周一","周二","周三","周四","周五","周六","周日",""};
        xl.setValueFormatter(new XAxisValueFormatter(Xlabel));

        YAxis yl = longTermChart.getAxisLeft();
        YAxis yr = longTermChart.getAxisRight();
        yl.setValueFormatter(new YAxisValueFormatter("h"));
        yr.setValueFormatter(new YAxisValueFormatter("h"));
    }

    private void setData(){
        //需要从数据库里读取最近一周数据
    }

    private void setDefaultData(){
        List<ILineDataSet> sets = new ArrayList<>();
        float lineWidth = 3f;
        for(int i = 0; i < typeNum; ++i){
            List<Entry> groupData = new ArrayList<>();
            for(int j = 0; j<showedNum; ++j){
                groupData.add(new Entry(j+1, (float) Math.random()*5));
            }
            LineDataSet set = new LineDataSet(groupData, Item.typeName[i]);
            set.setLineWidth(lineWidth);
            set.setColors(getResources().getColor(Item.colorId[i]));
            sets.add(set);
        }
        LineData lineData = new LineData(sets);
        longTermChart.setData(lineData);
        longTermChart.invalidate();
    }

    public class XAxisValueFormatter extends IndexAxisValueFormatter {
        private String[] values;

        public XAxisValueFormatter(String[] values) {
            this.values = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            if(value>=values.length||value<0){
                return "";
            }
            return values[(int)value];
        }

    }
    public class YAxisValueFormatter extends IndexAxisValueFormatter {
        private String values;

        public YAxisValueFormatter(String values) {
            this.values = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            DecimalFormat decimalFormat = new DecimalFormat(".0");
            return decimalFormat.format(value) + values;
        }

    }
}
