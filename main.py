from adafruit_mqtt import Adafruit_MQTT
import time
import random
# from rs485 import *
from fsm import *

def received(feed_id, payload):
    pass


# client = Adafruit_MQTT()
# client.setRecvCallBack(received) 

mqtt_json_data = '{"mixer1": 10, "mixer2": 10, "mixer3": 10, "pump_in": 5, "pump_out": 5, "selector": "A", "cycle": 2}'
sched1 = FarmScheduler(mqtt_json_data, True)

while True:
    sched1.start()
    time.sleep(1)
