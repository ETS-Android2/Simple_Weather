package entertainment.android_apps.simpleweather;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import entertainment.android_apps.simpleweather.constants.Constants;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private long backPressedTime;
    private Toast backToast;
    MediaPlayer mPlayer;
    DataStorage dataStorage;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();

        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS) == PackageManager.PERMISSION_GRANTED) {

            SharedPreferences sharedPreferences = getSharedPreferences("AlarmManager",MODE_PRIVATE);
            long lastAlarmManagerRun = sharedPreferences.getLong("AlarmManager",0);
            Intent receiverIntent = new Intent(this, LocationReceiver.class);
            receiverIntent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, receiverIntent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            long mSeconds = getCurrentTimeMilliSeconds() - lastAlarmManagerRun;
            int newHours = (int) (mSeconds / (60 * 60 * 1000));
            if(newHours >= 24){
                String packageName = getApplicationContext().getPackageName();
                Intent batteryOptimizationIntent = new Intent();
                String batteryOptimizationPackageName = getApplicationContext().getPackageName();
                PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
                if (!pm.isIgnoringBatteryOptimizations(batteryOptimizationPackageName))
                {
                    batteryOptimizationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    batteryOptimizationIntent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    batteryOptimizationIntent.setData(Uri.parse("package:" + batteryOptimizationPackageName));
                    getApplicationContext().startActivity(batteryOptimizationIntent);
                }
                Log.i("PowerManager", "PowerManager = " + pm.isIgnoringBatteryOptimizations(packageName));
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+ 2000, 5400000, pendingIntent);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong("AlarmManager", getLastUpdateMilliSeconds());
                editor.apply();
                Log.i("MAINACTIVITY", "Alarm manager is set !");
            }
            else{
                Log.i("MAINACTIVITY", "Alarm manager is running ...");
            }

            Intent intent = getIntent();
            String text = "";
            if (intent.getStringExtra("StopAlarm") != null) {
                text = intent.getStringExtra("StopAlarm");
            }
            if (text.equals("Stop")) {
                if (isLocationServiceRunning()) {
                    Log.i("MAINACTIVITY", "isLocationServiceRunning = " + isLocationServiceRunning());

                    getApplicationContext().stopService(new Intent(getApplicationContext(), LocationService.class));
                }
            }
        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION, Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS}, 1000);
            Intent intentPowerManager = new Intent();
            String secondPackageName = getApplicationContext().getPackageName();
            PowerManager pm_2 = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
            if (!pm_2.isIgnoringBatteryOptimizations(secondPackageName))
            {
                intentPowerManager.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentPowerManager.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intentPowerManager.setData(Uri.parse("package:" + secondPackageName));
                getApplicationContext().startActivity(intentPowerManager);
            }
        }

        dataStorage = new DataStorage(MainActivity.this);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;

        if(dataStorage.getShowDialog() == true){
            dialog = new Dialog(MainActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.help);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
            dialog.findViewById(R.id.btn_help).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dataStorage.setShowDialog(false);
                    dialog.dismiss();
                    Log.i("dialog()", "dismiss()");
                }
            });
            dialog.show();
        }
    }

    private boolean isLocationServiceRunning() {
        ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (LocationService.class.getName().equals(service.service.getClassName())) {
                    if (service.foreground) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }
    private long getCurrentTimeMilliSeconds() {
        Date curDate = new Date();
        return curDate.getTime();
    }

    private long getLastUpdateMilliSeconds() {
        Date lastUpdate = new Date();
        return lastUpdate.getTime();
    }

    void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = "NotificationChannel";
            String description = "Channel for notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(
                    Constants.NOTIFICATION_CHANNEL_ID,
                    name,
                    importance);
            channel.setSound(null,null);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void stopPlay(){
        try {
            mPlayer.stop();
        }
        catch (Exception ex) {
            Log.i(TAG, "EXCEPTION stop Play" + ex);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (mPlayer.isPlaying()) {
                stopPlay();
            }
        }
        catch (Exception ex) {
            Log.i(TAG, "EXCEPTION stop Play" + ex);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try{
            if (mPlayer.isPlaying()) {
                stopPlay();
            }
        }catch (Exception ex){
            Log.i("onPause()", "onPause()" + ex);
        }
    }
    @Override
    public void onBackPressed() {
        stopPlay();
        if(backPressedTime + 2000 > System.currentTimeMillis()){
            backToast.cancel();
            super.onBackPressed();
        }
        else{
            backToast = Toast.makeText(getBaseContext(),"Нажмите еще раз, чтобы выйти",Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis(); //Время нажатия кнопки
    }
}