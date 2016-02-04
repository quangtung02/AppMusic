package app.alone.com.appmusic.screen.home;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import app.alone.com.appmusic.R;
import app.alone.com.appmusic.config.Constants;
import app.alone.com.appmusic.db.DB_BASIC;
import app.alone.com.appmusic.db.TEST;
import app.alone.com.appmusic.main.BaseActivity;
import app.alone.com.appmusic.main.BaseFragment;
import app.alone.com.appmusic.main.MainApplication;

public class HomeActivity extends BaseActivity {

    private HashMap<String, Stack<Fragment>> mStacks;
    private Fragment fragmentPre;
    private String mCurrentTab = Constants.KEY_TAB_FRAGMENT;
    private Timer timer = new Timer();
    private boolean isQuit = false;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private FrameLayout mContentFrame;

    @Override
    protected int getIdLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void findViewById() {
        setUpToolbar();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);

//        mUserLearnedDrawer = Boolean.valueOf(readSharedSetting(this, PREF_USER_LEARNED_DRAWER, "false"));
//
//        if (savedInstanceState != null) {
//            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
//            mFromSavedInstanceState = true;
//        }

        setUpNavDrawer();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mContentFrame = (FrameLayout) findViewById(R.id.nav_contentframe);

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawer(mNavigationView);
                menuItem.setChecked(true);
                switch (menuItem.getItemId()) {
                    case R.id.navigation_item_1:
                        Snackbar.make(mContentFrame, "Item One", Snackbar.LENGTH_SHORT).show();
                        //mCurrentSelectedPosition = 0;
                        return true;
                    case R.id.navigation_item_2:
                        Snackbar.make(mContentFrame, "Item Two", Snackbar.LENGTH_SHORT).show();
                        //mCurrentSelectedPosition = 1;
                        return true;
                    default:
                        return true;
                }
            }
        });
    }

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }
    private void setUpNavDrawer() {
        if (mToolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationIcon(R.drawable.ic_drawer);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }
    }

    @Override
    protected void init() {
        mStacks = new HashMap<String, Stack<Fragment>>();
        mStacks.put(Constants.KEY_TAB_FRAGMENT, new Stack<Fragment>());
        pushFragments(Constants.KEY_TAB_FRAGMENT, new HomeFragment(), null, true, true);
       // GetAllData();
    }

    private boolean fragmentNeedRemoveWhenCall(Fragment f) {
        return false;
    }

    public void pushFragments(String tabMenu, Fragment fragment, Bundle bundle, boolean shouldAnimate, boolean shouldAdd) {
        // Fragment can xoa de update lai.
        if (fragmentPre != null && fragmentNeedRemoveWhenCall(fragmentPre)) {
            mStacks.get(Constants.KEY_TAB_FRAGMENT).removeElementAt(mStacks.get(fragmentPre).size() - 1);
            FragmentManager manager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction ft = manager.beginTransaction();
            ft.remove(fragmentPre);
            ft.addToBackStack(null);
            ft.commitAllowingStateLoss();
        }

        // add vao stack
        if (shouldAdd || !fragment.isAdded()) {
            mStacks.get(tabMenu).push(fragment);
        }

        FragmentManager manager = getSupportFragmentManager();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        android.support.v4.app.FragmentTransaction ft = manager.beginTransaction();
        if (shouldAnimate)
            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);

        if (fragmentPre != null) {
            ft.hide(fragmentPre);
        }

        if (!fragment.isAdded()) {
            ft.add(R.id.nav_contentframe, fragment, fragment.getClass().getSimpleName());
        } else {
            // ((BaseFragment) fragment).updateDataLoadding();
        }
        ft.commitAllowingStateLoss();
        fragmentPre = fragment;
    }


    public void popFragments(boolean isBeginFragment) {
        android.support.v4.app.Fragment fragment = null;

       /* We have the target fragment in hand.. Just show it.. Show a standard navigation animation*/
        FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft1 = manager.beginTransaction();
        if (fragmentPre != null) {
            ((BaseFragment) fragmentPre).onBackFragment();
            //ft1.hide(fragmentPre);
            ft1.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        }

        /*pop current fragment from stack.. */
        if (mStacks.get(mCurrentTab).size() >= 1) {

            // xoa stack vi tri hien back fragment A-B, B quay A lai xoa Bl
            Fragment fragmentRemove;
            if (!isBeginFragment) {
                fragment = mStacks.get(mCurrentTab).elementAt(mStacks.get(mCurrentTab).size() - 2);
                fragmentRemove = mStacks.get(mCurrentTab).elementAt(mStacks.get(mCurrentTab).size() - 1);
                ft1.remove(fragmentRemove).commit();
                mStacks.get(mCurrentTab).removeElementAt(mStacks.get(mCurrentTab).size() - 1);
            } else {
                fragment = mStacks.get(mCurrentTab).elementAt(0);
                int sizeStack = mStacks.get(mCurrentTab).size();

                //manager.popBackStack(1, sizeStack);
                //manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                for (int i = 1; i < sizeStack; i++) {
                    fragmentRemove = mStacks.get(mCurrentTab).elementAt(sizeStack - i);
                    ft1.hide(fragmentRemove);
                    ft1.remove(fragmentRemove);
                    mStacks.get(mCurrentTab).removeElementAt(sizeStack - i);
                }
                ft1.commit();
            }
        }

        android.support.v4.app.FragmentTransaction ft = manager.beginTransaction();
        Fragment f = mStacks.get(mCurrentTab).elementAt(0);
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        ft.show(fragment).addToBackStack(null);
        if (fragment != null) {
//            ((BaseFragment) fragment).updateActionBar();
//            ((BaseFragment) fragment).loadDataRetry();
        }
        //ft.add(R.id.frame_container, fragment, fragment.getClass().getName());
        ft.commit();


        fragmentPre = fragment;
        //loading fragment
        Runtime.getRuntime().gc();
        System.gc();
    }


    @Override
    public void onBackPressed() {
        BaseFragment f = (BaseFragment) mStacks.get(mCurrentTab).lastElement();
        if (!f.onBackPressed()) {
            /*
            * top fragment in current tab doesn't handles back press, we can do our thing, which is
            *
            * if current tab has only one fragment in stack, ie first fragment is showing for this tab.
            * finish the activity
            * else
            * pop to previous fragment in stack for the same tab
            *
            */
            if (mStacks.get(mCurrentTab).size() == 1) {
                if (!isQuit) {
                    if (mStacks.get(mCurrentTab).size() == 1) {
                        isQuit = true;
                        showToast(getString(R.string.msg_exit_app));
                        TimerTask task = null;
                        task = new TimerTask() {
                            @Override
                            public void run() {
                                isQuit = false;
                            }
                        };
                        timer.schedule(task, 2000);
                    } else {
                        //  popFragments(false);
                        return;
                    }
                } else {
                    finish();
                    System.exit(0);
                }

                //super.onBackPressed(); // or call finish..
            } else {
                popFragments(false);
            }
        } else {
            //do nothing.. fragment already handled back button press.

        }
    }


    private void GetAllData() {
        List<DB_BASIC> list = ((MainApplication)getApplication()).getDB().GetTableData(TEST.class);

        StringBuilder sb = new StringBuilder();

        if(list != null)
        {
            for (DB_BASIC object : list)
            {
                if(object != null && object instanceof TEST)
                {
                    TEST test = (TEST)object;
                    if(test.ID != null)
                    {
                        sb.append("ID: " + test.ID + "\n");
                    }
                    if(test.TEST_DOUBLE_FIELD_0 != null)
                    {
                        sb.append("TEST_DOUBLE_FIELD_0" + test.TEST_DOUBLE_FIELD_0 + "\n");
                    }
                    if(test.TEST_LONG_FIELD_1 != null)
                    {
                        sb.append("TEST_LONG_FIELD_1: " + test.TEST_LONG_FIELD_1 + "\n");
                    }
                    if(test.TEST_INTEGER_FIELD_2 != null)
                    {
                        sb.append("TEST_INTEGER_FIELD_2: " + test.TEST_INTEGER_FIELD_2 + "\n");
                    }

                    if(test.TEST_STRING_FIELD_3 != null)
                    {
                        sb.append("TEST_STRING_FIELD_3: " + test.TEST_STRING_FIELD_3 + "\n");
                    }
                }
            }
        }

        showToast(sb.toString());
    }
}
