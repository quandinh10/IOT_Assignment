# from physic import *
from timer import *
import json

class FarmScheduler():
    def __init__(self, data, debug=False):
        data_dict = json.loads(data)
        print(data_dict)
        self.sched = data_dict
        self.currState = "IDLE"
        self.timerState = Timer(0)
        self.debug = debug
    def start(self):
        self.timerState.timerRun()
        
        if (self.debug):
            print(self.timerState.timer_counter)
            
        if (self.currState == "IDLE"):
            if (self.sched['cycle'] > 0):
                if (self.debug):
                    print("NEW CYLCLE!")
                self.timerState.setTimer(int(self.sched['mixer1']))
                self.currState = "MIXER1"
                self.sched['cycle'] -= 1
                
        elif (self.currState == "MIXER1"):
            if (self.timerState.timer_flag):
                if (self.debug):
                    print("MIXER1 DONE!")
                self.currState = "MIXER2"
                self.timerState.setTimer(int(self.sched['mixer2']))
                
        elif (self.currState == "MIXER2"):
            if (self.timerState.timer_flag):
                if (self.debug):
                    print("MIXER2 DONE!")
                self.currState = "MIXER3"
                self.timerState.setTimer(int(self.sched['mixer3']))
    
        elif (self.currState == "MIXER3"):
            if (self.timerState.timer_flag):
                if (self.debug):
                    print("MIXER3 DONE!")
                self.currState = "PUMPIN"
                self.timerState.setTimer(int(self.sched['pump_in']))

        elif (self.currState == "PUMPIN"):
            if (self.timerState.timer_flag):
                if (self.debug):
                    print("PUMPIN DONE!")
                self.currState = "PUMPOUT"
                self.timerState.setTimer(int(self.sched['pump_out']))

        elif (self.currState == "PUMPOUT"):
            if (self.timerState.timer_flag):
                if (self.debug):
                    print("PUMPOUT DONE!")
                self.currState = "IDLE"
                self.timerState.setTimer(0)
                
                
if __name__ == '__main__':
    mqtt_json_data = '{"mixer1": 10, "mixer2": 10, "mixer3": 10, "pump_in": 5, "pump_out": 5, "selector": "A", "cycle": 2}'
    sched1 = FarmScheduler(mqtt_json_data)