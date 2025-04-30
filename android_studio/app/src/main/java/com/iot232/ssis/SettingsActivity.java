package com.iot232.ssis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.iot232.ssis.data.AdaInfo;
import com.iot232.ssis.data.TimerInfo;
import com.iot232.ssis.data.UserInfo;
import com.iot232.ssis.helper.ContentHelper;

public class SettingsActivity extends AppCompatActivity {
    Toolbar toolbar;
    CardView eraseData, resetTimer, resetServer;
    ContentHelper contentHelper;
    AdaInfo adaInfo;
    TimerInfo timerInfo;
    UserInfo userInfo;
    public int ERASE_DATA = 3, RESET_TIMER = 2, RESET_SERVER = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        contentHelper = new ContentHelper(this);

        timerInfo = new TimerInfo();
        adaInfo = new AdaInfo();

        /////TOOLBAR//////
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        eraseData = findViewById(R.id.eraseData);
        eraseData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmAction(ERASE_DATA);
            }
        });

        resetTimer = findViewById(R.id.resetTimer);
        resetTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmAction(RESET_TIMER);
            }
        });

        resetServer = findViewById(R.id.resetServer);
        resetServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmAction(RESET_SERVER);
            }
        });
    }


    public void confirmAction(int type) {
        ConstraintLayout constraintLayout = findViewById(R.id.popupDialog);
        View view = LayoutInflater.from(this).inflate(R.layout.popup_layout, constraintLayout);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();

        TextView notiText = view.findViewById(R.id.popup_title);
        notiText.setText("Confirm action");

        EditText insertText = view.findViewById(R.id.popup_insert);
        insertText.setVisibility(View.GONE);

        TextView popupText = view.findViewById(R.id.popup_desc);

        //////BUTTON1/////
        Button popupButton1 = view.findViewById(R.id.popup_button1);
        popupButton1.setText("Cancel");
        popupButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });

        Button popupButton2 = view.findViewById(R.id.popup_button2);
        popupButton2.setText("Reset");
        /////BUTTON2////

        if (type == ERASE_DATA) {
            popupText.setText("This will erase all data and is not reversible.");
            popupButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    contentHelper.deleteJSONFile("adaInfo.json", SettingsActivity.this);
                    contentHelper.deleteJSONFile("timerInfo.json", SettingsActivity.this);
                    contentHelper.deleteJSONFile("schedulerInfo.json", SettingsActivity.this);
                    alertDialog.dismiss();
                }
            });
        }
        else if (type == RESET_TIMER){
            popupText.setText("This will reset all timers and is not reversible.");
            popupButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    contentHelper.deleteJSONFile("timerInfo.json", SettingsActivity.this);
                    contentHelper.deleteJSONFile("schedulerInfo.json", SettingsActivity.this);
                    alertDialog.dismiss();
                }
            });
        }
        else if (type == RESET_SERVER){
            popupText.setText("This will reset server option is not reversible.");
            popupButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    contentHelper.deleteJSONFile("adaInfo.json", SettingsActivity.this);
                    alertDialog.dismiss();
                }
            });
        }

        ///////////////

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

}