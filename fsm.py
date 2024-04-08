# from physic import *
from timer import *
from datetime import datetime
import pytz

class FarmScheduler():
    
    def __init__(self, debug=False):
        self.sched = []
        self.currSched = {}
        self.currState = "IDLE"
        self.timerState = Timer(0)
        self.debug = debug
        
    def run(self):        
        if (not self.currSched):
            result = self.checkSched()
            if (result != -1):
                self.currSched = self.sched[result]
            else: 
                return
        
        if (self.debug):
            print(self.timerState.timer_counter)
            
        if (self.currState == "IDLE"):
            if (self.currSched['cycle'] > 0):
                if (self.debug):
                    print("NEW CYLCLE!")
                self.timerState.setTimer(int(self.currSched['mixer1']))
                self.currState = "MIXER1"
                self.currSched['cycle'] -= 1
            else:
                print("FINISHED !!!")
                self.currSched = {}
                
        elif (self.currState == "MIXER1"):
            if (self.timerState.timer_flag):
                if (self.debug):
                    print("MIXER1 DONE!")
                self.currState = "MIXER2"
                self.timerState.setTimer(int(self.currSched['mixer2']))
                
        elif (self.currState == "MIXER2"):
            if (self.timerState.timer_flag):
                if (self.debug):
                    print("MIXER2 DONE!")
                self.currState = "MIXER3"
                self.timerState.setTimer(int(self.currSched['mixer3']))
    
        elif (self.currState == "MIXER3"):
            if (self.timerState.timer_flag):
                if (self.debug):
                    print("MIXER3 DONE!")
                self.currState = "PUMPIN"
                self.timerState.setTimer(int(self.currSched['pump_in']))

        elif (self.currState == "PUMPIN"):
            if (self.timerState.timer_flag):
                if (self.debug):
                    print("PUMPIN DONE!")
                self.currState = "PUMPOUT"
                self.timerState.setTimer(int(self.currSched['pump_out']))

        elif (self.currState == "PUMPOUT"):
            if (self.timerState.timer_flag):
                if (self.debug):
                    print("PUMPOUT DONE!")
                self.currState = "IDLE"
                self.timerState.setTimer(0)
    
    def appendSched(self, data):
        self.sched.append(data)
        if (self.debug):
            print(self.sched)
            
    def checkSched(self):
        if (len(self.sched) == 0):
            return -1
        now = datetime.now()
        current_time = now.strftime("%H:%M")
        for i in range(len(self.sched)):
            if (current_time == self.sched[i]['startTime']):
                return i
        return -1
                
if __name__ == '__main__':
    sched1 = FarmScheduler()
    mqtt_json_data_t1 = '{"mixer1": 3, "mixer2": 3, "mixer3": 3, "pump_in": 3, "pump_out": 3, "selector": "A", "cycle": 2, "startTime": "16:43"}'
    mqtt_json_data_t2 = '{"mixer1": 3, "mixer2": 3, "mixer3": 3, "pump_in": 3, "pump_out": 3, "selector": "A", "cycle": 2, "startTime": "15:30"}'
    
    mqtt_json_data_1 = '{"mixer1": 5, "mixer2": 5, "mixer3": 5, "pump_in": 5, "pump_out": 5, "selector": "A", "cycle": 2}'
    mqtt_json_data_2 = '{"mixer1": 7, "mixer2": 6, "mixer3": 5, "pump_in": 4, "pump_out": 3, "selector": "A", "cycle": 1}'

    
    sched1.appendSched(mqtt_json_data_t1)
    sched1.appendSched(mqtt_json_data_t2)
    print(sched1.sched[1]['cycle'])
    

