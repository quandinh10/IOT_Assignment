package com.iot232.ssis.fragments;

import static com.iot232.ssis.MainActivity.getCurrentEpochTime;

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
import androidx.lifecycle.ViewModelProvider;

import com.iot232.ssis.MainActivity;

import com.iot232.ssis.R;
import com.iot232.ssis.databinding.FragmentDashboardBinding;
import com.iot232.ssis.recycler.SchedulerViewInterface;

import java.util.HashMap;
import java.util.Map;

public class DashboardFragment extends Fragment {
    View mView;
    MainActivity mainActivity;
    CardView mixer1Card, mixer2Card, mixer3Card, area1Card, area2Card, area3Card, pump1Card, pump2Card;
    TextView mixer1Time, mixer2Time, mixer3Time, pump1Time, pump2Time;
    ToggleButton mixer1Button, mixer2Button, mixer3Button, area1Button, area2Button, area3Button, pump1Button, pump2Button;
    TextView mixerTitle, areaTitle, pumpTitle;

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        mainActivity = (MainActivity) getActivity();

        assert mainActivity != null;
        mainActivity.checkCurrentFragment();

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
        for (int i : mainActivity.timerTypes) mainActivity.setTextView(i, mainActivity.getDuration(i, 0, mainActivity.timerInfo));

        /////INIT BUTTON STATE/////
        mixer1Button.setChecked(mainActivity.timerInfo.getMixerState() == mainActivity.MIXER1);
        mixer2Button.setChecked(mainActivity.timerInfo.getMixerState() == mainActivity.MIXER2);
        mixer3Button.setChecked(mainActivity.timerInfo.getMixerState() == mainActivity.MIXER3);
        pump1Button.setChecked(mainActivity.timerInfo.getPumpState() == mainActivity.PUMP1);
        pump2Button.setChecked(mainActivity.timerInfo.getPumpState() == mainActivity.PUMP2);
        area1Button.setChecked(mainActivity.timerInfo.getAreaType() == mainActivity.AREA1);
        area2Button.setChecked(mainActivity.timerInfo.getAreaType() == mainActivity.AREA2);
        area3Button.setChecked(mainActivity.timerInfo.getAreaType() == mainActivity.AREA3);

