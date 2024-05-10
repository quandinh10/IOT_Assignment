package com.iot232.ssis.recycler;

import static android.app.PendingIntent.getActivity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.iot232.ssis.MainActivity;
import com.iot232.ssis.R;

import java.util.Calendar;
import java.util.TimeZone;

public class SchedulerViewHolder extends RecyclerView.ViewHolder {
    TextView schedulerTitle, startTime, areaType, mixer1Time, mixer2Time, mixer3Time, pump1Time, pump2Time, cycleCount;
    CardView startCard, areaCard, mixer1Card, mixer2Card, mixer3Card, pump1Card, pump2Card, cycleCard;
    ToggleButton schedulerButton;
    ImageView schedulerOption;
    Context context;
    SchedulerAdapter schedulerAdapter;

    public final int NAN = 0, MIXER1 = 1, MIXER2 = 2, MIXER3 = 3, PUMP1 = 10,
            PUMP2 = 11, AREA = 20, NO_TIMER = 30, NO_ACTION = 31, CYCLE = 50, START = 51, TITLE = 52;
    public final int SCHEDULER_FILTER = 40, AREA_FILTER = 41;
    MainActivity mainActivity;
    public SchedulerViewHolder(@NonNull View itemView, Context context, SchedulerAdapter schedulerAdapter, SchedulerViewInterface schedulerViewInterface) {
        super(itemView);
        this.context = context;
        this.schedulerAdapter = schedulerAdapter;

        if (context instanceof MainActivity) mainActivity = (MainActivity) context;

        schedulerButton = itemView.findViewById(R.id.schedulerButton);
        schedulerOption = itemView.findViewById(R.id.schedulerOption);
        schedulerTitle = itemView.findViewById(R.id.schedulerTitle);
        //////
        areaType = itemView.findViewById(R.id.areaType);
        cycleCount = itemView.findViewById(R.id.cycleCount);
        startTime = itemView.findViewById(R.id.startTime);
        mixer1Time = itemView.findViewById(R.id.mixer1Time);
        mixer2Time = itemView.findViewById(R.id.mixer2Time);
        mixer3Time = itemView.findViewById(R.id.mixer3Time);
        pump1Time = itemView.findViewById(R.id.pump1Time);
        pump2Time = itemView.findViewById(R.id.pump2Time);
        //////
        startCard = itemView.findViewById(R.id.startCard);
        areaCard = itemView.findViewById(R.id.areaCard);
        mixer1Card = itemView.findViewById(R.id.mixer1Card);
        mixer2Card = itemView.findViewById(R.id.mixer2Card);
        mixer3Card = itemView.findViewById(R.id.mixer3Card);
        pump1Card = itemView.findViewById(R.id.pump1Card);
        pump2Card = itemView.findViewById(R.id.pump2Card);
        cycleCard = itemView.findViewById(R.id.cycleCard);


        ////ACTIVE BUTTON/////
        schedulerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (schedulerViewInterface != null){
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if (mainActivity.schedulerInfo.get(pos).getSchedulerState() == 1) {
                            schedulerButton.setChecked(true);
                            schedulerViewInterface.onItemClick(pos);
                        }
                        else {
                            boolean isTimerSet = schedulerViewInterface.checkSchedules(pos);
                            boolean isScheduleActive = schedulerViewInterface.checkTimer(pos);
                            if (!isTimerSet || !isScheduleActive){
                                showPopup(!isScheduleActive ? NO_TIMER : NO_ACTION, "Invalid action", schedulerViewInterface);
                                schedulerButton.setChecked(false);
                            }
                            else {
                                schedulerButton.setChecked(false);
                                schedulerViewInterface.onItemClick(pos);
                            }
                        }
                    }
                }
            }
        });


        ////OPTION BUTTON/////
        schedulerOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterMenu(schedulerOption, schedulerViewInterface, SCHEDULER_FILTER);
            }
        });

        schedulerTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(TITLE, "Title", schedulerViewInterface);
            }
        });

        startCard.setOnClickListener((v -> showTimePickerDialog(schedulerViewInterface)));


        ////TIMERS/////
        mixer1Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(MIXER1, "Mixer 1", schedulerViewInterface);
            }
        });
        mixer2Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(MIXER2, "Mixer 2", schedulerViewInterface);
            }
        });
        mixer3Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(MIXER3, "Mixer 3", schedulerViewInterface);
            }
        });
        pump1Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(PUMP1, "Pump 1", schedulerViewInterface);
            }
        });
        pump2Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(PUMP2, "Pump 2", schedulerViewInterface);
            }
        });
        areaCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterMenu(areaCard, schedulerViewInterface, AREA_FILTER);
            }
        });
        cycleCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(CYCLE, "Cycle", schedulerViewInterface);
            }
        });

    }


    public void showPopup(int id, String title, SchedulerViewInterface schedulerViewInterface) {
        ConstraintLayout constraintLayout = itemView.findViewById(R.id.popupDialog);
        View view = LayoutInflater.from(context).inflate(R.layout.popup_layout, constraintLayout);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();

        TextView notiText = view.findViewById(R.id.popup_title);
        notiText.setText(title);

        TextView popupText = view.findViewById(R.id.popup_desc);
        EditText insertText = view.findViewById(R.id.popup_insert);
        Button popupButton1 = view.findViewById(R.id.popup_button1);
        Button popupButton2 = view.findViewById(R.id.popup_button2);

        if (id == CYCLE){
            popupText.setText("Enter number of cycles");
            insertText.setHint("Cycle count");
            insertText.setInputType(InputType.TYPE_CLASS_NUMBER);
            popupButton1.setText("Cancel");
            popupButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            popupButton2.setText("Change");
            popupButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(insertText.getText().toString()) && TextUtils.isDigitsOnly(insertText.getText().toString())) {
                        saveChange(id, insertText.getText().toString(), schedulerViewInterface);
                        alertDialog.dismiss();
                    } else insertText.setError("Please enter an integer");

                }
            });
        }
        else if (id == TITLE){
            popupText.setText("Enter scheduler title");
            insertText.setHint("Title");
            popupButton1.setText("Cancel");
            popupButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            popupButton2.setText("Change");
            popupButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(insertText.getText().toString())) {
                        saveChange(id, insertText.getText().toString(), schedulerViewInterface);
                        alertDialog.dismiss();
                    } else insertText.setError("Please enter a title");

                }
            });
        }
        else if (id == NO_TIMER){
            popupText.setText("Please set parameters before activating schedule");
            insertText.setVisibility(View.GONE);
            popupButton1.setVisibility(View.GONE);
            popupButton2.setText("Cancel");
            popupButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
        }
        else if (id == NO_ACTION){
            popupText.setText("Another schedule is already active.");
            insertText.setVisibility(View.GONE);
            popupButton1.setVisibility(View.GONE);
            popupButton2.setText("Cancel");
            popupButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
        }
        else {
            popupText.setText("Enter a duration in seconds");
            insertText.setHint("Duration");
            insertText.setInputType(InputType.TYPE_CLASS_NUMBER);
            popupButton1.setText("Cancel");
            popupButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            popupButton2.setText("Change");
            popupButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(insertText.getText().toString()) && TextUtils.isDigitsOnly(insertText.getText().toString())) {
                        saveChange(id, insertText.getText().toString(), schedulerViewInterface);
                        alertDialog.dismiss();
                    } else insertText.setError("Please enter an integer");

                }
            });
        }

        ///////////////

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }


    ////SAVE CHANGE////
    private void saveChange(int id, String str, SchedulerViewInterface schedulerViewInterface) {
        int duration = (id == TITLE)? 0 : Integer.parseInt(str);
        long longDuration = (id == START)? Long.parseLong(str) : 0;
        if (schedulerViewInterface != null){
            int pos = getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION){
                if (id == START) schedulerViewInterface.onInfoChanged(pos, id, 0, longDuration, "");
                else if (id == TITLE) schedulerViewInterface.onInfoChanged(pos, id, 0, 0, str);
                else schedulerViewInterface.onInfoChanged(pos, id , duration, 0 ,"");
            }
        }
    }


    /////SHOW FILTER OPTIONS WHEN PRESSING THE 3 DOTS//////
    public void showFilterMenu(View anchorView, SchedulerViewInterface schedulerViewInterface, int type) {
        int id = (type == SCHEDULER_FILTER)? R.id.scheduler_filter : R.id.area_filter;
        int layout = (type == SCHEDULER_FILTER)? R.layout.scheduler_filter_layout : R.layout.area_filter_layout;
        ConstraintLayout constraintLayout = itemView.findViewById(id);
        View view = LayoutInflater.from(mainActivity).inflate(layout, constraintLayout);

        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();

        RelativeLayout popup = view.findViewById(id);
        popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        //////ALL/////
        if (type == SCHEDULER_FILTER) {
            TextView filterClose = view.findViewById(R.id.filter_close);
            filterClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (schedulerViewInterface != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            schedulerViewInterface.onItemDeleted(pos);
                        }
                    }
                    alertDialog.dismiss();
                }
            });
        }
        else if (type == AREA_FILTER){
            TextView filterA = view.findViewById(R.id.filter_a);
            filterA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveChange(AREA, String.valueOf(1), schedulerViewInterface);
                    alertDialog.dismiss();
                }
            });
            TextView filterB = view.findViewById(R.id.filter_b);
            filterB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveChange(AREA, String.valueOf(2), schedulerViewInterface);
                    alertDialog.dismiss();
                }
            });
            TextView filterC = view.findViewById(R.id.filter_c);
            filterC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveChange(AREA, String.valueOf(3), schedulerViewInterface);
                    alertDialog.dismiss();
                }
            });
        }

        // Prevent the background from dimming
        Window window = alertDialog.getWindow();
        if (window != null) window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        if (alertDialog.getWindow() != null) alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        // ANCHOR THE POPUP UNDER THE 3 DOTS/////
        int[] anchorLocation = new int[2];
        anchorView.getLocationOnScreen(anchorLocation);
        int anchorX = anchorLocation[0];
        int anchorY = anchorLocation[1];
        int anchorWidth = anchorView.getWidth();
        int anchorHeight = anchorView.getHeight();

        // Set the gravity to be below the anchor view
        WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
        params.gravity = Gravity.TOP | Gravity.END;
        params.x = anchorX + anchorWidth; // Adjust the x position
        params.y = anchorY + anchorHeight - 80; // Adjust the y position

        alertDialog.show();
    }


    private void showTimePickerDialog(SchedulerViewInterface schedulerViewInterface) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                context,
                (view, selectedHour, selectedMinute) -> {
                    String selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                    long epochTime = convertToEpoch(selectedHour, selectedMinute);
                    saveChange(START, String.valueOf(epochTime), schedulerViewInterface);
                    startTime.setText(selectedTime);
                },
                hour,
                minute,
                true
        );

        timePickerDialog.show();
    }

    private long convertToEpoch(int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        int offset = TimeZone.getDefault().getOffset(calendar.getTimeInMillis());
        return (calendar.getTimeInMillis() + offset) / 1000;
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