package com.iot232.ssis;

import static com.iot232.ssis.databinding.ActivityMainBinding.inflate;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.data.Entry;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
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
import com.google.gson.reflect.TypeToken;
import com.iot232.ssis.data.AdaInfo;
import com.iot232.ssis.data.TimerInfo;
import com.iot232.ssis.data.UserInfo;
import com.iot232.ssis.databinding.ActivityMainBinding;
import com.iot232.ssis.helper.AdaHelper;
import com.iot232.ssis.helper.ContentHelper;
import com.iot232.ssis.helper.DataEntry;
import com.iot232.ssis.helper.MqttHelper;
import com.iot232.ssis.fragments.DashboardFragment;
import com.iot232.ssis.fragments.HomeFragment;
import com.iot232.ssis.fragments.AutomationsFragment;
import com.iot232.ssis.recycler.SchedulerAdapter;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class MainActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    AlertDialog.Builder alertDialog;

    AppBarConfiguration sideAppBarConfiguration;
    public ActivityMainBinding binding;
    NavController navController;
    BottomNavigationView bottomNavView;
    Toolbar toolbar;
    NavigationView navView;
    FragmentManager fragmentManager;
    DrawerLayout sideDrawer;
    MqttHelper client;
    AdaHelper tempGetter, humidGetter;
    public ContentHelper contentHelper;
    public AdaInfo adaInfo;
    public TimerInfo timerInfo;
    public List<TimerInfo> schedulerInfo;
    public UserInfo userInfo;
    public final int NAN = 0, MIXER1 = 1, MIXER2 = 2, MIXER3 = 3, PUMP1 = 10,
            PUMP2 = 11, AREA = 20, AREA1 = 20, AREA2 = 21, AREA3 = 22, NO_TIMER = 30, NO_ACTION = 31, CYCLE = 50, START = 51, TITLE = 52;

    public final int SCHEDULER_FILTER = 40, AREA_FILTER = 41;

    public int[] timerTypes = {MIXER1, MIXER2, MIXER3, PUMP1, PUMP2};

    Handler handler = new Handler();
    Runnable timerRunnable;

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
        schedulerInfo = new ArrayList<TimerInfo>();
        contentHelper = new ContentHelper(this);

        TypeToken<AdaInfo> adaInfoTypeToken = new TypeToken<AdaInfo>() {};
        TypeToken<UserInfo> userInfoTypeToken = new TypeToken<UserInfo>() {};
        TypeToken<TimerInfo> timerInfoTypeToken = new TypeToken<TimerInfo>() {};
        TypeToken<ArrayList<TimerInfo>> listTypeToken = new TypeToken<ArrayList<TimerInfo>>() {};

        //////Load content//////
        if (contentHelper.loadContent(adaInfoTypeToken, "adaInfo.json",this) != null) adaInfo = contentHelper.loadContent(adaInfoTypeToken, "adaInfo.json",this);
        else contentHelper.writeContent(adaInfo, "adaInfo.json", this);
        if (contentHelper.loadContent(userInfoTypeToken, "userInfo.json",this) != null) userInfo = contentHelper.loadContent(userInfoTypeToken, "userInfo.json",this);
        else contentHelper.writeContent(userInfo, "userInfo.json", this);
        if (contentHelper.loadContent(timerInfoTypeToken, "timerInfo.json",this) != null) timerInfo = contentHelper.loadContent(timerInfoTypeToken, "timerInfo.json",this);
        else contentHelper.writeContent(timerInfo, "timerInfo.json", this);
        if (contentHelper.loadContent(listTypeToken, "schedulerInfo.json",this) != null) schedulerInfo = contentHelper.loadContent(listTypeToken, "schedulerInfo.json", this);
        else contentHelper.writeContent(schedulerInfo, "schedulerInfo.json", this);

        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new HomeFragment(), "HomeFragment").commit();

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

        contentHelper.printContentJson("timerInfo.json", this);

        //////MQTT///////
        startMQTT();

        //////Floating action button////////
        checkCurrentFragment();
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment currentFragment = getCurrentFragment();
                if (currentFragment instanceof AutomationsFragment) {
                    ((AutomationsFragment) currentFragment).addSchedule();
                }
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

        // Register the BroadcastReceiver to listen for connectivity changes
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityReceiver, filter);

        ///////////END OF ONCREATE///////////
    }

    /////DEFAULT FUNCTIONS, DO NOT TOUCH//////
    @Override
    protected void onDestroy() {
        contentHelper.writeContent(adaInfo, "adaInfo.json", this);
        contentHelper.writeContent(userInfo, "userInfo.json", this);
        contentHelper.writeContent(timerInfo, "timerInfo.json",this);
        contentHelper.writeContent(schedulerInfo, "schedulerInfo.json",this);
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
        NavController navController = Navigation.findNavController(this, R.id.fragment_container);
        return NavigationUI.navigateUp(navController, sideAppBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        long currentTime = getCurrentEpochTime();
        int mixerState = timerInfo.getMixerState();
        int pumpState = timerInfo.getPumpState();
        int mixerDelta = (int) (currentTime - timerInfo.getMixerStart());
        int pumpDelta = (int) (currentTime - timerInfo.getPumpStart());
        if (timerInfo.getMixerState() >= MIXER1 && timerInfo.getMixerState() <= MIXER3)
            startTimer(mixerState, mixerDelta, timerInfo, 0, null, null);
        if (timerInfo.getPumpState() >= PUMP1 && timerInfo.getPumpState() <= PUMP2)
            startTimer(pumpState, pumpDelta, timerInfo, 0, null, null);
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (sideDrawer.isDrawerOpen(GravityCompat.START)) {
            sideDrawer.closeDrawer(GravityCompat.START);
        }
        else super.onBackPressed();
    }
    /////////////////////


    /////SHARE APP//////
    private void shareApp(@NonNull Context context) {
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
                    startMQTT();
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

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                alertDialog.dismiss();
                startMQTT();
                return true;
            }
        });
        alertDialog.show();
    }

    private BroadcastReceiver connectivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork == null || !activeNetwork.isConnected()) {
                // WiFi is disconnected
                startMQTT();
            }
        }
    };


    ////CHECK FRAGMENT FOR FAB////
    private Fragment getCurrentFragment() {
        fragmentManager = getSupportFragmentManager();
        return fragmentManager.findFragmentById(R.id.fragment_container);
    }

    public void checkCurrentFragment() {
        Fragment currentFragment = getCurrentFragment();
        if (currentFragment instanceof HomeFragment || currentFragment instanceof DashboardFragment) {
            binding.appBarMain.fab.hide();
        }
        else binding.appBarMain.fab.show();
    }
    ///////////////////////////

    /////GET ENTRIES FOR GRAPH/////
    public void getEntries(String feedKey) {
        ArrayList<Entry> entries = new ArrayList<>();
        progressDialog.setMessage("Fetching data...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        AdaHelper getter = new AdaHelper(feedKey, new AdaHelper.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(List<DataEntry> dataEntries) {
                try {
                    int arrayLength = dataEntries.size();
                    for (int i = arrayLength - 1; i >= 0; i--) {
                        DataEntry dataEntry = dataEntries.get(i);
                        String valueString = dataEntry.getValue();
                        String createAt = dataEntry.getCreatedAt();
                        float value = Float.parseFloat(valueString) / 100;
                        Log.d("Data", "Value: " + value + " At: " + String.valueOf(formatTime(createAt)));
                        entries.add(new Entry(formatTime(createAt), value));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
                Fragment currentFragment = getCurrentFragment();
                if (currentFragment instanceof HomeFragment) {
                    ((HomeFragment) currentFragment).setupGraph(feedKey, entries);
                }
            }

            @Override
            public void onTaskFailed() {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        }, adaInfo.username, adaInfo.password);

        // Execute the data fetch
        getter.fetchData();
    }

    public int getDuration(int type, int delta, TimerInfo timerInfo){
        Map<Integer, Integer> map = new HashMap<>();
        map.put(MIXER1, timerInfo.getMixer1Time());
        map.put(MIXER2, timerInfo.getMixer2Time());
        map.put(MIXER3, timerInfo.getMixer3Time());
        map.put(PUMP1, timerInfo.getPump1Time());
        map.put(PUMP2, timerInfo.getPump2Time());

        return (delta == 0)? map.get(type) : map.get(type) - delta;
    }

    /////TIMERS////////
    public void startTimer(int type, int delta, TimerInfo timerInfo, int pos, Runnable onFinish, SchedulerAdapter schedulerAdapter) {
        int duration = getDuration(type, delta, timerInfo);

        int finalDuration = duration;
        timerRunnable = new Runnable() {
            int i = finalDuration;

            @Override
            public void run() {
                i--;
                Fragment currentFragment = getCurrentFragment();
                if (i > 0) {
                    if (currentFragment instanceof DashboardFragment && schedulerAdapter == null) setTextView(type, i);
                    else if (currentFragment instanceof AutomationsFragment && schedulerAdapter != null) setTextView(type, pos, i, schedulerAdapter);
                    Log.d("TIMER", String.valueOf(i));
                    handler.postDelayed(this, 1000);
                }
                else {
                    if (currentFragment instanceof DashboardFragment && schedulerAdapter == null) ((DashboardFragment) currentFragment).buttonPressed(type, false);
                    else if (currentFragment instanceof AutomationsFragment && schedulerAdapter != null) setTextView(type, pos, duration, schedulerAdapter);
                    stopTimer(type, timerInfo);
                    if (onFinish != null) onFinish.run();
                }
            }
        };

        handler.postDelayed(timerRunnable, 1000);
    }

    public void startSchedule(int pos, int delta, SchedulerAdapter schedulerAdapter){
        TimerInfo tempInfo = schedulerInfo.get(pos);
        contentHelper.writeContent(tempInfo, "tempInfo.json", this);
        int[] states = {MIXER1, MIXER2,  MIXER3, PUMP1, PUMP2};
        final int[] curr = {0};
//        Runnable startNextTimer = new Runnable() {
//            @Override
//            public void run() {
//                if (curr[0] == states.length - 1) stopSchedule(pos, schedulerInfo.get(pos), schedulerAdapter);
//                else {
//                    startTimer(states[curr[0]], delta, schedulerInfo.get(pos), pos, this, schedulerAdapter);
//                    curr[0]++;
//                }
//            }
//        };
//
//        // Start the first timer
//        startNextTimer.run();
    }

    public void stopTimer(int type, TimerInfo timerInfo){
        handler.removeCallbacks(timerRunnable);
        if (type >= MIXER1 && type <= MIXER3) {
            timerInfo.setMixerState(NAN);
            timerInfo.setMixerStart(0);
        }
        else if (type >= PUMP1 && type <= PUMP2) {
            timerInfo.setPumpState(NAN);
            timerInfo.setPumpStart(0);
        }
    }

    public void stopSchedule(int pos, TimerInfo timerInfo, SchedulerAdapter schedulerAdapter){
        handler.removeCallbacks(timerRunnable);
        TypeToken<TimerInfo> timerInfoTypeToken = new TypeToken<TimerInfo>() {};
        schedulerInfo.set(pos, contentHelper.loadContent(timerInfoTypeToken, "tempInfo.json", this));
        schedulerInfo.get(pos).setSchedulerState(NAN);
        Fragment currentFragment = getCurrentFragment();
        if (currentFragment instanceof AutomationsFragment) ((AutomationsFragment) currentFragment).getSchedules().get(pos).setSchedulerState(NAN);
        contentHelper.deleteJSONFile("tempInfo.json", this);
        schedulerAdapter.notifyItemChanged(pos);
    }

    public void setTextView(int type, int i){
        Fragment currentFragment = getCurrentFragment();
        if (currentFragment instanceof DashboardFragment) {
            if (type == MIXER1)  ((DashboardFragment) currentFragment).getMixer1Time().setText(formatTime(i));
            else if (type == MIXER2)  ((DashboardFragment) currentFragment).getMixer2Time().setText(formatTime(i));
            else if (type == MIXER3)  ((DashboardFragment) currentFragment).getMixer3Time().setText(formatTime(i));
            else if (type == PUMP1)  ((DashboardFragment) currentFragment).getPump1Time().setText(formatTime(i));
            else if (type == PUMP2)  ((DashboardFragment) currentFragment).getPump2Time().setText(formatTime(i));
        }
    }

    private void setTextView(int type, int pos, int i, SchedulerAdapter schedulerAdapter){
        Fragment currentFragment = getCurrentFragment();
        if (currentFragment instanceof AutomationsFragment) {
            if (type == MIXER1)  ((AutomationsFragment) currentFragment).getSchedules().get(pos).setMixer1Time(i);
            else if (type == MIXER2)  ((AutomationsFragment) currentFragment).getSchedules().get(pos).setMixer2Time(i);
            else if (type == MIXER3)  ((AutomationsFragment) currentFragment).getSchedules().get(pos).setMixer3Time(i);
            else if (type == PUMP1)  ((AutomationsFragment) currentFragment).getSchedules().get(pos).setPump1Time(i);
            else if (type == PUMP2)  ((AutomationsFragment) currentFragment).getSchedules().get(pos).setPump2Time(i);
        }
        schedulerAdapter.notifyItemChanged(pos);
    }

    public static long getCurrentEpochTime() {
        long currentTimeMillis = System.currentTimeMillis();
        return currentTimeMillis / 1000;
    }

    ////CHANGE INT TO MM:SS/////
    public String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public String formatTime(long totalSeconds){
        Date date = new Date(totalSeconds * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC+7"));
        return sdf.format(date);
    }

    public long formatTime(String isoTimestamp){
        ZonedDateTime dateTime = ZonedDateTime.parse(isoTimestamp, DateTimeFormatter.ISO_DATE_TIME);
        return dateTime.toInstant().getEpochSecond();
    }

    public void sendSchedule(int type, int duration, String str, int base){
        Map<Integer, String> areaMap = new HashMap<>();
        areaMap.put(AREA1, "A");
        areaMap.put(AREA2, "B");
        areaMap.put(AREA3, "C");
        String payload = (!Objects.equals(str, "selector"))? "{\"" + str + (type - base + 1) + "\":" + duration + "}" : "{\"" + str + "\":" + areaMap.get(duration) + "}";
        client.sendDataMQTT("project_IOT_hcmut/feeds/data", payload);
    }

    public void sendSchedule(int pos){
        char selector =  (char) ('A' + schedulerInfo.get(pos).getAreaType() - 1);
//        {"mixer1": 5, "mixer2": 5, "mixer3": 5, "pump_in": 5, "pump_out": 5, "selector": "A", "cycle": 2, "startTime": "13:51"}
        String payload = "{" +
                "\"mixer1\": " + schedulerInfo.get(pos).getMixer1Time() + ", " +
                "\"mixer2\": " + schedulerInfo.get(pos).getMixer2Time() + ", " +
                "\"mixer3\": " + schedulerInfo.get(pos).getMixer3Time() + ", " +
                "\"pump_in\": " + schedulerInfo.get(pos).getPump1Time() + ", " +
                "\"pump_out\": " + schedulerInfo.get(pos).getPump2Time() + ", " +
                "\"selector\": \"" + selector + "\", " +
                "\"cycle\": " + schedulerInfo.get(pos).getCycleCount() + ", " +
                "\"startTime\": " + "\"" + formatTime(schedulerInfo.get(pos).getMixerStart()) + "\"" +
                "}";
        client.sendDataMQTT("project_IOT_hcmut/feeds/data", payload);
    }


}