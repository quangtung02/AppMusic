package app.alone.com.appmusic.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

import app.alone.com.appmusic.R;
import app.alone.com.appmusic.network.api.RequestUtil;
import app.alone.com.appmusic.screen.home.HomeActivity;
import app.alone.com.appmusic.view.LoadingView;

public abstract class BaseFragment extends Fragment {
    protected DisplayImageOptions options;
    protected RecyclerView mRecyclerView;
    private ArrayList<String> listRequestTag;
    protected LoadingView loading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(getIdLayout(), container, false);
        initView(root);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loading =(LoadingView) getView().findViewById(R.id.loading);
        if(loading!=null){
            loading.setVisibility(View.GONE);
        }
        init();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
    }

    public abstract int getIdLayout();
    public abstract void initView(View view);
    public abstract void loadData();
    public abstract void init();

    public HomeActivity getHomeActivity(){
        if(getActivity() instanceof  HomeActivity){
            return (HomeActivity) getActivity();
        }
        return null;
    }

    public boolean onBackPressed(){
        return false;
    }
    public boolean onBackFragment(){
        return true;
    }

    public void addRequestTag(String tag) {
        if (listRequestTag == null) {
            listRequestTag = new ArrayList<String>();
        }
        listRequestTag.add(tag);
    }


    @Override
    public void onDetach() {
        if (listRequestTag != null) {
            for (String tag : listRequestTag) {
                RequestUtil.getInstant().cancelRequest(tag);
            }
        }
        RequestUtil.getInstant().clearRequestFail();
        super.onDetach();
    }
}
