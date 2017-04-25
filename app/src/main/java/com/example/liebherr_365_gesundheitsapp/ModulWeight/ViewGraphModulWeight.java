package com.example.liebherr_365_gesundheitsapp.ModulWeight;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;


import com.example.liebherr_365_gesundheitsapp.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

import com.example.liebherr_365_gesundheitsapp.Database.DBHelperDataSourceData;
import com.example.liebherr_365_gesundheitsapp.Database.Data;
import com.github.mikephil.charting.utils.ColorTemplate;

import static android.view.View.Y;

public class ViewGraphModulWeight extends AppCompatActivity {
    DBHelperDataSourceData databaseData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // set up navigation enabled
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_graph);
        databaseData = new DBHelperDataSourceData(this);
        databaseData.open();
        showAllListEntries();
        databaseData.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //function showAllListEntries
    private void showAllListEntries() {
        // intialize Linechart
        LineChart chart = (LineChart) findViewById(R.id.chart);

        // setScaleEnabled -> false
        chart.setScaleEnabled(false);

        // new DayAxisValueFormatter
        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(chart);

        // style xAxis
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setTextSize(12f); // set the text size
        xAxis.setValueFormatter(xAxisFormatter);

        // style yAxis
        YAxis yAxisleft = chart.getAxisLeft();
        yAxisleft.setTextSize(12f); // set the text size

        // yAxisleft.setAxisMinimum(40f); // start at 40
        yAxisleft.setGranularity(1f); // only intervals of 1 kg

        YAxis yAxisright = chart.getAxisRight();
        yAxisright.setDrawLabels(false);
        yAxisright.setDrawGridLines(false);

        // yAxisright.setAxisMinimum(40f); // start at 40
        yAxisright.setGranularity(1f); // only intervals of 1 kg

        // call function getAllDataasarray
        Data[] alldata = databaseData.getAllDataasarray();

        // defince ArrayList entries
        List<Entry> entries = new ArrayList<>();

        // declare maxValue and minValue for Viewport
        float maxValue = BmiCalculator.getMaxRecWeight();
        float minValue = BmiCalculator.getMinRecWeight();

        List<ILineDataSet> dataSets = new ArrayList<>();


        //~~~~~~~~~~~~~~~~~~~~ BORDER UPPER~~~~~~~~~~~~~~~~~~~~~~~
        Entry borderUpperleft = new Entry(480, 150); // 0 == quarter 1
        Entry borderUpperRight = new Entry(481, 150); // 0 == quarter 1

        List<Entry> borderUpper = new ArrayList<>();

        borderUpper.add(borderUpperleft);
        borderUpper.add(borderUpperRight);

        LineDataSet dataSetBorderUpper = new LineDataSet(borderUpper, "Gewicht"); // add entries to dataset

        dataSetBorderUpper.setFillColor(Color.GRAY);
        dataSetBorderUpper.setFillAlpha(100);
        dataSetBorderUpper.setDrawFilled(true);

        //Style weight line
        dataSetBorderUpper.setDrawValues(false);
        dataSetBorderUpper.setCircleRadius(1f);
        dataSetBorderUpper.setDrawCircleHole(false);
        dataSetBorderUpper.setLineWidth(2f);
        dataSetBorderUpper.setCircleColor(Color.GREEN);
        dataSetBorderUpper.setColor(Color.GREEN);

        // add Data to dataSets
        dataSets.add(dataSetBorderUpper);
        //~~~~~~~~~~~~~~~~~~~~ BORDER TOP~~~~~~~~~~~~~~~~~~~~~~~


        //~~~~~~~~~~~~~~~~~~~~ BORDER TOP~~~~~~~~~~~~~~~~~~~~~~~
        Entry borderTopleft = new Entry(480, maxValue); // 0 == quarter 1
        Entry borderTopRight = new Entry(481, maxValue); // 0 == quarter 1

        List<Entry> borderTop = new ArrayList<>();

        borderTop.add(borderTopleft);
        borderTop.add(borderTopRight);

        LineDataSet dataSetBorderTop = new LineDataSet(borderTop, "Obergrenze BMI"); // add entries to dataset

        dataSetBorderTop.setFillColor(Color.GRAY);
        dataSetBorderTop.setFillAlpha(100);
        dataSetBorderTop.setDrawFilled(true);

        //Style weight line
        dataSetBorderTop.setDrawValues(false);
        dataSetBorderTop.setCircleRadius(1f);
        dataSetBorderTop.setDrawCircleHole(false);
        dataSetBorderTop.setLineWidth(2f);
        dataSetBorderTop.setCircleColor(Color.RED);
        dataSetBorderTop.setColor(Color.RED);

        // add Data to dataSets
        dataSets.add(dataSetBorderTop);
        //~~~~~~~~~~~~~~~~~~~~ BORDER TOP~~~~~~~~~~~~~~~~~~~~~~~

        //~~~~~~~~~~~~~~~~~~~~ BORDER BOTTOM~~~~~~~~~~~~~~~~~~~~~~~
        Entry borderBottomleft = new Entry(480, minValue); // 0 == quarter 1
        Entry borderBottomRight = new Entry(481, minValue); // 0 == quarter 1

        List<Entry> borderBottom = new ArrayList<>();

        borderBottom.add(borderBottomleft);
        borderBottom.add(borderBottomRight);

        LineDataSet dataSetBorderBottom = new LineDataSet(borderBottom, "Untergrenze BMI"); // add entries to dataset

        dataSetBorderBottom.setFillColor(Color.WHITE);
        dataSetBorderBottom.setFillAlpha(80);
        dataSetBorderBottom.setDrawFilled(true);

        //Style weight line
        dataSetBorderBottom.setDrawValues(false);
        dataSetBorderBottom.setCircleRadius(1f);
        dataSetBorderBottom.setDrawCircleHole(false);
        dataSetBorderBottom.setLineWidth(2f);
        dataSetBorderBottom.setCircleColor(Color.RED);
        dataSetBorderBottom.setColor(Color.RED);

        // add Data to dataSets
        dataSets.add(dataSetBorderBottom);
        //~~~~~~~~~~~~~~~~~~~~ BORDER BOTTOM~~~~~~~~~~~~~~~~~~~~~~~

        for (Data anAlldata : alldata) {
            // declare float weight
            float weight = anAlldata.getPhysicalvalues();

            // add data to dataset
            entries.add(new Entry(anAlldata.getDays(), weight));

            Log.d("Day", String.valueOf(anAlldata.getDays()));

            // prepare viewport
            if (weight > maxValue) {
                maxValue = weight;
            } else if (weight < minValue) {
                minValue = weight;
            }
        }

        LineDataSet dataSet = new LineDataSet(entries, "Gewicht"); // add entries to dataset
        dataSet.setColor(Color.BLACK);
        dataSet.setLineWidth(2f);

        //define Viewport
        yAxisleft.setAxisMinValue(minValue - 2.0f);
        yAxisleft.setAxisMaxValue(maxValue + 2.0f);

        /*
        dataSet.setFillColor(Color.GREEN);
        dataSet.setFillAlpha(100);
        dataSet.setDrawFilled(true);
        */

        //Style weight line
        dataSet.setDrawValues(false);
        dataSet.setCircleRadius(3f);
        dataSet.setDrawCircleHole(false);
        dataSet.setCircleColor(Color.BLACK);
        dataSet.setColor(Color.BLACK);

        // add Data to dataSets
        dataSets.add(dataSet);

        //style legend
        Legend legend = chart.getLegend();
        legend.setEnabled(false);
        /*
        legend.setTextSize(15);
        legend.setTextColor(Color.BLACK);
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setXEntrySpace(20);
        legend.setWordWrapEnabled(true);
        */

        LineData lineData = new LineData(dataSets);

        // hide description
        chart.setDescription(null);

        // padding bottom
        chart.setExtraOffsets(0, 0, 0, 1.5f);

        // disable highlighting
        chart.setHighlightPerTapEnabled(false);
        chart.setHighlightPerDragEnabled(false);

        chart.setData(lineData);
        chart.invalidate(); // refresh
        drawWeightGoal(chart);
    }

    private void drawWeightGoal(LineChart chart) {

        float weightGoal = BmiCalculator.getAverageRecWeight();

        LimitLine ll = new LimitLine(weightGoal, "");
        ll.setLineColor(Color.BLUE);
        ll.setLineWidth(2f);

        chart.getAxisLeft().setDrawLimitLinesBehindData(false);
        chart.getAxisLeft().addLimitLine(ll);
    }

    /* DIESE FARBEN NICHT ZUSAMMEN, AUSSAGE BLONDINE MIT ZÖPFEN */
    // BLAU GELB
    // ROT GRÜN
}
