package com.iot232.ssis.helper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.iot232.ssis.MainActivity;
import com.iot232.ssis.SettingsActivity;
import com.iot232.ssis.data.AdaInfo;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


public class MqttHelper {
    private AdaInfo adaInfo;
    public MqttClient client;

    public final String[] topics = {"stryz_0709/feeds/sensor1", "stryz_0709/feeds/sensor2", "stryz_0709/feeds/sensor3"};


    public MqttHelper(Context context){
        if (context instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) context;
            if (mainActivity.adaInfo != null) adaInfo = mainActivity.adaInfo;
        }

        try {
            client = new MqttClient("tcp://io.adafruit.com:1883", adaInfo.clientID, new MemoryPersistence());
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setUserName(adaInfo.username);
            connectOptions.setPassword(adaInfo.password.toCharArray());
            connectOptions.setCleanSession(true);
            client.connect(connectOptions);
//            Toast.makeText(mainActivity, "Connected", Toast.LENGTH_SHORT).show();
            subscribeToTopic();
        } catch (MqttException e) {
//            Toast.makeText(mainActivity, "No signal", Toast.LENGTH_SHORT).show();
            Log.d("MQTT", "Connection failure");
            e.printStackTrace();
        }
    }


    private void subscribeToTopic() {
        for (String topic : topics) {
            try {client.subscribe(topic);}
            catch (MqttException ex) {
                ex.printStackTrace();
                Log.d("MQTT", "Subscribing failure");
            }
        }
        Log.d("MQTT", "Subscribing success");
    }

    public void setCallback(MqttCallbackExtended callback) {
        client.setCallback(callback);
    }

    public boolean isConnected(){
        return client.isConnected();
    }
}