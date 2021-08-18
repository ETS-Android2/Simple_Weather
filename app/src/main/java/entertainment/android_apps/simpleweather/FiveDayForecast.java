package entertainment.android_apps.simpleweather;

public class FiveDayForecast {
    private int iconDay;
    private String weatherTypeDay;
    private String tempDay;
    private String sunrise;
    private String sunset;
    private String realFeelDay;
    private String precipitationDay;
    private String rainDay;
    private String snowDay;
    private String windFromDay;
    private String windSpeedDay;
    private String cloudsDay;

    private int iconNight;
    private String weatherTypeNight;
    private String tempNight;
    private String moonrise;
    private String moonset;
    private String realFeelNight;
    private String precipitationNight;
    private String rainNight;
    private String snowNight;
    private String windFromNight;
    private String windSpeedNight;
    private String cloudsNight;

    public FiveDayForecast(int iconDay, String weatherTypeDay, String tempDay, String sunrise,
                           String sunset, String realFeelDay, String precipitationDay, String rainDay,
                           String snowDay, String windFromDay, String windSpeedDay, String cloudsDay,
                           int iconNight, String weatherTypeNight, String tempNight, String moonrise,
                           String moonset, String realFeelNight, String precipitationNight, String rainNight,
                           String snowNight, String windFromNight, String windSpeedNight, String cloudsNight) {
        this.iconDay = iconDay;
        this.weatherTypeDay = weatherTypeDay;
        this.tempDay = tempDay;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.realFeelDay = realFeelDay;
        this.precipitationDay = precipitationDay;
        this.rainDay = rainDay;
        this.snowDay = snowDay;
        this.windFromDay = windFromDay;
        this.windSpeedDay = windSpeedDay;
        this.cloudsDay = cloudsDay;
        this.iconNight = iconNight;
        this.weatherTypeNight = weatherTypeNight;
        this.tempNight = tempNight;
        this.moonrise = moonrise;
        this.moonset = moonset;
        this.realFeelNight = realFeelNight;
        this.precipitationNight = precipitationNight;
        this.rainNight = rainNight;
        this.snowNight = snowNight;
        this.windFromNight = windFromNight;
        this.windSpeedNight = windSpeedNight;
        this.cloudsNight = cloudsNight;
    }

    public int getIconDay() {
        return iconDay;
    }

    public String getWeatherTypeDay() {
        return weatherTypeDay;
    }

    public String getTempDay() {
        return tempDay;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public String getRealFeelDay() {
        return realFeelDay;
    }

    public String getPrecipitationDay() {
        return precipitationDay;
    }

    public String getRainDay() {
        return rainDay;
    }

    public String getSnowDay() {
        return snowDay;
    }

    public String getWindFromDay() {
        return windFromDay;
    }

    public String getWindSpeedDay() {
        return windSpeedDay;
    }

    public String getCloudsDay() {
        return cloudsDay;
    }

    public int getIconNight() {
        return iconNight;
    }

    public String getWeatherTypeNight() {
        return weatherTypeNight;
    }

    public String getTempNight() {
        return tempNight;
    }

    public String getMoonrise() {
        return moonrise;
    }

    public String getMoonset() {
        return moonset;
    }

    public String getRealFeelNight() {
        return realFeelNight;
    }

    public String getPrecipitationNight() {
        return precipitationNight;
    }

    public String getRainNight() {
        return rainNight;
    }

    public String getSnowNight() {
        return snowNight;
    }

    public String getWindFromNight() {
        return windFromNight;
    }

    public String getWindSpeedNight() {
        return windSpeedNight;
    }

    public String getCloudsNight() {
        return cloudsNight;
    }
}
