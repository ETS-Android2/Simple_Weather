package entertainment.android_apps.simpleweather;

import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Scanner;

public class CityConnection {
//    private final static String WEATHER_BASE_URL = "https://dataservice.accuweather.com/locations/v1/cities/search?";
    private final static String WEATHER_BASE_URL = "https://dataservice.accuweather.com/locations/v1/cities/geoposition/search?";
//    "https://dataservice.accuweather.com/locations/v1/cities/DE/search?apikey=Yfr0VvORcPqjtx8512O9A4FdAEABPB3f&q=brest&language=en&details=true"
    private final static String QWERTY = "https://dataservice.accuweather.com/locations/v1/cities/";
    private final static String UIOP = "/search?";

//    http://dataservice.accuweather.com/forecasts/v1/daily/1day/349727?apikey=aR3Gg1CK8ZEbREKRMl5y6KdKsrxG2lJo&language=ru-ua&details=true&metric=true
//    http://dataservice.accuweather.com/locations/v1/cities/search?apikey=aR3Gg1CK8ZEbREKRMl5y6KdKsrxG2lJo&q=Киев&language=ru-ua&details=true
//    private final static String WEATHER_BASE_URL = "https://dataservice.accuweather.com/locations/v1/cities/search?q=%s&language=ru-ua";
    private static final String TAG = "NetworkUtils";
    //Поиск города - начало
    private final static String API_KEY = "aR3Gg1CK8ZEbREKRMl5y6KdKsrxG2lJo";
    Locale currentLocale;
    String locale = "";
//    private final static String API_KEY = "xeSwGmxYU0zII4Lyyhtkad6YTnxmAfKa";
//    private final static String API_KEY = "YXxYWGBuaBjbAUKdxIqATZRUNI88ukGn";
//    private final static String API_KEY = "YFVjccNlCLpv82ZY83b9LO3pA8bZ3Z47";
//    private final static String API_KEY = "iWQlZ3cGl5TzpmzhdXONz4NqA1alhceb";
//    private final static String API_KEY = "I4Ud5NYiCa48buJCN8kGebpgK1QLwe0N";
//    private final static String API_KEY = "Yfr0VvORcPqjtx8512O9A4FdAEABPB3f";
//    private final static String API_KEY = "6WZGphj9AiO0XY1N7FgJc0J5xgu9qAuO";
//    private final static String API_KEY = "5TVlRNdy2MScPsfy6OdLc5a84BKsGAyo";
    CityConnection(){
        currentLocale = Locale.getDefault();
    }
    private final static String PARAM_API_KEY = "apikey";
    private final static  String LANGUAGE = "ru-ua";
    private final static String PARAM_LANGUAGE = "language";
    private final static String PARAM_LATITUDE_LONGITUDE = "q";
    //Поиск города - конец

    public URL buildUrlForCity(String latitude, String longitude){
//        Uri buildUriCity;
//        Uri buildUriCity = Uri.parse(WEATHER_BASE_URL).buildUpon().
        Uri buildUriCity = Uri.parse(WEATHER_BASE_URL).buildUpon().
                appendQueryParameter(PARAM_API_KEY,API_KEY).
                appendQueryParameter(PARAM_LATITUDE_LONGITUDE,latitude + ", " + longitude).
                appendQueryParameter(PARAM_LANGUAGE,LANGUAGE).build();
        URL url = null;
        locale = currentLocale.toString();
//        switch (locale){
//            case "ru_UA":
//                buildUriCity = Uri.parse(WEATHER_BASE_URL).buildUpon().
//                        appendQueryParameter(PARAM_API_KEY,API_KEY).
//                        appendQueryParameter(PARAM_LANGUAGE,LANGUAGE).
//                        appendQueryParameter(PARAM_CITY,city).build();
//                break;
//            case "en_US":
//                buildUriCity = Uri.parse(WEATHER_BASE_URL).buildUpon().
//                        appendQueryParameter(PARAM_API_KEY,API_KEY).
//                        appendQueryParameter(PARAM_LANGUAGE,LANGUAGE_US).
//                        appendQueryParameter(PARAM_CITY,city).build();
//                break;
//            default:
//                buildUriCity = Uri.parse(WEATHER_BASE_URL).buildUpon().
//                        appendQueryParameter(PARAM_API_KEY,API_KEY).
//                        appendQueryParameter(PARAM_LANGUAGE,LANGUAGE).
//                        appendQueryParameter(PARAM_CITY,city).build();
//        }
        try {
            url = new URL(buildUriCity.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

//    public String getResponseForCityFromHttpUrl(URL url) throws IOException {
//
//        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//
//        try {
//            InputStream in  = urlConnection.getInputStream();
//            Scanner scanner = new Scanner(in);
//            scanner.useDelimiter("\\A");
//            boolean hasInput = scanner.hasNext();
//            if(hasInput) {
//                return scanner.next();
//            } else {
//                return null;
//            }
//        } finally {
//            urlConnection.disconnect();
//        }
//    }

        public String getResponseForCityFromHttpUrl(URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

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


}
