package com.iot232.ssis.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iot232.ssis.R;
import com.iot232.ssis.ui.DashboardFragment;

import java.util.List;

public class SchedulerAdapter extends RecyclerView.Adapter<SchedulerViewHolder> {
    Context context;
    List<SchedulerItem> schedulerItems;

    public SchedulerAdapter(Context context, List<SchedulerItem>  schedulerItems){
        this.context = context;
        this.schedulerItems = schedulerItems;
    }

    @NonNull
    @Override
    public SchedulerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SchedulerViewHolder(LayoutInflater.from(context).inflate(R.layout.scheduler_layout, parent, false), context);
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
    }

    @Override
    public int getItemCount() {
        return schedulerItems.size();
    }

    ////CHANGE INT TO MM:SS/////
    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
