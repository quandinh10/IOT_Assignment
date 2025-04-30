package com.iot232.ssis.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.iot232.ssis.MainActivity;
import com.iot232.ssis.R;
import com.iot232.ssis.databinding.FragmentHomeBinding;
import com.iot232.ssis.helper.AxisFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


import android.graphics.drawable.Drawable;

public class HomeFragment extends Fragment {
    View mView;
    MainActivity mainActivity;
    LineChart tempGraph, humidGraph;
    TextView currentDay, currentDate;
    int graphState;

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        mainActivity = (MainActivity) getActivity();

        assert mainActivity != null;
        mainActivity.checkCurrentFragment();


        ///SHOW DATE////
        currentDay = mView.findViewById(R.id.current_day);
        currentDate = mView.findViewById(R.id.current_date);
        currentDay.setText(new SimpleDateFormat("EEE", Locale.getDefault()).format(new Date()));
        currentDate.setText(new SimpleDateFormat("d MMM", Locale.getDefault()).format(new Date()));

        tempGraph = mView.findViewById(R.id.tempGraph);
        humidGraph = mView.findViewById(R.id.humidGraph);

        mainActivity.getEntries("temperature");
        mainActivity.getEntries("moisture");

        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    /////DRAW GRAPH/////
    public void setupGraph(String feedKey, ArrayList<Entry> entries) {
        float minX = entries.get(0).getX();
        Context context = getContext();
        if (context != null) {
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            LineChart graph = getGraph(feedKey);
            graph.clear();
            LineDataSet dataSet = new LineDataSet(entries, feedKey.toUpperCase());
            dataSet.setDrawFilled(true);
            Drawable drawable = ContextCompat.getDrawable(getContext(), (feedKey.equals("temperature"))? R.drawable.gradient_red : R.drawable.gradient_blue);
            dataSet.setFillDrawable(drawable);
            dataSet.setDrawCircles(false);
            dataSet.setLineWidth(3);
            dataSet.setColor(Color.parseColor(getColor(feedKey)));
            dataSets.add(dataSet);

            // Set chart description
            Description description = new Description();
            description.setText("");
            description.setTextColor(Color.rgb(43, 101, 236));
            description.setTextSize(30);

            // Draw the graph
            drawGraph(dataSets, graph, minX, description);
        }
    }

    private void drawGraph(ArrayList<ILineDataSet> dataSets, LineChart graph, float minX, Description description) {
        LineData data = new LineData(dataSets);
        graph.setData(data);
        graph.setDescription(description);
        graph.setDrawGridBackground(false);
        graph.setDragEnabled(false);

        XAxis xAxis = graph.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setLabelRotationAngle(-45);
        xAxis.setGranularity(1f);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setAxisMinimum(minX);
        xAxis.setValueFormatter(new AxisFormatter());

        YAxis yAxisLeft = graph.getAxisLeft();
        YAxis yAxisRight = graph.getAxisRight();
        yAxisLeft.setAxisMinimum(0f);
        yAxisRight.setEnabled(false);

        graph.invalidate();
    }

    public LineChart getGraph (String feedKey){
        return (Objects.equals(feedKey, "temperature"))? tempGraph : humidGraph;
    }

    public String getColor (String feedKey){
        return (Objects.equals(feedKey, "temperature"))? "#F9966B" : "#5CB3FF";
    }
}