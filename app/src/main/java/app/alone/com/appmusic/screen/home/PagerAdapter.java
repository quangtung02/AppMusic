package app.alone.com.appmusic.screen.home;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.alone.com.appmusic.R;

/**
 * Created by ducthuan on 1/5/16.
 */
public class PagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> myFragments = new ArrayList<>();
    private final List<String> myFragmentTitles = new ArrayList<>();
    private Context context;
    private int[] imageResId = {
            R.drawable.ic_action_add,
            R.drawable.ic_action_add,
            R.drawable.ic_action_add
    };

    public PagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    public void addFragment(Fragment fragment, String title) {
        myFragments.add(fragment);
        myFragmentTitles.add(title);
    }

    public View getTabView(int position) {
        // Given you have a custom layout in `res/layout/custom_tab_item.xml` with a TextView and ImageView
        View v = LayoutInflater.from(context).inflate(R.layout.custom_tab_item, null);
        TextView tv = (TextView) v.findViewById(R.id.tab_item_txt);
        tv.setText(myFragmentTitles.get(position));
        ImageView img = (ImageView) v.findViewById(R.id.tab_item_view);
        img.setImageResource(imageResId[position]);
        return v;
    }

    @Override
    public Fragment getItem(int position) {
        return myFragments.get(position);
    }

    @Override
    public int getCount() {
        return myFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return myFragmentTitles.get(position);
    }
}
