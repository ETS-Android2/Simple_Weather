package entertainment.android_apps.simpleweather;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import entertainment.android_apps.simpleweather.interfaces.RecycleViewListener;

public class FiveDayForecastActivity extends AppCompatActivity implements RecycleViewListener {
    private List<FiveDay> fiveDayList = new ArrayList<>();
    private List<FiveDay> newFiveDayList = new ArrayList<>();
    private String fiveDayForecastResponse = "";
    private String cityTitle = "";
    private static final String SELECTED_TAB = "selected_tab";
    private static final String ACTIVITY = "activity";
    private static final String ACTIVITY_VALUE = "FiveDayForecastActivity";
    Animation scale_Up, scale_Down;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.five_day_forecast);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        scale_Up = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scale_Down = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        final TextView backBtn = (TextView) findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backBtn.startAnimation(scale_Up);
                try {
                    Intent intent = new Intent(FiveDayForecastActivity.this, MainActivity.class);
                    startActivity(intent);
                    Animatoo.animateSplit(view.getContext());
                    finish();
                } catch (Exception ex) {
                }
            }
        });
        try {
            Bundle arguments = getIntent().getExtras();
            cityTitle = arguments.get("cityTitle").toString();
            fiveDayForecastResponse = arguments.get("fiveDayForecastResponse").toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        setInitialData(fiveDayForecastResponse);
        RecyclerView recyclerView = findViewById(R.id.five_day_recycle_view);
        FiveDayRecycleViewAdapter adapter = new FiveDayRecycleViewAdapter(this, fiveDayList, this);
        recyclerView.setAdapter(adapter);
    }

    public void setInitialData(String fiveDayForecastResponse) {
        try {
            int iconDay = 0;
            int iconNight = 0;
            String wind = "";
            String windNight = "";
            if (fiveDayList != null) {
                fiveDayList.clear();
            }
            JSONObject jsonObject = new JSONObject(fiveDayForecastResponse);
            JSONArray jsonArray = jsonObject.getJSONArray("DailyForecasts");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject resultsObj = jsonArray.getJSONObject(i);

                String responseCurrentDate = resultsObj.getString("Date");
                String currentDay = renderDayOfWeek(responseCurrentDate);
                String currentDate = renderDate(responseCurrentDate);

                int weatherIconDay = resultsObj.getJSONObject("Day").getInt("Icon");
                switch (weatherIconDay) {
                    case 1:
                        iconDay = R.drawable.sunny;
                        break;
                    case 2:
                        iconDay = R.drawable.mostly_sunny;
                        break;
                    case 3:
                        iconDay = R.drawable.partly_sunny;
                        break;
                    case 4:
                        iconDay = R.drawable.intermittent_clouds;
                        break;
                    case 5:
                        iconDay = R.drawable.hazy_sunshine;
                        break;
                    case 6:
                        iconDay = R.drawable.mostly_cloudy;
                        break;
                    case 7:
                        iconDay = R.drawable.cloudy;
                        break;
                    case 8:
                        iconDay = R.drawable.dreary;
                        break;
                    case 11:
                        iconDay = R.drawable.fog;
                        break;
                    case 12:
                        iconDay = R.drawable.showers;
                        break;
                    case 13:
                        iconDay = R.drawable.mostly_cloudy_showers;
                        break;
                    case 14:
                        iconDay = R.drawable.partly_sunny_showers;
                        break;
                    case 15:
                        iconDay = R.drawable.t_storms;
                        break;
                    case 16:
                        iconDay = R.drawable.mostly_cloudy_t_storms;
                        break;
                    case 17:
                        iconDay = R.drawable.partly_sunny_t_storms;
                        break;
                    case 18:
                        iconDay = R.drawable.rain;
                        break;
                    case 19:
                        iconDay = R.drawable.flurries;
                        break;
                    case 20:
                        iconDay = R.drawable.mostly_coudy_flurries;
                        break;
                    case 21:
                        iconDay = R.drawable.partly_sunny_flurries;
                        break;
                    case 22:
                        iconDay = R.drawable.snow;
                        break;
                    case 23:
                        iconDay = R.drawable.mostly_cloudy_snow;
                        break;
                    case 24:
                        iconDay = R.drawable.ice;
                        break;
                    case 25:
                        iconDay = R.drawable.sleet;
                        break;
                    case 26:
                        iconDay = R.drawable.freezing_rain;
                        break;
                    case 29:
                        iconDay = R.drawable.rain_and_snow;
                        break;
                    case 30:
                        iconDay = R.drawable.hot;
                        break;
                    case 31:
                        iconDay = R.drawable.cold;
                        break;
                    case 32:
                        iconDay = R.drawable.windy;
                        break;
                    case 33:
                        iconDay = R.drawable.clear;
                        break;
                    case 34:
                        iconDay = R.drawable.mostly_clear;
                        break;
                    case 35:
                        iconDay = R.drawable.partly_cloudy;
                        break;
                    case 36:
                        iconDay = R.drawable.intermittent_clouds_night;
                        break;
                    case 37:
                        iconDay = R.drawable.hazy_moonlight;
                        break;
                    case 38:
                        iconDay = R.drawable.mostly_cloudy_night;
                        break;
                    case 39:
                        iconDay = R.drawable.partly_cloudy_showers_night;
                        break;
                    case 40:
                        iconDay = R.drawable.mostly_cloudy_showers_night;
                        break;
                    case 41:
                        iconDay = R.drawable.partly_cloudy_t_storms_night;
                        break;
                    case 42:
                        iconDay = R.drawable.mostly_cloudy_t_storms_night;
                        break;
                    case 43:
                        iconDay = R.drawable.mostly_cloudy_flurries_night;
                        break;
                    case 44:
                        iconDay = R.drawable.mostly_cloudy_snow_night;
                        break;
                    default:
                        iconDay = 0;
                        break;
                }
                String dayWeather = resultsObj.getJSONObject("Day").getString("IconPhrase");
                String minTemp = Integer.toString((int) resultsObj.getJSONObject("Temperature").getJSONObject("Minimum").getDouble("Value")) + "°";
                String maxTemp = Integer.toString((int) resultsObj.getJSONObject("Temperature").getJSONObject("Maximum").getDouble("Value")) + "°";

                wind = getString(R.string.wind, resultsObj.getJSONObject("Day").getJSONObject("Wind").getJSONObject("Direction").getString("Localized"),
                        Integer.toString((int) resultsObj.getJSONObject("Day").getJSONObject("Wind").getJSONObject("Speed").getDouble("Value")),
                        resultsObj.getJSONObject("Day").getJSONObject("Wind").getJSONObject("Speed").getString("Unit"));

                windNight = getString(R.string.wind, resultsObj.getJSONObject("Night").getJSONObject("Wind").getJSONObject("Direction").getString("Localized"),
                        Integer.toString((int) resultsObj.getJSONObject("Night").getJSONObject("Wind").getJSONObject("Speed").getDouble("Value")),
                        resultsObj.getJSONObject("Night").getJSONObject("Wind").getJSONObject("Speed").getString("Unit"));
                int weatherIconNight = resultsObj.getJSONObject("Night").getInt("Icon");
                switch (weatherIconNight) {
                    case 1:
                        iconNight = R.drawable.sunny;
                        break;
                    case 2:
                        iconNight = R.drawable.mostly_sunny;
                        break;
                    case 3:
                        iconNight = R.drawable.partly_sunny;
                        break;
                    case 4:
                        iconNight = R.drawable.intermittent_clouds;
                        break;
                    case 5:
                        iconNight = R.drawable.hazy_sunshine;
                        break;
                    case 6:
                        iconNight = R.drawable.mostly_cloudy;
                        break;
                    case 7:
                        iconNight = R.drawable.cloudy;
                        break;
                    case 8:
                        iconNight = R.drawable.dreary;
                        break;
                    case 11:
                        iconNight = R.drawable.fog;
                        break;
                    case 12:
                        iconNight = R.drawable.showers;
                        break;
                    case 13:
                        iconNight = R.drawable.mostly_cloudy_showers;
                        break;
                    case 14:
                        iconNight = R.drawable.partly_sunny_showers;
                        break;
                    case 15:
                        iconNight = R.drawable.t_storms;
                        break;
                    case 16:
                        iconNight = R.drawable.mostly_cloudy_t_storms;
                        break;
                    case 17:
                        iconNight = R.drawable.partly_sunny_t_storms;
                        break;
                    case 18:
                        iconNight = R.drawable.rain;
                        break;
                    case 19:
                        iconNight = R.drawable.flurries;
                        break;
                    case 20:
                        iconNight = R.drawable.mostly_coudy_flurries;
                        break;
                    case 21:
                        iconNight = R.drawable.partly_sunny_flurries;
                        break;
                    case 22:
                        iconNight = R.drawable.snow;
                        break;
                    case 23:
                        iconNight = R.drawable.mostly_cloudy_snow;
                        break;
                    case 24:
                        iconNight = R.drawable.ice;
                        break;
                    case 25:
                        iconNight = R.drawable.sleet;
                        break;
                    case 26:
                        iconNight = R.drawable.freezing_rain;
                        break;
                    case 29:
                        iconNight = R.drawable.rain_and_snow;
                        break;
                    case 30:
                        iconNight = R.drawable.hot;
                        break;
                    case 31:
                        iconNight = R.drawable.cold;
                        break;
                    case 32:
                        iconNight = R.drawable.windy;
                        break;
                    case 33:
                        iconNight = R.drawable.clear;
                        break;
                    case 34:
                        iconNight = R.drawable.mostly_clear;
                        break;
                    case 35:
                        iconNight = R.drawable.partly_cloudy;
                        break;
                    case 36:
                        iconNight = R.drawable.intermittent_clouds_night;
                        break;
                    case 37:
                        iconNight = R.drawable.hazy_moonlight;
                        break;
                    case 38:
                        iconNight = R.drawable.mostly_cloudy_night;
                        break;
                    case 39:
                        iconNight = R.drawable.partly_cloudy_showers_night;
                        break;
                    case 40:
                        iconNight = R.drawable.mostly_cloudy_showers_night;
                        break;
                    case 41:
                        iconNight = R.drawable.partly_cloudy_t_storms_night;
                        break;
                    case 42:
                        iconNight = R.drawable.mostly_cloudy_t_storms_night;
                        break;
                    case 43:
                        iconNight = R.drawable.mostly_cloudy_flurries_night;
                        break;
                    case 44:
                        iconNight = R.drawable.mostly_cloudy_snow_night;
                        break;
                    default:
                        iconNight = 0;
                        break;
                }
                String nightWeather = resultsObj.getJSONObject("Night").getString("IconPhrase");
                String dayTranslate = getString(R.string.day);
                String nightTranslate = getString(R.string.night);
                fiveDayList.add(new FiveDay(currentDay, currentDate, dayTranslate, iconDay, dayWeather, maxTemp, wind, windNight, minTemp, nightTranslate, iconNight, nightWeather));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EE", rus);

            dayTitle = simpleDateFormat.format(date).substring(0, 1).toUpperCase() + simpleDateFormat.format(date).substring(1);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dayTitle;
    }

    private String renderDate(String responseDate) {
        Locale rus = new Locale("ru", "RU");
        String resultData = "";
        String day = "";
        String month = "";
        for (int i = 1; i < responseDate.length(); i++) {
            // смотрим, был ли слева пробел:
            if ("T".equals(responseDate.substring(i - 1, i)))
                resultData = responseDate.substring(0, i - 1);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {

            Date date = formatter.parse(resultData);
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MM", rus);
            month = simpleDateFormat1.format(date);
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd", rus);
            day = simpleDateFormat2.format(date);
            return day + "/" + month;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        try {
            Intent intent = new Intent(FiveDayForecastActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(0,R.anim.animate_split_exit);
            finish();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void iTemClicked(View v, int position) {
        v.startAnimation(scale_Up);
        try {
            Intent intent = new Intent(FiveDayForecastActivity.this, DetailFiveDayForecastActivity.class);
            intent.putExtra(SELECTED_TAB, position);
            intent.putExtra(ACTIVITY, ACTIVITY_VALUE);
            intent.putExtra("cityTitle", cityTitle);
            intent.putExtra("fiveDayForecastResponse", fiveDayForecastResponse);
            startActivity(intent);
            Animatoo.animateSlideDown(v.getContext());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}