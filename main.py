from adafruit_mqtt import Adafruit_MQTT
import time
import random
import json
from fsm import *
from physic import *
from timer import *

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

# physic1 = Physic()
setTimer(1,15)
while True:
    timerRun()
    
    sched1.run()
    if (timer_flag[1]):
        setTimer(1,15)
        client.publish("mositure", 0)
        client.publish("temperature", 2500)
    
    time.sleep(1)
    pass
