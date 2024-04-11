package com.iot232.ssis.data;

public class TimerInfo {
    public int mixer1Time, mixer2Time, mixer3Time, area1Time, area2Time, area3Time, pump1Time, pump2Time;

    public TimerInfo() {
        this.mixer1Time = 5;
        this.mixer2Time = 5;
        this.mixer3Time = 5;
        this.area1Time = 5;
        this.area2Time = 5;
        this.area3Time = 5;
        this.pump1Time = 5;
        this.pump2Time = 5;
    }

    public TimerInfo(int mixer1Time, int mixer2Time, int mixer3Time, int area1Time, int area2Time, int area3Time, int pump1Time, int pump2Time) {
        this.mixer1Time = mixer1Time;
        this.mixer2Time = mixer2Time;
        this.mixer3Time = mixer3Time;
        this.area1Time = area1Time;
        this.area2Time = area2Time;
        this.area3Time = area3Time;
        this.pump1Time = pump1Time;
        this.pump2Time = pump2Time;
    }
}
