package entertainment.android_apps.simpleweather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.appwidget.AppWidgetManager;
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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import entertainment.android_apps.simpleweather.constants.Constants;

public class WidgetService extends Service {
    Handler handler;
    RemoteViews views;
    private String resultLocation = "";
    private String resultLocationKey = "";
    private String currentLocationCity = "";
    private String currentWeatherResponse = "";
    private String keyWeatherResponse = "";

    private double dLatitude = 0;
    private double dLongitude = 0;
    FusedLocationProviderClient fusedLocationProviderClient;

    int[] appWidgetIds;
    AppWidgetManager appWidgetManager;

    SharedPreferences sharedPreferences;
    private String keyForUpdate = "";
    private String keyCity = "";

    private static final String TAG = WeatherWidget.class.getSimpleName();
    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Log.i(TAG, "locationCallback: " + locationResult.getLastLocation().getLatitude());
            Log.i(TAG, "locationCallback: " + locationResult.getLastLocation().getLongitude());

            Log.i("locationResult", "locationResult: " + locationResult);
            if (locationResult == null) {
                return;
            }
            for (Location location : locationResult.getLocations()) {
                if (location != null) {
                    try {
                        dLatitude = location.getLatitude();
                        dLongitude = location.getLongitude();
                        new Thread() {
                            public void run() {
                                try {
                                    URL latitudeLongitudeURL = new LatitudeLongitudeConnection().buildUrlLatitudeLongitude_Widget(Double.toString(dLatitude), Double.toString(dLongitude));
                                    resultLocation = new LatitudeLongitudeConnection().getResponseForLatitudeLongitudeFromHttpUrl(latitudeLongitudeURL);
                                    Log.i(TAG, "latitudeLongitudeURL" + latitudeLongitudeURL);
                                    Log.i(TAG, "resultLocation: " + resultLocation);

                                    if (resultLocation == null || resultLocation.equals("ExceptionHappened")) {
                                        getApplicationContext().stopService(new Intent(getApplicationContext(), WidgetService.class));
                                    } else if (resultLocation.equals("[]")) {
                                        getApplicationContext().stopService(new Intent(getApplicationContext(), WidgetService.class));
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
                                                                    //Для погоды - начало
                                                                    URL currentWeatherURL = new ForecastConnection().buildUrlWeather_Widget(resultLocationKey);
                                                                    currentWeatherResponse = new ForecastConnection().getResponseWeather(currentWeatherURL);
                                                                    Log.i(TAG, "currentWeatherURL" + currentWeatherURL);
                                                                    Log.i(TAG, "currentWeatherResponse: " + currentWeatherResponse);

                                                                    if (currentWeatherResponse == null || currentWeatherResponse.equals("")
                                                                            || currentWeatherResponse.equals("[]") || currentWeatherResponse.equals("ExceptionHappened")) {
                                                                        getApplicationContext().stopService(new Intent(getApplicationContext(), WidgetService.class));
                                                                    } else {

                                                                        handler.post(new Runnable() {
                                                                            public void run() {
                                                                                try {

                                                                                    renderWeather(currentWeatherResponse, currentLocationCity);
                                                                                    if (appWidgetIds.length > 0) {
                                                                                        for (int widgetId : appWidgetIds) {
                                                                                            appWidgetManager.updateAppWidget(widgetId, views);
                                                                                            Log.i("WIDGET", "WIDGET UPDATE!! ");
                                                                                        }
                                                                                    }

                                                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                                                    editor.putString("resultLocationKey", resultLocationKey);
                                                                                    editor.putString("currentLocationCity", currentLocationCity);
                                                                                    editor.apply();

//                                                                                    Log.i("WIDGET", "WIDGET UPDATE!! ");
                                                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), WidgetService.class));
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
                                                    }
                                                    else getApplicationContext().stopService(new Intent(getApplicationContext(), WidgetService.class));
                                                } catch (Exception e) {
                                                }
                                            }
                                        });
                                    }
                                } catch (Exception ex) {
                                }
                            }
                        }.start();
//
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    private void startUpdateWeather(final Context context) {
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED){


            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @SuppressLint("MissingPermission")
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    Log.i("fusedLocationProviderClient", "Location = " + location);
                    if (location != null) {
                        try {
                            dLatitude = location.getLatitude();
                            dLongitude = location.getLongitude();
                            Log.i("WIDGET", "fusedLocationProviderClient: " + dLatitude);
                            Log.i("WIDGET", "fusedLocationProviderClient: " + dLongitude);
                            new Thread() {
                                public void run() {
                                    try {
                                        URL latitudeLongitudeURL = new LatitudeLongitudeConnection().buildUrlLatitudeLongitude_Widget(Double.toString(dLatitude), Double.toString(dLongitude));
                                        resultLocation = new LatitudeLongitudeConnection().getResponseForLatitudeLongitudeFromHttpUrl(latitudeLongitudeURL);
                                        Log.i("fusedLocationProviderClient", "latitudeLongitudeURL" + latitudeLongitudeURL);
                                        Log.i("fusedLocationProviderClient", "resultLocation: " + resultLocation);

                                        if (resultLocation == null || resultLocation.equals("ExceptionHappened")) {
                                            getApplicationContext().stopService(new Intent(getApplicationContext(), WidgetService.class));
                                        } else if (resultLocation.equals("[]")) {
                                            getApplicationContext().stopService(new Intent(getApplicationContext(), WidgetService.class));
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
                                                                        //Для погоды - начало
                                                                        URL currentWeatherURL = new ForecastConnection().buildUrlWeather_Widget(resultLocationKey);
                                                                        Log.i(TAG, "currentWeatherURL" + currentWeatherURL);
                                                                        currentWeatherResponse = new ForecastConnection().getResponseWeather(currentWeatherURL);

                                                                        Log.i(TAG, "currentWeatherResponse: " + currentWeatherResponse);

                                                                        if (currentWeatherResponse == null || currentWeatherResponse.equals("")
                                                                                || currentWeatherResponse.equals("[]") || currentWeatherResponse.equals("ExceptionHappened")) {
                                                                            getApplicationContext().stopService(new Intent(getApplicationContext(), WidgetService.class));

                                                                        } else {

                                                                            handler.post(new Runnable() {
                                                                                public void run() {
                                                                                    try {

                                                                                        renderWeather(currentWeatherResponse, currentLocationCity);
                                                                                        if (appWidgetIds.length > 0) {
                                                                                            for (int widgetId : appWidgetIds) {
                                                                                                appWidgetManager.updateAppWidget(widgetId, views);
                                                                                                Log.i("WIDGET", "WIDGET UPDATE!! ");
                                                                                            }
                                                                                        }

                                                                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                                                                        editor.putString("resultLocationKey", resultLocationKey);
                                                                                        editor.putString("currentLocationCity", currentLocationCity);
                                                                                        editor.apply();

//                                                                                        Log.i("WIDGET", "WIDGET UPDATE!! ");
                                                                                        getApplicationContext().stopService(new Intent(getApplicationContext(), WidgetService.class));


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
                                                        } else getApplicationContext().stopService(new Intent(getApplicationContext(), WidgetService.class));
                                                    } catch (Exception e) {
                                                    }
                                                }
                                            });
                                        }
                                    } catch (Exception ex) {
                                    }
                                }
                            }.start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        Log.i("WIDGET", "locationRequest !" );
                        LocationRequest locationRequest = new LocationRequest();
                        locationRequest.setInterval(4000);
                        locationRequest.setNumUpdates(1);
                        locationRequest.setFastestInterval(2000);
                        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


                        LocationServices.getFusedLocationProviderClient(getApplicationContext()).requestLocationUpdates(locationRequest, locationCallback, null);
                    }
                }
            });
        }
    }

    private void startUpdateWithoutGPS(final String keyForUpdate, final String keyCity) {
        new Thread() {
            public void run() {
                try {
                    //Для погоды - начало
                    URL currentWeatherURL = new ForecastConnection().buildUrlWeather_Widget(keyForUpdate);
                    keyWeatherResponse = new ForecastConnection().getResponseWeather(currentWeatherURL);
                    Log.i(TAG, "currentWeatherURL" + currentWeatherURL);
                    Log.i(TAG, "currentWeatherResponse: " + keyWeatherResponse);

                    if (keyWeatherResponse == null || keyWeatherResponse.equals("")
                            || keyWeatherResponse.equals("[]") || keyWeatherResponse.equals("ExceptionHappened")) {
                        getApplicationContext().stopService(new Intent(getApplicationContext(), WidgetService.class));
                    } else {
                        handler.post(new Runnable() {
                            public void run() {
                                try {

                                    renderWeather(keyWeatherResponse, keyCity);
                                    if (appWidgetIds.length > 0) {
                                        for (int widgetId : appWidgetIds) {
                                            appWidgetManager.updateAppWidget(widgetId, views);
                                            Log.i("startUpdateWithoutGPS", "startUpdateWithoutGPS!! ");
                                        }
                                    }
//                                                                                    Log.i("WIDGET", "WIDGET UPDATE!! ");
                                    getApplicationContext().stopService(new Intent(getApplicationContext(), WidgetService.class));
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
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("WidgetService", "onStartCommand !!");


        appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());
        appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

        sharedPreferences = getSharedPreferences("UpdateWithoutGPS",MODE_PRIVATE);
        keyForUpdate = sharedPreferences.getString("resultLocationKey",null);
        keyCity = sharedPreferences.getString("currentLocationCity",null);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        handler = new Handler();
        views = new RemoteViews(getApplicationContext().getPackageName(), R.layout.weather_widget);
        LocationManager manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean isConnected = cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
        Log.i("CONECTING", "CONECTING" + isConnected);
        if (isConnected) {
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                Log.i("startUpdateWeather", "startUpdateWeather");
                startUpdateWeather(getApplicationContext());
            }
//            else getApplicationContext().stopService(new Intent(getApplicationContext(), WidgetService.class));
                //Если GPS выключен - обновление для последнего обновляемого города - начало
            else if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                if(keyForUpdate != null && keyCity != null){
                    if(!keyForUpdate.equals("") && !keyCity.equals("")){
                        Log.i("startUpdateWithoutGPS", "startUpdateWithoutGPS");
                        startUpdateWithoutGPS(keyForUpdate,keyCity);
                    } else getApplicationContext().stopService(new Intent(getApplicationContext(), WidgetService.class));
                }
                else{
                    Log.i("keyForUpdate", "keyForUpdate" + keyForUpdate);
                    Log.i("keyCity", "keyCity" + keyCity);
                    getApplicationContext().stopService(new Intent(getApplicationContext(), WidgetService.class));
                }

            }
                //Если GPS выключен - обновление для последнего обновляемого города - конец
        } else
            getApplicationContext().stopService(new Intent(getApplicationContext(), WidgetService.class));
        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("WidgetService", "onCreate !!");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), Constants.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setOngoing(false)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle("")
                .setContentText("");
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED){
            
            startForeground(101, builder.build());
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("WidgetService", "onDestroy !!");
    }
    
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void renderWeather(String currentWeatherResponse, String currentCity) {
        Locale currentLocale = getApplicationContext().getResources().getConfiguration().locale;
        Calendar cal = GregorianCalendar.getInstance(TimeZone.getDefault(), currentLocale);

        String dayName = cal.getDisplayName(cal.DAY_OF_WEEK, Calendar.SHORT, currentLocale);
        String day = "", month = "";
        for (int i = 0; i < dayName.length(); i++) {
            if(i==0) day = day + dayName.substring(0, 1).toUpperCase();
            else day = day + dayName.substring(i, i + 1);
        }

        String monthName = cal.getDisplayName(cal.MONTH, Calendar.LONG, currentLocale);
        for (int i = 0; i < monthName.length(); i++) {
            if(i==0) month = month + monthName.substring(0, 1).toUpperCase();
            else month = month + monthName.substring(i, i + 1);
        }
        views.setCharSequence(R.id.textClock_currentDate,"setFormat12Hour","'" + day + "', dd '" + month);
        views.setCharSequence(R.id.textClock_currentDate,"setFormat24Hour","'" + day + "', dd '" + month);
        int icon = 0;
        try {
            JSONArray mJsonArray = new JSONArray(currentWeatherResponse);
            JSONObject mJsonObject = mJsonArray.getJSONObject(0);

            String temperature = Integer.toString((int) mJsonObject.getJSONObject("Temperature").getJSONObject("Metric").getDouble("Value")) + "°";
            views.setTextViewText(R.id.widget_degrees, temperature);

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
            views.setImageViewResource(R.id.widget_weather_icon, icon);
            views.setTextViewText(R.id.widget_weather_type, mJsonObject.getString("WeatherText"));
            views.setTextViewText(R.id.widget_city, currentCity);
            String date = new SimpleDateFormat("HH:mm:ss").format(new Date());
            String resString = "";

            for (int i = 1; i < date.length(); i++) {
                // смотрим, был ли слева пробел:
                if (":".equals(date.substring(i - 1, i)))
                    resString = date.substring(0, i - 1);
            }
            views.setTextViewText(R.id.widget_updateTime,resString);

            views.setImageViewResource(R.id.widget_no_gps_icon,R.drawable.gps_icon);
            LocationManager manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                views.setViewVisibility(R.id.widget_no_gps_icon, View.VISIBLE);
            }
            else {
                views.setViewVisibility(R.id.widget_no_gps_icon,View.GONE);
            }


            Log.i("WeatherWidget", "WeatherWidget!!!");
        } catch (Exception ex) {

        }
    }
}
