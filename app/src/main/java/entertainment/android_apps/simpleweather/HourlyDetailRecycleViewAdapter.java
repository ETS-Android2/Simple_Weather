package entertainment.android_apps.simpleweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HourlyDetailRecycleViewAdapter extends RecyclerView.Adapter<HourlyDetailRecycleViewAdapter.ViewHolder2> {
    private LayoutInflater inflater;
    private List<HourlyDetail> hourlyDetailList;
    HourlyDetailRecycleViewAdapter(Context context, List<HourlyDetail> hourlyDetails) {
        this.hourlyDetailList = hourlyDetails; this.inflater = LayoutInflater.from(context); }
    @Override
    public HourlyDetailRecycleViewAdapter.ViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.hourly_list2, parent, false);
        return new HourlyDetailRecycleViewAdapter.ViewHolder2(view);}
    @Override
    public void onBindViewHolder(HourlyDetailRecycleViewAdapter.ViewHolder2 holder, int position) {
        HourlyDetail hourlyDetail = hourlyDetailList.get(position);
        holder.day.setText(hourlyDetail.getDay());
        holder.time.setText(hourlyDetail.getTime());
        holder.weatherType.setText(hourlyDetail.getWeatherType());
        holder.temperature.setText(hourlyDetail.getTemperature());
        holder.weatherImage.setImageResource(hourlyDetail.getWeatherImage());
        holder.realFeel.setText(hourlyDetail.getRealFeel());
        holder.dewPoint.setText(hourlyDetail.getDewPoint());
        holder.visibility.setText(hourlyDetail.getVisibility());
        holder.windDirection.setText(hourlyDetail.getWindDirection());
        holder.windSpeed.setText(hourlyDetail.getWindSpeed());
        holder.uvIndex.setText(hourlyDetail.getUvIndex());
        holder.rain.setText(hourlyDetail.getRain());
        holder.snow.setText(hourlyDetail.getSnow());
        holder.clouds.setText(hourlyDetail.getClouds()); }
    @Override
    public int getItemCount() {
        return hourlyDetailList.size();
    }
    public class ViewHolder2 extends RecyclerView.ViewHolder {
        private TextView day;
        private TextView time;
        private TextView weatherType;
        private TextView temperature;
        private ImageView weatherImage;
        private TextView realFeel;
        private TextView dewPoint;
        private TextView visibility;
        private TextView windDirection;
        private TextView windSpeed;
        private TextView uvIndex;
        private TextView rain;
        private TextView snow;
        private TextView clouds;
        ViewHolder2(View view){
            super(view);
            day = (TextView)view.findViewById(R.id.day);
            time = (TextView)view.findViewById(R.id.time);
            weatherType = (TextView)view.findViewById(R.id.hourly_weather_type);
            temperature = (TextView)view.findViewById(R.id.hourly_temperature);
            weatherImage = (ImageView)view.findViewById(R.id.hourly_main_img);
            realFeel = (TextView)view.findViewById(R.id.hourly_real_feel);
            dewPoint = (TextView)view.findViewById(R.id.hourly_dew_point);
            visibility = (TextView)view.findViewById(R.id.hourly_visibility);
            windDirection = (TextView)view.findViewById(R.id.hourly_wind_direction);
            windSpeed = (TextView)view.findViewById(R.id.hourly_wind_speed);
            uvIndex = (TextView)view.findViewById(R.id.houtly_uv_index);
            rain = (TextView)view.findViewById(R.id.hourly_rain);
            snow = (TextView)view.findViewById(R.id.hourly_snow);
            clouds = (TextView)view.findViewById(R.id.hourly_clouds_cover); } } }

