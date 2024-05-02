package com.iot232.ssis.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.iot232.ssis.MainActivity;
import com.iot232.ssis.SettingsActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class ContentHelper {
    @SuppressLint("StaticFieldLeak")
    static Activity activity;
    public ContentHelper(Context context){
        if (context instanceof MainActivity) activity = (MainActivity) context;
        else if (context instanceof SettingsActivity) activity = (SettingsActivity) context;
    }
    public <T> T loadContent(TypeToken<T> typeToken, String child, Activity activity) {
        File path = activity.getApplicationContext().getFilesDir();
        File readFrom = new File(path, child);
        if (!readFrom.exists()) {
            Log.d("CONTENT", "WRITE CONTENT");
            T info = null;
            writeContent(info, child, activity);
            return null;
        }
        try {
            Log.d("CONTENT", "LOAD CONTENT");
            printContentJson(child, activity);
            FileInputStream fis = new FileInputStream(readFrom);
            InputStreamReader isr = new InputStreamReader(fis);

            Gson gson = new Gson();
            T info = gson.fromJson(isr, typeToken.getType());

            isr.close();
            fis.close();

            return info;
        } catch (Exception e) {
            e.printStackTrace();
            return null; // or handle the exception accordingly
        }
    }

    public void writeContent(Object info, String child, Activity activity) {
        File path = activity.getFilesDir();
        try {
            // Convert info to JSON string
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(info);

            // Write JSON string to file
            FileOutputStream fos = new FileOutputStream(new File(path, child));
            Writer writer = new OutputStreamWriter(fos);
            writer.write(json);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        printContentJson(child, activity);
    }

    public String convertContent(Object info){
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.toJson(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void printContentJson(String child, Activity activity) {
        File path = activity.getFilesDir();
        File jsonFile = new File(path, child);

        try {
            // Read the JSON file
            FileInputStream fis = new FileInputStream(jsonFile);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder content = new StringBuilder();
            String line;

            // Read each line of the file and append it to the content StringBuilder
            while ((line = br.readLine()) != null) content.append(line);

            // Log the content of the JSON file
            Log.d(child, content.toString());

            // Close streams
            br.close();
            isr.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteJSONFile(String child, Activity activity) {
        File path = activity.getFilesDir();
        File fileToDelete = new File(path, child);

        try {
            if (fileToDelete.exists()) {
                fileToDelete.delete();
                Log.d("CONTENT", "File deleted: " + child);
            }
            else Log.d("CONTENT", "File does not exist: " + child);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
