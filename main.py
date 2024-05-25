from adafruit_mqtt import Adafruit_MQTT
import time
import random
import json
from fsm import *
from physic import *
from timer import *

TEMP = "soil_temperature"
MOISTURE = "soil_moisture"
currDevice = None

def received(feed_id, payload):
    data_dict = json.loads(payload)
    
    # data_dict contains startTime
    if (len(data_dict) == 8):
        sched1.appendSched(data_dict)
    
    # data_dict does not contain startTime
    elif (len(data_dict) == 7):
        if (sched1.currSched):
            return
        sched1.currSched = data_dict
        
    # manual message *do it later*
    elif (len(data_dict) == 1):
        dict_key = next(iter(data_dict))
        duration = data_dict[dict_key]
        setTimer(3, duration)
        if ('mixer1' in data_dict):
            currDevice = MIXER1
        elif ('mixer2' in data_dict):
            currDevice = MIXER2
        elif ('mixer3' in data_dict):
            currDevice = MIXER3
        elif ('pump_in' in data_dict):
            currDevice = PUMPIN
        elif ('pump_out' in data_dict):
            currDevice = PUMPOUT
        if PHYSIC:
            physic1.setsetActuators(currDevice, True)
        


client = Adafruit_MQTT()
client.setRecvCallBack(received)

physic1 = Physic()
# physic1 = None
sched1 = FarmScheduler(physic1,True)


'''
    timer0: for FarmScheduler
    timer1: for environmental monitoring (temp and moisture)
    timer2: for physic testing
'''

setTimer(1,10)
state = False
setTimer(2,10)
while True:
    timerRun()
    
    ### Uncomment below code to enable sending moi and tmp to server ###
    # sched1.run()
    # if (timer_flag[1]):
    #     setTimer(1,10)
        # client.publish("moisture", physic1.readSensors(MOISTURE))
        # client.publish("temperature", physic1.readSensors(TEMP))
        
    # if (timer_flag[2]):
    #     setTimer(2,10)
    #     if (state):
    #         physic1.setActuators(2, False)
    #         state = False
    #     else:
    #         physic1.setActuators(2, True)
    #         state = True
        
    # if (timer_flag[3]):
    #     setTimer(3, 0)
    #     if (PHYSIC):
    #         physic1.setActuators(currDevice, False)
    #         currDevice = None
    #     print("Manual: FINISHED!!!")
    time.sleep(1)
    # print(physic1.serial_read_data())
    pass
