package entertainment.android_apps.simpleweather;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private int numberOfTabs;
    ViewPagerAdapter(FragmentManager fragmentManager, int numberOfTabs){
        super(fragmentManager);
        this.numberOfTabs = numberOfTabs;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return DetailFiveDayForecastFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
