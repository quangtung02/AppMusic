package app.alone.com.appmusic.screen.home;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;

import app.alone.com.appmusic.R;
import app.alone.com.appmusic.lib.PageSlidingTab;
import app.alone.com.appmusic.main.BaseFragment;
import app.alone.com.appmusic.model.CategoryTvObj;
import app.alone.com.appmusic.network.TvApi;
import app.alone.com.appmusic.network.api.GetListCategoryTv;

/**
 * Created by ducthuan on 1/6/16.
 */
public class FragmentTv extends BaseFragment {
    private TemplateAdapterPage templateAdapterPage;
    private PageSlidingTab pageSlidingTab;
    private ViewPager mViewPager;
    private int initPageIndex = 0;

    @Override
    public int getIdLayout() {
        return R.layout.fragment_tv;
    }

    @Override
    public void initView(View view) {
        pageSlidingTab = (PageSlidingTab) view.findViewById(R.id.pager_title_strip);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager_book_store);
        mViewPager.setPageMarginDrawable(new ColorDrawable(Color
                .parseColor("#D3D3D3")));
        mViewPager.setVisibility(View.VISIBLE);
    }

    private void showStoreTab(ArrayList<CategoryTvObj> listObject) {
        String TITLES[] = new String[listObject.size()];
        int ID[] = new int[listObject.size()];
        for (int i = 0; i < listObject.size(); i++) {
            TITLES[i] = listObject.get(i).category_name.toUpperCase();
            ID[i] = Integer.valueOf(listObject.get(i).category_id);
        }
        templateAdapterPage = new TemplateAdapterPage(
                getChildFragmentManager(), getActivity(), TITLES , ID, 1);
        mViewPager.setAdapter(templateAdapterPage);
        mViewPager.setCurrentItem(initPageIndex);
        mViewPager.setOffscreenPageLimit(4);
        pageSlidingTab.setViewPager(mViewPager);
    }

    private TvApi.ListObjectRequestListener<CategoryTvObj> listObjectRequestListener = new TvApi.ListObjectRequestListener<CategoryTvObj>() {

        @Override
        public void onStartRequest() {

        }

        @Override
        public void onRequestError(String message) {

        }

        @Override
        public void onFinshRequestListObject(boolean isSuccess, String message, ArrayList<CategoryTvObj> listObject) {
            if (listObject != null && listObject.size() > 0) {
                if(loading!=null){
                    loading.loadingFinish(true,"");
                }
                showStoreTab(listObject);
            }
        }
    };

    @Override
    public void loadData() {
        GetListCategoryTv.getInstant().getListCategoryTv(listObjectRequestListener);
    }

    @Override
    public void init() {
        if(loading!=null){
            loading.startLoading("");
        }
    }

    @Override
    public void onDestroyView() {
        if (pageSlidingTab != null) {
            pageSlidingTab.removeAllViews();
            pageSlidingTab = null;
        }
        if (mViewPager != null) {
            mViewPager.removeAllViews();
            mViewPager = null;
        }
        super.onDestroyView();
    }
}
