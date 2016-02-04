package app.alone.com.appmusic.screen.game;

import android.view.View;

import app.alone.com.appmusic.R;
import app.alone.com.appmusic.config.Constants;
import app.alone.com.appmusic.main.BaseFragment;
import app.alone.com.appmusic.view.TextViewFont;
import butterknife.Bind;
import butterknife.ButterKnife;

public class FragmentMenuGame extends BaseFragment implements View.OnClickListener {

    @Bind(R.id.tv_play_game) TextViewFont tvPlayGame;
    @Override
    public int getIdLayout() {
        return R.layout.activity_menu_game;
    }

    @Override
    public void initView(View view) {
        ButterKnife.bind(this, view);
        tvPlayGame.setOnClickListener(this);
    }

    @Override
    public void loadData() {

    }

    @Override
    public void init() {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_play_game) {
            if (getHomeActivity() != null) {
                getHomeActivity().pushFragments(Constants.KEY_TAB_FRAGMENT, new FragmentGame(), null, true, true);
            }
        }
    }
}
