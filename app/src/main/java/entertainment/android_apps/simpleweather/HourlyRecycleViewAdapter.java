package entertainment.android_apps.simpleweather;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import entertainment.android_apps.simpleweather.interfaces.RecycleViewListener;

public class HourlyRecycleViewAdapter extends RecyclerView.Adapter<HourlyRecycleViewAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<Hourly> hourlyList;

    RecycleViewListener itemListener;
    MediaPlayer mPlayer;

    HourlyRecycleViewAdapter(Context context, List<Hourly> hourlyList, RecycleViewListener listener) {
        this.hourlyList = hourlyList;
        this.inflater = LayoutInflater.from(context);
        this.itemListener = listener;
    }
    @Override
    public HourlyRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HourlyRecycleViewAdapter.ViewHolder holder, int position) {
        Hourly hourly = hourlyList.get(position);
        holder.imageView.setImageResource(hourly.getImage());
        holder.hour.setText(hourly.getHour());
        holder.deegrees.setText(hourly.getDeegrees());
        holder.wind.setText(hourly.getWind());
    }

    @Override
    public int getItemCount() {
        return hourlyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView imageView;
        final TextView hour, deegrees, wind;

        ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.image_1);
            hour = (TextView) view.findViewById(R.id.hour_1);
            deegrees = (TextView) view.findViewById(R.id.degrees_1);
            wind = (TextView) view.findViewById(R.id.wind_1);

            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View v)
        {
            itemListener.iTemClicked(v, this.getLayoutPosition());

        }
    }
}
