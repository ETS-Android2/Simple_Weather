package entertainment.android_apps.simpleweather;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class WeatherWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for(int appWidgetId:appWidgetIds){
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
            RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.weather_widget);
            Locale currentLocale = context.getResources().getConfiguration().locale;
            Calendar cal = GregorianCalendar.getInstance(TimeZone.getDefault(), currentLocale);
            String dayName = cal.getDisplayName(cal.DAY_OF_WEEK, Calendar.SHORT, currentLocale);
            String day = "", month = "";
            for (int i = 0; i < dayName.length(); i++) {
                if(i==0) day = day + dayName.substring(0, 1).toUpperCase();
                else day = day + dayName.substring(i, i + 1);
            }
            String monthName = cal.getDisplayName(cal.MONTH, Calendar.LONG, currentLocale);
            for (int i = 0; i < monthName.length(); i++) {
                if(i==0) month = month + monthName.substring(0, 1).toUpperCase();
                else month = month + monthName.substring(i, i + 1);
            }
            views.setCharSequence(R.id.textClock_currentDate,"setFormat12Hour","'" + day + "', dd '" + month);
            views.setCharSequence(R.id.textClock_currentDate,"setFormat24Hour","'" + day + "', dd '" + month);
            Intent widgetServiceIntent = new Intent(context, WidgetService.class);
            widgetServiceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            context.startForegroundService(widgetServiceIntent);
            views.setOnClickPendingIntent(R.id.widget_weather_icon,pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId,views);}}








}