        //////CHANGE DURATION/////////
        mixer1Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDuration(mainActivity.MIXER1, "Mixer 1");
            }
        });
        mixer2Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDuration(mainActivity.MIXER2, "Mixer 2");
            }
        });
        mixer3Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDuration(mainActivity.MIXER3, "Mixer 3");
            }
        });
        pump1Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDuration(mainActivity.PUMP1, "Pump 1");
            }
        });
        pump2Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDuration(mainActivity.PUMP2, "Pump 2");
            }
        });

        ///////BUTTONS////////
        mixer1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivity.timerInfo.getMixer1Time() == 0) invalidAction(mixer1Button, mainActivity.NO_TIMER);
                else if (mainActivity.timerInfo.getMixerState() == mainActivity.NAN)  buttonPressed(mainActivity.MIXER1, true);
                else if (mainActivity.timerInfo.getMixerState() == mainActivity.MIXER1)  buttonPressed(mainActivity.MIXER1, false);
                else invalidAction(mixer1Button, mainActivity.NO_ACTION);
            }
        });
        mixer2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivity.timerInfo.getMixer2Time() == 0) invalidAction(mixer2Button, mainActivity.NO_TIMER);
                else if (mainActivity.timerInfo.getMixerState() == mainActivity.NAN)  buttonPressed(mainActivity.MIXER2, true);
                else if (mainActivity.timerInfo.getMixerState() == mainActivity.MIXER2) buttonPressed(mainActivity.MIXER2, false);
                else invalidAction(mixer2Button, mainActivity.NO_ACTION);
            }
        });
        mixer3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivity.timerInfo.getMixer3Time() == 0) invalidAction(mixer3Button, mainActivity.NO_TIMER);
                else if (mainActivity.timerInfo.getMixerState() == mainActivity.NAN)  buttonPressed(mainActivity.MIXER3, true);
                else if (mainActivity.timerInfo.getMixerState() == mainActivity.MIXER3) buttonPressed(mainActivity.MIXER3, false);
                else invalidAction(mixer3Button, mainActivity.NO_ACTION);
            }
        });
        area1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivity.timerInfo.getAreaType() == mainActivity.AREA1) {
                    mainActivity.timerInfo.setAreaType(mainActivity.NAN);
                    buttonPressed(mainActivity.AREA1, false);
                }
                else {
                    mainActivity.timerInfo.setAreaType(mainActivity.AREA1);
                    mainActivity.sendSchedule(mainActivity.NAN, mainActivity.AREA1, "selector", mainActivity.NAN);
                    buttonPressed(mainActivity.AREA1, true);
                    buttonPressed(mainActivity.AREA2, false);
                    buttonPressed(mainActivity.AREA3, false);
                }
            }
        });
        area2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivity.timerInfo.getAreaType() == mainActivity.AREA2) {
                    mainActivity.timerInfo.setAreaType(mainActivity.NAN);
                    buttonPressed(mainActivity.AREA2, false);
                }
                else {
                    mainActivity.timerInfo.setAreaType(mainActivity.AREA2);
                    mainActivity.sendSchedule(mainActivity.NAN, mainActivity.AREA2, "selector", mainActivity.NAN);
                    buttonPressed(mainActivity.AREA2, true);
                    buttonPressed(mainActivity.AREA1, false);
                    buttonPressed(mainActivity.AREA3, false);

                }
            }
        });
        area3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivity.timerInfo.getAreaType() == mainActivity.AREA3) {
                    mainActivity.timerInfo.setAreaType(mainActivity.NAN);
                    buttonPressed(mainActivity.AREA3, false);
                }
                else {
                    mainActivity.timerInfo.setAreaType(mainActivity.AREA3);
                    mainActivity.sendSchedule(mainActivity.NAN, mainActivity.AREA3, "selector", mainActivity.NAN);
                    buttonPressed(mainActivity.AREA3, true);
                    buttonPressed(mainActivity.AREA2, false);
                    buttonPressed(mainActivity.AREA1, false);

                }
            }
        });
        pump1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivity.timerInfo.getPump1Time() == 0) invalidAction(pump1Button, mainActivity.NO_TIMER);
                else if (mainActivity.timerInfo.getPumpState() == mainActivity.NAN)  buttonPressed(mainActivity.PUMP1, true);
                else if (mainActivity.timerInfo.getPumpState() == mainActivity.PUMP1) buttonPressed(mainActivity.PUMP1, false);
                else invalidAction(pump1Button, mainActivity.NO_ACTION);
            }
        });
        pump2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivity.timerInfo.getPump2Time() == 0) invalidAction(pump2Button, mainActivity.NO_TIMER);
                else if (mainActivity.timerInfo.getPumpState() == mainActivity.NAN)  buttonPressed(mainActivity.PUMP2, true);
                else if (mainActivity.timerInfo.getPumpState() == mainActivity.PUMP2) buttonPressed(mainActivity.PUMP2, false);
                else invalidAction(pump2Button, mainActivity.NO_ACTION);
            }
        });

        return mView;
    }
    //////////////////////

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //////CHANGE DURATION POPUP//////
    public void changeDuration(int type, String title) {
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

        Button popupButton1 = view.findViewById(R.id.popup_button1);
        popupButton1.setText("Cancel");
        popupButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        Button popupButton2 = view.findViewById(R.id.popup_button2);
        popupButton2.setText("Change");
        popupButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(insertText.getText().toString()) && TextUtils.isDigitsOnly(insertText.getText().toString())) {
                    saveChange(type, insertText.getText().toString());
                    alertDialog.dismiss();
                } else insertText.setError("Please enter an integer");

            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    /////INVALID ACTION POPUP/////
    ////2 TYPE - NO_ACTION & NO_TIMER/////
    ////DESCRIPTION ALREADY SET IN POPUP TEXT/////
    public void invalidAction(ToggleButton toggleButton, int type) {
        toggleButton.setChecked(!toggleButton.isChecked());
        ConstraintLayout constraintLayout = mView.findViewById(R.id.popupDialog);
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.popup_layout, constraintLayout);

        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();

        TextView notiText = view.findViewById(R.id.popup_title);
        notiText.setText("Invalid action");

        TextView popupText = view.findViewById(R.id.popup_desc);
        if (type == mainActivity.NO_ACTION) popupText.setText("Action unavailable, a timer is already active.");
        else if (type == mainActivity.NO_TIMER) popupText.setText("Please set timer before activate.");

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
    private void saveChange(int type, String str) {
        if (type == mainActivity.MIXER1) mainActivity.timerInfo.setMixer1Time(Integer.parseInt(str));
        else if (type == mainActivity.MIXER2) mainActivity.timerInfo.setMixer2Time(Integer.parseInt(str));
        else if (type == mainActivity.MIXER3) mainActivity.timerInfo.setMixer3Time(Integer.parseInt(str));
        else if (type == mainActivity.PUMP1) mainActivity.timerInfo.setPump1Time(Integer.parseInt(str));
        else if (type == mainActivity.PUMP2) mainActivity.timerInfo.setPump2Time(Integer.parseInt(str));
        mainActivity.setTextView(type, mainActivity.getDuration(type, 0, mainActivity.timerInfo));
        mainActivity.contentHelper.writeContent(mainActivity.timerInfo, "timerInfo.json", mainActivity);
    }


    //////ON BUTTON PRESSED//////
    public void buttonPressed(int type, boolean status) {
        if (type == mainActivity.MIXER1) mixer1Button.setChecked(status);
        else if (type == mainActivity.MIXER2) mixer2Button.setChecked(status);
        else if (type == mainActivity.MIXER3) mixer3Button.setChecked(status);
        else if (type == mainActivity.PUMP1) pump1Button.setChecked(status);
        else if (type == mainActivity.PUMP2) pump2Button.setChecked(status);
        else if (type == mainActivity.AREA1) area1Button.setChecked(status);
        else if (type == mainActivity.AREA2) area2Button.setChecked(status);
        else if (type == mainActivity.AREA3) area3Button.setChecked(status);

        if (mainActivity.AREA1 <= type && type <= mainActivity.AREA3) return;
        if (status){
            mainActivity.startTimer(type, 0, mainActivity.timerInfo, 0,null, null);
            if (type >= mainActivity.MIXER1 && type <= mainActivity.MIXER3) {
                mainActivity.timerInfo.setMixerState(type);
                mainActivity.timerInfo.setMixerStart(getCurrentEpochTime());
                mainActivity.sendSchedule(type, mainActivity.getDuration(type, 0, mainActivity.timerInfo), "mixer", mainActivity.MIXER1);
            }
            else if (type >= mainActivity.PUMP1 && type <= mainActivity.PUMP2) {
                mainActivity.timerInfo.setPumpState(type);
                mainActivity.timerInfo.setPumpStart(getCurrentEpochTime());
                mainActivity.sendSchedule(type, mainActivity.getDuration(type, 0, mainActivity.timerInfo), "pump", mainActivity.PUMP1);
            }
        }
        else{
            mainActivity.setTextView(type, mainActivity.getDuration(type, 0, mainActivity.timerInfo));
            mainActivity.stopTimer(type, mainActivity.timerInfo);
        }
    }

    public TextView getMixer1Time() {
        return mixer1Time;
    }

    public TextView getMixer2Time() {
        return mixer2Time;
    }

    public TextView getMixer3Time() {
        return mixer3Time;
    }

    public TextView getPump1Time() {
        return pump1Time;
    }

    public TextView getPump2Time() {
        return pump2Time;
    }
}