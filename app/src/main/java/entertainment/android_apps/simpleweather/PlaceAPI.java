package entertainment.android_apps.simpleweather;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.TreeSet;

import entertainment.android_apps.simpleweather.constants.Constants;
import entertainment.android_apps.simpleweather.constants.SearchCity;

import static java.util.stream.Collectors.collectingAndThen;

public class PlaceAPI {
    static ArrayList<SearchCity> searchCityArrayList;
    public static ArrayList<SearchCity> getSearchCityArrayList(){
        return searchCityArrayList;
    }
//    public ArrayList<String> autoComplete(String input) {
//        ArrayList<String> arrayList = new ArrayList<>();
//        HttpURLConnection connection = null;
//        StringBuilder jsonResult = new StringBuilder();
//        Constants constants = new Constants();
//        try {
//            StringBuilder sb = new StringBuilder("https://autocomplete.geocoder.ls.hereapi.com/6.2/suggest.json?");
//            sb.append("apiKey=" + "4apKu72sTvNUZ_Hm0RlRFX6HNKtfNkfuxBtLz5pkwHk");
////            sb.append("&language=" + "ru-ua");
//            sb.append("&query=" + input);
//            URL url = new URL(sb.toString());
//            connection = (HttpURLConnection) url.openConnection();
//            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
//
//            int read;
//
//            char[] buff = new char[1024];
//            while ((read = inputStreamReader.read(buff)) != -1) {
//                jsonResult.append(buff, 0, read);
//            }
//
//            Log.d("JSon", jsonResult.toString());
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (connection != null) {
//                connection.disconnect();
//            }
//        }
//        try {
//            JSONObject jsonObject = new JSONObject(jsonResult.toString());
//            JSONArray suggestions = jsonObject.getJSONArray("suggestions");
//            for (int i = 0; i < suggestions.length(); i++) {
//                if (suggestions.getJSONObject(i).getJSONObject("address").has("city") &&
//                        suggestions.getJSONObject(i).getJSONObject("address").has("country") &&
//                        suggestions.getJSONObject(i).has("countryCode")) {
//
//                    arrayList.add(suggestions.getJSONObject(i).getJSONObject("address").getString("city") + ", " +
//                            suggestions.getJSONObject(i).getJSONObject("address").getString("country") + ", " +
//                            constants.getHashMapValue(suggestions.getJSONObject(i).getString("countryCode")));
//                }
//            }
//            for (String cities : arrayList) {
//                Log.i("CITIES", "CITY = " + cities);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        return arrayList;
//    }
public ArrayList<String> autoComplete(String input) {
    ArrayList<String> arrayList = new ArrayList<>();
    searchCityArrayList = new ArrayList<>();
    HttpURLConnection connection = null;
    StringBuilder jsonResult = new StringBuilder();
    Constants constants = new Constants();
    try {
        StringBuilder sb = new StringBuilder("https://nominatim.openstreetmap.org/search?");
//        sb.append("apiKey=" + "4apKu72sTvNUZ_Hm0RlRFX6HNKtfNkfuxBtLz5pkwHk");
//            sb.append("&language=" + "ru-ua");
        sb.append("city=" + input);
        sb.append("&format=" + "json");
        sb.append("&accept-language=" + "ru");
        URL url = new URL(sb.toString());
        connection = (HttpURLConnection) url.openConnection();
        InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());

        int read;

        char[] buff = new char[1024];
        while ((read = inputStreamReader.read(buff)) != -1) {
            jsonResult.append(buff, 0, read);
        }

        Log.d("JSon", jsonResult.toString());
    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        if (connection != null) {
            connection.disconnect();
        }
    }
    try {
        JSONArray jsonArray = new JSONArray(jsonResult.toString());
        ArrayList<String> withoutDuplicate = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject JsonObject = jsonArray.getJSONObject(i);
            String displayName = JsonObject.getString("display_name");
            String[] strArray = displayName.split("[,]+");
            String splitDisplayName = strArray[0] + ", " + strArray[1] + ", "+ strArray[strArray.length-1];
            String latitude = JsonObject.getString("lat");
            String longitude = JsonObject.getString("lon");

//            boolean qwerty = splitDisplayName.matches(".*[a-zA-Z].*");

            if(!splitDisplayName.matches(".*[a-zA-Z].*")){
                arrayList.add(splitDisplayName);
                searchCityArrayList.add(new SearchCity(splitDisplayName,latitude,longitude));
            }

        }

//        LinkedHashSet<SearchCity> hashSet = new LinkedHashSet<>(searchCityArrayList);
//
//        ArrayList<SearchCity> listWithoutDuplicates = new ArrayList<>(hashSet);
        for (String cities : arrayList) {
            Log.i("CITIES", "CITY = " + cities);
        }
    } catch (JSONException e) {
        e.printStackTrace();
    }

    return arrayList;
}
//    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list)
//    {
//
//        // Create a new ArrayList
//        ArrayList<T> newList = new ArrayList<T>();
//
//        // Traverse through the first list
//        for (T element : list) {
//
//            // If this element is not present in newList
//            // then add it
//            if (!newList.contains(element)) {
//
//                newList.add(element);
//            }
//        }
//
//        // return the new list
//        return newList;
//    }

}
