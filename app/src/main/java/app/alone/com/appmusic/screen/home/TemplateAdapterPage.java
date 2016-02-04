package app.alone.com.appmusic.screen.home;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * Created by ducthuan on 1/5/16.
 */
public class TemplateAdapterPage extends FragmentPagerAdapter  {
    private String[] TITLES;
    private int ID[];
    private Context ctx;
    private int typeTemplate;

    public TemplateAdapterPage(FragmentManager fm, Context ctx,String[] TITLES,int[] ID, int typeTemplate) {
        super(fm);
        this.TITLES = TITLES;
        this.ID = ID;
        this.typeTemplate = typeTemplate;
        this.ctx = ctx;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
        if (position >= TITLES.length) {
            position = position % TITLES.length;
        }
        return super.instantiateItem(container, position);
    }

    @Override
    public Fragment getItem(int position) {
        return ContentFragment.newInstance(ID[position]);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public Parcelable saveState() {
        return super.saveState();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position % (TITLES.length)];
    }
}

