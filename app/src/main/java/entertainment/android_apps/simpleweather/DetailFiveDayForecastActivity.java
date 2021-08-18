package entertainment.android_apps.simpleweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DetailFiveDayForecastActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    private static final String SELECTED_TAB = "selected_tab";
    private static final String ACTIVITY = "activity";
    private int tabPosition = 0;
    private List<FiveDayForecast_Day> dayList = new ArrayList<>();
    private static final int COUNT_OF_TABS = 5;
    private String cityTitle = "";
    private String fiveDayForecastResponse = "";
    private String activity_value = "";
    Intent intent;
    Animation scale_Up, scale_Down;
    boolean whatAnimation = false;
    boolean whatAnimation2 = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_tabs);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        final ImageView backBtn = (ImageView) findViewById(R.id.back);
        scale_Up = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scale_Down = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backBtn.startAnimation(scale_Up);
                try {
                    if (activity_value.equals("Weather_Fragment")) {
                        intent = new Intent(DetailFiveDayForecastActivity.this, MainActivity.class);
                        whatAnimation = true;
                    } else if (activity_value.equals("FiveDayForecastActivity")) {
                        intent = new Intent(DetailFiveDayForecastActivity.this, FiveDayForecastActivity.class);
                        intent.putExtra("cityTitle", cityTitle);
                        intent.putExtra("fiveDayForecastResponse", fiveDayForecastResponse);
                        whatAnimation = false;
                    }
                    startActivity(intent);
                    if(whatAnimation){
                        Animatoo.animateSplit(view.getContext());
                    } else Animatoo.animateSlideUp(view.getContext());
                    finish();//Закрытие старого окна
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        try {
            Bundle arguments = getIntent().getExtras();
            activity_value = arguments.get(ACTIVITY).toString();
            cityTitle = arguments.get("cityTitle").toString();
            fiveDayForecastResponse = arguments.get("fiveDayForecastResponse").toString();
            tabPosition = arguments.getInt(SELECTED_TAB);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        TextView city = (TextView) findViewById(R.id.current_city);
        city.setText(cityTitle);
        setInitialData(fiveDayForecastResponse);
        setDynamicFragmentToTabLayout();
        tabLayout.setScrollX(tabLayout.getWidth());
        tabLayout.getTabAt(tabPosition).select();
    }

    private void setDynamicFragmentToTabLayout() {
        for (int i = 0; i < COUNT_OF_TABS; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(dayList.get(i).getDayTitle() + "\n" + dayList.get(i).getDate()));
        }
        TextView myTV = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_textview, null);
        myTV.setText(getString(R.string.tab_format, getString(R.string.today), dayList.get(0).getDate()));
        tabLayout.getTabAt(0).setCustomView(myTV);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), COUNT_OF_TABS);
        viewPager.setAdapter(viewPagerAdapter);
    }

    public void setInitialData(String fiveDayForecastResponse) {
        try {
            if (dayList != null) {
                dayList.clear();
            }
            JSONObject jsonObject = new JSONObject(fiveDayForecastResponse);
            JSONArray jsonArray = jsonObject.getJSONArray("DailyForecasts");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject resultsObj = jsonArray.getJSONObject(i);

                String responseCurrentDate = resultsObj.getString("Date");
                String currentDay = renderDayOfWeek(responseCurrentDate);

                String currentDate = renderDate(responseCurrentDate);
                dayList.add(new FiveDayForecast_Day(currentDay, currentDate));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String renderDayOfWeek(String responseDate) {
        Locale rus = new Locale("ru", "RU");
        String resultDate = "";
        String dayTitle = "";
        for (int i = 1; i < responseDate.length(); i++) {
            // смотрим, был ли слева пробел:
            if ("T".equals(responseDate.substring(i - 1, i)))
                resultDate = responseDate.substring(0, i - 1);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = formatter.parse(resultDate);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EE", rus);

            dayTitle = simpleDateFormat.format(date).substring(0, 1).toUpperCase() + simpleDateFormat.format(date).substring(1);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dayTitle;
    }

    private String renderDate(String responseDate) {
        Locale rus = new Locale("ru", "RU");
        String resultData = "";
        String day = "";
        String month = "";
        for (int i = 1; i < responseDate.length(); i++) {
            // смотрим, был ли слева пробел:
            if ("T".equals(responseDate.substring(i - 1, i)))
                resultData = responseDate.substring(0, i - 1);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {

            Date date = formatter.parse(resultData);
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MM", rus);
            month = simpleDateFormat1.format(date);
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd", rus);
            day = simpleDateFormat2.format(date);
            return day + "/" + month;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        try {
            if (activity_value.equals("Weather_Fragment")) {
                intent = new Intent(DetailFiveDayForecastActivity.this, MainActivity.class);
                whatAnimation2 = true;
            } else if (activity_value.equals("FiveDayForecastActivity")) {
                intent = new Intent(DetailFiveDayForecastActivity.this, FiveDayForecastActivity.class);
                intent.putExtra("cityTitle", cityTitle);
                intent.putExtra("fiveDayForecastResponse", fiveDayForecastResponse);
                whatAnimation2 = false;
            }
            startActivity(intent);
            if(whatAnimation2){
                overridePendingTransition(0,R.anim.animate_split_exit);
            } else overridePendingTransition(0,R.anim.animate_slide_up_exit);

            finish();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}