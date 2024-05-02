package com.iot232.ssis.data;

public class TimerInfo {
    private int id;
    private int schedulerState;
    private int mixer1Time;
    private int mixer2Time;
    private int mixer3Time;
    private int areaType;
    private int pump1Time;
    private int pump2Time;
    private int mixerState;
    private int pumpState;

    public int getCycleCount() {
        return cycleCount;
    }

    public void setCycleCount(int cycleCount) {
        this.cycleCount = cycleCount;
    }

    private int cycleCount;
    private long mixerStart, pumpStart;
    private String schedulerTitle;

    public TimerInfo() {
        this.schedulerTitle = "";
        this.schedulerState = 0;
        this.id = 0;

        this.mixerState = 0;
        this.mixerStart = 0;
        this.mixer1Time = 0;
        this.mixer2Time = 0;
        this.mixer3Time = 0;

        this.pumpState = 0;
        this.pumpStart = 0;
        this.pump1Time = 0;
        this.pump2Time = 0;

        this.cycleCount = 0;

        this.areaType = 0;
    }

    public long getMixerStart() {
        return mixerStart;
    }

    public void setMixerStart(long mixerStart) {
        this.mixerStart = mixerStart;
    }

    public long getPumpStart() {
        return pumpStart;
    }

    public void setPumpStart(long pumpStart) {
        this.pumpStart = pumpStart;
    }

    public TimerInfo(String schedulerTitle, int id, int schedulerState, int areaType, int mixerState, long mixerStart, int mixer1Time, int mixer2Time, int mixer3Time, int pumpState, long pumpStart, int pump1Time, int pump2Time, int cycleCount) {
        this.id = id;
        this.schedulerState = schedulerState;
        this.schedulerTitle = schedulerTitle;

        this.mixerState = mixerState;
        this.mixerStart = mixerStart;
        this.mixer1Time = mixer1Time;
        this.mixer2Time = mixer2Time;
        this.mixer3Time = mixer3Time;

        this.pumpState = pumpState;
        this.pumpStart = pumpStart;
        this.pump1Time = pump1Time;
        this.pump2Time = pump2Time;

        this.areaType = areaType;
        this.cycleCount = cycleCount;

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
