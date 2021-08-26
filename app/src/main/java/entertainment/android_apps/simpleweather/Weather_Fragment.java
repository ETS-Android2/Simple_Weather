package entertainment.android_apps.simpleweather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import entertainment.android_apps.simpleweather.constants.Constants;
import entertainment.android_apps.simpleweather.constants.SearchCity;
import entertainment.android_apps.simpleweather.interfaces.RecycleViewListener;

public class Weather_Fragment extends Fragment implements RecycleViewListener {
    private static final String SELECTED_TAB = "selected_tab";
    private static final String ACTIVITY = "activity";
    private static final String ACTIVITY_VALUE = "Weather_Fragment";
    private int selected_tab = 0;
    private SwipeRefreshLayout mSwipeRefresh;
    ScrollView scrollView;
    Constants constants;
    MediaPlayer mPlayer;
    AudioManager audioManager;
    Handler handler;
    String result = "";
    String resultLocation = "";
    String key = "";
    int resultChoice = 0;
    boolean isSelected = false;
    int selectedCityName = 0;
    String resultLocationKey = "";
    String currentCity = "";
    String currentLocationCity = "";
    String weatherResponse = "";
    String currentWeatherResponse = "";
    String hourlyResponse = "";
    String currentHourlyResponse = "";
    String fiveDayForecastResponse = "";
    String currentFiveDayForecastResponse = "";
    String currentLatitude = "";
    String currentLongitude = "";

    String[] strArray;

    TextView changeLastUpdateCity;
    TextView cityTitle;
    TextView lastUpdate;
    ImageView mainImage;
    TextView weatherType;
    TextView currentDeegrees;
    TextView current_wind;
    TextView real_feel;
    TextView visibility;
    TextView pressure;
    ImageView img_wind;
    ImageView img_real_feel;
    ImageView img_visibility;
    ImageView img_pressure;
    ImageView sunriseIcon;
    ImageView sunsetIcon;
    ImageView mainBackgroundImage;
    TextView sunRise;
    TextView sunSet;
    TextView rain;
    TextView thunder;
    TextView snow;
    ImageView next_day_first_icon;
    TextView next_day_first;
    TextView next_day_first_type;
    TextView next_day_first_temperature;
    ImageView next_day_second_icon;
    TextView next_day_second;
    TextView next_day_second_type;
    TextView next_day_second_temperature;
    ImageView next_day_third_icon;
    TextView next_day_third;
    TextView next_day_third_type;
    TextView next_day_third_temperature;
    TextView separator_1;
    TextView separator_2;
    TextView separator_3;
    GridLayout details;
    TextView toastText;
    View rootView;
    Toast myToats1;
    Toast myToats2;
    Toast myToats3;
    Toast myToats4;
    Toast myToats5;
    Button fiveDayBtn;
    Animation scale_Up, scale_Down;
    RecyclerView recyclerView;
    HourlyRecycleViewAdapter adapter;
    List<Hourly> hourlyList = new ArrayList<>();
    LinearLayout noGPSLayout;
    LinearLayout accuweatherLogo;
    GridLayout gridlayout;
    DataStorage dataStorage;
    private TextView threeDots;
    private String timeForThreeDayForecast = "";
    FusedLocationProviderClient fusedLocationProviderClient;
    String accuWeatherLink = "";
    boolean dialogResult = false;
    private LocationCallback mLocationCallback;
    LocationRequest mLocationRequest;

