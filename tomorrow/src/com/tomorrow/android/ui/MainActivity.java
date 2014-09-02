package com.tomorrow.android.ui;

import android.app.*;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.tomorrow.android.R;
import com.tomorrow.android.mgr.SystemMgr;
import com.tomorrow.android.receiver.NetworkReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础Activity。
 * Created with IntelliJ IDEA.
 * User: jasontujun
 * Date: 13-11-4
 * Time: 上午10:57
 * To change this template use File | Settings | File Templates.
 */
public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    private static final int PRESS_BACK_INTERVAL = 1500; // back按键间隔，单位：毫秒
    private long lastBackTime;// 上一次back键的时间

    private ActionBar mActionBar;
    private ActionBar.Tab mHistoryTab;
    private ActionBar.Tab mFutureTab;

    private ViewPager mDragLayer;// 可拖动图层
    private DragLayerAdapter mDragAdapter;// 可拖动图层的Adapter

    private int mIndex;// 当前显示的Fragment的索引
    public static final int FRAGMENT_HISTORY = 0;
    public static final int FRAGMENT_TOMORROW = 1;

    private BroadcastReceiver mNetworkReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化系统公共模块
        SystemMgr.getInstance().init(this);

        // ui
//        mDragLayer = new ViewPager(this);
//        mDragLayer.setPredictionId(1);// TIP:手动创建的ViewPager必须设置id
//        setContentView(mDragLayer);
        setContentView(R.layout.activity_main);
        mDragLayer = (ViewPager) findViewById(R.id.drag_layer);

        mDragAdapter = new DragLayerAdapter(getSupportFragmentManager());
        mDragLayer.setAdapter(mDragAdapter);
        mDragLayer.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected().position=" + position + "mIndex=" + mIndex);
                if (mIndex == position) {
                    return;
                }

                mIndex = position;
                if (mActionBar != null) {
                    switch (position) {
                        case FRAGMENT_HISTORY:
                            mActionBar.selectTab(mHistoryTab);
                            break;
                        case FRAGMENT_TOMORROW:
                            mActionBar.selectTab(mFutureTab);
                            break;
                    }
                }
            }
        });
        mIndex = FRAGMENT_HISTORY;
        mDragLayer.setCurrentItem(mIndex);

        mActionBar = getActionBar();
        if (mActionBar != null) {
            mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);// 导航模式必须设为NAVIGATION_MODE_Tabs
            // For each of the sections in the app, add a tab to the action bar.
            mHistoryTab = mActionBar.newTab().setText(R.string.main_tab_history)
                    .setTabListener(this);
            mFutureTab = mActionBar.newTab().setText(R.string.main_tab_future)
                    .setTabListener(this);
            mActionBar.addTab(mHistoryTab);
            mActionBar.addTab(mFutureTab);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_action_bar, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mNetworkReceiver == null)
            mNetworkReceiver = new NetworkReceiver();

        // 注册对网络状况的监听
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 取消对网络状况的监听
        unregisterReceiver(mNetworkReceiver);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent e) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                    return false;
                } else {
                    // 在主界面，点击2次退出
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastBackTime <= PRESS_BACK_INTERVAL) {
//                        SystemMgr.getInstance().clearSystem();
                        finish();
                    } else {
                        lastBackTime = currentTime;
                        Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            case KeyEvent.KEYCODE_MENU:
                break;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // TODO 点击app图标的响应
                return super.onOptionsItemSelected(item);
            case R.id.menu_publish:
                Intent intent = new Intent(this, PublishActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        int position = tab.getPosition();
        Log.d(TAG, "onTabSelected().position=" + position + "mIndex=" + mIndex);
        if (mIndex == position)
            return;
        mIndex = position;
        switch (tab.getPosition()) {
            case FRAGMENT_HISTORY:
                mDragLayer.setCurrentItem(FRAGMENT_HISTORY, true);
                break;
            case FRAGMENT_TOMORROW:
                mDragLayer.setCurrentItem(FRAGMENT_TOMORROW, true);
                break;
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    /**
     * drag容器的adapter
     */
    private class DragLayerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments;

        public DragLayerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new ArrayList<Fragment>();
            fragments.add(new HistoryFragment());
            fragments.add(new FutureFragment());
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public float getPageWidth(int position) {
            return 1.0f;
        }
    }
}
