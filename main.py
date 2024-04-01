from adafruit_mqtt import Adafruit_MQTT
import time
import random
from rs485 import *

def received(feed_id, payload):
    print("Received from",feed_id,":",payload)
    # if feed_id == "led":
    #     if payload == "0":
    #         writeData("1")
    #     else:
    #         writeData("2")
            
    # if feed_id == "pipe":
    #     if payload == "0":
    #         writeData("3")
    #     else:
    #         writeData("4")


client = Adafruit_MQTT()
client.setRecvCallBack(received)

counter = 5

while True:
    pass
