package app.alone.com.appmusic.screen.home;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import app.alone.com.appmusic.R;
import app.alone.com.appmusic.main.BaseFragment;

public class HomeFragment extends BaseFragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    PagerAdapter adapter;


    @Override
    public int getIdLayout() {
        return R.layout.fragment_live_tv;
    }

    @Override
    public void initView(View view) {
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) view.findViewById(R.id.pager);

        if (viewPager != null) {
            setupViewPager(viewPager);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new PagerAdapter(getActivity().getSupportFragmentManager(), getActivity());
        adapter.addFragment(new FragmentTv(), getActivity().getResources().getString(R.string.title_kenh_tivi));
        adapter.addFragment(new TestFragment().newInstance("Page2"), getActivity().getResources().getString(R.string.title_kenh_live));
        adapter.addFragment(new TestFragment().newInstance("Page3"),  getActivity().getResources().getString(R.string.title_kenh_giai_tri));
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        // Iterate over all tabs and set the custom view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i));
        }
    }

    @Override
    public void loadData() {

    }

    @Override
    public void init() {

    }
}
