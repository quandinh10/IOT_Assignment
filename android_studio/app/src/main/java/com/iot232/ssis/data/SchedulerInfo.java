package com.iot232.ssis.data;

public class SchedulerInfo {
    public int id, startTime, areaType, mixer1Time, mixer2Time, mixer3Time, pump1Time, pump2Time;
    public String schedulerTitle;

    public SchedulerInfo() {
        this.id = 0;
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

    public int getId() {
        return id;
    }

    public SchedulerInfo(String schedulerTitle, int id, int startTime, int areaType, int mixer1Time, int mixer2Time, int mixer3Time, int pump1Time, int pump2Time){
        this.id = id;
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

    public void setId(int id) {
        this.id = id;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public void setAreaType(int areaType) {
        this.areaType = areaType;
    }

    public void setMixer1Time(int mixer1Time) {
        this.mixer1Time = mixer1Time;
    }

    public void setMixer2Time(int mixer2Time) {
        this.mixer2Time = mixer2Time;
    }

    public void setMixer3Time(int mixer3Time) {
        this.mixer3Time = mixer3Time;
    }

    public void setPump1Time(int pump1Time) {
        this.pump1Time = pump1Time;
    }

    public void setPump2Time(int pump2Time) {
        this.pump2Time = pump2Time;
    }

    public void setSchedulerTitle(String schedulerTitle) {
        this.schedulerTitle = schedulerTitle;
    }
}
