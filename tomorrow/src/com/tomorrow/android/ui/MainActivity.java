package com.tomorrow.android.ui;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.Toast;
import com.tomorrow.android.R;
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
public class MainActivity extends FragmentActivity {

    private static final int PRESS_BACK_INTERVAL = 1500; // back按键间隔，单位：毫秒
    private long lastBackTime;// 上一次back键的时间

    private ViewPager mDragLayer;// 可拖动图层
    private DragLayerAdapter mDragAdapter;// 可拖动图层的Adapter
    private int mIndex;// 当前显示的Fragment的索引
    public static final int FRAGMENT_OTHER = 0;
    public static final int FRAGMENT_MAIN = 1;
    public static final int FRAGMENT_ME = 2;

    private BroadcastReceiver mNetworkReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化系统相关组件
//        SystemMgr.getInstance().initEngine(getApplicationContext());
//        SystemMgr.getInstance().initSystem(getApplicationContext());

        // ui
//        mDragLayer = new ViewPager(this);
//        mDragLayer.setId(1);// TIP:手动创建的ViewPager必须设置id
//        setContentView(mDragLayer);
        setContentView(R.layout.activity_main);
        mDragLayer = (ViewPager) findViewById(R.id.drag_layer);

        mDragAdapter = new DragLayerAdapter(getSupportFragmentManager());
        mDragLayer.setAdapter(mDragAdapter);
        mDragLayer.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mIndex = position;
            }
        });
        mIndex = FRAGMENT_MAIN;
        mDragLayer.setCurrentItem(FRAGMENT_MAIN);
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
                } else if (mIndex > FRAGMENT_MAIN) {
                    // 主界面右边，向左边滑动
                    mDragLayer.setCurrentItem(mIndex - 1);
                    return true;
                }  else if (mIndex < FRAGMENT_MAIN) {
                    // 主界面右边，向左边滑动
                    mDragLayer.setCurrentItem(mIndex + 1);
                    return true;
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
//                if (mIndex != FRAGMENT_MAIN)
//                    mDragLayer.setCurrentItem(FRAGMENT_MAIN, true);
                return true;
        }
        return false;
    }

    /**
     * 切换屏幕
     */
    public boolean switchScreen(int index) {
        if (index == mIndex)
            return false;
        if (index < 0 || index >= mDragAdapter.getCount())
            return false;

        mDragLayer.setCurrentItem(index, true);
        return true;
    }

    public void showPublishFragment() {
        mDragAdapter.addFragment(new PublishFragment());
        mDragLayer.setCurrentItem(2, true);
    }

    public void showMyPredictionFragment() {
        mDragAdapter.addFragment(new MyPredictionFragment());
        mDragLayer.setCurrentItem(2, true);
    }

    public void showMyCollectionFragment() {
        mDragAdapter.addFragment(new MyCollectionFragment());
        mDragLayer.setCurrentItem(2, true);
    }

    /**
     * drag容器的adapter
     */
    private class DragLayerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments;

        public DragLayerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new ArrayList<Fragment>();
            fragments.add(new OtherPredictionFragment());
            fragments.add(new MainFragment());
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

        public void addFragment(Fragment fragment) {
            if (fragment == null)
                return;

            // 如果已有三屏，则替换
            if (fragments.size() == 3) {
                // 如果添加的屏幕和当前的是一样的，则什么都不做
                Fragment oldFragment = fragments.get(2);
                if (fragment.getClass() == oldFragment.getClass())
                    return;

                // 如果不一样，则替换第三屏
                List<Fragment> copyFragment = new ArrayList<Fragment>(fragments);
                copyFragment.remove(2);
                copyFragment.add(fragment);
                fragments = copyFragment;
            } else {
                fragments.add(fragment);
            }
            notifyDataSetChanged();
        }
    }
}
