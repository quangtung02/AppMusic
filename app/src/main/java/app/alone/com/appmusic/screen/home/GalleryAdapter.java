package app.alone.com.appmusic.screen.home;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

import app.alone.com.appmusic.R;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FRAMGIA\nguyen.quang.tung on 06/11/2015.
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.SimpleViewHolder> {
    private onItemCLickListener mOnItemCLickListener;

    private List<GalleryUntil> mListGalleryUntils;
    private LayoutInflater mLayoutInflater;
    private int mCount;
    private Context mContext;

    public GalleryAdapter(Context context, List<GalleryUntil> galleryUntils) {
        this.mContext = context;
        this.mListGalleryUntils = galleryUntils;
        this.mLayoutInflater = LayoutInflater.from(context);
        mCount = mListGalleryUntils.size();
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = mLayoutInflater.inflate(R.layout.item_gallery, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, int position) {
        final GalleryUntil galleryUntil = mListGalleryUntils.get(position);
        holder.mImageView.setVisibility(View.VISIBLE);
        if (galleryUntil.getId() != -1) {
            if (!TextUtils.isEmpty(galleryUntil.getPathBitmap())) {
                File fileImg = new File(galleryUntil.getPathBitmap().toString());
                try {
                    String url = fileImg.toURI().toURL().toString();
                    Picasso.with(mContext)
                        .load(url)
                        .placeholder(R.anim.loading)
                        .fit()
                        .error(R.drawable.ic_no_icon)
                        .into(holder.mImageView);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            } else {
                holder.mImageView.setVisibility(View.GONE);
            }
        }

        if (galleryUntil.getId() == -1) {
            holder.mRelativeLayout.setBackgroundColor(Color.BLACK);
            holder.mImageView.setImageResource(R.drawable.ic_camera_enhance_white);
        }
        // check image was click or not
        if (galleryUntil.isClick())
            holder.mRelativeLayout.setBackgroundColor(Color.BLUE);
        else
            holder.mRelativeLayout.setBackgroundColor(Color.BLACK);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemCLickListener != null) {
                    if (galleryUntil.getId() != -1) {
                        if (!galleryUntil.isClick()) {
                            holder.mRelativeLayout.setBackgroundColor(Color.BLUE);
                            galleryUntil.setIsClick(true);
                            mOnItemCLickListener.onItemClickListener(galleryUntil.getId(), true);
                        } else {
                            holder.mRelativeLayout.setBackgroundColor(Color.BLACK);
                            galleryUntil.setIsClick(false);
                            mOnItemCLickListener.onItemClickListener(galleryUntil.getId(), false);
                        }
                    } else {
                        mOnItemCLickListener.onItemClickListener(galleryUntil.getId(), true);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCount;
    }

    public void notifyDataSetChangedData() {
        mCount = mListGalleryUntils.size();
        this.notifyDataSetChanged();
    }


    public void setOnItemClickListener(final onItemCLickListener mOnItemCLickListener) {
        this.mOnItemCLickListener = mOnItemCLickListener;
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.layout_image_gallery) RelativeLayout mRelativeLayout;
        @Bind(R.id.image_thumbnail) ImageView mImageView;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface onItemCLickListener {

        void onItemClickListener(int id, boolean isClick);
    }
}
