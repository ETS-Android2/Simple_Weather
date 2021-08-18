package entertainment.android_apps.simpleweather;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.Filter;
import androidx.annotation.Nullable;

import java.util.ArrayList;


public class CitySuggestAdapter extends ArrayAdapter implements Filterable {
    ArrayList<String> results;
    int resource;
    Context context;
    PlaceAPI placeAPI = new PlaceAPI();
    public CitySuggestAdapter(Context context, int resID){
        super(context,resID);
        this.context = context;
        this.resource = resID;
    }

    @Override
    public int getCount() {
        return results.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return results.get(position);
    }
    @Override
    public Filter getFilter(){
        Filter filter=new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults=new FilterResults();
                if(constraint!=null){
                    results=placeAPI.autoComplete(constraint.toString());

                    filterResults.values=results;
                    filterResults.count=results.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if(results!=null && results.count>0){
                    notifyDataSetChanged();
                }
                else{
                    notifyDataSetInvalidated();
                }

            }
        };
        return filter;
    }
}
