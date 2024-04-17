package com.iot232.ssis.recycler;

import static android.app.PendingIntent.getActivity;

import android.app.AlertDialog;
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

public class SchedulerViewHolder extends RecyclerView.ViewHolder {
    TextView schedulerTitle, startTime, areaType, mixer1Time, mixer2Time, mixer3Time, pump1Time, pump2Time;
    CardView startCard, areaCard, mixer1Card, mixer2Card, mixer3Card, pump1Card, pump2Card;
    ToggleButton schedulerButton;
    ImageView schedulerOption;
    Context context;
    SchedulerAdapter schedulerAdapter;
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


        ////ACTIVE BUTTON/////
        schedulerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (schedulerViewInterface != null){
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        schedulerViewInterface.onItemClick(pos);
                    }
                }
            }
        });


        ////OPTION BUTTON/////
        schedulerOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterMenu(schedulerOption, schedulerViewInterface, 0);
            }
        });


        ////TIMERS/////
        mixer1Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDuration(R.id.mixer1Card, "Mixer 1", schedulerViewInterface);
            }
        });
        mixer2Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDuration(R.id.mixer2Card, "Mixer 2", schedulerViewInterface);
            }
        });
        mixer3Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDuration(R.id.mixer3Card, "Mixer 3", schedulerViewInterface);
            }
        });
        pump1Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDuration(R.id.pump1Card, "Pump 1", schedulerViewInterface);
            }
        });
        pump2Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDuration(R.id.pump2Card, "Pump 2", schedulerViewInterface);
            }
        });


        areaCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterMenu(areaCard, schedulerViewInterface, 1);
            }
        });

    }


    public void changeDuration(int id, String title, SchedulerViewInterface schedulerViewInterface) {
        ConstraintLayout constraintLayout = itemView.findViewById(R.id.popupDialog);
        View view = LayoutInflater.from(context).inflate(R.layout.popup_layout, constraintLayout);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();

        TextView notiText = view.findViewById(R.id.popup_title);
        notiText.setText(title);

        TextView popupText = view.findViewById(R.id.popup_desc);
        popupText.setText("Enter a duration in seconds");

        EditText insertText = view.findViewById(R.id.popup_insert);
        insertText.setHint("Duration");
        insertText.setInputType(InputType.TYPE_CLASS_NUMBER);

        //////CANCEL/////
        Button popupButton1 = view.findViewById(R.id.popup_button1);
        popupButton1.setText("Cancel");
        popupButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        //////////////////

        /////CHANGE////
        Button popupButton2 = view.findViewById(R.id.popup_button2);
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
        ///////////////

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }


    ////SAVE CHANGE////
    private void saveChange(int id, String str, SchedulerViewInterface schedulerViewInterface) {
        int type = -1;
        int duration = Integer.parseInt(str);
        if (id == R.id.mixer1Card) type = 0;
        else if (id == R.id.mixer2Card) type = 1;
        else if (id == R.id.mixer3Card) type = 2;
        else if (id == R.id.pump1Card) type = 3;
        else if (id == R.id.pump2Card) type = 4;
        else if (id == R.id.areaCard) type = 5;
        if (schedulerViewInterface != null){
            int pos = getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION){
                schedulerViewInterface.onInfoChanged(pos, type, duration);
            }
        }

//        setTimer();

    }

    ////SET TIMER/////
//    private void setTimer(int id) {
//        mixer1Time.setText(schedulerAdapter.formatTime(Integer.parseInt(str););
//        mixer2Time.setText(schedulerAdapter.formatTime(mainActivity.schedulerInfos.get(id).mixer2Time));
//        mixer3Time.setText(schedulerAdapter.formatTime(mainActivity.schedulerInfos.get(id).mixer3Time));
//        pump1Time.setText(schedulerAdapter.formatTime(mainActivity.schedulerInfos.get(id).pump1Time));
//        pump2Time.setText(schedulerAdapter.formatTime(mainActivity.schedulerInfos.get(id).pump2Time));
//    }


    /////SHOW FILTER OPTIONS WHEN PRESSING THE 3 DOTS//////
    public void showFilterMenu(View anchorView, SchedulerViewInterface schedulerViewInterface, int type) {
        int id = (type == 0)? R.id.scheduler_filter : R.id.area_filter;
        int layout = (type == 0)? R.layout.scheduler_filter_layout : R.layout.area_filter_layout;
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
        if (type == 0) {
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
        else if (type == 1){
            TextView filterA = view.findViewById(R.id.filter_a);
            filterA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (schedulerViewInterface != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            schedulerViewInterface.onInfoChanged(pos, 5, 1);
                        }
                    }
                    alertDialog.dismiss();
                }
            });
            TextView filterB = view.findViewById(R.id.filter_b);
            filterB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (schedulerViewInterface != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            schedulerViewInterface.onInfoChanged(pos, 5, 2);
                        }
                    }
                    alertDialog.dismiss();
                }
            });
            TextView filterC = view.findViewById(R.id.filter_c);
            filterC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (schedulerViewInterface != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            schedulerViewInterface.onInfoChanged(pos, 5, 3);
                        }
                    }
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
        params.gravity = Gravity.TOP | Gravity.END; // Adjust as per your requirement
        params.x = anchorX + anchorWidth; // Adjust the x position
        params.y = anchorY + anchorHeight - 80; // Adjust the y position

        alertDialog.show();
    }
}