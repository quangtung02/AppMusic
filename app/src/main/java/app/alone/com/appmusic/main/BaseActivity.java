package app.alone.com.appmusic.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public abstract class BaseActivity extends AppCompatActivity {
    protected Handler mHandler = null;
    private boolean isShowToast = true;
    private Toast toast;

    public void showToast(String message) {
        if(!isShowToast){
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(this, "", Toast.LENGTH_LONG);
        }
        toast.setText(message);
        toast.show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getIdLayout());
        findViewById();
        init();
    }

    protected abstract int getIdLayout();
    protected abstract void findViewById();
    protected abstract void init();

    protected void openActivity(Class<?> pClass) {
        openActivity(pClass, null);
    }

    protected void openActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }

}
