package entertainment.android_apps.simpleweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import entertainment.android_apps.simpleweather.interfaces.RecycleViewListener;

public class HourlyDetailActivity extends AppCompatActivity {
    private String hourlyForecastResponse = "";
    private List<HourlyDetail> hourlyDetailList = new ArrayList<>();
    private List<String> timeList;
    int currentPosition = 0;
    RecyclerView recyclerView;
    Animation scale_Up, scale_Down;
    LineChart lineChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_detail);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        final ImageView backBtn = (ImageView) findViewById(R.id.back);
        scale_Up = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scale_Down = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        lineChart = findViewById(R.id.lineChart);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backBtn.startAnimation(scale_Up);
                try {
                    Intent intent = new Intent(HourlyDetailActivity.this, MainActivity.class);
                    startActivity(intent);
                    Animatoo.animateSplit(view.getContext());
                    finish();
                } catch (Exception ex) {
                }
            }
        });
        try {
            Bundle arguments = getIntent().getExtras();
            currentPosition = arguments.getInt("Position");
            hourlyForecastResponse = arguments.get("hourlyForecastResponse").toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        setInitialData(hourlyForecastResponse);
        recyclerView = findViewById(R.id.hourly_detail_recycle_view);
        recyclerView.getLayoutManager().scrollToPosition(currentPosition);
        HourlyDetailRecycleViewAdapter adapter = new HourlyDetailRecycleViewAdapter(this, hourlyDetailList);
        recyclerView.setAdapter(adapter);
        timeList = new ArrayList<>();
        for (int i = 0; i < hourlyDetailList.size(); i++) {
            timeList.add(hourlyDetailList.get(i).getTime());
        }
        drawLineChart();
    }

    public void setInitialData(String hourlyForecastResponse) {
        try {
            int icon = 0;
            if (hourlyDetailList != null) {
                hourlyDetailList.clear();
            }
            JSONArray mJsonArray = new JSONArray(hourlyForecastResponse);
            for (int i = 0; i < 12; i++) {
                JSONObject mJsonObject = mJsonArray.getJSONObject(i);
                String resultTime = mJsonObject.getString("DateTime");
                String time = getHourlyTime(resultTime);
                String day = getDay(resultTime);
                String weatherType = mJsonObject.getString("IconPhrase");
                String temperature = Integer.toString((int) mJsonObject.getJSONObject("Temperature").getDouble("Value")) + "°C";
                double lineChartTemp = mJsonObject.getJSONObject("Temperature").getDouble("Value");
                String realFeelTemprerature = Integer.toString((int) mJsonObject.getJSONObject("RealFeelTemperature").getDouble("Value")) + "°C";
                String dewPoint = Integer.toString((int) mJsonObject.getJSONObject("DewPoint").getDouble("Value")) + "°C";
                String visibility = getString(R.string.visibility,
                        Integer.toString((int) mJsonObject.getJSONObject("Visibility").getDouble("Value")),
                        mJsonObject.getJSONObject("Visibility").getString("Unit"));

                String windDirection = mJsonObject.getJSONObject("Wind").getJSONObject("Direction").getString("Localized");
                String windSpeed = getString(R.string.current_wind,
                        Integer.toString((int) mJsonObject.getJSONObject("Wind").getJSONObject("Speed").getDouble("Value")),
                        mJsonObject.getJSONObject("Wind").getJSONObject("Speed").getString("Unit"));
                String uvIndex = Integer.toString(mJsonObject.getInt("UVIndex")) + "(" + mJsonObject.getString("UVIndexText") + ")";
                String rain = Integer.toString(mJsonObject.getInt("RainProbability")) + "%";
                String snow = Integer.toString(mJsonObject.getInt("SnowProbability")) + "%";
                String clouds = Integer.toString(mJsonObject.getInt("CloudCover")) + "%";

                int weatherIcon = mJsonObject.getInt("WeatherIcon");
                switch (weatherIcon) {
                    case 1:
                        icon = R.drawable.sunny;
                        break;
                    case 2:
                        icon = R.drawable.mostly_sunny;
                        break;
                    case 3:
                        icon = R.drawable.partly_sunny;
                        break;
                    case 4:
                        icon = R.drawable.intermittent_clouds;
                        break;
                    case 5:
                        icon = R.drawable.hazy_sunshine;
                        break;
                    case 6:
                        icon = R.drawable.mostly_cloudy;
                        break;
                    case 7:
                        icon = R.drawable.cloudy;
                        break;
                    case 8:
                        icon = R.drawable.dreary;
                        break;
                    case 11:
                        icon = R.drawable.fog;
                        break;
                    case 12:
                        icon = R.drawable.showers;
                        break;
                    case 13:
                        icon = R.drawable.mostly_cloudy_showers;
                        break;
                    case 14:
                        icon = R.drawable.partly_sunny_showers;
                        break;
                    case 15:
                        icon = R.drawable.t_storms;
                        break;
                    case 16:
                        icon = R.drawable.mostly_cloudy_t_storms;
                        break;
                    case 17:
                        icon = R.drawable.partly_sunny_t_storms;
                        break;
                    case 18:
                        icon = R.drawable.rain;
                        break;
                    case 19:
                        icon = R.drawable.flurries;
                        break;
                    case 20:
                        icon = R.drawable.mostly_coudy_flurries;
                        break;
                    case 21:
                        icon = R.drawable.partly_sunny_flurries;
                        break;
                    case 22:
                        icon = R.drawable.snow;
                        break;
                    case 23:
                        icon = R.drawable.mostly_cloudy_snow;
                        break;
                    case 24:
                        icon = R.drawable.ice;
                        break;
                    case 25:
                        icon = R.drawable.sleet;
                        break;
                    case 26:
                        icon = R.drawable.freezing_rain;
                        break;
                    case 29:
                        icon = R.drawable.rain_and_snow;
                        break;
                    case 30:
                        icon = R.drawable.hot;
                        break;
                    case 31:
                        icon = R.drawable.cold;
                        break;
                    case 32:
                        icon = R.drawable.windy;
                        break;
                    case 33:
                        icon = R.drawable.clear;
                        break;
                    case 34:
                        icon = R.drawable.mostly_clear;
                        break;
                    case 35:
                        icon = R.drawable.partly_cloudy;
                        break;
                    case 36:
                        icon = R.drawable.intermittent_clouds_night;
                        break;
                    case 37:
                        icon = R.drawable.hazy_moonlight;
                        break;
                    case 38:
                        icon = R.drawable.mostly_cloudy_night;
                        break;
                    case 39:
                        icon = R.drawable.partly_cloudy_showers_night;
                        break;
                    case 40:
                        icon = R.drawable.mostly_cloudy_showers_night;
                        break;
                    case 41:
                        icon = R.drawable.partly_cloudy_t_storms_night;
                        break;
                    case 42:
                        icon = R.drawable.mostly_cloudy_t_storms_night;
                        break;
                    case 43:
                        icon = R.drawable.mostly_cloudy_flurries_night;
                        break;
                    case 44:
                        icon = R.drawable.mostly_cloudy_snow_night;
                        break;
                    default:
                        icon = 0;
                        break;
                }
                hourlyDetailList.add(new HourlyDetail(day, time, weatherType, temperature, icon,
                        realFeelTemprerature, dewPoint, visibility, windDirection,
                        windSpeed, uvIndex, rain, snow, clouds, lineChartTemp));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String getDay(String resultTime) {
        String day = "";
        for (int i = 1; i < resultTime.length(); i++) {
            // смотрим, был ли слева пробел:
            if ("T".equals(resultTime.substring(i - 1, i)))
                day = resultTime.substring(0, i - 1);
        }
        return day;
    }

    private String getHourlyTime(String hourlyTime) {
        String resultData = "";
        for (int i = 1; i < hourlyTime.length(); i++) {
            // смотрим, был ли слева пробел:
            if ("T".equals(hourlyTime.substring(i - 1, i)))
                resultData = hourlyTime.substring(i, i + 5);
        }
        return resultData;
    }

    private void drawLineChart() {
//        LineChart lineChart = findViewById(R.id.lineChart);
        List<Entry> lineEntries = getDataSet();
        LineDataSet lineDataSet = new LineDataSet(lineEntries, getString(R.string.app_name));
        lineDataSet.setHighlightEnabled(true);
        lineDataSet.setLineWidth(2);
        lineDataSet.setColor(getResources().getColor(R.color.light_green));
        lineDataSet.setCircleColor(getResources().getColor(R.color.btn_main_pressed));
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleHoleRadius(3);
        lineDataSet.setDrawHighlightIndicators(true);
        lineDataSet.setValueTextSize(12);
        lineDataSet.setValueTextColor(getResources().getColor(R.color.white));
        lineDataSet.setDrawFilled(true);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.gradient);
        lineDataSet.setFillDrawable(drawable);
        YAxis leftAxis = lineChart.getAxisLeft();
        lineDataSet.setValueFormatter(new MyValueFormatter());
        int count = 0;
        for (int i = 0; i < lineEntries.size(); i++) {
            if (lineEntries.get(i).getY() < 0) {
                count++;
            }
        }
        if (count >= 10) {
            leftAxis.setAxisMaximum(0);
        }
        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.setBackgroundColor(getResources().getColor(R.color.hourly_list));
        lineChart.setScaleEnabled(false);
        lineChart.getAxisLeft().setDrawLabels(false);
        lineChart.getAxisRight().setDrawLabels(false);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(timeList));
        xAxis.setTextColor(getResources().getColor(R.color.white));
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisLeft().setEnabled(false);
        lineChart.setTouchEnabled(false);
        xAxis.setDrawAxisLine(false);
        LineData lineData = new LineData(lineDataSet);
        lineChart.getDescription().setText(getString(R.string.app_name));
        lineChart.getDescription().setTextSize(12);
        lineChart.setDrawMarkers(true);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.animateY(1000);
        lineChart.getXAxis().setGranularityEnabled(true);
        lineChart.getXAxis().setGranularity(1.0f);
        lineChart.getXAxis().setLabelCount(lineDataSet.getEntryCount());
        lineChart.setData(lineData);
    }

    public class MyValueFormatter implements IValueFormatter {
        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0"); // use one decimal
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return mFormat.format(value) + " °";
        }
    }

    private List<Entry> getDataSet() {
        List<Entry> lineEntries = new ArrayList<Entry>();
        for (int i = 0; i < hourlyDetailList.size(); i++) {
            double value = hourlyDetailList.get(i).getLineChartTemp();
            lineEntries.add(new Entry(i, (float) Math.round(value)));
        }
        return lineEntries;
    }

    @Override
    public void onBackPressed() {
        try {
            Intent intent = new Intent(HourlyDetailActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(0,R.anim.animate_split_exit);
            finish();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}