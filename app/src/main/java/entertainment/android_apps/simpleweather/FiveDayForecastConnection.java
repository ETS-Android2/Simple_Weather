package entertainment.android_apps.simpleweather;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class FiveDayForecastConnection {
    private final static String WEATHER_BASE_URL = "https://dataservice.accuweather.com/forecasts/v1/daily/5day/";
    private static final String TAG = "NetworkUtils";
    //Поиск города - начало
//    private final static String API_KEY = "aR3Gg1CK8ZEbREKRMl5y6KdKsrxG2lJo";
    private final static String API_KEY = "xeSwGmxYU0zII4Lyyhtkad6YTnxmAfKa";
//    private final static String API_KEY = "YXxYWGBuaBjbAUKdxIqATZRUNI88ukGn";
//    private final static String API_KEY = "YFVjccNlCLpv82ZY83b9LO3pA8bZ3Z47";
//    private final static String API_KEY = "iWQlZ3cGl5TzpmzhdXONz4NqA1alhceb";
//    private final static String API_KEY = "I4Ud5NYiCa48buJCN8kGebpgK1QLwe0N";
//    private final static String API_KEY = "Yfr0VvORcPqjtx8512O9A4FdAEABPB3f";
//    private final static String API_KEY = "6WZGphj9AiO0XY1N7FgJc0J5xgu9qAuO";
//    private final static String API_KEY = "5TVlRNdy2MScPsfy6OdLc5a84BKsGAyo";
    private final static String PARAM_API_KEY = "apikey";
    private final static String LANGUAGE = "ru-ua";
    private final static String PARAM_LANGUAGE = "language";
    private final static String PARAM_DETAILS = "details";
    private final static String DETAILS = "true";
    private final static String PARAM_METRIC = "metric";
    private final static String METRIC = "true";
    //Поиск города - конец

    public URL buildUrlForFiveDayForecast(String WEATHER_KEY){
        String WEATHER_URL = WEATHER_BASE_URL + WEATHER_KEY + "?";
        Uri buildUriCity = Uri.parse(WEATHER_URL).buildUpon().
                appendQueryParameter(PARAM_API_KEY,API_KEY).
                appendQueryParameter(PARAM_LANGUAGE,LANGUAGE).
                appendQueryParameter(PARAM_DETAILS,DETAILS).
                appendQueryParameter(PARAM_METRIC,METRIC).build();

        URL url = null;
        try {
            url = new URL(buildUriCity.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public String getResponseForFiveDay(URL url) throws IOException {
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
