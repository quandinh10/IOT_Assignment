package com.iot232.ssis.recycler;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iot232.ssis.MainActivity;
import com.iot232.ssis.R;
import com.iot232.ssis.data.SchedulerInfo;

import java.util.List;

public class SchedulerAdapter extends RecyclerView.Adapter<SchedulerViewHolder> {
    private final SchedulerViewInterface schedulerViewInterface;
    Context context;
    List<SchedulerInfo> schedulerItems;
    MainActivity mainActivity;

    int position = -1;

    public SchedulerAdapter(Context context, List<SchedulerInfo>  schedulerItems, SchedulerViewInterface schedulerViewInterface){
        this.context = context;
        this.schedulerItems = schedulerItems;
        this.schedulerViewInterface = schedulerViewInterface;

        if (context instanceof MainActivity) mainActivity = (MainActivity) context;
    }

    @NonNull
    @Override
    public SchedulerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        return new SchedulerViewHolder(LayoutInflater.from(context).inflate(R.layout.scheduler_layout, parent, false), context, this, schedulerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull SchedulerViewHolder holder, int position) {
        holder.areaType.setText(String.valueOf(schedulerItems.get(position).getAreaType()));
        holder.startTime.setText(formatTime(schedulerItems.get(position).getStartTime()));
        holder.mixer1Time.setText(formatTime(schedulerItems.get(position).getMixer1Time()));
        holder.mixer2Time.setText(formatTime(schedulerItems.get(position).getMixer2Time()));
        holder.mixer3Time.setText(formatTime(schedulerItems.get(position).getMixer3Time()));
        holder.pump1Time.setText(formatTime(schedulerItems.get(position).getPump1Time()));
        holder.pump2Time.setText(formatTime(schedulerItems.get(position).getPump2Time()));
        holder.schedulerTitle.setText(schedulerItems.get(position).getSchedulerTitle());
        this.position = holder.getAdapterPosition();
    }

    @Override
    public int getItemCount() {
        return schedulerItems.size();
    }


    ////CHANGE INT TO MM:SS/////
    public String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

}
