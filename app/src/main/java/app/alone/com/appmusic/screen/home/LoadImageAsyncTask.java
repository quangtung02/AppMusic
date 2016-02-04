package app.alone.com.appmusic.screen.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import app.alone.com.appmusic.R;

/**
 * Created by FRAMGIA\nguyen.quang.tung on 11/11/2015.
 */
public class LoadImageAsyncTask extends AsyncTask<Void, Void, Boolean> {

    private WeakReference<Context> mWeakReference;
    private List<GalleryUntil> mListGalleryUtil = new ArrayList<GalleryUntil>();
    private OnLoadImageListener mOnLoadImageListener;
    private ProgressDialog mProgressDialog;
    private Context mContext;

    public LoadImageAsyncTask(Context context, OnLoadImageListener onLoadImageListener) {
        this.mContext = context;
        this.mWeakReference = new WeakReference<Context>(context);
        this.mOnLoadImageListener = onLoadImageListener;
    }

    private boolean isLiveAsyncTask() {
        if (mWeakReference.get() == null || isCancelled()) {
            return false;
        }
        return true;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = ProgressDialog.show(mContext, "", mContext.getResources().getString(R.string.action_settings), true);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        if (!isLiveAsyncTask()) {
            return false;
        }

        if (mListGalleryUtil != null)
            mListGalleryUtil.clear();

        final String[] columns = {MediaStore.Images.Thumbnails._ID, MediaStore.Images.Media.DATA};
        final String orderBy = MediaStore.Images.Media._ID;
        Cursor mCursor = mWeakReference.get().getContentResolver().query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, columns,
            null, null, orderBy);

        // Add img camera at first position
        GalleryUntil galleryUntil = new GalleryUntil();
        galleryUntil.setId(-1);
        mListGalleryUtil.add(galleryUntil);

        if (mCursor != null && mCursor.getCount() > 0) {
            int count = mCursor.getCount();
            int image_column_index = mCursor.getColumnIndex(MediaStore.Images.Media._ID);

            for (int i = 0; i < count; i++) {
                mCursor.moveToPosition(i);
                int id = mCursor.getInt(image_column_index);

                GalleryUntil mGalleryUntil = new GalleryUntil();
                mGalleryUntil.setId(id);

                if (!TextUtils.isEmpty(mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA)))) {
                    mGalleryUntil.setPathBitmap(mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA)));
                }

                mGalleryUntil.setBitmap(MediaStore.Images.Thumbnails.getThumbnail(mWeakReference.get().getApplicationContext().getContentResolver(), id,
                    MediaStore.Images.Thumbnails.MINI_KIND, null));

                mListGalleryUtil.add(mGalleryUntil);
            }
            mCursor.close();
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean isPostExecute) {
        super.onPostExecute(isPostExecute);
        if (!isLiveAsyncTask()) {
            return;
        }
        if (isPostExecute) {
            if (mListGalleryUtil != null && mListGalleryUtil.size() > 0) {
                if (mOnLoadImageListener != null) {
                    mOnLoadImageListener.onLoadImage(mListGalleryUtil);
                }
            } else {
                if (mOnLoadImageListener != null) {
                    mOnLoadImageListener.onLoadImage(null);
                }
            }
        } else {
            if (mOnLoadImageListener != null) {
                mOnLoadImageListener.onLoadImage(null);
            }
        }
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    public interface OnLoadImageListener {
        void onLoadImage(List<GalleryUntil> mListGalleryUtil);
    }
}
