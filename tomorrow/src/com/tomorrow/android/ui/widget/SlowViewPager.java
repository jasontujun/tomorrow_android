package com.tomorrow.android.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * <pre>
 * User: jasontujun
 * Date: 14-9-2
 * Time: 下午5:45
 * </pre>
 */
public class SlowViewPager extends ViewPager {

    public SlowViewPager(Context context) {
        super(context);
        postInitViewPager();
    }

    public SlowViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        postInitViewPager();
    }

    private SlowScroller mScroller = null;

    /**
     * Override the Scroller instance with our own class so we can change the
     * duration
     */
    private void postInitViewPager() {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            Field interpolator = viewpager.getDeclaredField("sInterpolator");
            interpolator.setAccessible(true);

            mScroller = new SlowScroller(getContext(),
                    (Interpolator) interpolator.get(null));
            mScroller.setScrollDurationFactor(1.5f);// 延缓滑动速度
            scroller.set(this, mScroller);
        } catch (Exception e) {
        }
    }

    /**
     * Set the factor by which the duration will change
     */
    public void setScrollDurationFactor(double scrollFactor) {
        mScroller.setScrollDurationFactor(scrollFactor);
    }

    private class SlowScroller extends Scroller {

        private double mScrollFactor = 1;

        public SlowScroller(Context context) {
            super(context);
        }

        public SlowScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public SlowScroller(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }

        /**
         * Set the factor by which the duration will change
         */
        public void setScrollDurationFactor(double scrollFactor) {
            mScrollFactor = scrollFactor;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, (int) (duration * mScrollFactor));
        }

    }
}
