package entertainment.android_apps.simpleweather;

public class Hourly {
    private String hour;
    private String deegrees;
    private int image;
    private String wind;

    public Hourly(String hour, String deegrees, int image, String wind) {
        this.hour = hour;
        this.deegrees = deegrees;
        this.image = image;
        this.wind = wind;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getDeegrees() {
        return deegrees;
    }

    public void setDeegrees(String deegrees) {
        this.deegrees = deegrees;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }
}
