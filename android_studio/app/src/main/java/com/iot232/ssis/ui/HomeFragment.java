package com.iot232.ssis.ui;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.iot232.ssis.MainActivity;
import com.iot232.ssis.R;
import com.iot232.ssis.data.AdafruitIoRequestTask;
import com.iot232.ssis.databinding.FragmentHomeBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;


import android.graphics.drawable.Drawable;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {
    View mView;
    MainActivity mainActivity;
    LineChart lineChart;
    ImageView graphHamburger;
    TextView graphText, currentDay, currentDate;
    ArrayList<Entry> entries, entries2;
    int graphState;

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        mainActivity = (MainActivity) getActivity();

        assert mainActivity != null;
        mainActivity.checkCurrentFragment();

        currentDay = mView.findViewById(R.id.current_day);
        currentDate = mView.findViewById(R.id.current_date);
        currentDay.setText(new SimpleDateFormat("EEE", Locale.getDefault()).format(new Date()));
        currentDate.setText(new SimpleDateFormat("d MMM", Locale.getDefault()).format(new Date()));


        ////0 FOR ALL, 1 FOR TEMP, 2 FOR HUMID/////
        graphState = 0;

        lineChart = mView.findViewById(R.id.graph);

        ////3 DOTS OPTION FOR FILTER////
        graphHamburger = mView.findViewById(R.id.graph_hamburger);
        graphHamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show filter menu dialog anchored to the graph hamburger menu
                showFilterMenu(graphHamburger);
            }
        });

        /////GRAPH TITLE////
        graphText = mView.findViewById(R.id.graphText);

        //////SET ENTRIES//////
        entries = new ArrayList<Entry>();
        entries2 = new ArrayList<Entry>();
        AdafruitIoRequestTask requestTask = new AdafruitIoRequestTask("temperature", new AdafruitIoRequestTask.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String result) {
                // Handle successful result
                // Parse JSON response or do other processing here
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    int arrayLength = jsonArray.length();
                    for (int i = arrayLength - 1; i >= 0; i--) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String valueString = jsonObject.getString("value");
                        float value = Float.parseFloat(valueString);
                        entries.add(new Entry(arrayLength - i - 1, value)); // Subtracting i from arrayLength gives the reversed index
                        Log.d("Entry " + i + " value:", String.valueOf(value));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    // Handle JSON parsing error
                    Log.e("JSON parsing error", e.getMessage());
                }
            }

            @Override
            public void onTaskFailed() {
                // Handle task failure
                // Show an error message or retry the request
            }
        });

// Execute the AsyncTask
        requestTask.execute();
        AdafruitIoRequestTask requestTask2 = new AdafruitIoRequestTask("moisture", new AdafruitIoRequestTask.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String result) {
                // Handle successful result
                // Parse JSON response or do other processing here
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    int arrayLength = jsonArray.length();
                    for (int i = arrayLength - 1; i >= 0; i--) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String valueString = jsonObject.getString("value");
                        float value = Float.parseFloat(valueString);
                        entries2.add(new Entry(arrayLength - i - 1, value)); // Subtracting i from arrayLength gives the reversed index
                        Log.d("Entry " + i + " value:", String.valueOf(value));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    // Handle JSON parsing error
                    Log.e("JSON parsing error", e.getMessage());
                }
            }

            @Override
            public void onTaskFailed() {
                // Handle task failure
                // Show an error message or retry the request
            }
        });

// Execute the AsyncTask
        requestTask2.execute();
        ////TODO GET ENTRIES FROM API//////

//        Random random = new Random();
//        for (int i = 0; i < 50; i++) {
//            entries.add(new Entry(i - 1, random.nextInt(100)));
//            entries2.add(new Entry(i - 1, random.nextInt(100)));
//        }

        /////DRAW GRAPH////
        drawGraph(graphState, entries, entries2);


        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /////SHOW FILTER OPTIONS WHEN PRESSING THE 3 DOTS//////
    public void showFilterMenu(View anchorView) {
        ConstraintLayout constraintLayout = mView.findViewById(R.id.graph_filter);
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.graph_filter_layout, constraintLayout);

        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();

        RelativeLayout popup = view.findViewById(R.id.graph_filter);
        popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        //////ALL/////
        TextView fitlerAll = view.findViewById(R.id.filter_all);
        fitlerAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graphState = 0;
                drawGraph(graphState, entries, entries2);
                alertDialog.dismiss();
            }
        });

        //////TEMP/////
        TextView fitlerTemp = view.findViewById(R.id.filter_temperature);
        fitlerTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graphState = 1;
                drawGraph(graphState, entries, entries2);
                alertDialog.dismiss();

            }
        });

        //////HUMID/////
        TextView fitlerHumid = view.findViewById(R.id.filter_humidity);
        fitlerHumid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graphState = 2;
                drawGraph(graphState, entries, entries2);
                alertDialog.dismiss();
            }
        });

        // Prevent the background from dimming
        Window window = alertDialog.getWindow();
        if (window != null) window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        if (alertDialog.getWindow() != null) alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        // ANCHOR THE POPUP UNDER THE 3 DOTS/////
        int[] anchorLocation = new int[2];
        anchorView.getLocationOnScreen(anchorLocation);
        int anchorX = anchorLocation[0];
        int anchorY = anchorLocation[1];
        int anchorWidth = anchorView.getWidth();
        int anchorHeight = anchorView.getHeight();

        // Set the gravity to be below the anchor view
        WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
        params.gravity = Gravity.TOP | Gravity.END; // Adjust as per your requirement
        params.x = anchorX + anchorWidth; // Adjust the x position
        params.y = anchorY + anchorHeight - 80; // Adjust the y position

        alertDialog.show();
    }


    /////DRAW GRAPH/////
    private void drawGraph(int graphState, ArrayList<Entry> entries, ArrayList<Entry> entries2){
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        lineChart.clear();
        if (graphState != 2) {
            LineDataSet dataSet = new LineDataSet(entries, "Temperature");
            dataSet.setDrawFilled(true);
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.gradient_green);
            dataSet.setFillDrawable(drawable);
            dataSet.setDrawCircles(false);
            dataSet.setLineWidth(3);
            dataSet.setColor(Color.GREEN);
            dataSets.add(dataSet);
        }
        if (graphState != 1) {
            LineDataSet dataSet2 = new LineDataSet(entries2, "Humidity");
            dataSet2.setDrawFilled(true);
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.gradient_yellow);
            dataSet2.setFillDrawable(drawable);
            dataSet2.setDrawCircles(false);
            dataSet2.setLineWidth(3);
            dataSet2.setColor(Color.YELLOW);
            dataSets.add(dataSet2);
        }

        // Set chart description
        Description description = new Description();
        description.setText("");
        description.setTextColor(Color.rgb(43, 101, 236));
        description.setTextSize(30);

        // Create LineData from the data sets
        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        lineChart.setDescription(description);
        lineChart.setDrawGridBackground(false);
        lineChart.setDragEnabled(false);

        // Configure X axis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Show X axis at the bottom
        xAxis.setAxisMinimum(0f);

        // Configure Y axis
        YAxis yAxisLeft = lineChart.getAxisLeft();
        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisLeft.setAxisMinimum(0f);
        yAxisRight.setEnabled(false); // Disable right Y axis
    }

}
