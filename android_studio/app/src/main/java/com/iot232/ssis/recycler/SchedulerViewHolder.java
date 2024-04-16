package com.iot232.ssis.recycler;

import static android.app.PendingIntent.getActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    public SchedulerViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
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

            }
        });


        ////OPTION BUTTON/////
        schedulerOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        ////TIMERS/////
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
        areaCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


    public void changeDuration(int id, String title) {
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
//                    saveChange(id, insertText.getText().toString());
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
}