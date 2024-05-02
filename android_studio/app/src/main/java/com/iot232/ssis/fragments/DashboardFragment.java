package com.iot232.ssis.fragments;

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

public class DashboardFragment extends Fragment {
    View mView;
    MainActivity mainActivity;
    CardView mixer1Card, mixer2Card, mixer3Card, area1Card, area2Card, area3Card, pump1Card, pump2Card;
    TextView mixer1Time, mixer2Time, mixer3Time, pump1Time, pump2Time;
    ToggleButton mixer1Button, mixer2Button, mixer3Button, area1Button, area2Button, area3Button, pump1Button, pump2Button;
    TextView mixerTitle, areaTitle, pumpTitle;

    public final int NAN = 0, MIXER1 = 1, MIXER2 = 2, MIXER3 = 3, PUMP1 = 10,
            PUMP2 = 11, AREA1 = 20, AREA2 = 21, AREA3 = 22, NO_TIMER = 30, NO_ACTION = 31;

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
        setTimer(MIXER1);
        setTimer(MIXER2);
        setTimer(MIXER3);
        setTimer(PUMP1);
        setTimer(PUMP2);

        /////INIT BUTTON STATE/////
        mixer1Button.setChecked(mainActivity.timerInfo.getMixerState() == MIXER1);
        mixer2Button.setChecked(mainActivity.timerInfo.getMixerState() == MIXER2);
        mixer3Button.setChecked(mainActivity.timerInfo.getMixerState() == MIXER3);
        pump1Button.setChecked(mainActivity.timerInfo.getPumpState() == PUMP1);
        pump2Button.setChecked(mainActivity.timerInfo.getPumpState() == PUMP2);
        area1Button.setChecked(mainActivity.timerInfo.getAreaType() == AREA1);
        area2Button.setChecked(mainActivity.timerInfo.getAreaType() == AREA2);
        area3Button.setChecked(mainActivity.timerInfo.getAreaType() == AREA3);

