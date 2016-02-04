package app.alone.com.appmusic.screen.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;

import app.alone.com.appmusic.R;
import app.alone.com.appmusic.listener.OnLoadMoreListener;
import app.alone.com.appmusic.model.ChanelObj;

public class ChanelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ChanelObj> mListChanel;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private Context context;
    private DisplayImageOptions options;

    public ChanelAdapter(Context context ,ArrayList<ChanelObj> mListChanel) {
        this.context = context;
        this.mListChanel = mListChanel;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_no_icon)
                .showImageForEmptyUri(R.drawable.ic_no_icon)
                .showImageOnFail(R.drawable.ic_no_icon)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(20))
                .build();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        return mListChanel.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_item_chanel, parent, false);
            return new ChanelHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_loading_item, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ChanelHolder) {
            ChanelObj chanelObj = mListChanel.get(position);
            ChanelHolder chanelHolder = (ChanelHolder) holder;
            chanelHolder.tvName.setText(chanelObj.channel_name);
            chanelHolder.tvEmailId.setText(chanelObj.des);
            ImageLoader.getInstance().displayImage(chanelObj.image_uri, chanelHolder.ivPic, options);
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return mListChanel == null ? 0 : mListChanel.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    static class ChanelHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvEmailId;
        public ImageView ivPic;

        public ChanelHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvEmailId = (TextView) itemView.findViewById(R.id.tvEmailId);
            ivPic = (ImageView) itemView.findViewById(R.id.ivPic);
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }
    }
}