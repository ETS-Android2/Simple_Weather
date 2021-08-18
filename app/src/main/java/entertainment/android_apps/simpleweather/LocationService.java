package entertainment.android_apps.simpleweather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import entertainment.android_apps.simpleweather.constants.Constants;

public class LocationService extends Service {
    Handler handler;
    private String resultLocation = "";
    private String resultLocationKey = "";
    private String currentLocationCity = "";
    private String currentWeatherResponse = "";
    private String sharedWeatherResponse = "";
    private double dLatitude = 0;
    private double dLongitude = 0;
    FusedLocationProviderClient fusedLocationProviderClient;
    SharedPreferences sharedPreferences;
    private String sharedKey = "";
    private String sharedCity = "";
    RemoteViews remoteViews;
    String date = "";
    private static final String TAG = MainActivity.class.getSimpleName();
    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult == null) { return;}
            for (Location location : locationResult.getLocations()) {
                if (location != null) {
                    try {
                        dLatitude = location.getLatitude();dLongitude = location.getLongitude();
                        new Thread() {
                            public void run() {
                                try {
                                    URL latitudeLongitudeURL = new LatitudeLongitudeConnection().buildUrlLatitudeLongitude_Notification(Double.toString(dLatitude), Double.toString(dLongitude));
                                    resultLocation = new LatitudeLongitudeConnection().getResponseForLatitudeLongitudeFromHttpUrl(latitudeLongitudeURL);
                                    if (resultLocation == null || resultLocation.equals("ExceptionHappened")) {
                                        getApplicationContext().stopService(new Intent(getApplicationContext(), LocationService.class));
                                    } else if (resultLocation.equals("[]")) {
                                        getApplicationContext().stopService(new Intent(getApplicationContext(), LocationService.class));
                                    } else {
                                        handler.post(new Runnable() {
                                            public void run() {
                                                try {
                                                    JSONArray jsonarray = new JSONArray(resultLocation);
                                                    JSONObject jsonobject = jsonarray.getJSONObject(0);
                                                    resultLocationKey = jsonobject.getString("Key");
                                                    currentLocationCity = jsonobject.getString("LocalizedName");
                                                    if (resultLocationKey != null && !currentLocationCity.equals("")) {
                                                        new Thread() {
                                                            public void run() {
                                                                try {
                                                                    URL currentWeatherURL = new ForecastConnection().buildUrlWeather_Notification(resultLocationKey);
                                                                    currentWeatherResponse = new ForecastConnection().getResponseWeather(currentWeatherURL);
                                                                    if (currentWeatherResponse == null || currentWeatherResponse.equals("") ||
                                                                            currentWeatherResponse.equals("[]") || currentWeatherResponse.equals("ExceptionHappened")) {
                                                                        getApplicationContext().stopService(new Intent(getApplicationContext(), LocationService.class));
                                                                    } else {
                                                                        handler.post(new Runnable() {
                                                                            public void run() {
                                                                                try {
                                                                                    renderWeather(currentWeatherResponse, currentLocationCity);
                                                                                    sendNotification(getApplicationContext());
                                                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                                                    editor.putString("resLocationKey", resultLocationKey);
                                                                                    editor.putString("curLocationCity", currentLocationCity);
                                                                                    editor.apply();
                                                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), LocationService.class)); } catch (Exception ex) { } }});
                                                                    }
                                                                } catch (Exception ex) { } }
                                                        }.start(); } else{
                                                        getApplicationContext().stopService(new Intent(getApplicationContext(), LocationService.class));}
                                                } catch (Exception e) { } }
                                        });}} catch (Exception ex) {}    }
                        }.start();} catch (Exception e) {e.printStackTrace();}} }    }
    };
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        sharedPreferences = getSharedPreferences("NotificationWithoutGPS",MODE_PRIVATE);
        sharedKey = sharedPreferences.getString("resLocationKey",null);
        sharedCity = sharedPreferences.getString("curLocationCity",null);
        handler = new Handler();
        remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification);
        LocationManager manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isConnected = cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
        if (isConnected) {
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                String action = intent.getAction();
                Log.i("onStartCommand : action", action);
                if (action != null) {if (action.equals(Constants.ACTION_START_LOCATION_SERVICE)) {
                        startLocationService(getApplicationContext()); }}}
            else if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                if(sharedKey != null && sharedCity != null){
                    if(!sharedKey.equals("") && !sharedCity.equals("")){
                        startLocationServiceWithoutGPS(getApplicationContext(),sharedKey,sharedCity);
                    } else getApplicationContext().stopService(new Intent(getApplicationContext(), LocationService.class));}
                else{getApplicationContext().stopService(new Intent(getApplicationContext(), LocationService.class));} } else
                getApplicationContext().stopService(new Intent(getApplicationContext(), LocationService.class));} else
            getApplicationContext().stopService(new Intent(getApplicationContext(), LocationService.class));
        return super.onStartCommand(intent, flags, startId); }

    private void sendNotification(Context context) {
        Intent ii = new Intent(context, MainActivity.class);
        ii.putExtra("StopAlarm", "Stop");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, ii, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.root, pendingIntent);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setOngoing(false)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContent(remoteViews);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(200, builder.build()); }

    private void startLocationService(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher) .setAutoCancel(true) .setOngoing(false) .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle("") .setContentText("");
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @SuppressLint("MissingPermission")
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        try {
                            dLatitude = location.getLatitude();dLongitude = location.getLongitude();
                            new Thread() {
                                public void run() {
                                    try {
                                        URL latitudeLongitudeURL = new LatitudeLongitudeConnection().buildUrlLatitudeLongitude_Notification(Double.toString(dLatitude), Double.toString(dLongitude));
                                        resultLocation = new LatitudeLongitudeConnection().getResponseForLatitudeLongitudeFromHttpUrl(latitudeLongitudeURL);
                                        if (resultLocation == null || resultLocation.equals("ExceptionHappened")) {
                                            getApplicationContext().stopService(new Intent(getApplicationContext(), LocationService.class));
                                        } else if (resultLocation.equals("[]")) {
                                            getApplicationContext().stopService(new Intent(getApplicationContext(), LocationService.class));
                                        } else { handler.post(new Runnable() {
                                                public void run() {
                                                    try {
                                                        JSONArray jsonarray = new JSONArray(resultLocation);
                                                        JSONObject jsonobject = jsonarray.getJSONObject(0);
                                                        resultLocationKey = jsonobject.getString("Key");
                                                        currentLocationCity = jsonobject.getString("LocalizedName");
                                                        if (resultLocationKey != null && !currentLocationCity.equals("")) {
                                                            new Thread() {
                                                                public void run() {
                                                                    try {
                                                                        URL currentWeatherURL = new ForecastConnection().buildUrlWeather_Notification(resultLocationKey);
                                                                        currentWeatherResponse = new ForecastConnection().getResponseWeather(currentWeatherURL);
                                                                        if (currentWeatherResponse == null || currentWeatherResponse.equals("") ||
                                                                                currentWeatherResponse.equals("[]") || currentWeatherResponse.equals("ExceptionHappened")) {
                                                                            getApplicationContext().stopService(new Intent(getApplicationContext(), LocationService.class));
                                                                        } else {
                                                                            handler.post(new Runnable() {
                                                                                public void run() {
                                                                                    try {
                                                                                        renderWeather(currentWeatherResponse, currentLocationCity);
                                                                                        sendNotification(getApplicationContext());
                                                                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                                                                        editor.putString("resLocationKey", resultLocationKey);
                                                                                        editor.putString("curLocationCity", currentLocationCity);
                                                                                        editor.apply();
                                                                                        getApplicationContext().stopService(new Intent(getApplicationContext(), LocationService.class));
                                                                                    } catch (Exception ex) {} }});}} catch (Exception ex) { } }}.start();}
                                                        else{getApplicationContext().stopService(new Intent(getApplicationContext(), LocationService.class)); }
                                                    } catch (Exception e) {} }
                                            });}} catch (Exception ex) { }} }.start(); } catch (Exception e) {e.printStackTrace(); } }
                    else{
                        LocationRequest locationRequest = new LocationRequest();
                        locationRequest.setInterval(4000);
                        locationRequest.setNumUpdates(1);
                        locationRequest.setFastestInterval(2000);
                        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                        LocationServices.getFusedLocationProviderClient(getApplicationContext()).requestLocationUpdates(locationRequest, locationCallback, null);
                    } }
            }); startForeground(101, builder.build());}
        if(dLatitude == 0 && dLongitude == 0){ getApplicationContext().stopService(new Intent(getApplicationContext(), LocationService.class));}}

    private void startLocationServiceWithoutGPS(Context context, final String sharedKey, final String sharedCity) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setOngoing(false)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle("")
                .setContentText("");
        new Thread() {
            public void run() {
                try {
                    //Для погоды - начало
                    URL currentWeatherURL = new ForecastConnection().buildUrlWeather_Notification(sharedKey);
                    sharedWeatherResponse = new ForecastConnection().getResponseWeather(currentWeatherURL);
                    Log.i(TAG, "currentWeatherURL" + currentWeatherURL);
                    Log.i(TAG, "currentWeatherResponse: " + sharedWeatherResponse);

                    if (sharedWeatherResponse == null || sharedWeatherResponse.equals("") ||
                            sharedWeatherResponse.equals("[]") || sharedWeatherResponse.equals("ExceptionHappened")) {
                        getApplicationContext().stopService(new Intent(getApplicationContext(), LocationService.class));
                    } else {

                        handler.post(new Runnable() {
                            public void run() {
                                try {

                                    renderWeather(sharedWeatherResponse, sharedCity);

                                    sendNotification(getApplicationContext());
                                    Log.i("NOTIFICATION", "NOTIFICATION sent!! ");
                                    getApplicationContext().stopService(new Intent(getApplicationContext(), LocationService.class));

                                } catch (Exception ex) {
                                }
                            }
                        });
                    }
                    //Для погоды - конец
                } catch (Exception ex) {
                }
            }
        }.start();




