package com.iot232.ssis.recycler;

public interface SchedulerViewInterface {
    void onItemClick(int position);
    void onItemDeleted(int position);
    void onInfoChanged(int position, int type, int duration, long longDuration, String str);
    boolean checkTimer(int position);
    boolean checkSchedules(int postition);
}
