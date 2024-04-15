package com.iot232.ssis;

import static com.iot232.ssis.databinding.ActivityMainBinding.inflate;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.iot232.ssis.data.AdaInfo;
import com.iot232.ssis.data.TimerInfo;
import com.iot232.ssis.data.UserInfo;
import com.iot232.ssis.databinding.ActivityMainBinding;
import com.iot232.ssis.helper.ContentHelper;
import com.iot232.ssis.helper.MqttHelper;
import com.iot232.ssis.ui.DashboardFragment;
import com.iot232.ssis.ui.HomeFragment;
import com.iot232.ssis.ui.automations.AutomationsFragment;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    AlertDialog.Builder alertDialog;

    AppBarConfiguration sideAppBarConfiguration, bottomAppBarConfiguration;
    ActivityMainBinding binding;
    NavController navController;
    BottomNavigationView bottomNavView;
    Toolbar toolbar;
    NavigationView navView;
    FragmentManager fragmentManager;
    DrawerLayout sideDrawer;

    MqttHelper client;
    public ContentHelper contentHelper;
    public AdaInfo adaInfo;
    public TimerInfo timerInfo;
    UserInfo userInfo;

    TextView currentDate, currentDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        alertDialog = new AlertDialog.Builder(this);

        /////TOOLBAR//////
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        setSupportActionBar(toolbar);


        ////SIDE DRAWER//////
        sideDrawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, sideDrawer, toolbar, R.string.open_nav, R.string.close_nav);
        sideDrawer.addDrawerListener(toggle);
        toggle.syncState();


        ////INITALIZE INFO//////
        adaInfo = new AdaInfo();
        userInfo = new UserInfo();
        timerInfo = new TimerInfo();
        contentHelper = new ContentHelper(this);

        //////Load content//////
        if (contentHelper.loadContent(AdaInfo.class, "adaInfo.json",this) != null) adaInfo = contentHelper.loadContent(AdaInfo.class, "adaInfo.json",this);
        if (contentHelper.loadContent(UserInfo.class, "userInfo.json",this) != null) userInfo = contentHelper.loadContent(UserInfo.class, "userInfo.json",this);
        if (contentHelper.loadContent(TimerInfo.class, "timerInfo.json",this) != null) timerInfo = contentHelper.loadContent(TimerInfo.class, "timerInfo.json",this);

        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment_content_main, new HomeFragment(), "HomeFragment").commit();

        ////CHECK FOR ACTIVITY TRANSITION//////
        Intent intent = getIntent();
        if (intent.hasExtra("editAdaInfo")) {
            adaInfo = new Gson().fromJson(getIntent().getExtras().getString("editAdaInfo"), AdaInfo.class);
            contentHelper.writeContent(adaInfo, "adaInfo.json",this);
        }
        else if (intent.hasExtra("editUserInfo")) {
            userInfo = new Gson().fromJson(getIntent().getExtras().getString("editUserInfo"), UserInfo.class);
            contentHelper.writeContent(userInfo, "userInfo.json",this);
        }
        else if (intent.hasExtra("editTimerInfo")) {
            timerInfo = new Gson().fromJson(getIntent().getExtras().getString("editTimerInfo"), TimerInfo.class);
            contentHelper.writeContent(timerInfo, "timerInfo.json",this);
        }
        else if (intent.hasExtra("loggedIn")){
            userInfo.isLogged = "1";
            contentHelper.writeContent(userInfo, "userInfo.json",this);
        }

        if (Objects.equals(userInfo.isLogged, " ")) {
            intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        /////SET DATE/////
        currentDay = findViewById(R.id.current_day);
        currentDate = findViewById(R.id.current_date);
        currentDay.setText(new SimpleDateFormat("EEE", Locale.getDefault()).format(new Date()));
        currentDate.setText(new SimpleDateFormat("d MMM", Locale.getDefault()).format(new Date()));

        //////MQTT///////
        startMQTT();


        //////Floating action button////////
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Action not available", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        /////Side panel/////
        navView = binding.navView;
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    bottomNavView.setSelectedItemId(R.id.nav_home);
                    openFragment(new HomeFragment());
                } else if (itemId == R.id.nav_settings) {
                    Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(intent);
                } else if (itemId == R.id.nav_editprofile) {
                    Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
                    intent.putExtra("changeUserInfo", new Gson().toJson(userInfo));
                    startActivity(intent);
                } else if (itemId == R.id.nav_editconnection) {
                    Intent intent = new Intent(MainActivity.this, ConnectInfoActivity.class);
                    intent.putExtra("changeAdaInfo", new Gson().toJson(adaInfo));
                    startActivity(intent);
                } else if (itemId == R.id.nav_about) {
////            openFragment(new AboutFragment());
                } else if (itemId == R.id.nav_share) {
                    shareApp(MainActivity.this);
                } else if (itemId == R.id.nav_logout) {
                    userInfo = new UserInfo();
                    contentHelper.writeContent(userInfo, "userInfo.json",MainActivity.this);
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

                sideDrawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });


        /////Bottom navigation panel//////
        bottomNavView = binding.appBarMain.bottomNavView;
        bottomNavView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    Menu menu = navView.getMenu();
                    menu.findItem(R.id.nav_home).setChecked(true);
                    openFragment(new HomeFragment());
                    return true;
                } else if (itemId == R.id.nav_dashboard) {
                    clearNavigationSelection(navView);
                    openFragment(new DashboardFragment());
                    return true;
                } else if (itemId == R.id.nav_automations) {
                    clearNavigationSelection(navView);
                    openFragment(new AutomationsFragment());
                    return true;
                }
                return false;
            }
        });

        /////FRAGMENT MANAGER//////
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            openFragment(new HomeFragment());
            navView.setCheckedItem(R.id.nav_home);
        }
    }

    /////DEFAULT FUNCTIONS, DO NOT TOUCH//////
    @Override
    protected void onDestroy() {
        contentHelper.writeContent(adaInfo, "adaInfo.json", this);
        contentHelper.writeContent(userInfo, "userInfo.json", this);
        contentHelper.writeContent(timerInfo, "timerInfo.json",this);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.action_notifications) {
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, sideAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.nav_host_fragment_content_main, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (sideDrawer.isDrawerOpen(GravityCompat.START)) {
            sideDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    /////SHARE APP//////
    private void shareApp(@NonNull Context context) {
        // code here
        final String appPackageName = context.getPackageName();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Download Now: https://play.google.com/store/apps/details?id=" + appPackageName);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

    /////CLEAR SIDE BAR NAVIGATION/////
    private void clearNavigationSelection(@NonNull NavigationView navigationView) {
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_home).setChecked(false);
        menu.findItem(R.id.nav_about).setChecked(false);
        menu.findItem(R.id.nav_settings).setChecked(false);
        menu.findItem(R.id.nav_editprofile).setChecked(false);
        menu.findItem(R.id.nav_editconnection).setChecked(false);
    }

    /////MQTT/////
    private void startMQTT() {
        if (Objects.equals(adaInfo.clientID, " ") ||
                Objects.equals(adaInfo.username, " ") ||
                Objects.equals(adaInfo.password, " ")){
            failedMQTTPopup();
            return;
        }
        client = new MqttHelper(this);
        progressDialog.setMessage("Connecting to server");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (client.isConnected()) {
            progressDialog.dismiss();

            client.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean reconnect, String serverURI) {

                }

                @Override
                public void connectionLost(Throwable cause) {
                    failedMQTTPopup();
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {

                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });

        } else {
            progressDialog.dismiss();
            failedMQTTPopup();
        }
    }


    ///////POPUP FOR MQTT///////
    public void failedMQTTPopup() {
        ConstraintLayout constraintLayout = findViewById(R.id.popupDialog);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.popup_layout, constraintLayout);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();

        TextView popupText = view.findViewById(R.id.popup_desc);
        popupText.setText("Failed to connect to server. Try again or change connection information");

        EditText insertText = view.findViewById(R.id.popup_insert);
        insertText.setVisibility(View.GONE);

        //////TRY AGAIN/////
        Button popupButton2 = view.findViewById(R.id.popup_button2);
        popupButton2.setText("Change");
        popupButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ConnectInfoActivity.class);
                intent.putExtra("changeAdaInfo", new Gson().toJson(adaInfo));
                startActivity(intent);
            }
        });
        //////////////////

        ////CONNECT/////
        Button popupButton1 = view.findViewById(R.id.popup_button1);
        popupButton1.setText("Try again");
        popupButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                startMQTT();
            }
        });
        ///////////////

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }



}