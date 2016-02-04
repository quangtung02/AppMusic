package app.alone.com.appmusic.screen.home;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.alone.com.appmusic.R;
import app.alone.com.appmusic.view.TextViewFont;
import butterknife.Bind;
import butterknife.ButterKnife;

public class GalleryActivity extends AppCompatActivity implements GalleryAdapter.onItemCLickListener, View.OnClickListener {

    private static final int COLUMN_GALLERY = 3;
    private static final int ACTION_TAKE_IMAGE = 1;
    public static final String KEY_CALL_FROM = "key_call_from";

    private GalleryAdapter mGalleryAdapter;
    private List<GalleryUntil> mListGalleryUtil = new ArrayList<GalleryUntil>();
    private GalleryUntil mGalleryUntil;
    private Uri mUriImage;
    private MediaScannerConnection mScanner;
    private int mLastId;
    private int mCallFrom = -1;

    private GridLayoutManager mGridLayoutManager;
    @Bind(R.id.text_done_selected)
    TextViewFont mTxtDoneSelect;
    @Bind(R.id.image_back_topic) ImageView mImageBack;
    @Bind(R.id.recycle_bitmap) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);

        if (getIntent().getExtras() != null) {
            mCallFrom = getIntent().getExtras().getInt(KEY_CALL_FROM);
        }

        mGridLayoutManager = new GridLayoutManager(this, COLUMN_GALLERY);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mGalleryAdapter = new GalleryAdapter(this, mListGalleryUtil);
        mGalleryAdapter.setOnItemClickListener(this);

        mRecyclerView.setAdapter(mGalleryAdapter);

        mImageBack.setOnClickListener(this);
        mTxtDoneSelect.setOnClickListener(this);

        new LoadImageAsyncTask(this, onLoadImageListener).execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACTION_TAKE_IMAGE:
                try {
                    if (resultCode == RESULT_OK) {
                        mScanner = new MediaScannerConnection(this,
                            new MediaScannerConnection.MediaScannerConnectionClient() {
                                public void onMediaScannerConnected() {
                                    mScanner.scanFile(mUriImage.getPath(), null);
                                }

                                public void onScanCompleted(String path, Uri uri) {
                                    if (path.equals(mUriImage.getPath())) {
                                        mScanner.disconnect();
                                        GalleryActivity.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                addPictureAfterCapture(mUriImage.getPath());
                                            }
                                        });
                                    }
                                }
                            });
                        mScanner.connect();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_done_selected:
                getBitmapClicked();
                break;
            case R.id.image_back_topic:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onItemClickListener(int id, boolean isClick) {
        if (id == -1 && isClick) {
            capturePicture();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private LoadImageAsyncTask.OnLoadImageListener onLoadImageListener = new LoadImageAsyncTask.OnLoadImageListener() {
        @Override
        public void onLoadImage(List<GalleryUntil> mListGalleryUtil) {
            showImageOnDevice(mListGalleryUtil);
        }
    };

    private void showImageOnDevice(List<GalleryUntil> mListGalleryUtil) {
        if (mListGalleryUtil != null && mListGalleryUtil.size() > 0) {
            mLastId = mListGalleryUtil.get(mListGalleryUtil.size() - 1).getId();
            this.mListGalleryUtil = mListGalleryUtil;
            mGalleryAdapter = new GalleryAdapter(this, mListGalleryUtil);
            mGalleryAdapter.setOnItemClickListener(this);

            mRecyclerView.setAdapter(mGalleryAdapter);
            mGalleryAdapter.notifyDataSetChangedData();
        }
    }

    public void getBitmapClicked() {
//        ArrayList<String> mArrayList = new ArrayList<String>();
//        mArrayList.clear();
//        if (mGalleryAdapter != null) {
//            for (int i = 0; i < mListGalleryUtil.size(); i++) {
//                if (mListGalleryUtil.get(i).isClick()) {
//                    if (mArrayList.size() < AppConst.MAX_IMAGE_POST) {
//                        mArrayList.add(mListGalleryUtil.get(i).getPathBitmap());
//                    } else
//                        break;
//                }
//            }
//        }
//        if (mArrayList.size() > 0) {
//            Intent intent = null;
//            if (mCallFrom == PostTopicActivity.CALL_FROM_POST_ACTIVITY) {
//                intent = new Intent(this, PostTopicActivity.class);
//            } else if (mCallFrom == CommentActivity.CALL_FROM_COMMENT_ACTIVITY) {
//                intent = new Intent(this, CommentActivity.class);
//            }
//            if (intent != null) {
//                intent.putStringArrayListExtra(AppConst.KEY_URL_BITMAP, mArrayList);
//                startActivity(intent);
//                finish();
//            }
//        } else {
//            T.show(getResources().getString(R.string.gallery_select_picture));
//        }
    }

    public static final String FORMAT_DATE_CAPTURE_IMG = "yyyyMMdd_HHmmss";

    public void capturePicture() {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE_CAPTURE_IMG);
        String fileName = "IMG_" + sdf.format(new Date()) + ".jpg";
        File myDirectory = new File(Environment.getExternalStorageDirectory() + "/geeeksplay/myPicture/");
        myDirectory.mkdirs();
        File file = new File(myDirectory, fileName);
        mUriImage = Uri.fromFile(file);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUriImage);
        startActivityForResult(intent, ACTION_TAKE_IMAGE);
    }

    public void addPictureAfterCapture(String content) {
        Cursor mCursor =  getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{content}, null);
        if (mCursor != null) {
            try {
                if (mCursor.moveToFirst()) {
                    int id = mCursor.getInt(mCursor.getColumnIndex(MediaStore.MediaColumns._ID));
                    mGalleryUntil = new GalleryUntil();
                    mGalleryUntil.setId(id);
                    mGalleryUntil.setPathBitmap(content);

                    mGalleryUntil.setBitmap(MediaStore.Images.Thumbnails.getThumbnail(getContentResolver(), id,
                            MediaStore.Images.Thumbnails.MINI_KIND, null));

                    mListGalleryUtil.add(mGalleryUntil);
                    mGalleryAdapter.notifyDataSetChangedData();
                }
            } finally {
                mCursor.close();
            }
        }
    }
}