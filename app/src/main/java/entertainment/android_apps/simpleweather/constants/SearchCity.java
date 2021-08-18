package entertainment.android_apps.simpleweather.constants;

public class SearchCity{
    private String displayName;
    private String latitude;
    private String longitude;

    public SearchCity(String displayName, String latitude, String longitude) {
        this.displayName = displayName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
