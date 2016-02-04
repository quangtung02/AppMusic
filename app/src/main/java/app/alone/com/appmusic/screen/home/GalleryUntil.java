package app.alone.com.appmusic.screen.home;

import android.graphics.Bitmap;

/**
 * Created by FRAMGIA\nguyen.quang.tung on 06/11/2015.
 */
public class GalleryUntil {

    private int id;
    private boolean isClick;
    private String pathBitmap;
    private Bitmap bitmap;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setIsClick(boolean isClick) {
        this.isClick = isClick;
    }

    public String getPathBitmap() {
        return pathBitmap;
    }

    public void setPathBitmap(String pathBitmap) {
        this.pathBitmap = pathBitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
