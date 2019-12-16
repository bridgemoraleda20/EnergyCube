package com.example.jsondemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;


public class FragmentReports extends Fragment  {

    private static final String TAG = "home2";
    String[] SPINNER_LIST = {"hour", "day", "week", "month", "year"};

    Spinner spinner;

    private LineChart mChart;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);


        home2 home = (home2) getActivity();

        spinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(home, android.R.layout.simple_dropdown_item_1line, SPINNER_LIST);

       arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

       spinner.setAdapter(arrayAdapter);


        mChart  = (LineChart) view.findViewById(R.id.lineChart);

        //mChart.setOnChartGestureListener((OnChartGestureListener) home);
        //mChart.setOnChartValueSelectedListener((OnChartValueSelectedListener) home);

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        ArrayList<Entry> yValues = new ArrayList<>();

        yValues.add(new Entry(0, 60f));
        yValues.add(new Entry(1, 66f));
        yValues.add(new Entry(2, 63f));
        yValues.add(new Entry(3, 58f));
        yValues.add(new Entry(4, 57f));

        LineDataSet set1 = new LineDataSet(yValues, "data set 1");

        set1.setFillAlpha(110);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        dataSets.add(set1);

        LineData data = new LineData(dataSets);

        mChart.setData(data);







        return view;
    }

}
