package entertainment.android_apps.simpleweather;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class LatitudeLongitudeConnection {
    private final static String BASE_URL = "https://dataservice.accuweather.com/locations/v1/cities/search?";


   // https://dataservice.accuweather.com/locations/v1/cities/geoposition/search?

    // apikey=Yfr0VvORcPqjtx8512O9A4FdAEABPB3f&q=50.431204449999996%2C30.53090410908157&language=ru&details=true

    //    private final static String WEATHER_BASE_URL = "https://dataservice.accuweather.com/locations/v1/cities/search?q=%s&language=ru-ua";
    private static final String TAG = "NetworkUtils";
    //Поиск города - начало
//    private final static String API_KEY = "aR3Gg1CK8ZEbREKRMl5y6KdKsrxG2lJo";
    //    private final static String API_KEY = "xeSwGmxYU0zII4Lyyhtkad6YTnxmAfKa";
//    private final static String API_KEY = "YXxYWGBuaBjbAUKdxIqATZRUNI88ukGn";
//    private final static String API_KEY = "YFVjccNlCLpv82ZY83b9LO3pA8bZ3Z47";
    private final static String API_KEY_MAIN = "iWQlZ3cGl5TzpmzhdXONz4NqA1alhceb";
    private final static String API_KEY_NOTIFICATION = "I4Ud5NYiCa48buJCN8kGebpgK1QLwe0N";
//    private final static String API_KEY = "Yfr0VvORcPqjtx8512O9A4FdAEABPB3f";
    private final static String API_KEY_WIDGET = "6WZGphj9AiO0XY1N7FgJc0J5xgu9qAuO";
//    private final static String API_KEY_MAIN = "5TVlRNdy2MScPsfy6OdLc5a84BKsGAyo";

    private final static String PARAM_API_KEY = "apikey";
    private final static  String LANGUAGE = "ru-ua";
    private final static String PARAM_LANGUAGE = "language";
    private final static String PARAM_LATITUDE_LONGITUDE = "q";
    //Поиск города - конец

    public URL buildUrlForLatitudeLongitude(String Latitude, String Longitude){
        Uri buildUriCity = Uri.parse(BASE_URL).buildUpon().
                appendQueryParameter(PARAM_API_KEY,API_KEY_MAIN).
                appendQueryParameter(PARAM_LATITUDE_LONGITUDE,Latitude + ", " + Longitude).
                appendQueryParameter(PARAM_LANGUAGE,LANGUAGE).build();
        URL url = null;
        try {
            url = new URL(buildUriCity.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public String getResponseForLatitudeLongitudeFromHttpUrl(URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        int x = 0;
        try {
            InputStream in  = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if(hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            return "ExceptionHappened";
        }
        finally {
            urlConnection.disconnect();
        }
//        return null;
    }


    public URL buildUrlLatitudeLongitude_Notification(String Latitude, String Longitude){
        Uri buildUriCity = Uri.parse(BASE_URL).buildUpon().
                appendQueryParameter(PARAM_API_KEY,API_KEY_NOTIFICATION).
                appendQueryParameter(PARAM_LATITUDE_LONGITUDE,Latitude + ", " + Longitude).
                appendQueryParameter(PARAM_LANGUAGE,LANGUAGE).build();
        URL url = null;
        try {
            url = new URL(buildUriCity.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public URL buildUrlLatitudeLongitude_Widget(String Latitude, String Longitude){
        Uri buildUriCity = Uri.parse(BASE_URL).buildUpon().
                appendQueryParameter(PARAM_API_KEY,API_KEY_WIDGET).
                appendQueryParameter(PARAM_LATITUDE_LONGITUDE,Latitude + ", " + Longitude).
                appendQueryParameter(PARAM_LANGUAGE,LANGUAGE).build();
        URL url = null;
        try {
            url = new URL(buildUriCity.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

}
