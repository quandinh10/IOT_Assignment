from adafruit_mqtt import Adafruit_MQTT
import time
import random
import json
from fsm import *
from physic import *
from timer import *

TEMP = "soil_temperature"
MOISTURE = "soil_moisture"

def received(feed_id, payload):
    data_dict = json.loads(payload)
    
    # data_dict contains startTime
    if (len(data_dict) == 8):
        sched1.appendSched(data_dict)
    
    # data_dict does not contain startTime
    elif (len(data_dict) == 7):
        sched1.currSched = data_dict
        
    # manual message *do it later*
    elif (len(data_dict) == 1):
        
        return


client = Adafruit_MQTT()
client.setRecvCallBack(received)

sched1 = FarmScheduler(True)

physic1 = Physic()
setTimer(1,10)
state = False
setTimer(2,10)
while True:
    timerRun()
    
    sched1.run()
    if (timer_flag[1]):
        setTimer(1,10)
        client.publish("moisture", physic1.readSensors(MOISTURE))
        client.publish("temperature", physic1.readSensors(TEMP))
        
    if (timer_flag[2]):
        setTimer(2,10)
        if (state):
            physic1.setActuators(1, False)
            state = False
        else:
            physic1.setActuators(1, True)
            state = True
        
    time.sleep(1)
    pass
