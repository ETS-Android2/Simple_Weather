package entertainment.android_apps.simpleweather;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailFiveDayForecastFragment extends Fragment {
    private static final String TAB_POSITION = "tab_position";
    private int position;
    List<FiveDayForecast> fiveDayForecastList = new ArrayList<>();
    ImageView iconDay;
    TextView typeDay;
    TextView tempDay;
    TextView sunrise;
    TextView sunset;
    TextView realFeelDay;
    TextView precipitationDay;
    TextView rainDay;
    TextView snowDay;
    TextView windFromDay;
    TextView windSpeedDay;
    TextView cloudsDay;

    ImageView iconNight;
    TextView typeNight;
    TextView tempNight;
    TextView moonrise;
    TextView moonset;
    TextView realFeelNight;
    TextView precipitationNight;
    TextView rainNight;
    TextView snowNight;
    TextView windFromNight;
    TextView windSpeedNight;
    TextView cloudsNight;
    private SharedPreferences sharedPrefs;
    String fiveDayForecastResponse = "";
    boolean whatUpdate = false;
    private final String whatUpdateKey = "whatUpdate";
    private final String fiveDayForecastResponseKey = "fiveDayForecastResponseKey";
    private final String currentFiveDayForecastResponseKey = "currentFiveDayForecastResponse";
    public DetailFiveDayForecastFragment() {}
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments() != null ? getArguments().getInt(TAB_POSITION) : 0;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dynamic_fragment, container, false);
        iconDay = (ImageView)view.findViewById(R.id.fiveday_day_icon);
        typeDay = (TextView)view.findViewById(R.id.fiveday_day_weather_type);
        tempDay = (TextView)view.findViewById(R.id.max_temp_day);
        sunrise = (TextView)view.findViewById(R.id.sunrise_day);
        sunset = (TextView)view.findViewById(R.id.sunset_day);
        realFeelDay = (TextView)view.findViewById(R.id.fiveday_realfeel_day);
        precipitationDay = (TextView)view.findViewById(R.id.fiveday_precipitation_day);
        rainDay = (TextView)view.findViewById(R.id.fiveday_rain_day);
        snowDay = (TextView)view.findViewById(R.id.fiveday_snow_day);
        windFromDay = (TextView)view.findViewById(R.id.fiveday_wind_direction_day);
        windSpeedDay = (TextView)view.findViewById(R.id.fiveday_wind_speed_day);
        cloudsDay = (TextView)view.findViewById(R.id.fiveday_clouds_day);

        iconNight = (ImageView)view.findViewById(R.id.fiveday_nigth_icon);
        typeNight = (TextView)view.findViewById(R.id.fiveday_night_weather_type);
        tempNight = (TextView)view.findViewById(R.id.min_temp_night);
        moonrise = (TextView)view.findViewById(R.id.moonrise_night);
        moonset = (TextView)view.findViewById(R.id.moonset_night);
        realFeelNight = (TextView)view.findViewById(R.id.fiveday_realfeel_night);
        precipitationNight = (TextView)view.findViewById(R.id.fiveday_precipitation_night);
        rainNight = (TextView)view.findViewById(R.id.fiveday_rain_night);
        snowNight = (TextView)view.findViewById(R.id.fiveday_snow_night);
        windFromNight = (TextView)view.findViewById(R.id.fiveday_wind_direction_night);
        windSpeedNight = (TextView)view.findViewById(R.id.fiveday_wind_speed_night);
        cloudsNight = (TextView)view.findViewById(R.id.fiveday_clouds_night);

        sharedPrefs = this.getActivity().getSharedPreferences("pref",Context.MODE_PRIVATE);
        whatUpdate = sharedPrefs.getBoolean(whatUpdateKey,false);
        if(whatUpdate == false){ fiveDayForecastResponse = sharedPrefs.getString(fiveDayForecastResponseKey,null); }
        else{ fiveDayForecastResponse = sharedPrefs.getString(currentFiveDayForecastResponseKey,null); }
        setInitialData(fiveDayForecastResponse);
        iconDay.setImageResource(fiveDayForecastList.get(position).getIconDay());
        typeDay.setText(fiveDayForecastList.get(position).getWeatherTypeDay());
        tempDay.setText(fiveDayForecastList.get(position).getTempDay());
        sunrise.setText(fiveDayForecastList.get(position).getSunrise());
        sunset.setText(fiveDayForecastList.get(position).getSunset());
        realFeelDay.setText(fiveDayForecastList.get(position).getRealFeelDay());
        precipitationDay.setText(fiveDayForecastList.get(position).getPrecipitationDay());
        rainDay.setText(fiveDayForecastList.get(position).getRainDay());
        snowDay.setText(fiveDayForecastList.get(position).getSnowDay());
        windFromDay.setText(fiveDayForecastList.get(position).getWindFromDay());
        windSpeedDay.setText(fiveDayForecastList.get(position).getWindSpeedDay());
        cloudsDay.setText(fiveDayForecastList.get(position).getCloudsDay());

        iconNight.setImageResource(fiveDayForecastList.get(position).getIconNight());
        typeNight.setText(fiveDayForecastList.get(position).getWeatherTypeNight());
        tempNight.setText(fiveDayForecastList.get(position).getTempNight());
        moonrise.setText(fiveDayForecastList.get(position).getMoonrise());
        moonset.setText(fiveDayForecastList.get(position).getMoonset());
        realFeelNight.setText(fiveDayForecastList.get(position).getRealFeelNight());
        precipitationNight.setText(fiveDayForecastList.get(position).getPrecipitationNight());
        rainNight.setText(fiveDayForecastList.get(position).getRainNight());
        snowNight.setText(fiveDayForecastList.get(position).getSnowNight());
        windFromNight.setText(fiveDayForecastList.get(position).getWindFromNight());
        windSpeedNight.setText(fiveDayForecastList.get(position).getWindSpeedNight());
        cloudsNight.setText(fiveDayForecastList.get(position).getCloudsNight());
        return  view; }


    public void setInitialData(String fiveDayForecastResponse){
        try{
            int iconDay = 0;
            int iconNight = 0;
            if(fiveDayForecastList != null) { fiveDayForecastList.clear();}
            JSONObject jsonObject = new JSONObject(fiveDayForecastResponse);
            JSONArray jsonArray = jsonObject.getJSONArray("DailyForecasts");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject resultsObj = jsonArray.getJSONObject(i);

                int weatherIconDay = resultsObj.getJSONObject("Day").getInt("Icon");
                switch (weatherIconDay){
                    case 1: iconDay = R.drawable.sunny;break;
                    case 2: iconDay = R.drawable.mostly_sunny;break;
                    case 3: iconDay = R.drawable.partly_sunny;break;
                    case 4: iconDay = R.drawable.intermittent_clouds;break;
                    case 5: iconDay = R.drawable.hazy_sunshine;break;
                    case 6: iconDay = R.drawable.mostly_cloudy;break;
                    case 7: iconDay = R.drawable.cloudy;break;
                    case 8: iconDay = R.drawable.dreary;break;
                    case 11: iconDay = R.drawable.fog;break;
                    case 12: iconDay = R.drawable.showers;break;
                    case 13: iconDay = R.drawable.mostly_cloudy_showers;break;
                    case 14: iconDay = R.drawable.partly_sunny_showers;break;
                    case 15: iconDay = R.drawable.t_storms;break;
                    case 16: iconDay = R.drawable.mostly_cloudy_t_storms;break;
                    case 17: iconDay = R.drawable.partly_sunny_t_storms;break;
                    case 18: iconDay = R.drawable.rain;break;
                    case 19: iconDay = R.drawable.flurries;break;
                    case 20: iconDay = R.drawable.mostly_coudy_flurries;break;
                    case 21: iconDay = R.drawable.partly_sunny_flurries;break;
                    case 22: iconDay = R.drawable.snow;break;
                    case 23: iconDay = R.drawable.mostly_cloudy_snow;break;
                    case 24: iconDay = R.drawable.ice;break;
                    case 25: iconDay = R.drawable.sleet;break;
                    case 26: iconDay = R.drawable.freezing_rain;break;
                    case 29: iconDay = R.drawable.rain_and_snow;break;
                    case 30: iconDay = R.drawable.hot;break;
                    case 31: iconDay = R.drawable.cold;break;
                    case 32: iconDay = R.drawable.windy;break;
                    case 33: iconDay = R.drawable.clear;break;
                    case 34: iconDay = R.drawable.mostly_clear;break;
                    case 35: iconDay = R.drawable.partly_cloudy;break;
                    case 36: iconDay = R.drawable.intermittent_clouds_night;break;
                    case 37: iconDay = R.drawable.hazy_moonlight;break;
                    case 38: iconDay = R.drawable.mostly_cloudy_night;break;
                    case 39: iconDay = R.drawable.partly_cloudy_showers_night;break;
                    case 40: iconDay = R.drawable.mostly_cloudy_showers_night;break;
                    case 41: iconDay = R.drawable.partly_cloudy_t_storms_night;break;
                    case 42: iconDay = R.drawable.mostly_cloudy_t_storms_night;break;
                    case 43: iconDay = R.drawable.mostly_cloudy_flurries_night;break;
                    case 44: iconDay = R.drawable.mostly_cloudy_snow_night;break;
                    default: iconDay =0; break;
                }
                String weatherTypeDay = resultsObj.getJSONObject("Day").getString("IconPhrase");
                String tempDay= Integer.toString((int)resultsObj.getJSONObject("Temperature").getJSONObject("Maximum").getDouble("Value"))+ "°C";
                String responseSunrise = resultsObj.getJSONObject("Sun").getString("Rise");
                String sunrise = "";
                if(responseSunrise.equals("") || responseSunrise == null){ sunrise = getString(R.string.not_available);  }
                else { sunrise = renderSunTime(responseSunrise); }
                String responseSunset = resultsObj.getJSONObject("Sun").getString("Set");
                String sunset = "";
                if(responseSunset.equals("") || responseSunset == null){ sunset = getString(R.string.not_available); }else{ sunset = renderSunTime(responseSunset); }
                String realfeelDay = Integer.toString((int)resultsObj.getJSONObject("RealFeelTemperature").getJSONObject("Maximum").getDouble("Value"))+ "°C";
                String precipitationDay = Integer.toString(resultsObj.getJSONObject("Day").getInt("PrecipitationProbability"))+ "%";
                String rainDay = Integer.toString(resultsObj.getJSONObject("Day").getInt("RainProbability")) + "%";
                String snowDay = Integer.toString(resultsObj.getJSONObject("Day").getInt("SnowProbability")) + "%";
                String windFromDay = resultsObj.getJSONObject("Day").getJSONObject("Wind").getJSONObject("Direction").getString("Localized");
                String windSpeedDay = Integer.toString((int)resultsObj.getJSONObject("Day").getJSONObject("Wind").getJSONObject("Speed").getDouble("Value")) + " " +
                                resultsObj.getJSONObject("Day").getJSONObject("Wind").getJSONObject("Speed").getString("Unit");
                String cloudsDay = Integer.toString(resultsObj.getJSONObject("Day").getInt("CloudCover"))+ "%";
                int weatherIconNight = resultsObj.getJSONObject("Night").getInt("Icon");
                switch (weatherIconNight){
                    case 1: iconNight = R.drawable.sunny;break;
                    case 2: iconNight = R.drawable.mostly_sunny;break;
                    case 3: iconNight = R.drawable.partly_sunny;break;
                    case 4: iconNight = R.drawable.intermittent_clouds;break;
                    case 5: iconNight = R.drawable.hazy_sunshine;break;
                    case 6: iconNight = R.drawable.mostly_cloudy;break;
                    case 7: iconNight= R.drawable.cloudy;break;
                    case 8: iconNight = R.drawable.dreary;break;
                    case 11: iconNight = R.drawable.fog;break;
                    case 12: iconNight= R.drawable.showers;break;
                    case 13: iconNight = R.drawable.mostly_cloudy_showers;break;
                    case 14: iconNight = R.drawable.partly_sunny_showers;break;
                    case 15: iconNight = R.drawable.t_storms;break;
                    case 16: iconNight = R.drawable.mostly_cloudy_t_storms;break;
                    case 17: iconNight = R.drawable.partly_sunny_t_storms;break;
                    case 18: iconNight = R.drawable.rain;break;
                    case 19: iconNight = R.drawable.flurries;break;
                    case 20: iconNight = R.drawable.mostly_coudy_flurries;break;
                    case 21: iconNight = R.drawable.partly_sunny_flurries;break;
                    case 22: iconNight = R.drawable.snow;break;
                    case 23: iconNight = R.drawable.mostly_cloudy_snow;break;
                    case 24: iconNight = R.drawable.ice;break;
                    case 25: iconNight = R.drawable.sleet;break;
                    case 26: iconNight = R.drawable.freezing_rain;break;
                    case 29: iconNight = R.drawable.rain_and_snow;break;
                    case 30: iconNight = R.drawable.hot;break;
                    case 31: iconNight = R.drawable.cold;break;
                    case 32: iconNight = R.drawable.windy;break;
                    case 33: iconNight = R.drawable.clear;break;
                    case 34: iconNight = R.drawable.mostly_clear;break;
                    case 35: iconNight = R.drawable.partly_cloudy;break;
                    case 36: iconNight = R.drawable.intermittent_clouds_night;break;
                    case 37: iconNight = R.drawable.hazy_moonlight;break;
                    case 38: iconNight = R.drawable.mostly_cloudy_night;break;
                    case 39: iconNight = R.drawable.partly_cloudy_showers_night;break;
                    case 40: iconNight = R.drawable.mostly_cloudy_showers_night;break;
                    case 41: iconNight = R.drawable.partly_cloudy_t_storms_night;break;
                    case 42: iconNight = R.drawable.mostly_cloudy_t_storms_night;break;
                    case 43: iconNight = R.drawable.mostly_cloudy_flurries_night;break;
                    case 44: iconNight = R.drawable.mostly_cloudy_snow_night;break;
                    default: iconNight =0; break;
                }
                String weatherTypeNight= resultsObj.getJSONObject("Night").getString("IconPhrase");
                String tempNight = Integer.toString((int)resultsObj.getJSONObject("Temperature").getJSONObject("Minimum").getDouble("Value"))+ "°C";
                String responseMoonrise = resultsObj.getJSONObject("Moon").getString("Rise");
                String moonrise = renderSunTime(responseMoonrise);

                String responseMoonset = resultsObj.getJSONObject("Moon").getString("Set");
                String moonset = renderSunTime(responseMoonset);
                if(moonrise.equals("") || moonrise == null){
                    moonrise = getString(R.string.not_available);
                }
                if(moonset.equals("") || moonset == null){
                    moonset = getString(R.string.not_available);
                }

                String realfeelNight = Integer.toString((int)resultsObj.getJSONObject("RealFeelTemperature").getJSONObject("Minimum").getDouble("Value"))+ "°C";
                String precipitationNight = Integer.toString(resultsObj.getJSONObject("Night").getInt("PrecipitationProbability"))+ "%";
                String rainNight = Integer.toString(resultsObj.getJSONObject("Night").getInt("RainProbability")) + "%";
                String snowNight = Integer.toString(resultsObj.getJSONObject("Night").getInt("SnowProbability")) + "%";

                String windFromNight = resultsObj.getJSONObject("Night").getJSONObject("Wind").getJSONObject("Direction").getString("Localized");
                String windSpeedNight = Integer.toString((int)resultsObj.getJSONObject("Night").getJSONObject("Wind").getJSONObject("Speed").getDouble("Value")) + " " +
                        resultsObj.getJSONObject("Night").getJSONObject("Wind").getJSONObject("Speed").getString("Unit");

                String cloudsNight = Integer.toString(resultsObj.getJSONObject("Night").getInt("CloudCover"))+ "%";


                fiveDayForecastList.add(new FiveDayForecast(iconDay,weatherTypeDay,tempDay,sunrise,sunset,realfeelDay,precipitationDay,rainDay,snowDay,windFromDay,windSpeedDay,cloudsDay,
                        iconNight,weatherTypeNight,tempNight,moonrise,moonset,realfeelNight,precipitationNight,rainNight,snowNight,windFromNight,windSpeedNight,cloudsNight));
            } }catch (Exception ex){ex.printStackTrace();}}

    private String renderSunTime(String responseDate) {
        String resultData = "";
        String res = "";
        String resString = "";
        for (int i = 1; i < responseDate.length(); i++) {
            // смотрим, был ли слева пробел:
            if ("T".equals(responseDate.substring(i - 1, i)))
                res = responseDate.substring(i);
        }

        for (int i = 1; i < res.length(); i++) {
            // смотрим, был ли слева пробел:
            if ("+".equals(res.substring(i - 1, i)) || "-".equals(res.substring(i - 1, i)))
                resultData = res.substring(0, i - 1);
        }
        for (int i = 1; i < resultData.length(); i++) {
            // смотрим, был ли слева пробел:
            if (":".equals(resultData.substring(i - 1, i)))
                resString = res.substring(0, i - 1);
        }
        return resString;
    }

    public static DetailFiveDayForecastFragment newInstance(int position) {
        DetailFiveDayForecastFragment fragment = new DetailFiveDayForecastFragment();
        Bundle args = new Bundle();
        args.putInt(TAB_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }
}
