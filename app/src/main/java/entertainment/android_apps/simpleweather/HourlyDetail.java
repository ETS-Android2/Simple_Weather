package entertainment.android_apps.simpleweather;

public class HourlyDetail {
    private String day;
    private String time;
    private String weatherType;
    private String temperature;
    private double lineChartTemp;
    private int weatherImage;
    private String realFeel;
    private String dewPoint;
    private String visibility;
    private String windDirection;
    private String windSpeed;
    private String uvIndex;
    private String rain;
    private String snow;
    private String clouds;

    public HourlyDetail(String day, String time, String weatherType, String temperature, int weatherImage,
                        String realFeel, String dewPoint, String visibility, String windDirection, String windSpeed,
                        String uvIndex, String rain, String snow, String clouds, double lineChartTemp) {
        this.day = day;
        this.time = time;
        this.weatherType = weatherType;
        this.temperature = temperature;
        this.weatherImage = weatherImage;
        this.realFeel = realFeel;
        this.dewPoint = dewPoint;
        this.visibility = visibility;
        this.windDirection = windDirection;
        this.windSpeed = windSpeed;
        this.uvIndex = uvIndex;
        this.rain = rain;
        this.snow = snow;
        this.clouds = clouds;
        this.lineChartTemp = lineChartTemp;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(String weatherType) {
        this.weatherType = weatherType;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public int getWeatherImage() {
        return weatherImage;
    }

    public void setWeatherImage(int weatherImage) {
        this.weatherImage = weatherImage;
    }

    public String getRealFeel() {
        return realFeel;
    }

    public void setRealFeel(String realFeel) {
        this.realFeel = realFeel;
    }

    public String getDewPoint() {
        return dewPoint;
    }

    public void setDewPoint(String dewPoint) {
        this.dewPoint = dewPoint;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getUvIndex() {
        return uvIndex;
    }

    public void setUvIndex(String uvIndex) {
        this.uvIndex = uvIndex;
    }

    public String getRain() {
        return rain;
    }

    public void setRain(String rain) {
        this.rain = rain;
    }

    public String getSnow() {
        return snow;
    }

    public void setSnow(String snow) {
        this.snow = snow;
    }

    public String getClouds() {
        return clouds;
    }

    public void setClouds(String clouds) {
        this.clouds = clouds;
    }

    public double getLineChartTemp() {
        return lineChartTemp;
    }

    public void setLineChartTemp(double lineChartTemp) {
        this.lineChartTemp = lineChartTemp;
    }
}