        //////CHANGE DURATION/////////
        mixer1Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDuration(MIXER1, "Mixer 1");
            }
        });
        mixer2Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDuration(MIXER2, "Mixer 2");
            }
        });
        mixer3Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDuration(MIXER3, "Mixer 3");
            }
        });
        pump1Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDuration(PUMP1, "Pump 1");
            }
        });
        pump2Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDuration(PUMP2, "Pump 2");
            }
        });

        ///////BUTTONS////////
        mixer1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivity.timerInfo.getMixer1Time() == 0) invalidAction(mixer1Button, NO_TIMER);
                else if (mainActivity.timerInfo.getMixerState() == NAN)  buttonPressed(MIXER1, true);
                else if (mainActivity.timerInfo.getMixerState() == MIXER1)  buttonPressed(MIXER1, false);
                else invalidAction(mixer1Button, NO_ACTION);
            }
        });
        mixer2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivity.timerInfo.getMixer2Time() == 0) invalidAction(mixer2Button, NO_TIMER);
                else if (mainActivity.timerInfo.getMixerState() == NAN)  buttonPressed(MIXER2, true);
                else if (mainActivity.timerInfo.getMixerState() == MIXER2) buttonPressed(MIXER2, false);
                else invalidAction(mixer2Button, NO_ACTION);
            }
        });
        mixer3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivity.timerInfo.getMixer3Time() == 0) invalidAction(mixer3Button, NO_TIMER);
                else if (mainActivity.timerInfo.getMixerState() == NAN)  buttonPressed(MIXER3, true);
                else if (mainActivity.timerInfo.getMixerState() == MIXER3) buttonPressed(MIXER3, false);
                else invalidAction(mixer3Button, NO_ACTION);
            }
        });
        area1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivity.timerInfo.getAreaType() == AREA1) {
                    mainActivity.timerInfo.setAreaType(NAN);
                    buttonPressed(AREA1, false);
                }
                else {
                    mainActivity.timerInfo.setAreaType(AREA1);
                    buttonPressed(AREA1, true);
                    buttonPressed(AREA2, false);
                    buttonPressed(AREA3, false);
                }
            }
        });
        area2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivity.timerInfo.getAreaType() == AREA2) {
                    mainActivity.timerInfo.setAreaType(NAN);
                    buttonPressed(AREA2, false);
                }
                else {
                    mainActivity.timerInfo.setAreaType(AREA2);
                    buttonPressed(AREA2, true);
                    buttonPressed(AREA1, false);
                    buttonPressed(AREA3, false);

                }
            }
        });
        area3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivity.timerInfo.getAreaType() == AREA3) {
                    mainActivity.timerInfo.setAreaType(NAN);
                    buttonPressed(AREA3, false);
                }
                else {
                    mainActivity.timerInfo.setAreaType(AREA3);
                    buttonPressed(AREA3, true);
                    buttonPressed(AREA2, false);
                    buttonPressed(AREA1, false);

                }
            }
        });
        pump1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivity.timerInfo.getPump1Time() == 0) invalidAction(pump1Button, NO_TIMER);
                else if (mainActivity.timerInfo.getPumpState() == NAN)  buttonPressed(PUMP1, true);
                else if (mainActivity.timerInfo.getPumpState() == PUMP1) buttonPressed(PUMP1, false);
                else invalidAction(pump1Button, NO_ACTION);
            }
        });
        pump2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivity.timerInfo.getPump2Time() == 0) invalidAction(pump2Button, NO_TIMER);
                else if (mainActivity.timerInfo.getPumpState() == NAN)  buttonPressed(PUMP2, true);
                else if (mainActivity.timerInfo.getPumpState() == PUMP2) buttonPressed(PUMP2, false);
                else invalidAction(pump2Button, NO_ACTION);
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
        if (type == NO_ACTION) popupText.setText("Action unavailable, a timer is already active.");
        else if (type == NO_TIMER) popupText.setText("Please set timer before activate.");

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
        if (type == MIXER1) mainActivity.timerInfo.setMixer1Time(Integer.parseInt(str));
        else if (type == MIXER2) mainActivity.timerInfo.setMixer2Time(Integer.parseInt(str));
        else if (type == MIXER3) mainActivity.timerInfo.setMixer3Time(Integer.parseInt(str));
        else if (type == PUMP1) mainActivity.timerInfo.setPump1Time(Integer.parseInt(str));
        else if (type == PUMP2) mainActivity.timerInfo.setPump2Time(Integer.parseInt(str));
        setTimer(type);
        mainActivity.contentHelper.writeContent(mainActivity.timerInfo, "timerInfo.json", mainActivity);
    }

    ////SET TIMER/////
    public void setTimer(int type) {
        if (type == MIXER1) mixer1Time.setText(mainActivity.formatTime(mainActivity.timerInfo.getMixer1Time()));
        else if (type == MIXER2) mixer2Time.setText(mainActivity.formatTime(mainActivity.timerInfo.getMixer2Time()));
        else if (type == MIXER3) mixer3Time.setText(mainActivity.formatTime(mainActivity.timerInfo.getMixer3Time()));
        else if (type == PUMP1) pump1Time.setText(mainActivity.formatTime(mainActivity.timerInfo.getPump1Time()));
        else if (type == PUMP2) pump2Time.setText(mainActivity.formatTime(mainActivity.timerInfo.getPump2Time()));
    }

    //////ON BUTTON PRESSED//////
    public void buttonPressed(int type, boolean status) {
        if (type == MIXER1) mixer1Button.setChecked(status);
        else if (type == MIXER2) mixer2Button.setChecked(status);
        else if (type == MIXER3) mixer3Button.setChecked(status);
        else if (type == PUMP1) pump1Button.setChecked(status);
        else if (type == PUMP2) pump1Button.setChecked(status);
        else if (type == AREA1) area1Button.setChecked(status);
        else if (type == AREA2) area2Button.setChecked(status);
        else if (type == AREA3) area3Button.setChecked(status);
        if (AREA1 <= type && type <= AREA3) return;
        if (status) mainActivity.startTimer(type, 0);
        else{
            setTimer(type);
            mainActivity.stopTimer(type);
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