//            startForeground(200, builder.build());
//            startForeground(0, null);
            startForeground(101, builder.build());

        if(dLatitude == 0 && dLongitude == 0){    getApplicationContext().stopService(new Intent(getApplicationContext(), LocationService.class));
        }    }

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), Constants.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher).setAutoCancel(true).setOngoing(false)
                .setPriority(NotificationCompat.PRIORITY_HIGH) .setContentTitle("") .setContentText("");
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startForeground(101, builder.build()); }}

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("onDestroy", "onDestroy = ");
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    private void renderWeather(String currentWeatherResponse, String currentCity) {
        int icon = 0;
        try {
            JSONArray mJsonArray = new JSONArray(currentWeatherResponse);
            JSONObject mJsonObject = mJsonArray.getJSONObject(0);
            String temperature = Integer.toString((int) mJsonObject.getJSONObject("Temperature").getJSONObject("Metric").getDouble("Value")) + "°";
            remoteViews.setTextViewText(R.id.notification_degrees, temperature);
            int weatherIcon = mJsonObject.getInt("WeatherIcon");
            switch (weatherIcon) {
                case 1:
                    icon = R.drawable.big_sunny;
                    break;
                case 2:
                    icon = R.drawable.big_mostly_sunny;
                    break;
                case 3:
                    icon = R.drawable.big_partly_sunny;
                    break;
                case 4:
                    icon = R.drawable.big_intermittent_clouds;
                    break;
                case 5:
                    icon = R.drawable.big_hazy_sunshine;
                    break;
                case 6:
                    icon = R.drawable.big_mostly_cloudy;
                    break;
                case 7:
                    icon = R.drawable.big_cloudy;
                    break;
                case 8:
                    icon = R.drawable.big_dreary;
                    break;
                case 11:
                    icon = R.drawable.big_fog;
                    break;
                case 12:
                    icon = R.drawable.big_showers;
                    break;
                case 13:
                    icon = R.drawable.big_mostly_cloudy_showers;
                    break;
                case 14:
                    icon = R.drawable.big_partly_sunny_showers;
                    break;
                case 15:
                    icon = R.drawable.big_t_storms;
                    break;
                case 16:
                    icon = R.drawable.big_mostly_cloudy_t_storms;
                    break;
                case 17:
                    icon = R.drawable.big_partly_sunny_t_storms;
                    break;
                case 18:
                    icon = R.drawable.big_rain;
                    break;
                case 19:
                    icon = R.drawable.big_flurries;
                    break;
                case 20:
                    icon = R.drawable.big_mostly_cloudy_flurries;
                    break;
                case 21:
                    icon = R.drawable.big_partly_sunny_flurries;
                    break;
                case 22:
                    icon = R.drawable.big_snow;
                    break;
                case 23:
                    icon = R.drawable.big_mostly_cloudy_snow;
                    break;
                case 24:
                    icon = R.drawable.big_ice;
                    break;
                case 25:
                    icon = R.drawable.big_sleet;
                    break;
                case 26:
                    icon = R.drawable.big_freezing_rain;
                    break;
                case 29:
                    icon = R.drawable.big_rain_and_snow;
                    break;
                case 30:
                    icon = R.drawable.big_hot;
                    break;
                case 31:
                    icon = R.drawable.big_cold;
                    break;
                case 32:
                    icon = R.drawable.big_windy;
                    break;
                case 33:
                    icon = R.drawable.big_clear;
                    break;
                case 34:
                    icon = R.drawable.big_mostly_clear;
                    break;
                case 35:
                    icon = R.drawable.big_partly_cloudy;
                    break;
                case 36:
                    icon = R.drawable.big_intermittent_clouds_night;
                    break;
                case 37:
                    icon = R.drawable.big_hazy_moonlight;
                    break;
                case 38:
                    icon = R.drawable.big_mostly_cloudy_night;
                    break;
                case 39:
                    icon = R.drawable.big_partly_cloudy_showers_night;
                    break;
                case 40:
                    icon = R.drawable.big_mostly_cloudy_showers_night;
                    break;
                case 41:
                    icon = R.drawable.big_partly_cloudy_t_storms_night;
                    break;
                case 42:
                    icon = R.drawable.big_mostly_cloudy_t_storms_night;
                    break;
                case 43:
                    icon = R.drawable.big_mostly_cloudy_flurries_night;
                    break;
                case 44:
                    icon = R.drawable.big_mostly_cloudy_snow_night;
                    break;
                default:
                    icon = 0;
                    break;
            }
            remoteViews.setImageViewResource(R.id.notification_icon, icon);
            remoteViews.setTextViewText(R.id.notification_wether_type, mJsonObject.getString("WeatherText"));
            remoteViews.setTextViewText(R.id.notification_city, currentCity);
            remoteViews.setImageViewResource(R.id.notification_no_gps_icon,R.drawable.gps_icon);
            LocationManager manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                remoteViews.setViewVisibility(R.id.notification_no_gps_icon,View.VISIBLE);
            }else {remoteViews.setViewVisibility(R.id.notification_no_gps_icon,View.GONE);}
            String date = new SimpleDateFormat("HH:mm:ss").format(new Date());
            String resString = "";
            for (int i = 1; i < date.length(); i++) {
                if (":".equals(date.substring(i - 1, i)))
                    resString = date.substring(0, i - 1); }
            remoteViews.setTextViewText(R.id.notification_time,resString);
        } catch (Exception ex) { }}}
