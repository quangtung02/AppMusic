package app.alone.com.appmusic.screen.splash;

import android.util.DisplayMetrics;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import app.alone.com.appmusic.R;
import app.alone.com.appmusic.config.Constants;
import app.alone.com.appmusic.db.TEST;
import app.alone.com.appmusic.main.BaseActivity;
import app.alone.com.appmusic.main.MainApplication;
import app.alone.com.appmusic.screen.home.HomeActivity;

public class SplashActivity extends BaseActivity {
    private ImageView mSplashItem = null;
    private MainApplication mainApplication;

    @Override
    protected int getIdLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void findViewById() {
        mSplashItem = (ImageView) findViewById(R.id.splash_loading_item);
    }

    @Override
    protected void init() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Constants.SCREEN_DENSITY = metrics.density;
        Constants.SCREEN_HEIGHT = metrics.heightPixels;
        Constants.SCREEN_WIDTH = metrics.widthPixels;
       /// mainApplication = (MainApplication) getApplication();
       // AddNewObjectToDB();
       // mHandler = new Handler(getMainLooper());
        animationLoadImage();

    }

    private void AddNewObjectToDB()
    {
        TEST t = GenerateTestobj();
        mainApplication.getDB().AddNewObject(t);
    }


    /*Generate TEST Object*/
    private TEST GenerateTestobj()
    {
        TEST result = new TEST();
        result.TEST_DOUBLE_FIELD_0 = 0.890;
        result.TEST_INTEGER_FIELD_2 = 34;
        result.TEST_STRING_FIELD_3 = "This is string field";
        return result;
    }


    private void animationLoadImage(){
        final Animation translate = AnimationUtils.loadAnimation(this,
                R.anim.splash_loading);
        translate.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                openActivity(HomeActivity.class);
                overridePendingTransition(R.anim.push_left_in,
                        R.anim.push_left_out);
                SplashActivity.this.finish();
            }
        });
        mSplashItem.setAnimation(translate);
    }
}
