package com.iot232.ssis.helper;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AxisFormatter extends ValueFormatter {

    private final SimpleDateFormat mFormat;

    public AxisFormatter() {
        // Customize the format as needed
        mFormat = new SimpleDateFormat("dd/MM HH:mm:ss", Locale.getDefault());
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        long millis = (long) value * 1000;  // Convert seconds to milliseconds
        return mFormat.format(new Date(millis));
    }
}