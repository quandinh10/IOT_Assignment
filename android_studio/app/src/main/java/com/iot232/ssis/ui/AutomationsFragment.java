package com.iot232.ssis.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iot232.ssis.MainActivity;
import com.iot232.ssis.R;
import com.iot232.ssis.recycler.SchedulerAdapter;
import com.iot232.ssis.recycler.SchedulerItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AutomationFragment extends Fragment {
    View mView;
    MainActivity mainActivity;
    RecyclerView schedulerView;
    List<SchedulerItem> schedules;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        mainActivity = (MainActivity) getActivity();

        schedules = new ArrayList<SchedulerItem>();



        schedulerView = mView.findViewById(R.id.schedulerView);
        schedulerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        schedulerView.setAdapter(new SchedulerAdapter(mainActivity.getApplicationContext(),schedules));


        return mView;
    }

    public void addSchedule(){
        schedules.add(new SchedulerItem("", 0, 0, 0, 0, 0, 0, 0));
    }

    public void loadSchedule(){
        for (int i = 0; i < 10; i++){

        }
    }

}
