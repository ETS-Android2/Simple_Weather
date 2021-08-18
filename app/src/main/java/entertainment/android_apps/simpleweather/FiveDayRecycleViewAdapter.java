package entertainment.android_apps.simpleweather;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import entertainment.android_apps.simpleweather.interfaces.RecycleViewListener;

public class FiveDayRecycleViewAdapter extends RecyclerView.Adapter<FiveDayRecycleViewAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<FiveDay> fiveDayList;

    RecycleViewListener itemListener;

    FiveDayRecycleViewAdapter(Context context, List<FiveDay> fiveDayList, RecycleViewListener listener) {
        this.fiveDayList = fiveDayList;
        this.inflater = LayoutInflater.from(context);
        this.itemListener = listener;
    }
    @Override
    public FiveDayRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.five_day_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FiveDayRecycleViewAdapter.ViewHolder holder, int position) {
        FiveDay fiveDay = fiveDayList.get(position);
        if(position == 0){
            holder.linearLayout.setBackgroundResource(R.drawable.style_recycleview);
        }
        holder.day.setText(fiveDay.getDay());
        holder.date.setText(fiveDay.getDate());
        holder.dayTranslate.setText(fiveDay.getDayTranslate());
        holder.imgDay.setImageResource(fiveDay.getImgDay());
        holder.dayWeatherType.setText(fiveDay.getDayWeatherType());
        holder.dayTemperature.setText(fiveDay.getDayTemperature());
        holder.wind.setText(fiveDay.getWind());
        holder.windNight.setText(fiveDay.getWindNight());
        holder.nightTemperature.setText(fiveDay.getNightTemperature());
        holder.nightTranslate.setText(fiveDay.getNightTranslate());
        holder.imgNight.setImageResource(fiveDay.getImgNight());
        holder.nightWeatherType.setText(fiveDay.getNightWeatherType());


    }

    @Override
    public int getItemCount() {
        return fiveDayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView day;
        private TextView date;
        private TextView dayTranslate;
        private ImageView imgDay;
        private TextView dayWeatherType;
        private TextView dayTemperature;
        private TextView wind;
        private TextView windNight;
        private TextView nightTemperature;
        private TextView nightTranslate;
        private ImageView imgNight;
        private TextView nightWeatherType;
        private LinearLayout linearLayout;
        ViewHolder(View view){
            super(view);
            day = (TextView)view.findViewById(R.id.five_day_day);
            date = (TextView)view.findViewById(R.id.five_day_date);
            dayTranslate = (TextView)view.findViewById(R.id.five_day_day_translate);
            imgDay = (ImageView)view.findViewById(R.id.five_day_img_day);
            dayWeatherType = (TextView)view.findViewById(R.id.five_day_weather_type_day);
            dayTemperature = (TextView)view.findViewById(R.id.five_day_day_temperature);
            wind = (TextView)view.findViewById(R.id.five_day_wind);
            windNight = (TextView)view.findViewById(R.id.five_day_wind_night);
            nightTemperature = (TextView)view.findViewById(R.id.five_day_night_temperature);
            nightTranslate = (TextView)view.findViewById(R.id.five_day_night_translate);
            imgNight = (ImageView)view.findViewById(R.id.five_day_img_night);
            nightWeatherType = (TextView) view.findViewById(R.id.five_day_weather_type_night);
            linearLayout = (LinearLayout)view.findViewById(R.id.recycle_view_background);

            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View v)
        {
            itemListener.iTemClicked(v, this.getLayoutPosition());

        }
    }
}
