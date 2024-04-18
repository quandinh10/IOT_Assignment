package com.iot232.ssis.data;

public class TimerInfo {
    private int id, schedulerState, startTime, mixer1Time, mixer2Time, mixer3Time, areaType, pump1Time, pump2Time,
            mixerState, pumpState;
    private String schedulerTitle;

    public TimerInfo() {
        this.schedulerTitle = "";
        this.schedulerState = 0;
        this.startTime = 0;
        this.id = -1;
        this.mixer1Time = 0;
        this.mixer2Time = 0;
        this.mixer3Time = 0;
        this.areaType = 0;
        this.pump1Time = 0;
        this.pump2Time = 0;
        this.mixerState = 0;
        this.pumpState = 0;
    }

    public TimerInfo(String schedulerTitle, int id, int schedulerState, int startTime, int areaType, int mixer1Time, int mixer2Time, int mixer3Time, int pump1Time, int pump2Time, int mixerState, int pumpState) {
        this.id = id;
        this.schedulerState = schedulerState;
        this.schedulerTitle = schedulerTitle;
        this.startTime = startTime;
        this.mixer1Time = mixer1Time;
        this.mixer2Time = mixer2Time;
        this.areaType = areaType;
        this.mixer3Time = mixer3Time;
        this.pump1Time = pump1Time;
        this.pump2Time = pump2Time;
        this.mixerState = mixerState;
        this.pumpState = pumpState;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSchedulerState() {
        return schedulerState;
    }

    public void setSchedulerState(int schedulerState) {
        this.schedulerState = schedulerState;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getMixer1Time() {
        return mixer1Time;
    }

    public void setMixer1Time(int mixer1Time) {
        this.mixer1Time = mixer1Time;
    }

    public int getMixer2Time() {
        return mixer2Time;
    }

    public void setMixer2Time(int mixer2Time) {
        this.mixer2Time = mixer2Time;
    }

    public int getMixer3Time() {
        return mixer3Time;
    }

    public void setMixer3Time(int mixer3Time) {
        this.mixer3Time = mixer3Time;
    }

    public int getAreaType() {
        return areaType;
    }

    public void setAreaType(int areaType) {
        this.areaType = areaType;
    }

    public int getPump1Time() {
        return pump1Time;
    }

    public void setPump1Time(int pump1Time) {
        this.pump1Time = pump1Time;
    }

    public int getPump2Time() {
        return pump2Time;
    }

    public void setPump2Time(int pump2Time) {
        this.pump2Time = pump2Time;
    }

    public String getSchedulerTitle() {
        return schedulerTitle;
    }

    public void setSchedulerTitle(String schedulerTitle) {
        this.schedulerTitle = schedulerTitle;
    }

    public int getMixerState() {
        return mixerState;
    }

    public void setMixerState(int mixerState) {
        this.mixerState = mixerState;
    }

    public int getPumpState() {
        return pumpState;
    }

    public void setPumpState(int pumpState) {
        this.pumpState = pumpState;
    }
}
