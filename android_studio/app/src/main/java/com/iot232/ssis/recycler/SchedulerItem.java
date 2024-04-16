package com.iot232.ssis.recycler;

public class SchedulerItem {
    public int startTime, areaType, mixer1Time, mixer2Time, mixer3Time, pump1Time, pump2Time;
    public String schedulerTitle;

    public SchedulerItem() {
        this.schedulerTitle = "";
        this.startTime = 0;
        this.mixer1Time = 0;
        this.mixer2Time = 0;
        this.areaType = 0;
        this.mixer3Time = 0;
        this.pump1Time = 0;
        this.pump2Time = 0;
    }

    public int getAreaType() {
        return areaType;
    }

    public String getSchedulerTitle() {
        return schedulerTitle;
    }

    public SchedulerItem(String schedulerTitle, int startTime, int areaType, int mixer1Time, int mixer2Time, int mixer3Time, int pump1Time, int pump2Time){
        this.schedulerTitle = schedulerTitle;
        this.startTime = startTime;
        this.mixer1Time = mixer1Time;
        this.mixer2Time = mixer2Time;
        this.areaType = areaType;
        this.mixer3Time = mixer3Time;
        this.pump1Time = pump1Time;
        this.pump2Time = pump2Time;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getMixer1Time() {
        return mixer1Time;
    }

    public int getMixer2Time() {
        return mixer2Time;
    }

    public int getMixer3Time() {
        return mixer3Time;
    }

    public int getPump1Time() {
        return pump1Time;
    }

    public int getPump2Time() {
        return pump2Time;
    }
}
