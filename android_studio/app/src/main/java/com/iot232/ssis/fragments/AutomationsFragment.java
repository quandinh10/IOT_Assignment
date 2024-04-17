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
import com.iot232.ssis.data.SchedulerInfo;
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
    List<SchedulerInfo> schedules;
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

        schedules = new ArrayList<SchedulerInfo>();

        schedulerView = mView.findViewById(R.id.schedulerView);
        noScheduleText = mView.findViewById(R.id.noScheduleText);

        loadSchedules();
        schedulerAdapter = new SchedulerAdapter(mainActivity.getApplicationContext(), schedules, this);

        showSchedules();

        return mView;
    }

    public void addSchedule(){
        Log.d("POSITION", String.valueOf(savedPosition));
        int id = (savedPosition != -1)? savedPosition : mainActivity.schedulerInfos.size();
        savedPosition = -1;
        String schedulerName = (numUntitled == 0)? "Untitled" : "Untitled " + "(" + String.valueOf(numUntitled) + ")";
        SchedulerInfo newSchedule = new SchedulerInfo(schedulerName, id, 0, 0, 0, 0, 0, 0, 0);
        schedules.add(newSchedule);
        mainActivity.schedulerInfos.add(newSchedule);
        numUntitled++;
        mainActivity.contentHelper.writeContent(mainActivity.schedulerInfos, "schedulerInfo.json", (MainActivity) mainActivity);
        showSchedules();
    }

    public void loadSchedules(){
        schedules.addAll(mainActivity.schedulerInfos);
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
        mainActivity.contentHelper.writeContent(mainActivity.schedulerInfos, "schedulerInfo.json", (MainActivity) mainActivity);
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemDeleted(int position){
        savedPosition = position;
        Log.d("POSITION", String.valueOf(savedPosition));
        schedules.remove(position);
        mainActivity.schedulerInfos.remove(position);
        schedulerAdapter.notifyItemRemoved(position);
        saveSchedules();
    }

    @Override
    public void onInfoChanged(int position, int type, int duration) {
        switch (type){
            case 0:
                schedules.get(position).setMixer1Time(duration);
                mainActivity.schedulerInfos.get(position).setMixer1Time(duration);
                break;
            case 1:
                schedules.get(position).setMixer2Time(duration);
                mainActivity.schedulerInfos.get(position).setMixer2Time(duration);
                break;
            case 2:
                schedules.get(position).setMixer3Time(duration);
                mainActivity.schedulerInfos.get(position).setMixer3Time(duration);
                break;
            case 3:
                schedules.get(position).setPump1Time(duration);
                mainActivity.schedulerInfos.get(position).setPump1Time(duration);
                break;
            case 4:
                schedules.get(position).setPump2Time(duration);
                mainActivity.schedulerInfos.get(position).setPump2Time(duration);
                break;
            case 5:
                schedules.get(position).setAreaType(duration);
                mainActivity.schedulerInfos.get(position).setAreaType(duration);
                break;
            default:
                break;
        }
        schedulerAdapter.notifyItemChanged(position);
        saveSchedules();
    }
}
