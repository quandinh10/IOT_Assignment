package com.iot232.ssis.ui;

import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.iot232.ssis.MainActivity;

import com.iot232.ssis.R;
import com.iot232.ssis.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {
    View mView;
    MainActivity mainActivity;
    CardView mixer1Card, mixer2Card, mixer3Card, area1Card, area2Card, area3Card, pump1Card, pump2Card;
    TextView mixer1Time, mixer2Time, mixer3Time, area1Time, area2Time, area3Time, pump1Time, pump2Time;
    ToggleButton mixer1Button, mixer2Button, mixer3Button, area1Button, area2Button, area3Button, pump1Button, pump2Button;
    boolean isMixerSelected, isAreaSelected, isPumpSelected;

    TextView mixerTitle, areaTitle, pumpTitle;

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        mainActivity = (MainActivity) getActivity();

        for (int i = 0; i < 3; i ++) setSelected(i, false);

        /////INITIALIZATION, DO NOT TOUCH//////
        mixer1Card = mView.findViewById(R.id.mixer1Card);
        mixer2Card = mView.findViewById(R.id.mixer2Card);
        mixer3Card = mView.findViewById(R.id.mixer3Card);
        area1Card = mView.findViewById(R.id.area1Card);
        area2Card = mView.findViewById(R.id.area2Card);
        area3Card = mView.findViewById(R.id.area3Card);
        pump1Card = mView.findViewById(R.id.pump1Card);
        pump2Card = mView.findViewById(R.id.pump2Card);

        mixer1Time = mView.findViewById(R.id.mixer1Time);
        mixer2Time = mView.findViewById(R.id.mixer2Time);
        mixer3Time = mView.findViewById(R.id.mixer3Time);
        area1Time = mView.findViewById(R.id.area1Time);
        area2Time = mView.findViewById(R.id.area2Time);
        area3Time = mView.findViewById(R.id.area3Time);
        pump1Time = mView.findViewById(R.id.pump1Time);
        pump2Time = mView.findViewById(R.id.pump2Time);

        mixer1Button = mView.findViewById(R.id.mixer1Button);
        mixer2Button = mView.findViewById(R.id.mixer2Button);
        mixer3Button = mView.findViewById(R.id.mixer3Button);
        area1Button = mView.findViewById(R.id.area1Button);
        area2Button = mView.findViewById(R.id.area2Button);
        area3Button = mView.findViewById(R.id.area3Button);
        pump1Button = mView.findViewById(R.id.pump1Button);
        pump2Button = mView.findViewById(R.id.pump2Button);

        mixerTitle = mView.findViewById(R.id.mixerText);
        areaTitle = mView.findViewById(R.id.areaText);
        pumpTitle = mView.findViewById(R.id.pumpText);

        /////SET TIMER OF RELAYS////
        setTimer();

        //////CHANGE FEEDS////////
        mixerTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFeed(R.id.mixerText, "Mixer");
            }
        });
        areaTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFeed(R.id.areaText, "Area");
            }
        });
        pumpTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFeed(R.id.pumpText, "Pump");
            }
        });


        //////CHANGE DURATION/////////
        mixer1Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDuration(R.id.mixer1Card, "Mixer 1");
            }
        });
        mixer2Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDuration(R.id.mixer2Card, "Mixer 2");
            }
        });
        mixer3Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDuration(R.id.mixer3Card, "Mixer 3");
            }
        });
        area1Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDuration(R.id.area1Card, "Area 1");
            }
        });
        area2Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDuration(R.id.area2Card, "Area 2");
            }
        });
        area3Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDuration(R.id.area3Card, "Area 3");
            }
        });
        pump1Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDuration(R.id.pump1Card, "Pump 1");
            }
        });
        pump2Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDuration(R.id.pump2Card, "Pump 2");
            }
        });


        ///////BUTTONS////////
        mixer1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMixerSelected)  buttonPressed(mixer1Button, mixer1Time, mainActivity.timerInfo.mixer1Time, 0);
                else if (!mixer1Button.isChecked())  buttonUnpressed(mixer1Button, mixer1Time, mainActivity.timerInfo.mixer1Time, 0);
                else{
                    mixer1Button.setChecked(!mixer1Button.isChecked());
                    invalidAction();
                }
            }
        });
        mixer2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMixerSelected)  buttonPressed(mixer2Button, mixer2Time, mainActivity.timerInfo.mixer2Time, 0);
                else if (!mixer2Button.isChecked()) buttonUnpressed(mixer2Button, mixer2Time, mainActivity.timerInfo.mixer2Time, 0);
                else{
                    mixer2Button.setChecked(!mixer2Button.isChecked());
                    invalidAction();
                }
            }
        });
        mixer3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMixerSelected)  buttonPressed(mixer3Button, mixer3Time, mainActivity.timerInfo.mixer3Time, 0);
                else if (!mixer3Button.isChecked()) buttonUnpressed(mixer3Button, mixer3Time, mainActivity.timerInfo.mixer3Time, 0);
                else{
                    mixer3Button.setChecked(!mixer3Button.isChecked());
                    invalidAction();
                }
            }
        });
        area1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAreaSelected)  buttonPressed(area1Button, area1Time, mainActivity.timerInfo.area1Time, 1);
                else if (!area1Button.isChecked()) buttonUnpressed(area1Button, area1Time, mainActivity.timerInfo.area1Time, 1);
                else{
                    area1Button.setChecked(!area1Button.isChecked());
                    invalidAction();
                }
            }
        });
        area2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAreaSelected)  buttonPressed(area2Button, area2Time, mainActivity.timerInfo.area2Time, 1);
                else if (!area2Button.isChecked()) buttonUnpressed(area2Button, area2Time, mainActivity.timerInfo.area2Time, 1);
                else{
                    area2Button.setChecked(!area2Button.isChecked());
                    invalidAction();
                }
            }
        });
        area3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAreaSelected)  buttonPressed(area3Button, area3Time, mainActivity.timerInfo.area3Time, 1);
                else if (!area3Button.isChecked()) buttonUnpressed(area3Button, area3Time, mainActivity.timerInfo.area3Time, 1);
                else{
                    area3Button.setChecked(!area3Button.isChecked());
                    invalidAction();
                }
            }
        });
        pump1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPumpSelected)  buttonPressed(pump1Button, pump1Time, mainActivity.timerInfo.pump1Time, 2);
                else if (!pump1Button.isChecked()) buttonUnpressed(pump1Button, pump1Time, mainActivity.timerInfo.pump1Time, 2);
                else{
                    pump1Button.setChecked(!pump1Button.isChecked());
                    invalidAction();
                }
            }
        });
        pump2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPumpSelected)  buttonPressed(pump2Button, pump2Time, mainActivity.timerInfo.pump2Time, 2);
                else if (!pump2Button.isChecked()) buttonUnpressed(pump2Button, pump2Time, mainActivity.timerInfo.pump2Time, 2);
                else{
                    pump2Button.setChecked(!pump2Button.isChecked());
                    invalidAction();
                }
            }
        });


        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    ////CHANGE INT TO MM:SS/////
    public String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    //////CHANGE DURATION POPUP//////
    public void changeDuration(int id, String title) {
        ConstraintLayout constraintLayout = mView.findViewById(R.id.popupDialog);
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.popup_layout, constraintLayout);

        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();

        TextView notiText = view.findViewById(R.id.popup_title);
        notiText.setText(title);

        TextView popupText = view.findViewById(R.id.popup_desc);
        popupText.setText("Enter a duration in seconds");

        EditText insertText = view.findViewById(R.id.popup_insert);
        insertText.setHint("Duration");
        insertText.setInputType(InputType.TYPE_CLASS_NUMBER);

        //////CONNECT/////
        Button popupButton1 = view.findViewById(R.id.popup_button1);
        popupButton1.setText("Cancel");
        popupButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        //////////////////

        /////TRY AGAIN////
        Button popupButton2 = view.findViewById(R.id.popup_button2);
        popupButton2.setText("Change");
        popupButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(insertText.getText().toString()) && TextUtils.isDigitsOnly(insertText.getText().toString())) {
                    saveChange(id, insertText.getText().toString());
                    alertDialog.dismiss();
                } else insertText.setError("Please enter an integer");

            }
        });
        ///////////////

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    /////CHANGE FEED POPUP//////
    public void changeFeed(int id, String title) {
        ConstraintLayout constraintLayout = mView.findViewById(R.id.popupDialog);
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.popup_layout, constraintLayout);

        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();

        TextView notiText = view.findViewById(R.id.popup_title);
        notiText.setText(title);

        TextView popupText = view.findViewById(R.id.popup_desc);
        popupText.setText("Enter a feed");

        EditText insertText = view.findViewById(R.id.popup_insert);
        insertText.setHint("Feed");

        //////CONNECT/////
        Button popupButton1 = view.findViewById(R.id.popup_button1);
        popupButton1.setText("Cancel");
        popupButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        //////////////////

        /////TRY AGAIN////
        Button popupButton2 = view.findViewById(R.id.popup_button2);
        popupButton2.setText("Change");
        popupButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(insertText.getText().toString())) {
                    saveChange(id, insertText.getText().toString());
                    alertDialog.dismiss();
                } else insertText.setError("Please enter a valid feed");

            }
        });
        ///////////////

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }


    /////INVALID ACTION POPUP/////
    public void invalidAction() {
        ConstraintLayout constraintLayout = mView.findViewById(R.id.popupDialog);
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.popup_layout, constraintLayout);

        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();

        TextView notiText = view.findViewById(R.id.popup_title);
        notiText.setText("Invalid action");

        TextView popupText = view.findViewById(R.id.popup_desc);
        popupText.setText("Action unavailable, a timer is already active.");

        EditText insertText = view.findViewById(R.id.popup_insert);
        insertText.setVisibility(View.GONE);

        //////BUTTON1/////
        Button popupButton1 = view.findViewById(R.id.popup_button1);
        popupButton1.setVisibility(View.GONE);
        //////////////////

        /////BUTTON2////
        Button popupButton2 = view.findViewById(R.id.popup_button2);
        popupButton2.setText("Cancel");
        popupButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });
        ///////////////

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();
    }

    /////SAVE CHANGE////
    private void saveChange(int id, String str) {
        if (id == R.id.mixer1Card) mainActivity.timerInfo.mixer1Time = Integer.parseInt(str);
        else if (id == R.id.mixer2Card) mainActivity.timerInfo.mixer2Time = Integer.parseInt(str);
        else if (id == R.id.mixer3Card) mainActivity.timerInfo.mixer3Time = Integer.parseInt(str);
        else if (id == R.id.area1Card) mainActivity.timerInfo.area1Time = Integer.parseInt(str);
        else if (id == R.id.area2Card) mainActivity.timerInfo.area2Time = Integer.parseInt(str);
        else if (id == R.id.area3Card) mainActivity.timerInfo.area3Time = Integer.parseInt(str);
        else if (id == R.id.pump1Card) mainActivity.timerInfo.pump1Time = Integer.parseInt(str);
        else if (id == R.id.pump2Card) mainActivity.timerInfo.pump2Time = Integer.parseInt(str);
        else if (id == R.id.mixerText) mainActivity.timerInfo.mixerFeed = str;
        else if (id == R.id.areaText) mainActivity.timerInfo.areaFeed = str;
        else if (id == R.id.pumpText) mainActivity.timerInfo.pumpFeed = str;
        setTimer();
        mainActivity.contentHelper.writeContent(mainActivity.timerInfo, "userInfo.json");
    }

    ////SET TIMER/////
    private void setTimer() {
        mixer1Time.setText(formatTime(mainActivity.timerInfo.mixer1Time));
        mixer2Time.setText(formatTime(mainActivity.timerInfo.mixer2Time));
        mixer3Time.setText(formatTime(mainActivity.timerInfo.mixer3Time));
        area1Time.setText(formatTime(mainActivity.timerInfo.area1Time));
        area2Time.setText(formatTime(mainActivity.timerInfo.area2Time));
        area3Time.setText(formatTime(mainActivity.timerInfo.area3Time));
        pump1Time.setText(formatTime(mainActivity.timerInfo.pump1Time));
        pump2Time.setText(formatTime(mainActivity.timerInfo.pump2Time));
    }

    //////ON BUTTON PRESSED//////
    private void buttonPressed(ToggleButton toggleButton, TextView textView, int duration, int type) {
        Handler handler = new Handler();
        toggleButton.setChecked(true);
        setSelected(type, true);
        ////TODO SEND MQTT//////

        handler.postDelayed(new Runnable() {
            int i = duration;

            @Override
            public void run() {
                i--;
                if (i > 0 && checkSelected(type)) {
                    textView.setText(formatTime(i));
                    /////TODO SEND MQTT////

                    handler.postDelayed(this, 1000);
                }
                else {
                    textView.setText(formatTime(duration));
                    toggleButton.setChecked(false);
                    setSelected(type, false);
                }
            }
        }, 1000);
    }

    //////ON BUTTON UNPRESSED//////
    private void buttonUnpressed(ToggleButton toggleButton, TextView textView, int duration, int type) {
        Log.d("UNPRESSED", "UNPRESSED");
        textView.setText(formatTime(duration));
        toggleButton.setChecked(false);
        setSelected(type, false);
    }

    /////CHANGE STATE//////
    private void setSelected(int type, boolean state){
        switch (type){
            case 0:
                isMixerSelected = state;
                break;
            case 1:
                isAreaSelected = state;
                break;
            case 2:
                isPumpSelected = state;
                break;
        }
        Log.d(String.valueOf(type), String.valueOf(state));
    }

    //////CHECK STATE/////
    private boolean checkSelected(int type){
        switch (type){
            case 0: return isMixerSelected;
            case 1: return isAreaSelected;
            case 2: return isPumpSelected;
            default: return false;
        }
    }

}