package com.iot232.ssis.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iot232.ssis.MainActivity;
import com.iot232.ssis.R;
import com.iot232.ssis.data.TimerInfo;
import com.iot232.ssis.databinding.FragmentAutomationsBinding;
import com.iot232.ssis.recycler.SchedulerAdapter;
import com.iot232.ssis.recycler.SchedulerViewInterface;

import java.util.ArrayList;
import java.util.List;

public class AutomationsFragment extends Fragment implements SchedulerViewInterface {
    View mView;
    TextView noScheduleText;
    MainActivity mainActivity;
    RecyclerView schedulerView;
    SchedulerAdapter schedulerAdapter;
    List<TimerInfo> schedules;
    public int savedPosition = -1;
    int numUntitled = 0;

    private FragmentAutomationsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_automations, container, false);
        mainActivity = (MainActivity) getActivity();

        assert mainActivity != null;
        mainActivity.checkCurrentFragment();

        schedules = new ArrayList<TimerInfo>();

        schedulerView = mView.findViewById(R.id.schedulerView);
        noScheduleText = mView.findViewById(R.id.noScheduleText);

        loadSchedules();
        schedulerAdapter = new SchedulerAdapter(mainActivity.getApplicationContext(), schedules, this);

        showSchedules();

        return mView;
    }

    public void addSchedule(){
        Log.d("POSITION", String.valueOf(savedPosition));
        int id = (savedPosition != -1)? savedPosition : mainActivity.schedulerInfo.size();
        savedPosition = -1;
        String schedulerName = (numUntitled == 0)? "Untitled" : "Untitled " + "(" + String.valueOf(numUntitled) + ")";
        TimerInfo newSchedule = new TimerInfo(schedulerName, id, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        schedules.add(newSchedule);
        mainActivity.schedulerInfo.add(newSchedule);
        numUntitled++;
        mainActivity.contentHelper.writeContent(mainActivity.schedulerInfo, "schedulerInfo.json", (MainActivity) mainActivity);
        showSchedules();
    }

    public void loadSchedules(){
        schedules.addAll(mainActivity.schedulerInfo);
    }

    public void showSchedules(){
        if (!schedules.isEmpty()) {
            noScheduleText.setVisibility(View.GONE);
            schedulerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            schedulerView.setAdapter(schedulerAdapter);
        }
        else noScheduleText.setVisibility(View.VISIBLE);
    }

    private void saveSchedules(){
        mainActivity.contentHelper.writeContent(mainActivity.schedulerInfo, "schedulerInfo.json", (MainActivity) mainActivity);
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemDeleted(int position){
        savedPosition = position;
        Log.d("POSITION", String.valueOf(savedPosition));
        schedules.remove(position);
        mainActivity.schedulerInfo.remove(position);
        schedulerAdapter.notifyItemRemoved(position);
        saveSchedules();
    }

    @Override
    public void onInfoChanged(int position, int type, int duration) {
        switch (type){
            case 0:
                schedules.get(position).setMixer1Time(duration);
                mainActivity.schedulerInfo.get(position).setMixer1Time(duration);
                break;
            case 1:
                schedules.get(position).setMixer2Time(duration);
                mainActivity.schedulerInfo.get(position).setMixer2Time(duration);
                break;
            case 2:
                schedules.get(position).setMixer3Time(duration);
                mainActivity.schedulerInfo.get(position).setMixer3Time(duration);
                break;
            case 3:
                schedules.get(position).setPump1Time(duration);
                mainActivity.schedulerInfo.get(position).setPump1Time(duration);
                break;
            case 4:
                schedules.get(position).setPump2Time(duration);
                mainActivity.schedulerInfo.get(position).setPump2Time(duration);
                break;
            case 5:
                schedules.get(position).setAreaType(duration);
                mainActivity.schedulerInfo.get(position).setAreaType(duration);
                break;
            default:
                break;
        }
        schedulerAdapter.notifyItemChanged(position);
        saveSchedules();
    }
}
