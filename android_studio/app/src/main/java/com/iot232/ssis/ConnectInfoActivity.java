package com.iot232.ssis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.appcompat.widget.Toolbar;

import com.iot232.ssis.data.AdaInfo;

import com.google.gson.Gson;

import java.util.Objects;

public class ConnectInfoActivity extends AppCompatActivity {

    EditText connectURI, connectID, connectUsername, connectPassword;
    MainActivity mainActivity;
    private AdaInfo adaInfo;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_options);

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
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        Intent intent = getIntent();
        if (intent.hasExtra("changeAdaInfo")) adaInfo = new Gson().fromJson(getIntent().getExtras().getString("changeAdaInfo"), AdaInfo.class);


        connectID = findViewById(R.id.connect_id);
        connectUsername = findViewById(R.id.connect_username);
        connectPassword = findViewById(R.id.connect_password);

        if (!Objects.equals(adaInfo.clientID, " ") &&
                !Objects.equals(adaInfo.password, " ") &&
                !Objects.equals(adaInfo.username, " ")){
                connectID.setText(adaInfo.clientID);
                connectUsername.setText(adaInfo.username);
                connectPassword.setText(adaInfo.password);
        }


        Button connectButton = findViewById(R.id.connect_button);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBlank(connectID)) connectID.setError("Please enter clientID");
                else if (isBlank(connectUsername)) connectUsername.setError("Please enter username");
                else if (isBlank(connectPassword)) connectPassword.setError("Please enter password");
                else{
                    Intent intent = new Intent(ConnectInfoActivity.this, MainActivity.class);
                    intent.putExtra("editAdaInfo", new Gson().toJson(new AdaInfo(connectID.getText().toString(), connectUsername.getText().toString(),
                            connectPassword.getText().toString())));
                    startActivity(intent);
                }

            }
        });

        TextView cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConnectInfoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ToggleButton showPasswordButton = findViewById(R.id.show_password_button);

        showPasswordButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Toggle password visibility based on the state of the ToggleButton
                if (isChecked) {
                    // Show password
                    connectPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    // Hide password
                    connectPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                // Move cursor to the end of the text
                connectPassword.setSelection(connectPassword.getText().length());
            }
        });
    }


    public String getString(EditText editText) {return editText.getText().toString();}

    public Boolean isBlank(EditText editText){return getString(editText).equals("");}

}