package app.alone.com.appmusic.screen.home;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import app.alone.com.appmusic.R;
import app.alone.com.appmusic.listener.OnLoadMoreListener;
import app.alone.com.appmusic.main.BaseFragment;
import app.alone.com.appmusic.model.ChanelObj;
import app.alone.com.appmusic.network.TvApi;
import app.alone.com.appmusic.network.api.GetListCategoryTv;

/**
 * Created by ducthuan on 1/5/16.
 */
public class ContentFragment extends BaseFragment implements OnLoadMoreListener{

    private RecyclerView mRecyclerView;
    private static final String ARG_POSITION = "position";
    private int idCategory;
    private ChanelAdapter chanelAdapter;

    public static ContentFragment newInstance(int idCategory) {
        ContentFragment f = new ContentFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, idCategory);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private TvApi.ListObjectRequestListener<ChanelObj> listObjectRequestListener = new TvApi.ListObjectRequestListener<ChanelObj>() {

        @Override
        public void onStartRequest() {

        }

        @Override
        public void onRequestError(String message) {

        }

        @Override
        public void onFinshRequestListObject(boolean isSuccess, String message, ArrayList<ChanelObj> listObject) {
            if (listObject != null && listObject.size() > 0) {
                if(loading!=null){
                    loading.loadingFinish(true,"");
                }
                showData(listObject);
            }
        }
    };

    private void showData( ArrayList<ChanelObj> listObject) {
        chanelAdapter = new ChanelAdapter(getActivity(), listObject);
        chanelAdapter.setOnLoadMoreListener(this);
        mRecyclerView.setAdapter(chanelAdapter);
    }


    @Override
    public int getIdLayout() {
        return R.layout.fragment_content;
    }

    @Override
    public void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void loadData() {
        if(loading!=null){
            loading.startLoading("");
        }
        idCategory = getArguments().getInt(ARG_POSITION);
        GetListCategoryTv.getInstant().getListChanelWithCategory(idCategory, listObjectRequestListener);
    }

    @Override
    public void init() {

    }

    @Override
    public void onLoadMore() {
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

//                totalItemCount = linearLayoutManager.getItemCount();
//                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
//
//                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
//                    if (mOnLoadMoreListener != null) {
//                        mOnLoadMoreListener.onLoadMore();
//                    }
//                    isLoading = true;
//                }
            }
        });
    }
}