package entertainment.android_apps.simpleweather;

import android.app.Activity;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataStorage extends AppCompatActivity {
//    private final String lastCityKey = "city";
//    private final String lastCityValue = "Киев";
//    private final String lastCityValue = "";

    private final String showDialogKey = "showDialog";
    private final boolean showDialogValue = true;

    private final String lastUpdatedCityKey = "lastUpdatedCity";
//    private final String lastUpdatedCityValue = "Киев";
    private final String lastUpdatedCityValue = "";

//    private final String lastUpdatedCountryCodeKey = "lastUpdatedCountryCode";
//    //    private final String lastUpdatedCityValue = "Киев";
//    private final String lastUpdatedCountryCodeValue = "";


    private final String latitudeKey = "latitudeKey";
    private final String latitudeValue = "";

    private final String longitudeKey = "longitudeKey";
    private final String longitudeValue = "";


    private final String searchCityLatitudeKey = "searchCityLatitudeKey";
    private final String searchCityLatitudeValue = "";

    private final String searchCityLongitudeKey = "searchCityLongitudeKey";
    private final String searchCityLongitudeValue = "";


    private final String lastCurrentCityKey = "lastCurrentCity";
    private final String lastCurrentCityValue = "";


    private final String weatherResponseKey = "weatherResponse";
    private final String weatherResponseValue = "";

    private final String currentWeatherResponseKey = "currentWeatherResponse";
    private final String currentWeatherResponseValue = "";


    private final String hourlyResponseKey = "hourlyResponse";
    private final String hourlyResponseValue = "";

    private final String currentHourlyResponseKey = "currentHourlyResponseKey";
    private final String currentHourlyResponseValue = "";

    private final String lastUpdateWeatherKey = "lastUpdateWeatherKey";
    private final String lastUpdateWeatherValue = "";

    private final String lastUpdateDateKey = "lastUpdateDate";
    private final long lastUpdateDateValue = 0;

    private final String lastCurrentUpdateDateKey = "lastCurrentUpdateDate";
    private final long lastCurrentUpdateDateValue = 0;

    private final String lastCurrentUpdateWeatherKey = "lastCurrentUpdateWeather";
    private final String lastCurrentUpdateWeatherValue = "";

//    private final String lastCurrentWeatherIconKey = "lastCurrentWeatherIconKey";
//    private final int lastCurrentWeatherIconValue = 0;
//
//    private final String lastCurrentWeatherTypeKey = "lastCurrentWeatherType";
//    private final String lastCurrentWeatherTypeValue = "";
//
//    private final String lastCurrentDegreesKey = "lastCurrentDegreesKey";
//    private final String lastCurrentDegreesValue = "";
//
//    private final String lastSunriseKey = "lastSunriseKey";
//    private final String lastSunriseValue = "";
//
//    private final String lastSunsetKey = "lastSunsetKey";
//    private final String lastSunsetValue = "";
//
//    private final String firstDayIconKey = "firstDayIconKey";
//    private final int firstDayIconValue = 0;
//
//    private final String firstDayKey = "firstDayKey";
//    private final String firstDayValue = "";
//
//    private final String firstDayTypeKey = "firstDayTypeKey";
//    private final String firstDayTypeValue = "";
//
//    private final String firstDayTemperatureKey = "firstDayTemperatureKey";
//    private final String firstDayTemperatureValue = "";
//
//    private final String secondDayIconKey = "secondDayIconKey";
//    private final int secondDayIconValue = 0;
//
//    private final String secondDayKey = "secondDayKey";
//    private final String secondDayValue = "";
//
//    private final String secondDayTypeKey = "secondDayTypeKey";
//    private final String secondDayTypeValue = "";
//
//    private final String secondDayTemperatureKey = "secondDayTemperatureKey";
//    private final String secondDayTemperatureValue = "";
//
//    private final String thirdDayIconKey = "thirdDayIconKey";
//    private final int thirdDayIconValue = 0;
//
//    private final String thirdDayKey = "thirdDayKey";
//    private final String thirdDayValue = "";
//
//    private final String thirdDayTypeKey = "thirdDayTypeKey";
//    private final String thirdDayTypeValue = "";
//
//    private final String thirdDayTemperatureKey = "thirdDayTemperatureKey";
//    private final String thirdDayTemperatureValue = "";
//
//    private final String separatorKey = "separator";
//    private final int separatorValue = 0;
//
//    private final String windKey = "windKey";
//    private final String windValue = "";
//
//    private final String realFeelKey = "realFeelKey";
//    private final String realFeelValue = "";
//
//    private final String visibilityKey = "visibilityKey";
//    private final String visibilityValue = "";
//
//    private final String pressureKey = "pressureKey";
//    private final String pressureValue = "";
//
//    private final String rainKey = "rainKey";
//    private final String rainValue = "";
//
//    private final String thunderKey = "thunderKey";
//    private final String thunderValue = "";

    private final String snowKey = "snowKey";
    private final String snowValue = "";

    private final String fiveDayForecastResponseKey = "fiveDayForecastResponseKey";
    private final String fiveDayForecastResponseValue = "";

    private final String currentFiveDayForecastResponseKey = "currentFiveDayForecastResponse";
    private final String currentFiveDayForecastResponseValue = "";

//    private final String mainBackgroundImageKey = "mainBackgroundImageKey";
//    private final int mainBackgroundImageValue = 0;

    private final String exitKey = "exitKey";
    private final boolean exitValue = false;

    private final String whatUpdateKey = "whatUpdate";
    private final boolean whatUpdateValue = false;


    private SharedPreferences sharedPreferences;

    public DataStorage(Activity activity){
        sharedPreferences = activity.getSharedPreferences("pref",Activity.MODE_PRIVATE);
    }
//    public String getCity() {
//        return sharedPreferences.getString(lastCityKey, lastCityValue);
//    }
//
//    public void setCity(String city) {
//        sharedPreferences.edit().putString(lastCityKey,city).apply();
//    }

    public String getWeatherResponse() {
        return sharedPreferences.getString(weatherResponseKey, weatherResponseValue);
    }

    public void setWeatherResponse(String weatherResponse) {
        sharedPreferences.edit().putString(weatherResponseKey,weatherResponse).apply();
    }


    public String getCurrentWeatherResponse() {
        return sharedPreferences.getString(currentWeatherResponseKey, currentWeatherResponseValue);
    }

    public void setCurrentWeatherResponse(String currentWeatherResponse) {
        sharedPreferences.edit().putString(currentWeatherResponseKey,currentWeatherResponse).apply();
    }

    public String getHourlyResponse() {
        return sharedPreferences.getString(hourlyResponseKey, hourlyResponseValue);
    }

    public void setHourlyResponse(String hourlyResponse) {
        sharedPreferences.edit().putString(hourlyResponseKey,hourlyResponse).apply();
    }


    public String getCurrentHourlyResponse() {
        return sharedPreferences.getString(currentHourlyResponseKey, currentHourlyResponseValue);
    }

    public void setCurrentHourlyResponse(String currentHourlyResponse) {
        sharedPreferences.edit().putString(currentHourlyResponseKey,currentHourlyResponse).apply();
    }

    public String getFiveDayForecastResponse() {
        return sharedPreferences.getString(fiveDayForecastResponseKey, fiveDayForecastResponseValue);
    }

    public void setFiveDayForecastResponse(String fiveDayForecastResponse) {
        sharedPreferences.edit().putString(fiveDayForecastResponseKey,fiveDayForecastResponse).apply();
    }

    public String getCurrentFiveDayForecastResponse() {
        return sharedPreferences.getString(currentFiveDayForecastResponseKey, currentFiveDayForecastResponseValue);
    }

    public void setCurrentFiveDayForecastResponse(String currentFiveDayForecastResponse) {
        sharedPreferences.edit().putString(currentFiveDayForecastResponseKey,currentFiveDayForecastResponse).apply();
    }


    public String getLastUpdatedCity() {
        return sharedPreferences.getString(lastUpdatedCityKey,lastUpdatedCityValue);
    }

    public void setLastUpdatedCity(String city) {
        sharedPreferences.edit().putString(lastUpdatedCityKey,city).apply();
    }

//    public String getLastUpdatedCountryCode() {
//        return sharedPreferences.getString(lastUpdatedCountryCodeKey,lastUpdatedCountryCodeValue);
//    }
//
//    public void setLastUpdatedCountryCode(String countryCode) {
//        sharedPreferences.edit().putString(lastUpdatedCountryCodeKey,countryCode).apply();
//    }


    public long getLastUpdatedDate() {
        return sharedPreferences.getLong(lastUpdateDateKey,lastUpdateDateValue);
    }

    public void setLastUpdatedDate(long date) {
        sharedPreferences.edit().putLong(lastUpdateDateKey,date).apply();
    }


    public long getLastCurrentUpdateDate() {
        return sharedPreferences.getLong(lastCurrentUpdateDateKey,lastCurrentUpdateDateValue);
    }

    public void setLastCurrentUpdateDate(long date) {
        sharedPreferences.edit().putLong(lastCurrentUpdateDateKey,date).apply();
    }


    public String getLastCurrentCity() {
        return sharedPreferences.getString(lastCurrentCityKey,lastCurrentCityValue);
    }

    public void setLastCurrentCity(String currentCity) {
        sharedPreferences.edit().putString(lastCurrentCityKey,currentCity).apply();
    }


    public String getSearchCityLatitude() {
        return sharedPreferences.getString(searchCityLatitudeKey,searchCityLatitudeValue);
    }

    public void setSearchCityLatitude(String latitude) {
        sharedPreferences.edit().putString(searchCityLatitudeKey,latitude).apply();
    }

    public String getSearchCityLongitude() {
        return sharedPreferences.getString(searchCityLongitudeKey,searchCityLongitudeValue);
    }

    public void setSearchCityLongitude(String longitude) {
        sharedPreferences.edit().putString(searchCityLongitudeKey,longitude).apply();
    }



    public String getLatitude() {
        return sharedPreferences.getString(latitudeKey,latitudeValue);
    }

    public void setLatitude(String latitude) {
        sharedPreferences.edit().putString(latitudeKey,latitude).apply();
    }

    public String getLongitude() {
        return sharedPreferences.getString(longitudeKey,longitudeValue);
    }

    public void setLongitude(String longitude) {
        sharedPreferences.edit().putString(longitudeKey,longitude).apply();
    }





    public String getLastUpdateWeather() {
        return sharedPreferences.getString(lastUpdateWeatherKey,lastUpdateWeatherValue);
    }

    public void setLastUpdateWeather(String lastUpdateWeather) {
        sharedPreferences.edit().putString(lastUpdateWeatherKey,lastUpdateWeather).apply();
    }


    public String getLastCurrentUpdateWeather() {
        return sharedPreferences.getString(lastCurrentUpdateWeatherKey,lastCurrentUpdateWeatherValue);
    }

    public void setLastCurrentUpdateWeather(String lastCurrentUpdateWeather) {
        sharedPreferences.edit().putString(lastCurrentUpdateWeatherKey,lastCurrentUpdateWeather).apply();
    }


//    public int getCurrentWeatherIcon() {
//        return sharedPreferences.getInt(lastCurrentWeatherIconKey,lastCurrentWeatherIconValue);
//    }
//
//    public void setLastCurrentWeatherIcon(int currentWeatherIcon) {
//        sharedPreferences.edit().putInt(lastCurrentWeatherIconKey,currentWeatherIcon).apply();
//    }
//
//    public String getLastCurrentWeatherType() {
//        return sharedPreferences.getString(lastCurrentWeatherTypeKey,lastCurrentWeatherTypeValue);
//    }
//
//    public void setLastCurrentWeatherType(String currentWeatherType) {
//        sharedPreferences.edit().putString(lastCurrentWeatherTypeKey,currentWeatherType).apply();
//    }
//
//    public String getLastCurrentDegrees() {
//        return sharedPreferences.getString(lastCurrentDegreesKey,lastCurrentDegreesValue);
//    }
//
//    public void setLastCurrenttDegrees(String CurrentDegrees) {
//        sharedPreferences.edit().putString(lastCurrentDegreesKey,CurrentDegrees).apply();
//    }
//
//    public String getLastSunrise() {
//        return sharedPreferences.getString(lastSunriseKey,lastSunriseValue);
//    }
//
//    public void setLastSunrise(String sunrise) {
//        sharedPreferences.edit().putString(lastSunriseKey,sunrise).apply();
//    }
//
//    public String getLastSunset() {
//        return sharedPreferences.getString(lastSunsetKey,lastSunsetValue);
//    }
//
//    public void setLastSunset(String sunset) {
//        sharedPreferences.edit().putString(lastSunsetKey,sunset).apply();
//    }
//
//    public int getFirstDayIcon() {
//        return sharedPreferences.getInt(firstDayIconKey,firstDayIconValue);
//    }
//
//    public void setFirstDayIcon(int firstDayIcon) {
//        sharedPreferences.edit().putInt(firstDayIconKey,firstDayIcon).apply();
//    }
//
//    public String getFirstDay() {
//        return sharedPreferences.getString(firstDayKey,firstDayValue);
//    }
//
//    public void setFirstDay(String firstDay) {
//        sharedPreferences.edit().putString(firstDayKey,firstDay).apply();
//    }
//
//    public String getFirstDayType() {
//        return sharedPreferences.getString(firstDayTypeKey,firstDayTypeValue);
//    }
//
//    public void setFirstDayType(String firstDayType) {
//        sharedPreferences.edit().putString(firstDayTypeKey,firstDayType).apply();
//    }
//
//    public String getFirstDayTemperature() {
//        return sharedPreferences.getString(firstDayTemperatureKey,firstDayTemperatureValue);
//    }
//
//    public void setFirstDayTemperature(String firstDayTemperature) {
//        sharedPreferences.edit().putString(firstDayTemperatureKey,firstDayTemperature).apply();
//    }
//
//    public int getSecondDayIcon() {
//        return sharedPreferences.getInt(secondDayIconKey,secondDayIconValue);
//    }
//
//    public void setSecondDayIcon(int secondDayIcon) {
//        sharedPreferences.edit().putInt(secondDayIconKey,secondDayIcon).apply();
//    }
//
//    public String getSecondDay() {
//        return sharedPreferences.getString(secondDayKey,secondDayValue);
//    }
//
//    public void setSecondDay(String secondDay) {
//        sharedPreferences.edit().putString(secondDayKey,secondDay).apply();
//    }
//
//    public String getSecondDayType() {
//        return sharedPreferences.getString(secondDayTypeKey,secondDayTypeValue);
//    }
//
//    public void setSecondDayType(String secondDayType) {
//        sharedPreferences.edit().putString(secondDayTypeKey,secondDayType).apply();
//    }
//
//    public String getSecondDayTemperature() {
//        return sharedPreferences.getString(secondDayTemperatureKey,secondDayTemperatureValue);
//    }
//
//    public void setSecondDayTemperature(String secondDayTemperature) {
//        sharedPreferences.edit().putString(secondDayTemperatureKey,secondDayTemperature).apply();
//    }
//
//    public int getThirdDayIcon() {
//        return sharedPreferences.getInt(thirdDayIconKey,thirdDayIconValue);
//    }
//
//    public void setThirdDayIcon(int thirdDayIcon) {
//        sharedPreferences.edit().putInt(thirdDayIconKey,thirdDayIcon).apply();
//    }
//
//    public String getThirdDay() {
//        return sharedPreferences.getString(thirdDayKey,thirdDayValue);
//    }
//
//    public void setThirdDay(String thirdDay) {
//        sharedPreferences.edit().putString(thirdDayKey,thirdDay).apply();
//    }
//
//    public String getThirdDayType() {
//        return sharedPreferences.getString(thirdDayTypeKey,thirdDayTypeValue);
//    }
//
//    public void setThirdDayType(String thirdDayType) {
//        sharedPreferences.edit().putString(thirdDayTypeKey,thirdDayType).apply();
//    }
//
//    public String getThirdDayTemperature() {
//        return sharedPreferences.getString(thirdDayTemperatureKey,thirdDayTemperatureValue);
//    }
//
//    public void setThirdDayTemperature(String thirdDayTemperature) {
//        sharedPreferences.edit().putString(thirdDayTemperatureKey,thirdDayTemperature).apply();
//    }
//
//    public int getSeparator() {
//        return sharedPreferences.getInt(separatorKey,separatorValue);
//    }
//
//    public void setSeparator(int separator) {
//        sharedPreferences.edit().putInt(separatorKey,separator).apply();
//    }
//
//    public String getWind() {
//        return sharedPreferences.getString(windKey,windValue);
//    }
//
//    public void setWind(String wind) {
//        sharedPreferences.edit().putString(windKey,wind).apply();
//    }
//
//    public String getRealFeel() {
//        return sharedPreferences.getString(realFeelKey,realFeelValue);
//    }
//
//    public void setRealFeel(String realFeel) {
//        sharedPreferences.edit().putString(realFeelKey,realFeel).apply();
//    }
//
//    public String getVisibility() {
//        return sharedPreferences.getString(visibilityKey,visibilityValue);
//    }
//
//    public void setVisibility(String visibility) {
//        sharedPreferences.edit().putString(visibilityKey,visibility).apply();
//    }
//
//    public String getPressure() {
//        return sharedPreferences.getString(pressureKey,pressureValue);
//    }
//
//    public void setPressure(String pressure) {
//        sharedPreferences.edit().putString(pressureKey,pressure).apply();
//    }
//
//    public String getRain() {
//        return sharedPreferences.getString(rainKey,rainValue);
//    }
//
//    public void setRain(String rain) {
//        sharedPreferences.edit().putString(rainKey,rain).apply();
//    }
//
//    public String getThunder() {
//        return sharedPreferences.getString(thunderKey,thunderValue);
//    }
//
//    public void setThunder(String thunder) {
//        sharedPreferences.edit().putString(thunderKey,thunder).apply();
//    }

    public String getSnow() {
        return sharedPreferences.getString(snowKey,snowValue);
    }

    public void setSnow(String snow) {
        sharedPreferences.edit().putString(snowKey,snow).apply();
    }

//    public int getMainBackgroundImage() {
//        return sharedPreferences.getInt(mainBackgroundImageKey,mainBackgroundImageValue);
//    }

//    public void setMainBackgroundImage(int mainBackgroundImage) {
//        sharedPreferences.edit().putInt(mainBackgroundImageKey,mainBackgroundImage).apply();
//    }

    public boolean getExit() {
        return sharedPreferences.getBoolean(exitKey,exitValue);
    }
    public void setExit(boolean exit) {
        sharedPreferences.edit().putBoolean(exitKey,exit).apply();
    }

    public boolean getWhatUpdate() {
        return sharedPreferences.getBoolean(whatUpdateKey,whatUpdateValue);
    }

    public void setWhatUpdate(boolean whatUpdate) {
        sharedPreferences.edit().putBoolean(whatUpdateKey,whatUpdate).apply();
    }

    public boolean getShowDialog() {
        return sharedPreferences.getBoolean(showDialogKey,showDialogValue);
    }

    public void setShowDialog(boolean dialog) {
        sharedPreferences.edit().putBoolean(showDialogKey,dialog).apply();
    }

}
