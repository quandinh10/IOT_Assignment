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
    CardView eraseData;
    ContentHelper contentHelper;
    AdaInfo adaInfo;
    TimerInfo timerInfo;
    UserInfo userInfo;
    int erased;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        contentHelper = new ContentHelper(this);

        timerInfo = new TimerInfo();
        adaInfo = new AdaInfo();
        userInfo = new UserInfo();

        erased = 0;

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
                if (erased == 0) onBackPressed();
                else {
                    Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                    intent.putExtra("editAdaInfo", new Gson().toJson(adaInfo));
                    intent.putExtra("editUserInfo", new Gson().toJson(userInfo));
                    intent.putExtra("editTimerInfo", new Gson().toJson(timerInfo));
                    intent.putExtra("editSchedulerInfo", new Gson().toJson(timerInfo));
                    contentHelper.deleteJSONFile("adaInfo.json", SettingsActivity.this);
                    contentHelper.deleteJSONFile("userInfo.json", SettingsActivity.this);
                    contentHelper.deleteJSONFile("timerInfo.json", SettingsActivity.this);
                    contentHelper.deleteJSONFile("schedulerInfo.json", SettingsActivity.this);
                    startActivity(intent);
                }
            }
        });

        eraseData = findViewById(R.id.eraseData);
        eraseData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmAction();
            }
        });

    }


    public void confirmAction() {
        ConstraintLayout constraintLayout = findViewById(R.id.popupDialog);
        View view = LayoutInflater.from(this).inflate(R.layout.popup_layout, constraintLayout);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();

        TextView notiText = view.findViewById(R.id.popup_title);
        notiText.setText("Confirm action");

        TextView popupText = view.findViewById(R.id.popup_desc);
        popupText.setText("This will erase all data. Existing actions will be deleted.");

        EditText insertText = view.findViewById(R.id.popup_insert);
        insertText.setVisibility(View.GONE);

        //////BUTTON1/////
        Button popupButton1 = view.findViewById(R.id.popup_button1);
        popupButton1.setText("Cancel");
        popupButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });
        ///////////////
        //////////////////

        /////BUTTON2////
        Button popupButton2 = view.findViewById(R.id.popup_button2);
        popupButton2.setText("Erase");
        popupButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                contentHelper.writeContent(adaInfo, "adaInfo.json",SettingsActivity.this);
                contentHelper.writeContent(timerInfo, "timerInfo.json",SettingsActivity.this);
                contentHelper.printContentJson("adaInfo.json", SettingsActivity.this);
                erased = 1;
                alertDialog.dismiss();

            }
        });
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