package entertainment.android_apps.simpleweather;

public class FiveDayForecast_Day {
    private String dayTitle;
    private String date;
    public FiveDayForecast_Day(String dayTitle, String date){
        this.dayTitle = dayTitle;
        this.date = date;
    }

    public String getDayTitle() {
        return dayTitle;
    }

    public void setDayTitle(String dayTitle) {
        this.dayTitle = dayTitle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
