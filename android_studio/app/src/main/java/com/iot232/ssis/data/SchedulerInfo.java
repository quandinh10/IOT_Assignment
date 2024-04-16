package com.iot232.ssis.data;

public class SchedulerInfo {
    public int mixer1Time, mixer2Time, mixer3Time, areaType, pump1Time, pump2Time;
    public String schedulerName;

    public SchedulerInfo() {
        this.schedulerName = "";
        this.mixer1Time = 0;
        this.mixer2Time = 0;
        this.mixer3Time = 0;
        this.areaType = 0;
        this.pump1Time = 0;
        this.pump2Time = 0;
    }

    public SchedulerInfo(String schedulerName, int mixer1Time, int mixer2Time, int mixer3Time, int areaType, int pump1Time, int pump2Time) {
        this.schedulerName = schedulerName;
        this.mixer1Time = mixer1Time;
        this.mixer2Time = mixer2Time;
        this.mixer3Time = mixer3Time;
        this.areaType = areaType;
        this.pump1Time = pump1Time;
        this.pump2Time = pump2Time;
    }
}
