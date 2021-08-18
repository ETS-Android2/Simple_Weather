package entertainment.android_apps.simpleweather;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import entertainment.android_apps.simpleweather.constants.Constants;

public class LocationReceiver extends BroadcastReceiver {
    Context context1;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Broadcast Listened", "Service tried to run");
        context1 = context;
//        sendNotification(context);
//        SharedPreferences sharedPreferences = context.getSharedPreferences("StartLocationService",Context.MODE_PRIVATE);
//        long lastServiceStart = sharedPreferences.getLong("StartLocationService",0);
//        long mSeconds = System.currentTimeMillis() - lastServiceStart;
//        int hours = (int) (mSeconds / (60 * 1000));

//        Log.d("Broadcast Listened", "isLocationServiceRunning = " + isLocationServiceRunning());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            if(hours >= 15){
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putLong("StartLocationService", System.currentTimeMillis());
//                editor.apply();
//                Log.i("LocationReceiver", "Start Location Service !");
                Intent startIntent = new Intent(context, LocationService.class);
                startIntent.setAction(Constants.ACTION_START_LOCATION_SERVICE);
                context.startForegroundService(startIntent);
//            }
//            else{
//                Log.i("LocationReceiver", "Not time to Start Location Service");
//            }


        } else {
            Intent intent1 = new Intent(context, LocationService.class);
            intent1.setAction(Constants.ACTION_START_LOCATION_SERVICE);
            context.startService(intent1);
        }
    }
    private boolean isLocationServiceRunning() {
        ActivityManager activityManager =
                (ActivityManager) context1.getSystemService(Context.ACTIVITY_SERVICE);
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
    private void sendNotification(Context context) {
        Intent ii = new Intent(context, MainActivity.class);
        ii.putExtra("StopAlarm", "Stop");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, ii, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setOngoing(false)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle("LocationReceiver")
                .setContentText(new Date().toString());
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(202, builder.build());
    }
}
