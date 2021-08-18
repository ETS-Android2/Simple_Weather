package entertainment.android_apps.simpleweather;

import java.io.Serializable;

public class FiveDay implements Serializable {
    private String day;
    private String date;
    private String dayTranslate;
    private int imgDay;
    private String dayWeatherType;
    private String dayTemperature;
    private String wind;
    private String windNight;
    private String nightTemperature;
    private String nightTranslate;
    private int imgNight;
    private String nightWeatherType;

    public FiveDay(String day, String date, String dayTranslate, int imgDay, String dayWeatherType,
                   String dayTemperature, String wind, String windNight, String nightTemperature,
                   String nightTranslate, int imgNight, String nightWeatherType) {
        this.day = day;
        this.date = date;
        this.dayTranslate = dayTranslate;
        this.imgDay = imgDay;
        this.dayWeatherType = dayWeatherType;
        this.dayTemperature = dayTemperature;
        this.wind = wind;
        this.windNight = windNight;
        this.nightTemperature = nightTemperature;
        this.nightTranslate = nightTranslate;
        this.imgNight = imgNight;
        this.nightWeatherType = nightWeatherType;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDayTranslate() {
        return dayTranslate;
    }

    public void setDayTranslate(String dayTranslate) {
        this.dayTranslate = dayTranslate;
    }

    public int getImgDay() {
        return imgDay;
    }

    public void setImgDay(int imgDay) {
        this.imgDay = imgDay;
    }

    public String getDayWeatherType() {
        return dayWeatherType;
    }

    public void setDayWeatherType(String dayWeatherType) {
        this.dayWeatherType = dayWeatherType;
    }

    public String getDayTemperature() {
        return dayTemperature;
    }

    public void setDayTemperature(String dayTemperature) {
        this.dayTemperature = dayTemperature;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getWindNight() {
        return windNight;
    }

    public void setWindNight(String windNight) {
        this.windNight = windNight;
    }

    public String getNightTemperature() {
        return nightTemperature;
    }

    public void setNightTemperature(String nightTemperature) {
        this.nightTemperature = nightTemperature;
    }

    public String getNightTranslate() {
        return nightTranslate;
    }

    public void setNightTranslate(String nightTranslate) {
        this.nightTranslate = nightTranslate;
    }

    public int getImgNight() {
        return imgNight;
    }

    public void setImgNight(int imgNight) {
        this.imgNight = imgNight;
    }

    public String getNightWeatherType() {
        return nightWeatherType;
    }

    public void setNightWeatherType(String nightWeatherType) {
        this.nightWeatherType = nightWeatherType;
    }
}
