package app.alone.com.appmusic.main;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.L;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

import app.alone.com.appmusic.db.DataBaseHandler;
import app.alone.com.appmusic.db.TEST;

public class MainApplication extends Application {
    private RequestQueue requestQueue;
    private DataBaseHandler dbHandler;
    public static final String baseUrl ="http://thinknet.vn:8383/api_tivi/";
    public static MainApplication sInstance;
    public static int maxHeap = 64;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        initImageLoader();
        requestQueue = Volley.newRequestQueue(this);
        getDB();
    }

    public DataBaseHandler getDB() {
        if (dbHandler == null) {
            initDB();
        }
        return dbHandler;
    }

    private void initDB() {
        //Initialize DB handler
        dbHandler = new DataBaseHandler(this);
        //Adding table based on class TEST reflection
        dbHandler.CreateTable(new TEST());
    }

    private void initImageLoader() {
        int cacheScale = 64 / maxHeap;
        if (cacheScale < 1) {
            cacheScale = 1;
        }
        File cacheDir = StorageUtils.getCacheDirectory(this);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this)
                .memoryCacheExtraOptions(480 / cacheScale, 800 / cacheScale)
                .diskCacheExtraOptions(480 / cacheScale, 800 / cacheScale, null)
                .threadPoolSize(3).threadPriority(Thread.NORM_PRIORITY - 1)
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .memoryCacheSize(2 * 1024 * 1024).memoryCacheSizePercentage(13)
                .diskCacheSize(50 * 1024 * 1024 / cacheScale).diskCacheFileCount(100)
                .build();
        ImageLoader.getInstance().init(config);
        L.writeDebugLogs(false);
        L.writeLogs(false);
    }

    public static synchronized MainApplication getsInstance() {
        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }
}
