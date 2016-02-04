package app.alone.com.appmusic.screen.home;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import app.alone.com.appmusic.R;
import app.alone.com.appmusic.main.BaseFragment;

public class FragmentLiveTv extends BaseFragment {

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
        adapter.addFragment(new TestFragment().newInstance("Page1"), "Tab 1");
        adapter.addFragment(new TestFragment().newInstance("Page2"), "Tab 2");
        adapter.addFragment(new TestFragment().newInstance("Page3"), "Tab 3");
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