    public Weather_Fragment() {
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_weather_fragment, container, false);
        scale_Up = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
        scale_Down = AnimationUtils.loadAnimation(getContext(), R.anim.scale_down);
        stopPlay();
        rootView.findViewById(R.id.refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataStorage.setWhatUpdate(false);
                localStorage();
                ConnectivityManager cm =
                        (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                boolean isConnected = cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
                if (isConnected == false) {
                    myToats5.show();
                } else {
                    result = "";
                    new MyTask().execute();
                }
            }
        });
        final LinearLayout firstDay = (LinearLayout) rootView.findViewById(R.id.three_day_forecast_first);

        firstDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstDay.startAnimation(scale_Up);
                stopPlay();
                dataStorage.setExit(true);
                try {
                    Intent intent = new Intent(getActivity(), DetailFiveDayForecastActivity.class);
                    selected_tab = 0;
                    intent.putExtra(SELECTED_TAB, selected_tab);
                    intent.putExtra(ACTIVITY, ACTIVITY_VALUE);
                    if (dataStorage.getWhatUpdate() == false) {
                        intent.putExtra("cityTitle", dataStorage.getLastUpdatedCity());
                        intent.putExtra("fiveDayForecastResponse", dataStorage.getFiveDayForecastResponse());
                    } else {
                        intent.putExtra("cityTitle", dataStorage.getLastCurrentCity());
                        intent.putExtra("fiveDayForecastResponse", dataStorage.getCurrentFiveDayForecastResponse());
                    }
                    startActivity(intent);
                    Animatoo.animateZoom(getContext());
                    getActivity().finish();
                } catch (Exception ex) {
                }
            }
        });
        final LinearLayout secondDay = (LinearLayout) rootView.findViewById(R.id.three_day_forecast_second);
        secondDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                secondDay.startAnimation(scale_Up);
                stopPlay();
                dataStorage.setExit(true);
                try {
                    Intent intent = new Intent(getActivity(), DetailFiveDayForecastActivity.class);
                    selected_tab = 1;
                    intent.putExtra(SELECTED_TAB, selected_tab);
                    intent.putExtra(ACTIVITY, ACTIVITY_VALUE);
                    if (dataStorage.getWhatUpdate() == false) {
                        intent.putExtra("cityTitle", dataStorage.getLastUpdatedCity());
                        intent.putExtra("fiveDayForecastResponse", dataStorage.getFiveDayForecastResponse());
                    } else {
                        intent.putExtra("cityTitle", dataStorage.getLastCurrentCity());
                        intent.putExtra("fiveDayForecastResponse", dataStorage.getCurrentFiveDayForecastResponse());
                    }
                    startActivity(intent);
                    Animatoo.animateZoom(getContext());
                    getActivity().finish();
                } catch (Exception ex) {

                }
            }
        });
        final LinearLayout thirdDay = (LinearLayout) rootView.findViewById(R.id.three_day_forecast_third);

        thirdDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thirdDay.startAnimation(scale_Up);
                stopPlay();
                dataStorage.setExit(true);
                try {
                    Intent intent = new Intent(getActivity(), DetailFiveDayForecastActivity.class);
                    selected_tab = 2;
                    intent.putExtra(SELECTED_TAB, selected_tab);
                    intent.putExtra(ACTIVITY, ACTIVITY_VALUE);
                    if (dataStorage.getWhatUpdate() == false) {
                        intent.putExtra("cityTitle", dataStorage.getLastUpdatedCity());
                        intent.putExtra("fiveDayForecastResponse", dataStorage.getFiveDayForecastResponse());
                    } else {
                        intent.putExtra("cityTitle", dataStorage.getLastCurrentCity());
                        intent.putExtra("fiveDayForecastResponse", dataStorage.getCurrentFiveDayForecastResponse());
                    }

                    startActivity(intent);
                    Animatoo.animateZoom(getContext());
                    getActivity().finish();
                } catch (Exception ex) {

                }
            }
        });
        recyclerView = rootView.findViewById(R.id.recycle_view);
        changeLastUpdateCity = (TextView) rootView.findViewById(R.id.lastCity);
        changeLastUpdateCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataStorage.setWhatUpdate(false);
                stopPlay();
                long mSeconds = getCurrentDateMiliseconds() - dataStorage.getLastUpdatedDate();
                int newHours = (int) (mSeconds / (60 * 1000));
                if (newHours >= 15) {
                    updateWeatherData(dataStorage.getSearchCityLatitude(), dataStorage.getSearchCityLongitude());
                } else {
                    localStorage();
                }
            }
        });
        cityTitle = (TextView) rootView.findViewById(R.id.cityTitle);
        noGPSLayout = (LinearLayout) rootView.findViewById(R.id.no_gps_layout);
        accuweatherLogo = (LinearLayout) rootView.findViewById(R.id.linearlayout_logo);
        gridlayout = (GridLayout) rootView.findViewById(R.id.details);
        accuweatherLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent browserIntent = new
                            Intent(Intent.ACTION_VIEW, Uri.parse(accuWeatherLink));
                    startActivity(browserIntent);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        lastUpdate = (TextView) rootView.findViewById(R.id.lastUpdate);
        mainImage = (ImageView) rootView.findViewById(R.id.main_image);
        mainImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    mainImage.startAnimation(scale_Up);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    mainImage.startAnimation(scale_Down);
                }
                return true;
            }
        });
        weatherType = (TextView) rootView.findViewById(R.id.weather_type);
        currentDeegrees = (TextView) rootView.findViewById(R.id.currentDegrees);
        current_wind = (TextView) rootView.findViewById(R.id.current_wind);
        real_feel = (TextView) rootView.findViewById(R.id.real_feel);
        visibility = (TextView) rootView.findViewById(R.id.visibility);
        pressure = (TextView) rootView.findViewById(R.id.pressure);

        img_wind = (ImageView) rootView.findViewById(R.id.img_wind);
        img_real_feel = (ImageView) rootView.findViewById(R.id.img_real_feel);
        img_visibility = (ImageView) rootView.findViewById(R.id.img_visibility);
        img_pressure = (ImageView) rootView.findViewById(R.id.img_pressure);


        next_day_first_icon = (ImageView) rootView.findViewById(R.id.next_day_first_icon);
        next_day_first = (TextView) rootView.findViewById(R.id.next_day_first);
        next_day_first_type = (TextView) rootView.findViewById(R.id.next_day_first_type);
        next_day_first_temperature = (TextView) rootView.findViewById(R.id.next_day_first_temperature);

        next_day_second_icon = (ImageView) rootView.findViewById(R.id.next_day_second_icon);
        next_day_second = (TextView) rootView.findViewById(R.id.next_day_second);
        next_day_second_type = (TextView) rootView.findViewById(R.id.next_day_second_type);
        next_day_second_temperature = (TextView) rootView.findViewById(R.id.next_day_second_temperature);

        next_day_third_icon = (ImageView) rootView.findViewById(R.id.next_day_third_icon);
        next_day_third = (TextView) rootView.findViewById(R.id.next_day_third);
        next_day_third_type = (TextView) rootView.findViewById(R.id.next_day_third_type);
        next_day_third_temperature = (TextView) rootView.findViewById(R.id.next_day_third_temperature);

        separator_1 = (TextView) rootView.findViewById(R.id.separator_1);
        separator_2 = (TextView) rootView.findViewById(R.id.separator_2);
        separator_3 = (TextView) rootView.findViewById(R.id.separator_3);

        sunRise = (TextView) rootView.findViewById(R.id.sunrise);
        sunSet = (TextView) rootView.findViewById(R.id.sunset);

        rain = (TextView) rootView.findViewById(R.id.rain);
        thunder = (TextView) rootView.findViewById(R.id.thunder);
        snow = (TextView) rootView.findViewById(R.id.snow);

        sunriseIcon = (ImageView) rootView.findViewById(R.id.sunrise_icon);
        sunsetIcon = (ImageView) rootView.findViewById(R.id.sunset_icon);

        details = (GridLayout) rootView.findViewById(R.id.details);
        mainBackgroundImage = (ImageView) rootView.findViewById(R.id.main_background_image);


        threeDots = (TextView) rootView.findViewById(R.id.three_dots);
        threeDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                threeDots.setTextColor(getResources().getColor(R.color.grey));
                showPopupMenu(view);
            }
        });
        View layouttoast = inflater.inflate(R.layout.custom_toast, (ViewGroup) rootView.findViewById(R.id.toast_layout));
        ((TextView) layouttoast.findViewById(R.id.toast_text)).setText(R.string.city_not_found);
        toastText = (TextView) rootView.findViewById(R.id.toast_text);
        myToats1 = new Toast(getContext());
        myToats1.setView(layouttoast);
        myToats1.setGravity(Gravity.TOP, 0, 0);
        myToats1.setDuration(Toast.LENGTH_SHORT);

        View layouttoast2 = inflater.inflate(R.layout.custom_toast2, (ViewGroup) rootView.findViewById(R.id.toast_2_layout));
        ((TextView) layouttoast2.findViewById(R.id.toast_2_text)).setText(R.string.number_of_requests_has_been_exceeded);
        toastText = (TextView) rootView.findViewById(R.id.toast_2_text);
        myToats2 = new Toast(getContext());
        myToats2.setView(layouttoast2);
        myToats2.setGravity(Gravity.TOP, 0, 0);
        myToats2.setDuration(Toast.LENGTH_SHORT);

        View layouttoast3 = inflater.inflate(R.layout.custom_toast3, (ViewGroup) rootView.findViewById(R.id.toast_3_layout));
        ((TextView) layouttoast3.findViewById(R.id.toast_3_text)).setText(R.string.updating);
        toastText = (TextView) rootView.findViewById(R.id.toast_3_text);
        myToats3 = new Toast(getContext());
        myToats3.setView(layouttoast3);
        myToats3.setGravity(Gravity.TOP, 0, 0);
        myToats3.setDuration(Toast.LENGTH_SHORT);

        View layouttoast4 = inflater.inflate(R.layout.custom_toast4, (ViewGroup) rootView.findViewById(R.id.toast_4_layout));
        ((TextView) layouttoast4.findViewById(R.id.toast_4_text)).setText(R.string.data_updated);
        toastText = (TextView) rootView.findViewById(R.id.toast_4_text);
        myToats4 = new Toast(getContext());
        myToats4.setView(layouttoast4);
        myToats4.setGravity(Gravity.TOP, 0, 0);
        myToats4.setDuration(Toast.LENGTH_SHORT);

        View layouttoast5 = inflater.inflate(R.layout.custom_toast5, (ViewGroup) rootView.findViewById(R.id.toast_5_layout));
        ((TextView) layouttoast5.findViewById(R.id.toast_5_text)).setText(R.string.internet_connection);
        toastText = (TextView) rootView.findViewById(R.id.toast_5_text);
        myToats5 = new Toast(getContext());
        myToats5.setView(layouttoast5);
        myToats5.setGravity(Gravity.TOP, 0, 0);
        myToats5.setDuration(Toast.LENGTH_SHORT);

        mSwipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
        mSwipeRefresh.setColorSchemeResources(R.color.green, R.color.ligth_blue, R.color.purple, R.color.orange, R.color.purple, R.color.ligth_blue);
        mSwipeRefresh.setProgressBackgroundColorSchemeResource(R.color.btn_main);
        scrollView = (ScrollView) rootView.findViewById(R.id.scroll);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (scrollView != null) {
                    if (scrollView.getScrollY() == 0) {
                        mSwipeRefresh.setEnabled(true);
                        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {
                                dataStorage.setWhatUpdate(true);
                                localStorage();
                                ConnectivityManager cm =
                                        (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                                boolean isConnected = cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
                                Log.i("mSwipeRefresh", "mSwipeRefresh - " + isConnected);
                                if (isConnected == false) {
                                    mSwipeRefresh.setRefreshing(false);
                                    myToats5.show();
                                } else {
                                    startUpdateWeather();
                                }
                            }
                        });
                    } else {
                        mSwipeRefresh.setEnabled(false);
                        mSwipeRefresh.setRefreshing(false);

                    }
                }
            }
        });
        fiveDayBtn = (Button) rootView.findViewById(R.id.five_day_button);
        fiveDayBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    fiveDayBtn.startAnimation(scale_Up);
                    stopPlay();
                    dataStorage.setExit(true);
                    try {
                        Intent intent = new Intent(getActivity(), FiveDayForecastActivity.class);
                        if (dataStorage.getWhatUpdate() == false) {
                            intent.putExtra("cityTitle", dataStorage.getLastUpdatedCity());
                            intent.putExtra("fiveDayForecastResponse", dataStorage.getFiveDayForecastResponse());
                        } else {
                            intent.putExtra("cityTitle", dataStorage.getLastCurrentCity());
                            intent.putExtra("fiveDayForecastResponse", dataStorage.getCurrentFiveDayForecastResponse());
                        }
                        startActivity(intent);
                        Animatoo.animateFade(getContext());
                        getActivity().finish();
                    } catch (Exception ex) {

                    }
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    fiveDayBtn.startAnimation(scale_Down);
                }
                return true;
            }
        });
        Log.i("DATASTOTAGE", "DATASTOTAGE" + dataStorage.getExit());
        if (dataStorage.getLastUpdatedCity().isEmpty() && dataStorage.getLastCurrentCity().isEmpty()) {
            fiveDayBtn.setVisibility(View.INVISIBLE);
            accuweatherLogo.setVisibility(View.INVISIBLE);
            gridlayout.setVisibility(View.INVISIBLE);
            sunriseIcon.setVisibility(View.INVISIBLE);
            sunsetIcon.setVisibility(View.INVISIBLE);
            LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                noGPSLayout.setVisibility(View.VISIBLE);
            } else {
                noGPSLayout.setVisibility(View.GONE);
            }
            rootView.findViewById(R.id.refresh).setVisibility(View.INVISIBLE);
            mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    dataStorage.setWhatUpdate(true);
                    localStorage();
                    ConnectivityManager cm =
                            (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    boolean isConnected = cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
                    Log.i("mSwipeRefresh", "mSwipeRefresh BEGIN - " + isConnected);
                    if (isConnected == false) {
                        mSwipeRefresh.setRefreshing(false);
                        myToats5.show();
                    } else {
                        startUpdateWeather();
                    }
                }
            });
        } else if (!dataStorage.getLastUpdatedCity().isEmpty() || !dataStorage.getLastCurrentCity().isEmpty()) {
            fiveDayBtn.setVisibility(View.VISIBLE);
            accuweatherLogo.setVisibility(View.VISIBLE);
            gridlayout.setVisibility(View.VISIBLE);
            sunriseIcon.setVisibility(View.VISIBLE);
            sunsetIcon.setVisibility(View.VISIBLE);
            LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                noGPSLayout.setVisibility(View.VISIBLE);
            } else {
                noGPSLayout.setVisibility(View.GONE);
            }
            stopPlay();
            rootView.findViewById(R.id.refresh).setVisibility(View.VISIBLE);
            if (dataStorage.getWhatUpdate() == true) {
                long milliseconds = getCurrentDateMiliseconds() - dataStorage.getLastCurrentUpdateDate();
                int hours = (int) (milliseconds / (60 * 1000));
                if (hours >= 15) {
                    ConnectivityManager cm =
                            (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    boolean isConnected = cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
                    if (isConnected) {
                        if (ContextCompat.checkSelfPermission(getContext(),
                                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                                ContextCompat.checkSelfPermission(getContext(),
                                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                                ActivityCompat.checkSelfPermission(getContext(),
                                        Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                noGPSLayout.setVisibility(View.VISIBLE);
                                if (dataStorage.getLatitude() != null && dataStorage.getLongitude() != null)
                                    if (!dataStorage.getLatitude().equals("") && !dataStorage.getLongitude().equals("")) {
                                        mSwipeRefresh.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                mSwipeRefresh.setRefreshing(true);
                                                needToUpdateCurrentLocation(dataStorage.getLatitude(), dataStorage.getLongitude());
                                            }
                                        });
                                    }
                            } else {
                                noGPSLayout.setVisibility(View.GONE);
                                mSwipeRefresh.post(new Runnable() {
                                    @SuppressLint("MissingPermission")
                                    @Override
                                    public void run() {
                                        mSwipeRefresh.setRefreshing(true);
                                        mLocationRequest = LocationRequest.create();
                                        mLocationRequest.setNumUpdates(1);
                                        mLocationRequest.setInterval(10000);
                                        mLocationRequest.setFastestInterval(2000);
                                        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                        mLocationCallback = new LocationCallback() {
                                            @Override
                                            public void onLocationResult(LocationResult locationResult) {
                                                if (locationResult == null) {
                                                    return;
                                                }
                                                for (Location location : locationResult.getLocations()) {
                                                    if (location != null) {
                                                        try {
                                                            currentLatitude = Double.toString(location.getLatitude());
                                                            currentLongitude = Double.toString(location.getLongitude());
                                                            dataStorage.setLatitude(currentLatitude);
                                                            dataStorage.setLongitude(currentLongitude);
                                                            resultLocation = "";
                                                            needToUpdateCurrentLocation(dataStorage.getLatitude(), dataStorage.getLongitude());
                                                            LocationServices.getFusedLocationProviderClient(getContext()).removeLocationUpdates(mLocationCallback);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
                                            }
                                        };
                                        LocationServices.getFusedLocationProviderClient(getContext()).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
                                    }
                                });
                            }
                        } else {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                                            Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 1000);
                            mSwipeRefresh.setRefreshing(false);
                        }
                    } else {
                        localStorage();
                    }
                } else {
                    localStorage();
                }
            } else {
                noGPSLayout.setVisibility(View.GONE);
                long mSeconds = getCurrentDateMiliseconds() - dataStorage.getLastUpdatedDate();
                int newHours = (int) (mSeconds / (60 * 1000));
                if (newHours >= 15) {
                    ConnectivityManager cm =
                            (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    boolean isConnected = cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
                    if (isConnected == true) {
                        mSwipeRefresh.post(new Runnable() {
                            @Override
                            public void run() {
                                mSwipeRefresh.setRefreshing(true);
                                updateWeatherData(dataStorage.getSearchCityLatitude(), dataStorage.getSearchCityLongitude());
                            }
                        });
                    } else {
                        localStorage();
                    }
                } else {
                    localStorage();
                }
            }
        }
        setRetainInstance(true);
        return rootView;
    }

    private void startUpdateWeather() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            noGPSLayout.setVisibility(View.GONE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                if (!dataStorage.getLastCurrentCity().isEmpty()) {
                    noGPSLayout.setVisibility(View.VISIBLE);
                }
                mSwipeRefresh.setRefreshing(false);
                buildAlertMessageNoGps();
            }
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                mLocationRequest = LocationRequest.create();
                mLocationRequest.setNumUpdates(1);
                mLocationRequest.setInterval(10000);
                mLocationRequest.setFastestInterval(2000);
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                mLocationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        if (locationResult == null) {
                            return;
                        }
                        for (Location location : locationResult.getLocations()) {
                            if (location != null) {
                                try {
                                    currentLatitude = Double.toString(location.getLatitude());
                                    currentLongitude = Double.toString(location.getLongitude());
                                    dataStorage.setLatitude(currentLatitude);
                                    dataStorage.setLongitude(currentLongitude);
                                    resultLocation = "";
                                    Log.i("resultLocation", "-----------------------------");
                                    dataStorage.setWhatUpdate(true);
                                    new MyTask().execute();
                                    noGPSLayout.setVisibility(View.GONE);
                                    LocationServices.getFusedLocationProviderClient(getContext()).removeLocationUpdates(mLocationCallback);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                };
                LocationServices.getFusedLocationProviderClient(getContext()).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 1000);
            mSwipeRefresh.setRefreshing(false);
        }
    }

    private void buildAlertMessageNoGps() {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setMessage(getString(R.string.gps_message))
                .setCancelable(false)
                .setTitle(getString(R.string.gps_title))
                .setIcon(R.drawable.gps_icon)
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialogResult = true;
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialogResult = false;
                        dialog.cancel();
                        if (dataStorage.getLatitude() != null && dataStorage.getLongitude() != null)
                            if (!dataStorage.getLatitude().equals("") && !dataStorage.getLongitude().equals("")) {
                                mSwipeRefresh.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mSwipeRefresh.setRefreshing(true);
                                        needToUpdateCurrentLocation(dataStorage.getLatitude(), dataStorage.getLongitude());
                                    }
                                });
                            }
                    }
                });
        final android.app.AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void iTemClicked(View v, int position) {
        v.startAnimation(scale_Up);
        stopPlay();
        dataStorage.setExit(true);
        try {
            Intent intent = new Intent(getActivity(), HourlyDetailActivity.class);
            intent.putExtra("Position", position);
            if (dataStorage.getWhatUpdate() == false) {
                intent.putExtra("hourlyForecastResponse", dataStorage.getHourlyResponse());
            } else {
                intent.putExtra("hourlyForecastResponse", dataStorage.getCurrentHourlyResponse());
            }
            startActivity(intent);
            Animatoo.animateDiagonal(getContext());
            getActivity().finish();
        } catch (Exception ex) {

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
            }
        } catch (Exception ex) {
            Log.i("onPause()", "onPause()" + ex);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            localStorage();
        } catch (Exception ex) {
            Log.i("onPause()", "onPause()" + ex);
        }
    }

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataStorage = new DataStorage(getActivity());
        constants = new Constants();
        audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
    }

    class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            myToats3.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                ConnectivityManager cm =
                        (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                boolean isConnected = cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
                if (isConnected == false) {
                    myToats5.show();
                    mSwipeRefresh.setRefreshing(false);
                } else {
                    if (dataStorage.getWhatUpdate() == true) {
                        updateCurrentLocation(dataStorage.getLatitude(), dataStorage.getLongitude());
                    } else {
                        noGPSLayout.setVisibility(View.GONE);
                        updateWeatherData(dataStorage.getSearchCityLatitude(), dataStorage.getSearchCityLongitude());
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }
    }


    private void updateWeatherData(final String latitude, final String longitude) {
        new Thread() {
            public void run() {
                try {
                    URL cityUrl = new CityConnection().buildUrlForCity(latitude, longitude);
                    result = new CityConnection().getResponseForCityFromHttpUrl(cityUrl);
                    if (result == null || result.equals("ExceptionHappened")) {
                        handler.post(new Runnable() {
                            public void run() {
                                localStorage();
                                myToats2.show();
                                mSwipeRefresh.setRefreshing(false);
                            }
                        });
                    } else if (result.equals("[]")) {
                        handler.post(new Runnable() {
                            public void run() {
                                myToats1.show();
                                mSwipeRefresh.setRefreshing(false);
                            }
                        });
                    } else {
                        handler.post(new Runnable() {
                            public void run() {
                                localStorage();
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    key = jsonObject.getString("Key");
                                    if (isSelected) {
                                        String[] array = searchCityArrayList.get(selectedCityName).getDisplayName().split("[, ]+");
                                        currentCity = array[0];
                                    } else {
                                        if (!dataStorage.getLastUpdatedCity().isEmpty()) {
                                            currentCity = dataStorage.getLastUpdatedCity();
                                        }
                                    }
                                    if (key != null && !currentCity.equals("")) {
                                        new Thread() {
                                            public void run() {
                                                try {
                                                    dataStorage.setLastUpdatedCity(currentCity);
                                                    dataStorage.setSearchCityLatitude(latitude);
                                                    dataStorage.setSearchCityLongitude(longitude);
                                                    URL weatherURL = new ForecastConnection().buildUrlForWeather(key);
                                                    weatherResponse = new ForecastConnection().getResponseWeather(weatherURL);
                                                    if (weatherResponse == null || weatherResponse.equals("") ||
                                                            weatherResponse.equals("[]") || weatherResponse.equals("ExceptionHappened")) {
                                                        handler.post(new Runnable() {
                                                            public void run() {
                                                                myToats1.show();
                                                            }
                                                        });
                                                    } else {
                                                        handler.post(new Runnable() {
                                                            public void run() {
                                                                try {
                                                                    dataStorage.setWhatUpdate(false);
                                                                    dataStorage.setWeatherResponse(weatherResponse);
                                                                    if (key != null) {
                                                                        new Thread() {
                                                                            public void run() {
                                                                                try {
                                                                                    URL hourlyURL = new HourlyConnection().buildUrlForHourlyWeather(key);
                                                                                    hourlyResponse = new HourlyConnection().getResponseHourlyWeather(hourlyURL);
                                                                                    if (hourlyResponse == null || hourlyResponse.equals("") ||
                                                                                            hourlyResponse.equals("[]") || hourlyResponse.equals("ExceptionHappened")) {
                                                                                        handler.post(new Runnable() {
                                                                                            public void run() {
                                                                                                myToats1.show();
                                                                                            }
                                                                                        });
                                                                                    } else {
                                                                                        handler.post(new Runnable() {
                                                                                            public void run() {
                                                                                                try {
                                                                                                    setInitialData(hourlyResponse);
                                                                                                    dataStorage.setHourlyResponse(hourlyResponse);//
                                                                                                    adapter = new HourlyRecycleViewAdapter(getContext(), hourlyList, Weather_Fragment.this);
                                                                                                    recyclerView.setAdapter(adapter);
                                                                                                    if (key != null) {
                                                                                                        new Thread() {
                                                                                                            public void run() {
                                                                                                                try {
                                                                                                                    URL fiveDayURL = new FiveDayForecastConnection().buildUrlForFiveDayForecast(key);
                                                                                                                    fiveDayForecastResponse = new FiveDayForecastConnection().getResponseForFiveDay(fiveDayURL);
                                                                                                                    if (fiveDayForecastResponse == null || fiveDayForecastResponse.equals("") ||
                                                                                                                            fiveDayForecastResponse.equals("[]") || fiveDayForecastResponse.equals("ExceptionHappened")) {
                                                                                                                        handler.post(new Runnable() {
                                                                                                                            public void run() {
                                                                                                                                myToats1.show();
                                                                                                                            }
                                                                                                                        });
                                                                                                                    } else {
                                                                                                                        handler.post(new Runnable() {
                                                                                                                            public void run() {
                                                                                                                                try {
                                                                                                                                    dataStorage.setFiveDayForecastResponse(fiveDayForecastResponse);
                                                                                                                                    dataStorage.setLastUpdateWeather(lastUpdateDate());
                                                                                                                                    dataStorage.setLastUpdatedDate(getLastUpdateMilisecond());
                                                                                                                                    lastUpdate.setText(getString(R.string.last_update, dataStorage.getLastUpdateWeather()));
                                                                                                                                    renderWeather(weatherResponse);
                                                                                                                                    renderFiveDayForecast(fiveDayForecastResponse);
                                                                                                                                    mSwipeRefresh.setRefreshing(false);
                                                                                                                                    rootView.findViewById(R.id.refresh).setVisibility(View.VISIBLE);
                                                                                                                                    fiveDayBtn.setVisibility(View.VISIBLE);
                                                                                                                                    accuweatherLogo.setVisibility(View.VISIBLE);
                                                                                                                                    gridlayout.setVisibility(View.VISIBLE);
                                                                                                                                    sunriseIcon.setVisibility(View.VISIBLE);
                                                                                                                                    sunsetIcon.setVisibility(View.VISIBLE);
                                                                                                                                    noGPSLayout.setVisibility(View.GONE);
                                                                                                                                    myToats4.show();
                                                                                                                                } catch (Exception ex) {
                                                                                                                                }
                                                                                                                            }
                                                                                                                        });
                                                                                                                    }
                                                                                                                } catch (Exception ex) {
                                                                                                                }
                                                                                                            }
                                                                                                        }.start();
                                                                                                    }
                                                                                                } catch (Exception ex) {
                                                                                                }
                                                                                            }
                                                                                        });
                                                                                    }
                                                                                } catch (Exception ex) {
                                                                                }
                                                                            }
                                                                        }.start();
                                                                    }
                                                                } catch (Exception ex) {
                                                                }
                                                            }
                                                        });
                                                    }
                                                } catch (Exception ex) {
                                                }
                                            }
                                        }.start();
                                    }
                                } catch (Exception e) {
                                }
                            }
                        });
                    }
                } catch (Exception ex) {
                }
            }
        }.start();
    }

    private void updateCurrentLocation(final String latitude, final String longitude) {

        new Thread() {
            public void run() {
                try {
                    URL latitudeLongitudeURL = new LatitudeLongitudeConnection().buildUrlForLatitudeLongitude(latitude, longitude);
                    resultLocation = new LatitudeLongitudeConnection().getResponseForLatitudeLongitudeFromHttpUrl(latitudeLongitudeURL);
                    Log.i(TAG, "latitudeLongitudeURL" + latitudeLongitudeURL);
                    Log.i(TAG, "resultLocation: " + resultLocation);

                    Log.i(TAG, "latitude = **************" + dataStorage.getLatitude());
                    Log.i(TAG, "longitude = *********** " + dataStorage.getLongitude());
                    if (resultLocation == null || resultLocation.equals("ExceptionHappened")) {
                        handler.post(new Runnable() {
                            public void run() {
                                localStorage();
                                myToats2.show();
                            }
                        });
                    } else if (resultLocation.equals("[]")) {
                        handler.post(new Runnable() {
                            public void run() {
                                myToats1.show();
                            }
                        });
                    } else {
                        handler.post(new Runnable() {
                            public void run() {
                                localStorage();
                                try {
                                    JSONArray jsonarray = new JSONArray(resultLocation);
                                    JSONObject jsonobject = jsonarray.getJSONObject(0);
                                    resultLocationKey = jsonobject.getString("Key");
                                    currentLocationCity = jsonobject.getString("LocalizedName");
                                    if (resultLocationKey != null && !currentLocationCity.equals("")) {
                                        new Thread() {
                                            public void run() {
                                                try {
                                                    dataStorage.setLastCurrentCity(currentLocationCity);
                                                    URL currentWeatherURL = new ForecastConnection().buildUrlForWeather(resultLocationKey);
                                                    currentWeatherResponse = new ForecastConnection().getResponseWeather(currentWeatherURL);
                                                    Log.i(TAG, "currentWeatherURL" + currentWeatherURL);
                                                    Log.i(TAG, "currentWeatherResponse: " + currentWeatherResponse);
                                                    if (currentWeatherResponse == null || currentWeatherResponse.equals("") ||
                                                            currentWeatherResponse.equals("[]") || currentWeatherResponse.equals("ExceptionHappened")) {
                                                        handler.post(new Runnable() {
                                                            public void run() {
                                                                myToats1.show();
                                                                mSwipeRefresh.setRefreshing(false);
                                                            }
                                                        });
                                                    } else {
                                                        handler.post(new Runnable() {
                                                            public void run() {
                                                                try {
                                                                    dataStorage.setWhatUpdate(true);
                                                                    dataStorage.setCurrentWeatherResponse(currentWeatherResponse);
                                                                    if (resultLocationKey != null) {
                                                                        new Thread() {
                                                                            public void run() {
                                                                                try {
                                                                                    URL currentHourlyURL = new HourlyConnection().buildUrlForHourlyWeather(resultLocationKey);
                                                                                    currentHourlyResponse = new HourlyConnection().getResponseHourlyWeather(currentHourlyURL);
                                                                                    Log.i(TAG, "currentHourlyResponse" + currentHourlyResponse);
                                                                                    if (currentHourlyResponse == null || currentHourlyResponse.equals("") ||
                                                                                            currentHourlyResponse.equals("[]") || currentHourlyResponse.equals("ExceptionHappened")) {
                                                                                        handler.post(new Runnable() {
                                                                                            public void run() {
                                                                                                myToats1.show();
                                                                                                mSwipeRefresh.setRefreshing(false);
                                                                                            }
                                                                                        });
                                                                                    } else {
                                                                                        handler.post(new Runnable() {
                                                                                            public void run() {
                                                                                                try {
                                                                                                    setInitialData(currentHourlyResponse);
                                                                                                    dataStorage.setCurrentHourlyResponse(currentHourlyResponse);
                                                                                                    adapter = new HourlyRecycleViewAdapter(getContext(), hourlyList, Weather_Fragment.this);
                                                                                                    recyclerView.setAdapter(adapter);
                                                                                                    if (resultLocationKey != null) {
                                                                                                        new Thread() {
                                                                                                            public void run() {
                                                                                                                try {
                                                                                                                    URL currentFiveDayURL = new FiveDayForecastConnection().buildUrlForFiveDayForecast(resultLocationKey);
                                                                                                                    currentFiveDayForecastResponse = new FiveDayForecastConnection().getResponseForFiveDay(currentFiveDayURL);
                                                                                                                    Log.i(TAG, "currentFiveDayURL" + currentFiveDayURL);
                                                                                                                    Log.i(TAG, "currentFiveDayForecastResponse: " + currentFiveDayForecastResponse);
                                                                                                                    if (currentFiveDayForecastResponse == null || currentFiveDayForecastResponse.equals("") ||
                                                                                                                            currentFiveDayForecastResponse.equals("[]") || currentFiveDayForecastResponse.equals("ExceptionHappened")) {
                                                                                                                        handler.post(new Runnable() {
                                                                                                                            public void run() {
                                                                                                                                myToats1.show();
                                                                                                                                mSwipeRefresh.setRefreshing(false);
                                                                                                                            }
                                                                                                                        });
                                                                                                                    } else {
                                                                                                                        handler.post(new Runnable() {
                                                                                                                            public void run() {
                                                                                                                                try {
                                                                                                                                    dataStorage.setCurrentFiveDayForecastResponse(currentFiveDayForecastResponse);
                                                                                              
                                                                                                                                    dataStorage.setLastCurrentUpdateWeather(lastUpdateDate());
                                                                                                                                    dataStorage.setLastCurrentUpdateDate(getLastUpdateMilisecond());
                                                                                                                                    renderWeather(currentWeatherResponse);
                                                                                                                                    renderFiveDayForecast(currentFiveDayForecastResponse);
                                                                                                                                    rootView.findViewById(R.id.refresh).setVisibility(View.INVISIBLE);
                                                                                                                                    changeLastUpdateCity.setVisibility(View.VISIBLE);
                                                                                                                                    if (!dataStorage.getLastUpdatedCity().isEmpty()) {
                                                                                                                                        changeLastUpdateCity.setText(dataStorage.getLastUpdatedCity());
                                                                                                                                    }                                                                                                                                    
                                                                                                                                    mSwipeRefresh.setRefreshing(false);
                                                                                                                                    fiveDayBtn.setVisibility(View.VISIBLE);
                                                                                                                                    accuweatherLogo.setVisibility(View.VISIBLE);
                                                                                                                                    gridlayout.setVisibility(View.VISIBLE);
                                                                                                                                    sunriseIcon.setVisibility(View.VISIBLE);
                                                                                                                                    sunsetIcon.setVisibility(View.VISIBLE);
                                                                                                                                    myToats4.show();                                                                               

                                                                                                                                } catch (Exception ex) {
                                                                                                                                }
                                                                                                                            }
                                                                                                                        });
                                                                                                                    }
                                                                                                                } catch (Exception ex) {

                                                                                                                }
                                                                                                            }
                                                                                                        }.start();
                                                                                                    }
                                                                                                } catch (Exception ex) {
                                                                                                }

                                                                                            }
                                                                                        });
                                                                                    }
                                                                                } catch (Exception ex) {

                                                                                }
                                                                            }
                                                                        }.start();
                                                                    }
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
                                } catch (Exception e) {
                                }
                            }
                        });
                    }
                } catch (Exception ex) {
                }
            }
        }.start();
    }


    private void needToUpdateCurrentLocation(final String latitude, final String longitude) {
        new Thread() {
            public void run() {
                try {
                    URL latitudeLongitudeURL = new LatitudeLongitudeConnection().buildUrlForLatitudeLongitude(latitude, longitude);
                    resultLocation = new LatitudeLongitudeConnection().getResponseForLatitudeLongitudeFromHttpUrl(latitudeLongitudeURL);
                    Log.i(TAG, "latitudeLongitudeURL" + latitudeLongitudeURL);
                    Log.i(TAG, "resultLocation: " + resultLocation);

                    Log.i(TAG, "latitude = **************" + dataStorage.getLatitude());
                    Log.i(TAG, "longitude = *********** " + dataStorage.getLongitude());
                    if (resultLocation == null || resultLocation.equals("ExceptionHappened")) {
                        handler.post(new Runnable() {
                            public void run() {
                                localStorage();
                                myToats2.show();
                                mSwipeRefresh.setRefreshing(false);
                            }
                        });
                    } else if (resultLocation.equals("[]")) {
                        handler.post(new Runnable() {
                            public void run() {
                                myToats1.show();
                                mSwipeRefresh.setRefreshing(false);
                            }
                        });
                    } else {
                        handler.post(new Runnable() {
                            public void run() {
                                localStorage();
                                try {
                                    JSONArray jsonarray = new JSONArray(resultLocation);
                                    JSONObject jsonobject = jsonarray.getJSONObject(0);
                                    resultLocationKey = jsonobject.getString("Key");
                                    currentLocationCity = jsonobject.getString("LocalizedName");
                                    if (resultLocationKey != null && !currentLocationCity.equals("")) {
                                        new Thread() {
                                            public void run() {
                                                try {
                                                    dataStorage.setLastCurrentCity(currentLocationCity);
                                                    URL currentWeatherURL = new ForecastConnection().buildUrlForWeather(resultLocationKey);
                                                    currentWeatherResponse = new ForecastConnection().getResponseWeather(currentWeatherURL);
                                                    Log.i(TAG, "currentWeatherURL" + currentWeatherURL);
                                                    Log.i(TAG, "currentWeatherResponse: " + currentWeatherResponse);
                                                    if (currentWeatherResponse == null || currentWeatherResponse.equals("") ||
                                                            currentWeatherResponse.equals("[]") || currentWeatherResponse.equals("ExceptionHappened")) {
                                                        handler.post(new Runnable() {
                                                            public void run() {
                                                                myToats1.show();
                                                                mSwipeRefresh.setRefreshing(false);
                                                            }
                                                        });
                                                    } else {
                                                        handler.post(new Runnable() {
                                                            public void run() {
                                                                try {
                                                                    dataStorage.setWhatUpdate(true);
                                                                    dataStorage.setCurrentWeatherResponse(currentWeatherResponse);
                                                                    if (resultLocationKey != null) {
                                                                        new Thread() {
                                                                            public void run() {
                                                                                try {
                                                                                    URL currentHourlyURL = new HourlyConnection().buildUrlForHourlyWeather(resultLocationKey);
                                                                                    currentHourlyResponse = new HourlyConnection().getResponseHourlyWeather(currentHourlyURL);
                                                                                    Log.i(TAG, "currentHourlyResponse" + currentHourlyResponse);
                                                                                    if (currentHourlyResponse == null || currentHourlyResponse.equals("") ||
                                                                                            currentHourlyResponse.equals("[]") || currentHourlyResponse.equals("ExceptionHappened")) {
                                                                                        handler.post(new Runnable() {
                                                                                            public void run() {
                                                                                                myToats1.show();
                                                                                                mSwipeRefresh.setRefreshing(false);
                                                                                            }
                                                                                        });
                                                                                    } else {
                                                                                        handler.post(new Runnable() {
                                                                                            public void run() {
                                                                                                try {
                                                                                                    setInitialData(currentHourlyResponse);
                                                                                                    dataStorage.setCurrentHourlyResponse(currentHourlyResponse);
                                                                                                    adapter = new HourlyRecycleViewAdapter(getContext(), hourlyList, Weather_Fragment.this);
                                                                                                    recyclerView.setAdapter(adapter);
                                                                                                    if (resultLocationKey != null) {
                                                                                                        new Thread() {
                                                                                                            public void run() {
                                                                                                                try {
                                                                                                                    URL currentFiveDayURL = new FiveDayForecastConnection().buildUrlForFiveDayForecast(resultLocationKey);
                                                                                                                    currentFiveDayForecastResponse = new FiveDayForecastConnection().getResponseForFiveDay(currentFiveDayURL);
                                                                                                                    Log.i(TAG, "currentFiveDayURL" + currentFiveDayURL);
                                                                                                                    Log.i(TAG, "currentFiveDayForecastResponse: " + currentFiveDayForecastResponse);
                                                                                                                    if (currentFiveDayForecastResponse == null || currentFiveDayForecastResponse.equals("") ||
                                                                                                                            currentFiveDayForecastResponse.equals("[]") || currentFiveDayForecastResponse.equals("ExceptionHappened")) {
                                                                                                                        handler.post(new Runnable() {
                                                                                                                            public void run() {
                                                                                                                                myToats1.show();
                                                                                                                                mSwipeRefresh.setRefreshing(false);
                                                                                                                            }
                                                                                                                        });
                                                                                                                    } else {
                                                                                                                        handler.post(new Runnable() {
                                                                                                                            public void run() {
                                                                                                                                try {
                                                                                                                                    dataStorage.setCurrentFiveDayForecastResponse(currentFiveDayForecastResponse);                                                                                                                                    
                                                                                                                                    dataStorage.setLastCurrentUpdateWeather(lastUpdateDate());
                                                                                                                                    dataStorage.setLastCurrentUpdateDate(getLastUpdateMilisecond());
                                                                                                                                    renderWeather(currentWeatherResponse);
                                                                                                                                    renderFiveDayForecast(currentFiveDayForecastResponse);                                                                                                                                  
                                                                                                                                    mSwipeRefresh.setRefreshing(false);
                                                                                                                                    fiveDayBtn.setVisibility(View.VISIBLE);
                                                                                                                                    accuweatherLogo.setVisibility(View.VISIBLE);
                                                                                                                                    sunriseIcon.setVisibility(View.VISIBLE);
                                                                                                                                    sunsetIcon.setVisibility(View.VISIBLE);
                                                                                                                                    gridlayout.setVisibility(View.VISIBLE);
                                                                                                                                    myToats4.show();                                                                                                                                   

                                                                                                                                } catch (Exception ex) {
                                                                                                                                }
                                                                                                                            }
                                                                                                                        });
                                                                                                                    }
                                                                                                                } catch (Exception ex) {

                                                                                                                }
                                                                                                            }
                                                                                                        }.start();
                                                                                                    }
                                                                                                } catch (Exception ex) {
                                                                                                }

                                                                                            }
                                                                                        });
                                                                                    }
                                                                                } catch (Exception ex) {

                                                                                }
                                                                            }
                                                                        }.start();
                                                                    }
                                                                } catch (Exception ex) {
                                                                }
                                                            }
                                                        });
                                                    }
                                                } catch (Exception ex) {
                                                }
                                            }
                                        }.start();
                                    }
                                } catch (Exception e) {
                                }
                            }
                        });
                    }
                } catch (Exception ex) {
                }
            }
        }.start();
    }


    public void setInitialData(String hourlyResponse) {
        try {
            int icon = 0;
            String wind = "";
            if (hourlyList != null) {
                hourlyList.clear();
            }
            JSONArray mJsonArray = new JSONArray(hourlyResponse);
            for (int i = 0; i < 12; i++) {
                JSONObject mJsonObject = mJsonArray.getJSONObject(i);
                String resultTime = mJsonObject.getString("DateTime");
                String hours = getHourlyTime(resultTime);
                String temperature = Integer.toString((int) mJsonObject.getJSONObject("Temperature").getDouble("Value")) + "°";
                int weatherIcon = mJsonObject.getInt("WeatherIcon");
                switch (weatherIcon) {
                    case 1:
                        icon = R.drawable.sunny;
                        break;
                    case 2:
                        icon = R.drawable.mostly_sunny;
                        break;
                    case 3:
                        icon = R.drawable.partly_sunny;
                        break;
                    case 4:
                        icon = R.drawable.intermittent_clouds;
                        break;
                    case 5:
                        icon = R.drawable.hazy_sunshine;
                        break;
                    case 6:
                        icon = R.drawable.mostly_cloudy;
                        break;
                    case 7:
                        icon = R.drawable.cloudy;
                        break;
                    case 8:
                        icon = R.drawable.dreary;
                        break;
                    case 11:
                        icon = R.drawable.fog;
                        break;
                    case 12:
                        icon = R.drawable.showers;
                        break;
                    case 13:
                        icon = R.drawable.mostly_cloudy_showers;
                        break;
                    case 14:
                        icon = R.drawable.partly_sunny_showers;
                        break;
                    case 15:
                        icon = R.drawable.t_storms;
                        break;
                    case 16:
                        icon = R.drawable.mostly_cloudy_t_storms;
                        break;
                    case 17:
                        icon = R.drawable.partly_sunny_t_storms;
                        break;
                    case 18:
                        icon = R.drawable.rain;
                        break;
                    case 19:
                        icon = R.drawable.flurries;
                        break;
                    case 20:
                        icon = R.drawable.mostly_coudy_flurries;
                        break;
                    case 21:
                        icon = R.drawable.partly_sunny_flurries;
                        break;
                    case 22:
                        icon = R.drawable.snow;
                        break;
                    case 23:
                        icon = R.drawable.mostly_cloudy_snow;
                        break;
                    case 24:
                        icon = R.drawable.ice;
                        break;
                    case 25:
                        icon = R.drawable.sleet;
                        break;
                    case 26:
                        icon = R.drawable.freezing_rain;
                        break;
                    case 29:
                        icon = R.drawable.rain_and_snow;
                        break;
                    case 30:
                        icon = R.drawable.hot;
                        break;
                    case 31:
                        icon = R.drawable.cold;
                        break;
                    case 32:
                        icon = R.drawable.windy;
                        break;
                    case 33:
                        icon = R.drawable.clear;
                        break;
                    case 34:
                        icon = R.drawable.mostly_clear;
                        break;
                    case 35:
                        icon = R.drawable.partly_cloudy;
                        break;
                    case 36:
                        icon = R.drawable.intermittent_clouds_night;
                        break;
                    case 37:
                        icon = R.drawable.hazy_moonlight;
                        break;
                    case 38:
                        icon = R.drawable.mostly_cloudy_night;
                        break;
                    case 39:
                        icon = R.drawable.partly_cloudy_showers_night;
                        break;
                    case 40:
                        icon = R.drawable.mostly_cloudy_showers_night;
                        break;
                    case 41:
                        icon = R.drawable.partly_cloudy_t_storms_night;
                        break;
                    case 42:
                        icon = R.drawable.mostly_cloudy_t_storms_night;
                        break;
                    case 43:
                        icon = R.drawable.mostly_cloudy_flurries_night;
                        break;
                    case 44:
                        icon = R.drawable.mostly_cloudy_snow_night;
                        break;
                    default:
                        icon = 0;
                        break;
                }
                wind = getString(R.string.current_wind,
                        Integer.toString((int) mJsonObject.getJSONObject("Wind").getJSONObject("Speed").getDouble("Value")),
                        mJsonObject.getJSONObject("Wind").getJSONObject("Speed").getString("Unit"));
                hourlyList.add(new Hourly(hours, temperature, icon, wind));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String getHourlyTime(String hourlyTime) {
        String resultData = "";
        for (int i = 1; i < hourlyTime.length(); i++) {
            // смотрим, был ли слева пробел:
            if ("T".equals(hourlyTime.substring(i - 1, i)))
                resultData = hourlyTime.substring(i, i + 5);
        }
        return resultData;
    }

    public String lastUpdateDate() {
        Date currentDate = new Date();
        Locale rus = new Locale("ru", "RU");
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE ',' dd-MMMM-yyyy ',' HH:mm:ss ", rus);
        String data = dateFormat.format(currentDate);
        String resultData = "";
        resultData = resultData + data.substring(0, 1).toUpperCase(); 
        for (int i = 1; i < data.length(); i++) {
            if (" ".equals(data.substring(i - 1, i)) || "-".equals(data.substring(i - 1, i)))
                resultData = resultData + data.substring(i, i + 1).toUpperCase();
            else
                resultData = resultData + data.substring(i, i + 1);
        }
        return resultData;
    }

    public long getCurrentDateMiliseconds() {
        Date curDate = new Date();
        return curDate.getTime();
    }

    public long getLastUpdateMilisecond() {
        Date lastUpdate = new Date();
        return lastUpdate.getTime();
    }

    private void stopPlay() {
        try {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        } catch (Exception ex) {
            Log.i(TAG, "EXCEPTION stop Play" + ex);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("WEATHER_FRAGMENT", "onDestroy ************8");
        try {
            stopPlay();
        } catch (Exception ex) {
            Log.i(TAG, "EXCEPTION stop Play" + ex);
        }
    }


    private void renderWeather(String weatherResponse) {
        int icon = 0;
        int mainBackground = 0;
        try {
            JSONArray mJsonArray = new JSONArray(weatherResponse);
            JSONObject mJsonObject = mJsonArray.getJSONObject(0);
            timeForThreeDayForecast = mJsonObject.getString("LocalObservationDateTime");
            accuWeatherLink = mJsonObject.getString("MobileLink");
            if (dataStorage.getWhatUpdate() == false) {
                cityTitle.setText(dataStorage.getLastUpdatedCity());
                lastUpdate.setText(getString(R.string.last_update, dataStorage.getLastUpdateWeather()));
            } else {
                cityTitle.setText(dataStorage.getLastCurrentCity());
                lastUpdate.setText(getString(R.string.last_update, dataStorage.getLastCurrentUpdateWeather()));
            }
            int temperature = (int) mJsonObject.getJSONObject("Temperature").getJSONObject("Metric").getDouble("Value");
            currentDeegrees.setText(getString(R.string.current_deegrees, Integer.toString(temperature)));
            int weatherIcon = mJsonObject.getInt("WeatherIcon");
            stopPlay();
            switch (weatherIcon) {
                case 1:
                    icon = R.drawable.big_sunny;
                    mainBackground = R.drawable.clear_sky_background;

                    mPlayer = MediaPlayer.create(getContext(), constants.getSong(0));
                    if(audioManager.getRingerMode() == 0 || audioManager.getRingerMode() == 1){
                        mPlayer.setVolume(0, 0);
                    }
                    else{
                        mPlayer.setVolume(0.35f, .35f);
                    }
                    mPlayer.start();
                    mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            stopPlay();
                        }
                    });
                    break;
                case 2:
                    icon = R.drawable.big_mostly_sunny;
                    mainBackground = R.drawable.partly_sunny_background;
                    break;
                case 3:
                    icon = R.drawable.big_partly_sunny;
                    mainBackground = R.drawable.partly_sunny_background;
                    break;
                case 4:
                    icon = R.drawable.big_intermittent_clouds;
                    mainBackground = R.drawable.partly_sunny_background;
                    break;
                case 5:
                    icon = R.drawable.big_hazy_sunshine;
                    mainBackground = R.drawable.partly_sunny_background;
                    break;
                case 6:
                    icon = R.drawable.big_mostly_cloudy;
                    mainBackground = R.drawable.mostly_clouds_background;
                    break;
                case 7:
                    icon = R.drawable.big_cloudy;
                    mainBackground = R.drawable.clouds_background;
                    break;
                case 8:
                    icon = R.drawable.big_dreary;
                    mainBackground = R.drawable.dreary_background;
                    break;
                case 11:
                    icon = R.drawable.big_fog;
                    mainBackground = R.drawable.fog_background;
                    break;
                case 12:
                    icon = R.drawable.big_showers;
                    mainBackground = R.drawable.showers_background;
                    mPlayer = MediaPlayer.create(getContext(), constants.getSong(1));
                    if(audioManager.getRingerMode() == 0 || audioManager.getRingerMode() == 1){
                        mPlayer.setVolume(0, 0);
                    }
                    else{
                        mPlayer.setVolume(0.35f, .35f);
                    }
                    mPlayer.start();
                    mPlayer.setLooping(true);
                    break;
                case 13:
                    icon = R.drawable.big_mostly_cloudy_showers;
                    mainBackground = R.drawable.showers_background;
                    mPlayer = MediaPlayer.create(getContext(), constants.getSong(1));
                    if(audioManager.getRingerMode() == 0 || audioManager.getRingerMode() == 1){
                        mPlayer.setVolume(0, 0);
                    }
                    else{
                        mPlayer.setVolume(0.35f, .35f);
                    }
                    mPlayer.start();
                    mPlayer.setLooping(true);
                    break;
                case 14:
                    icon = R.drawable.big_partly_sunny_showers;
                    mainBackground = R.drawable.showers_background;
                    mPlayer = MediaPlayer.create(getContext(), constants.getSong(1));
                    if(audioManager.getRingerMode() == 0 || audioManager.getRingerMode() == 1){
                        mPlayer.setVolume(0, 0);
                    }
                    else{
                        mPlayer.setVolume(0.35f, .35f);
                    }
                    mPlayer.start();
                    mPlayer.setLooping(true);
                    break;
                case 15:
                    icon = R.drawable.big_t_storms;
                    mainBackground = R.drawable.t_storm_background;
                    mPlayer = MediaPlayer.create(getContext(), constants.getSong(2));
                    if(audioManager.getRingerMode() == 0 || audioManager.getRingerMode() == 1){
                        mPlayer.setVolume(0, 0);
                    }
                    else{
                        mPlayer.setVolume(0.35f, .35f);
                    }
                    mPlayer.start();
                    mPlayer.setLooping(true);
                    break;
                case 16:
                    icon = R.drawable.big_mostly_cloudy_t_storms;
                    mainBackground = R.drawable.t_storm_background;
                    mPlayer = MediaPlayer.create(getContext(), constants.getSong(3));
                    if(audioManager.getRingerMode() == 0 || audioManager.getRingerMode() == 1){
                        mPlayer.setVolume(0, 0);
                    }
                    else{
                        mPlayer.setVolume(0.35f, .35f);
                    }
                    mPlayer.start();
                    mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            stopPlay();
                        }
                    });
                    break;
                case 17:
                    icon = R.drawable.big_partly_sunny_t_storms;
                    mainBackground = R.drawable.t_storm_background;
                    mPlayer = MediaPlayer.create(getContext(), constants.getSong(3));
                    if(audioManager.getRingerMode() == 0 || audioManager.getRingerMode() == 1){
                        mPlayer.setVolume(0, 0);
                    }
                    else{
                        mPlayer.setVolume(0.35f, .35f);
                    }
                    mPlayer.start();
                    mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            stopPlay();
                        }
                    });
                    break;
                case 18:
                    icon = R.drawable.big_rain;
                    mainBackground = R.drawable.rain_background;
                    mPlayer = MediaPlayer.create(getContext(), constants.getSong(4));
                    if(audioManager.getRingerMode() == 0 || audioManager.getRingerMode() == 1){
                        mPlayer.setVolume(0, 0);
                    }
                    else{
                        mPlayer.setVolume(0.35f, .35f);
                    }
                    mPlayer.start();
                    mPlayer.setLooping(true);
                    break;
                case 19:
                    icon = R.drawable.big_flurries;
                    mainBackground = R.drawable.snow_background;
                    stopPlay();
                    mPlayer = MediaPlayer.create(getContext(), constants.getSong(5));
                    if(audioManager.getRingerMode() == 0 || audioManager.getRingerMode() == 1){
                        mPlayer.setVolume(0, 0);
                    }
                    else{
                        mPlayer.setVolume(0.35f, .35f);
                    }
                    mPlayer.start();
                    mPlayer.setLooping(true);
                    Log.i("CASE", "Case = 19");
                    break;
                case 20:
                    icon = R.drawable.big_mostly_cloudy_flurries;
                    mainBackground = R.drawable.snow_background;
                    mPlayer = MediaPlayer.create(getContext(), constants.getSong(6));
                    if(audioManager.getRingerMode() == 0 || audioManager.getRingerMode() == 1){
                        mPlayer.setVolume(0, 0);
                    }
                    else{
                        mPlayer.setVolume(0.35f, .35f);
                    }
                    mPlayer.start();
                    mPlayer.setLooping(true);
                    Log.i("CASE", "Case = 20");
                    break;
                case 21:
                    icon = R.drawable.big_partly_sunny_flurries;
                    mainBackground = R.drawable.snow_background;
                    mPlayer = MediaPlayer.create(getContext(), constants.getSong(6));
                    if(audioManager.getRingerMode() == 0 || audioManager.getRingerMode() == 1){
                        mPlayer.setVolume(0, 0);
                    }
                    else{
                        mPlayer.setVolume(0.35f, .35f);
                    }
                    mPlayer.start();
                    mPlayer.setLooping(true);
                    Log.i("CASE", "Case = 21");
                    break;
                case 22:
                    icon = R.drawable.big_snow;
                    mainBackground = R.drawable.snow_background;
                    mPlayer = MediaPlayer.create(getContext(), constants.getSong(6));
                    if(audioManager.getRingerMode() == 0 || audioManager.getRingerMode() == 1){
                        mPlayer.setVolume(0, 0);
                    }
                    else{
                        mPlayer.setVolume(0.35f, .35f);
                    }
                    mPlayer.start();
                    mPlayer.setLooping(true);
                    Log.i("CASE", "Case = 22");
                    break;
                case 23:
                    icon = R.drawable.big_mostly_cloudy_snow;
                    mainBackground = R.drawable.snow_background;
                    mPlayer = MediaPlayer.create(getContext(), constants.getSong(6));
                    if(audioManager.getRingerMode() == 0 || audioManager.getRingerMode() == 1){
                        mPlayer.setVolume(0, 0);
                    }
                    else{
                        mPlayer.setVolume(0.35f, .35f);
                    }
                    mPlayer.start();
                    mPlayer.setLooping(true);
                    Log.i("CASE", "Case = 23");
                    break;
                case 24:
                    icon = R.drawable.big_ice;
                    mainBackground = R.drawable.ice_background;
                    break;
                case 25:
                    icon = R.drawable.big_sleet;
                    mainBackground = R.drawable.snow_background;
                    mPlayer = MediaPlayer.create(getContext(), constants.getSong(1));
                    if(audioManager.getRingerMode() == 0 || audioManager.getRingerMode() == 1){
                        mPlayer.setVolume(0, 0);
                    }
                    else{
                        mPlayer.setVolume(0.35f, .35f);
                    }
                    mPlayer.start();
                    mPlayer.setLooping(true);
                    Log.i("CASE", "Case = 25");
                    break;
                case 26:
                    icon = R.drawable.big_freezing_rain;
                    mainBackground = R.drawable.rain_background;
                    mPlayer = MediaPlayer.create(getContext(), constants.getSong(1));
                    if(audioManager.getRingerMode() == 0 || audioManager.getRingerMode() == 1){
                        mPlayer.setVolume(0, 0);
                    }
                    else{
                        mPlayer.setVolume(0.35f, .35f);
                    }
                    mPlayer.start();
                    mPlayer.setLooping(true);
                    Log.i("CASE", "Case = 26");
                    break;
                case 29:
                    icon = R.drawable.big_rain_and_snow;
                    mainBackground = R.drawable.rain_with_snow_background;
                    mPlayer = MediaPlayer.create(getContext(), constants.getSong(1));
                    if(audioManager.getRingerMode() == 0 || audioManager.getRingerMode() == 1){
                        mPlayer.setVolume(0, 0);
                    }
                    else{
                        mPlayer.setVolume(0.35f, .35f);
                    }
                    mPlayer.start();
                    mPlayer.setLooping(true);
                    Log.i("CASE", "Case = 29");
                    break;
                case 30:
                    icon = R.drawable.big_hot;
                    mainBackground = R.drawable.hot_background;
                    break;
                case 31:
                    icon = R.drawable.big_cold;
                    mainBackground = R.drawable.cold_background;
                    break;
                case 32:
                    icon = R.drawable.big_windy;
                    mainBackground = R.drawable.windy_background;
                    mPlayer = MediaPlayer.create(getContext(), constants.getSong(5));
                    if(audioManager.getRingerMode() == 0 || audioManager.getRingerMode() == 1){
                        mPlayer.setVolume(0, 0);
                    }
                    else{
                        mPlayer.setVolume(0.35f, .35f);
                    }
                    mPlayer.start();
                    mPlayer.setLooping(true);
                    Log.i("CASE", "Case = 32");
                    break;
                case 33:
                    icon = R.drawable.big_clear;
                    mainBackground = R.drawable.clear_sky_night;
                    mPlayer = MediaPlayer.create(getContext(), constants.getSong(7));
                    if(audioManager.getRingerMode() == 0 || audioManager.getRingerMode() == 1){
                        mPlayer.setVolume(0, 0);
                    }
                    else{
                        mPlayer.setVolume(0.35f, .35f);
                    }
                    mPlayer.start();
                    mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            stopPlay();
                        }
                    });
                    break;
                case 34:
                    icon = R.drawable.big_mostly_clear;
                    mainBackground = R.drawable.partly_sunny_background;
                    break;
                case 35:
                    icon = R.drawable.big_partly_cloudy;
                    mainBackground = R.drawable.partly_sunny_background;
                    break;
                case 36:
                    icon = R.drawable.big_intermittent_clouds_night;
                    mainBackground = R.drawable.partly_sunny_background;
                    break;
                case 37:
                    icon = R.drawable.big_hazy_moonlight;
                    mainBackground = R.drawable.partly_sunny_background;
                    break;
                case 38:
                    icon = R.drawable.big_mostly_cloudy_night;
                    mainBackground = R.drawable.mostly_clouds_background;
                    break;
                case 39:
                    icon = R.drawable.big_partly_cloudy_showers_night;
                    mainBackground = R.drawable.showers_background;
                    mPlayer = MediaPlayer.create(getContext(), constants.getSong(1));
                    if(audioManager.getRingerMode() == 0 || audioManager.getRingerMode() == 1){
                        mPlayer.setVolume(0, 0);
                    }
                    else{
                        mPlayer.setVolume(0.35f, .35f);
                    }
                    mPlayer.start();
                    mPlayer.setLooping(true);
                    Log.i("CASE", "Case = 39");
                    break;
                case 40:
                    icon = R.drawable.big_mostly_cloudy_showers_night;
                    mainBackground = R.drawable.showers_background;
                    mPlayer = MediaPlayer.create(getContext(), constants.getSong(1));
                    if(audioManager.getRingerMode() == 0 || audioManager.getRingerMode() == 1){
                        mPlayer.setVolume(0, 0);
                    }
                    else{
                        mPlayer.setVolume(0.35f, .35f);
                    }
                    mPlayer.start();
                    mPlayer.setLooping(true);
                    Log.i("CASE", "Case = 40");
                    break;
                case 41:
                    icon = R.drawable.big_partly_cloudy_t_storms_night;
                    mainBackground = R.drawable.t_storm_background;
                    mPlayer = MediaPlayer.create(getContext(), constants.getSong(2));
                    if(audioManager.getRingerMode() == 0 || audioManager.getRingerMode() == 1){
                        mPlayer.setVolume(0, 0);
                    }
                    else{
                        mPlayer.setVolume(0.35f, .35f);
                    }
                    mPlayer.start();
                    mPlayer.setLooping(true);
                    Log.i("CASE", "Case = 41");
                    break;
                case 42:
                    icon = R.drawable.big_mostly_cloudy_t_storms_night;
                    mainBackground = R.drawable.t_storm_background;
                    mPlayer = MediaPlayer.create(getContext(), constants.getSong(2));
                    if(audioManager.getRingerMode() == 0 || audioManager.getRingerMode() == 1){
                        mPlayer.setVolume(0, 0);
                    }
                    else{
                        mPlayer.setVolume(0.35f, .35f);
                    }
                    mPlayer.start();
                    mPlayer.setLooping(true);
                    Log.i("CASE", "Case = 42");
                    break;
                case 43:
                    icon = R.drawable.big_mostly_cloudy_flurries_night;
                    mainBackground = R.drawable.snow_background;
                    mPlayer = MediaPlayer.create(getContext(), constants.getSong(5));
                    if(audioManager.getRingerMode() == 0 || audioManager.getRingerMode() == 1){
                        mPlayer.setVolume(0, 0);
                    }
                    else{
                        mPlayer.setVolume(0.35f, .35f);
                    }
                    mPlayer.start();
                    mPlayer.setLooping(true);
                    Log.i("CASE", "Case = 43");
                    break;
                case 44:
                    icon = R.drawable.big_mostly_cloudy_snow_night;
                    mainBackground = R.drawable.snow_background;
                    mPlayer = MediaPlayer.create(getContext(), constants.getSong(6));
                    if(audioManager.getRingerMode() == 0 || audioManager.getRingerMode() == 1){
                        mPlayer.setVolume(0, 0);
                    }
                    else{
                        mPlayer.setVolume(0.35f, .35f);
                    }
                    mPlayer.start();
                    mPlayer.setLooping(true);
                    Log.i("CASE", "Case = 44");
                    break;
                default:
                    icon = 0;
                    break;
            }
            mainImage.setImageResource(icon);
            mainBackgroundImage.setImageResource(mainBackground);
            weatherType.setText(mJsonObject.getString("WeatherText"));
            current_wind.setText(getString(R.string.current_wind,
                    Integer.toString((int) mJsonObject.getJSONObject("Wind").getJSONObject("Speed").getJSONObject("Metric").getDouble("Value")),
                    mJsonObject.getJSONObject("Wind").getJSONObject("Speed").getJSONObject("Metric").getString("Unit")));
            img_wind.setBackgroundResource(R.drawable.icon_smal_wind);
            real_feel.setText(getString(R.string.real_feel,
                    Integer.toString((int) mJsonObject.getJSONObject("RealFeelTemperature").getJSONObject("Metric").getDouble("Value"))));
            img_real_feel.setBackgroundResource(R.drawable.icon_small_temp);
            visibility.setText(getString(R.string.visibility,
                    Integer.toString((int) mJsonObject.getJSONObject("Visibility").getJSONObject("Metric").getDouble("Value")),
                    mJsonObject.getJSONObject("Visibility").getJSONObject("Metric").getString("Unit")));
            img_visibility.setBackgroundResource(R.drawable.icon_small_visibility);
            pressure.setText(getString(R.string.pressure,
                    Integer.toString((int) mJsonObject.getJSONObject("Pressure").getJSONObject("Metric").getDouble("Value")),
                    mJsonObject.getJSONObject("Pressure").getJSONObject("Metric").getString("Unit")));
            img_pressure.setBackgroundResource(R.drawable.icon_small_pressure);
        } catch (Exception ex) {

        }
    }

    private void renderFiveDayForecast(String fiveDayForecastResponse) {

        IconArray iconArray = new IconArray();
        String percentOfRain = "";
        String percentOfThuder = "";
        String percentOfSnow = "";
        int icon1 = 0;
        int icon2 = 0;
        int icon3 = 0;
        try {
            JSONObject jsonObject = new JSONObject(fiveDayForecastResponse);
            JSONArray jsonArray = jsonObject.getJSONArray("DailyForecasts");
            JSONObject resultsObj1 = jsonArray.getJSONObject(0);


            String responseSunRise = resultsObj1.getJSONObject("Sun").getString("Rise");
            if (responseSunRise.equals("") || responseSunRise == null) {
                sunRise.setText(R.string.not_available);
            } else {
                sunRise.setText(getString(R.string.sunrise, renderSunTime(responseSunRise)));
            }


            String sunrise = renderSunTime(responseSunRise);
            String responseSunSet = resultsObj1.getJSONObject("Sun").getString("Set");
            if (responseSunSet.equals("") || responseSunSet == null) {
                sunSet.setText(R.string.not_available);
            } else {
                sunSet.setText(getString(R.string.sunset, renderSunTime(responseSunSet)));
            }
            String sunset = renderSunTime(responseSunSet);
            String time = renderSunTime(timeForThreeDayForecast);
            Log.i(TAG, "responseTime ----------------------------" + timeForThreeDayForecast);
            LocalTime time1 = LocalTime.parse(time);
            LocalTime time2 = LocalTime.parse(sunset);
            LocalTime time3 = LocalTime.parse(sunrise);
            Log.i(TAG, "time1 ----------------------------" + time1);
            int position1 = 0;
            String dayType1 = "";
            if (time1.compareTo(time3) > 0 && time1.compareTo(time2) < 0) {
                percentOfRain = Integer.toString(resultsObj1.getJSONObject("Day").getInt("RainProbability")) + "%";
                percentOfThuder = Integer.toString(resultsObj1.getJSONObject("Day").getInt("ThunderstormProbability")) + "%";
                percentOfSnow = Integer.toString(resultsObj1.getJSONObject("Day").getInt("SnowProbability")) + "%";
                rain.setText(getString(R.string.rain, percentOfRain));
                thunder.setText(getString(R.string.thunder, percentOfThuder));
                snow.setText(getString(R.string.snow, percentOfSnow));
                int dayIcon1 = resultsObj1.getJSONObject("Day").getInt("Icon");

                for (int i = 0; i < iconArray.iconsNumber.length; i++) {
                    if (dayIcon1 == iconArray.iconsNumber[i]) {
                        position1 = i;
                    }
                }
                icon1 = iconArray.icons[position1];
                dayType1 = resultsObj1.getJSONObject("Day").getString("IconPhrase");
            } else {
                percentOfRain = Integer.toString(resultsObj1.getJSONObject("Night").getInt("RainProbability")) + "%";
                percentOfThuder = Integer.toString(resultsObj1.getJSONObject("Night").getInt("ThunderstormProbability")) + "%";
                percentOfSnow = Integer.toString(resultsObj1.getJSONObject("Night").getInt("SnowProbability")) + "%";
                rain.setText(getString(R.string.rain, percentOfRain));
                thunder.setText(getString(R.string.thunder, percentOfThuder));
                snow.setText(getString(R.string.snow, percentOfSnow));
                int nightIcon1 = resultsObj1.getJSONObject("Night").getInt("Icon");
                for (int i = 0; i < iconArray.iconsNumber.length; i++) {
                    if (nightIcon1 == iconArray.iconsNumber[i]) {
                        position1 = i;
                    }
                }
                icon1 = iconArray.icons[position1];
                dayType1 = resultsObj1.getJSONObject("Night").getString("IconPhrase");
            }

            String responseDate1 = resultsObj1.getString("Date");
            String dayOfWeek1 = renderDayOfWeek(responseDate1);
            String minTemp1 = Integer.toString((int) resultsObj1.getJSONObject("Temperature").getJSONObject("Minimum").getDouble("Value"));
            String maxTemp1 = Integer.toString((int) resultsObj1.getJSONObject("Temperature").getJSONObject("Maximum").getDouble("Value"));

            next_day_first_icon.setImageResource(icon1);
            next_day_first.setText(getString(R.string.today));
            next_day_first_type.setText(dayType1);
            next_day_first_temperature.setText(getString(R.string.day_night_temp, maxTemp1, minTemp1));
            separator_1.setText(R.string.separator);
            JSONObject resultsObj2 = jsonArray.getJSONObject(1);
            String responseDate2 = resultsObj2.getString("Date");
            String dayOfWeek2 = renderDayOfWeek(responseDate2);
            String minTemp2 = Integer.toString((int) resultsObj2.getJSONObject("Temperature").getJSONObject("Minimum").getDouble("Value"));
            String maxTemp2 = Integer.toString((int) resultsObj2.getJSONObject("Temperature").getJSONObject("Maximum").getDouble("Value"));

            String dayType2 = "";
            int position2 = 0;
            if (time1.compareTo(time3) > 0 && time1.compareTo(time2) < 0) {
                int dayIcon2 = resultsObj2.getJSONObject("Day").getInt("Icon");
                for (int i = 0; i < iconArray.iconsNumber.length; i++) {
                    if (dayIcon2 == iconArray.iconsNumber[i]) {
                        position2 = i;
                    }
                }
                icon2 = iconArray.icons[position2];
                dayType2 = resultsObj2.getJSONObject("Day").getString("IconPhrase");
            } else {
                int nightIcon2 = resultsObj2.getJSONObject("Night").getInt("Icon");
                for (int i = 0; i < iconArray.iconsNumber.length; i++) {
                    if (nightIcon2 == iconArray.iconsNumber[i]) {
                        position2 = i;
                    }
                }
                icon2 = iconArray.icons[position2];
                dayType2 = resultsObj2.getJSONObject("Night").getString("IconPhrase");
            }
            next_day_second_icon.setImageResource(icon2);
            next_day_second.setText(dayOfWeek2);
            next_day_second_type.setText(dayType2);
            next_day_second_temperature.setText(getString(R.string.day_night_temp, maxTemp2, minTemp2));

            separator_2.setText(R.string.separator);

            JSONObject resultsObj3 = jsonArray.getJSONObject(2);
            String responseDate3 = resultsObj3.getString("Date");
            String dayOfWeek3 = renderDayOfWeek(responseDate3);
            String minTemp3 = Integer.toString((int) resultsObj3.getJSONObject("Temperature").getJSONObject("Minimum").getDouble("Value"));
            String maxTemp3 = Integer.toString((int) resultsObj3.getJSONObject("Temperature").getJSONObject("Maximum").getDouble("Value"));

            String dayType3 = "";

            int position3 = 0;

            if (time1.compareTo(time3) > 0 && time1.compareTo(time2) < 0) {
                int dayIcon3 = resultsObj3.getJSONObject("Day").getInt("Icon");
                for (int i = 0; i < iconArray.iconsNumber.length; i++) {
                    if (dayIcon3 == iconArray.iconsNumber[i]) {
                        position3 = i;
                    }
                }
                icon3 = iconArray.icons[position3];
                dayType3 = resultsObj3.getJSONObject("Day").getString("IconPhrase");
            } else {
                int nightIcon3 = resultsObj3.getJSONObject("Night").getInt("Icon");
                for (int i = 0; i < iconArray.iconsNumber.length; i++) {
                    if (nightIcon3 == iconArray.iconsNumber[i]) {
                        position3 = i;
                    }
                }
                icon3 = iconArray.icons[position3];
                dayType3 = resultsObj3.getJSONObject("Night").getString("IconPhrase");
            }

            next_day_third_icon.setImageResource(icon3);
            next_day_third.setText(dayOfWeek3);
            next_day_third_type.setText(dayType3);
            next_day_third_temperature.setText(getString(R.string.day_night_temp, maxTemp3, minTemp3));
            separator_3.setText(R.string.separator);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private String renderSunTime(String responseDate) {
        String resultData = "";
        String res = "";
        String resString = "";
        for (int i = 1; i < responseDate.length(); i++) {
            if ("T".equals(responseDate.substring(i - 1, i)))
                res = responseDate.substring(i);
        }

        for (int i = 1; i < res.length(); i++) {
            if ("+".equals(res.substring(i - 1, i)) || "-".equals(res.substring(i - 1, i)))
                resultData = res.substring(0, i - 1);
        }
        for (int i = 1; i < resultData.length(); i++) {
            if (":".equals(resultData.substring(i - 1, i)))
                resString = res.substring(0, i - 1);
        }
        return resString;
    }

    private String renderDayOfWeek(String responseDate) {
        Locale rus = new Locale("ru", "RU");
        String resultDate = "";
        String dayTitle = "";
        for (int i = 1; i < responseDate.length(); i++) {
            if ("T".equals(responseDate.substring(i - 1, i)))
                resultDate = responseDate.substring(0, i - 1);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = formatter.parse(resultDate);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE", rus);

            dayTitle = simpleDateFormat.format(date).substring(0, 1).toUpperCase() + simpleDateFormat.format(date).substring(1);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dayTitle;
    }

    private void localStorage() {
        stopPlay();
        if (dataStorage.getLastUpdatedCity().isEmpty() && dataStorage.getLastCurrentCity().isEmpty()) {
            fiveDayBtn.setVisibility(View.INVISIBLE);
            accuweatherLogo.setVisibility(View.INVISIBLE);
            gridlayout.setVisibility(View.INVISIBLE);
            sunriseIcon.setVisibility(View.INVISIBLE);
            sunsetIcon.setVisibility(View.INVISIBLE);
        } else {

            sunriseIcon.setImageResource(R.drawable.sunrise);
            sunsetIcon.setImageResource(R.drawable.sunset);
            if (dataStorage.getWhatUpdate() == true) {
                if (!dataStorage.getLastCurrentCity().isEmpty()) {
                    LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        noGPSLayout.setVisibility(View.VISIBLE);
                    } else {
                        noGPSLayout.setVisibility(View.GONE);
                    }
                    rootView.findViewById(R.id.refresh).setVisibility(View.INVISIBLE);
                    fiveDayBtn.setVisibility(View.VISIBLE);
                    accuweatherLogo.setVisibility(View.VISIBLE);
                    gridlayout.setVisibility(View.VISIBLE);
                    sunriseIcon.setVisibility(View.VISIBLE);
                    sunsetIcon.setVisibility(View.VISIBLE);
                    changeLastUpdateCity.setVisibility(View.VISIBLE);
                    changeLastUpdateCity.setText(dataStorage.getLastUpdatedCity());
                    renderWeather(dataStorage.getCurrentWeatherResponse());
                    setInitialData(dataStorage.getCurrentHourlyResponse());
                    adapter = new HourlyRecycleViewAdapter(getContext(), hourlyList, Weather_Fragment.this);
                    recyclerView.setAdapter(adapter);
                    renderFiveDayForecast(dataStorage.getCurrentFiveDayForecastResponse());
                } else dataStorage.setWhatUpdate(false);
            } else {
                if (!dataStorage.getLastUpdatedCity().isEmpty()) {
                    noGPSLayout.setVisibility(View.GONE);
                    rootView.findViewById(R.id.refresh).setVisibility(View.VISIBLE);
                    fiveDayBtn.setVisibility(View.VISIBLE);
                    accuweatherLogo.setVisibility(View.VISIBLE);
                    sunriseIcon.setVisibility(View.VISIBLE);
                    sunsetIcon.setVisibility(View.VISIBLE);
                    gridlayout.setVisibility(View.VISIBLE);
                    changeLastUpdateCity.setVisibility(View.INVISIBLE);
                    renderWeather(dataStorage.getWeatherResponse());
                    setInitialData(dataStorage.getHourlyResponse());
                    adapter = new HourlyRecycleViewAdapter(getContext(), hourlyList, Weather_Fragment.this);
                    recyclerView.setAdapter(adapter);
                    renderFiveDayForecast(dataStorage.getFiveDayForecastResponse());
                }
            }
        }
    }

    private List<SearchCity> searchCityArrayList;

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_change_city:
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(R.string.alert_dialog_changing);
                        builder.setPositiveButton(R.string.choose, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (searchCityArrayList != null) {
                                    if (searchCityArrayList.size() != 0) {
                                        isSelected = true;
                                        selectedCityName = resultChoice;
                                        changeCity(searchCityArrayList.get(resultChoice).getLatitude(), searchCityArrayList.get(resultChoice).getLongitude());

                                    }
                                }
                            }
                        });
                        ConnectivityManager cm =
                                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                        boolean isConnected = cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
                        if (isConnected == false) {
                            builder.setMessage(R.string.internet_connection);
                            final AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                            ((AlertDialog) alertDialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        } else {


                            final AutoCompleteTextView autoCompleteTextView = new AutoCompleteTextView(getActivity());
                            autoCompleteTextView.setInputType(InputType.TYPE_CLASS_TEXT);
                            autoCompleteTextView.setMaxLines(1);
                            autoCompleteTextView.setHint(R.string.edit_city);

                            autoCompleteTextView.setAdapter(new CitySuggestAdapter(getContext(), android.R.layout.simple_list_item_1));
                            builder.setView(autoCompleteTextView);
                            final AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                            alertDialog.getWindow().setGravity(Gravity.TOP);
                            ((AlertDialog) alertDialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                            searchCityArrayList = new ArrayList<>();
                            autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    searchCityArrayList = PlaceAPI.getSearchCityArrayList();

                                    resultChoice = i;
                                    Log.i("resultChoice", "resultChoice = " + i);
                                    ((AlertDialog) alertDialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                                }
                            });
                        }
                        return true;
                    default:
                        return false;
                }

            }
        });
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu popupMenu) {
                threeDots.setTextColor(getResources().getColor(R.color.white));
            }
        });
        popupMenu.setGravity(Gravity.RIGHT);
        popupMenu.show();
    }

    public void changeCity(String latitude, String longitude) {
        dataStorage.setWhatUpdate(false);
        updateWeatherData(latitude, longitude);
    }
}
