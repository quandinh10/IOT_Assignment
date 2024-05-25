package com.iot232.ssis.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iot232.ssis.MainActivity;
import com.iot232.ssis.R;
import com.iot232.ssis.data.TimerInfo;
import com.iot232.ssis.databinding.FragmentAutomationsBinding;
import com.iot232.ssis.recycler.SchedulerAdapter;
import com.iot232.ssis.recycler.SchedulerViewHolder;
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
    public int savedpos = -1;

    public final int NAN = 0, MIXER1 = 1, MIXER2 = 2, MIXER3 = 3, PUMP1 = 10,
            PUMP2 = 11, AREA = 20, NO_TIMER = 30, NO_ACTION = 31, CYCLE = 50, START = 51, TITLE = 52;

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
        Log.d("pos", String.valueOf(savedpos));
        int id = (savedpos != -1)? savedpos : mainActivity.schedulerInfo.size();
        savedpos = -1;
        String schedulerName = "Untitled";
        TimerInfo newSchedule = new TimerInfo(schedulerName, id, NAN, 0, NAN, NAN, 0, 0, 0, 0, NAN, 0, 0, 0);
        schedules.add(newSchedule);
        mainActivity.schedulerInfo.add(newSchedule);
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
    public void onItemClick(int pos) {
        int inv = 1 - mainActivity.schedulerInfo.get(pos).getSchedulerState();
        schedules.get(pos).setSchedulerState(inv);
        mainActivity.schedulerInfo.get(pos).setSchedulerState(inv);
        if (inv == 1){
            mainActivity.sendSchedule(pos);
//            mainActivity.startSchedule(pos, 0, schedulerAdapter);
        }
        schedulerAdapter.notifyItemChanged(pos);
        saveSchedules();
    }

    @Override
    public void onItemDeleted(int pos){
        savedpos = pos;
        schedules.remove(pos);
        mainActivity.schedulerInfo.remove(pos);
        schedulerAdapter.notifyItemRemoved(pos);
        saveSchedules();
    }

    @Override
    public void onInfoChanged(int pos, int type, int duration, long longDuration, String str) {
        switch (type){
            case MIXER1:
                schedules.get(pos).setMixer1Time(duration);
                mainActivity.schedulerInfo.get(pos).setMixer1Time(duration);
                break;
            case MIXER2:
                schedules.get(pos).setMixer2Time(duration);
                mainActivity.schedulerInfo.get(pos).setMixer2Time(duration);
                break;
            case MIXER3:
                schedules.get(pos).setMixer3Time(duration);
                mainActivity.schedulerInfo.get(pos).setMixer3Time(duration);
                break;
            case PUMP1:
                schedules.get(pos).setPump1Time(duration);
                mainActivity.schedulerInfo.get(pos).setPump1Time(duration);
                break;
            case PUMP2:
                schedules.get(pos).setPump2Time(duration);
                mainActivity.schedulerInfo.get(pos).setPump2Time(duration);
                break;
            case AREA:
                schedules.get(pos).setAreaType(duration);
                mainActivity.schedulerInfo.get(pos).setAreaType(duration);
                break;
            case CYCLE:
                schedules.get(pos).setCycleCount(duration);
                mainActivity.schedulerInfo.get(pos).setCycleCount(duration);
                break;
            case START:
                schedules.get(pos).setMixerStart(longDuration);
                mainActivity.schedulerInfo.get(pos).setMixerStart(longDuration);
                break;
            case TITLE:
                schedules.get(pos).setSchedulerTitle(str);
                mainActivity.schedulerInfo.get(pos).setSchedulerTitle(str);
                break;
            default:
                break;
        }
        schedulerAdapter.notifyItemChanged(pos);
        saveSchedules();
    }

    public boolean checkTimer(int pos){
        int[] time = {mainActivity.schedulerInfo.get(pos).getMixer1Time(),
                mainActivity.schedulerInfo.get(pos).getMixer2Time(),
                mainActivity.schedulerInfo.get(pos).getMixer3Time(),
                mainActivity.schedulerInfo.get(pos).getPump1Time(),
                mainActivity.schedulerInfo.get(pos).getPump2Time(),
                mainActivity.schedulerInfo.get(pos).getAreaType(),
                mainActivity.schedulerInfo.get(pos).getCycleCount()};
        for (int i : time) {
            if (i == NAN) return false;
        }
        return true;
    }

    public boolean checkSchedules(int pos){
        for (int i = 0; i < mainActivity.schedulerInfo.size(); i++){
            if (i == pos) continue;
            if (mainActivity.schedulerInfo.get(i).getSchedulerState() == 1) return false;
        }
        return true;
    }
}
