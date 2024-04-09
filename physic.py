import sys
import time
import serial.tools.list_ports

TEMP = "soil_temperature"
MOISTURE = "soil_moisture"

class Physic:
    def __init__(self, debug_flag = 0):
        """Initializes the Physics class with a debug flag and the actuators and sensors formats.
        It attempts to open a serial connection to a specified port."""
        self.debug_flag = debug_flag # Debug flag to control debug output

        # Each key-value pair represents the command to turn a relay on or off
        # The array format is:
        # [ID, function code, starting address high byte, starting address low byte, data high byte, data low byte, CRC low byte, CRC high byte]
        self.RS485_actuartors_format = {
            'relay1_ON': [1, 6, 0, 0, 0, 255, 201, 138],
             'relay1_OFF': [1, 6, 0, 0, 0, 0, 137, 202],
             'relay2_ON': [2, 6, 0, 0, 0, 255, 201, 185],
             'relay2_OFF': [2, 6, 0, 0, 0, 0, 137, 249],
             'relay3_ON': [3, 6, 0, 0, 0, 255, 200, 104],
             'relay3_OFF': [3, 6, 0, 0, 0, 0, 136, 40],
             'relay4_ON': [4, 6, 0, 0, 0, 255, 201, 223],
             'relay4_OFF': [4, 6, 0, 0, 0, 0, 137, 159],
             'relay5_ON': [5, 6, 0, 0, 0, 255, 200, 14],
             'relay5_OFF': [5, 6, 0, 0, 0, 0, 136, 78],
             'relay6_ON': [6, 6, 0, 0, 0, 255, 200, 61],
             'relay6_OFF': [6, 6, 0, 0, 0, 0, 136, 125],
             'relay7_ON': [7, 6, 0, 0, 0, 255, 201, 236],
             'relay7_OFF': [7, 6, 0, 0, 0, 0, 137, 172],
             'relay8_ON': [8, 6, 0, 0, 0, 255, 201, 19],
             'relay8_OFF': [8, 6, 0, 0, 0, 0, 137, 83]
        }

        self.RS485_sensors_format = {
                "soil_temperature" : [1, 3, 0, 6, 0, 1, 100, 11],
                "soil_moisture" : [1, 3, 0, 7, 0, 1, 53, 203]
            }

        self.portname = self.getPort()  
        try:
            self.ser = serial.Serial(port=self.portname, baudrate=9600)
            print("Open successfully port: ", self.portname)
        except:
            print("Exception: Can not open the port")
            sys.exit() 

    def getPort(self):
        """Searches for and returns the first available USB serial port."""
        ports = serial.tools.list_ports.comports()
        commPort = "None"
        for i in range(len(ports)):
            port = ports[i]
            strPort = str(port)
            if "USB" in strPort:  # Checks if the port description contains 'USB'
                splitPort = strPort.split(" ")
                commPort = splitPort[0]  # Assumes the first part is the port name
        return commPort

    def serial_read_data(self):
        bytesToRead = self.ser.inWaiting() 
        if bytesToRead > 0:
            out = self.ser.read(bytesToRead)
            data_array = [b for b in out]  # Converts the bytes to a list for easier processing
            if self.debug_flag: print("Return data:", data_array)
            if len(data_array) >= 7:
                array_size = len(data_array)
                value = data_array[array_size - 4] * 256 + data_array[array_size - 3]
                return value
            else:
                return -1
        return 0

    def setActuators(self, ID, state):
        """Sends a command to set the state of an actuator (relay) based on its ID."""
        command_key = f'relay{ID}_{"ON" if state else "OFF"}'
        command_data = self.RS485_actuartors_format.get(command_key)
        self.ser.write(command_data)  # Sends the command data to the actuator
        result = self.serial_read_data()  # Reads the response from the actuator
        if self.debug_flag and (result == 0 or result == -1):
            print("Failed to set Actuator!")

    def readSensors(self, sensorName):
        """Sends a command to read data from a specified sensor."""
        command_data = self.RS485_sensors_format.get(sensorName)
        self.ser.write(command_data)  # Sends the command data to the sensor
        time.sleep(1)  # Wait a bit for the sensor to respond
        return_data, result = self.serial_read_data()  # Reads the response from the sensor
        return result  # Returns the decoded sensor value


        
        

if __name__ == '__main__':
    physic = Physic(True)  # Initialize the class with debug mode enabled

    # Test sequence for actuators and sensors
    while True:
        # Testing actuator control
        print("Testing Actuators with ID 2: ")
        print("Turn on relay_2: ")
        physic.setActuators(2, True)  # Turn on relay 2
        time.sleep(2)
        print("Turn off relay_2: ")
        physic.setActuators(2, False)  # Turn off relay 2
        time.sleep(2)

        # Testing sensor reading
        # print("\nTesting reading sensor: ")
        # print("Soil temperature: ", physic.readSensors(TEMP))  # Read and print soil temperature
        # time.sleep(1)
        # print("Soil moisture: ", physic.readSensors(MOISTURE))  # Read and print soil moisture
        # time.sleep(5)
