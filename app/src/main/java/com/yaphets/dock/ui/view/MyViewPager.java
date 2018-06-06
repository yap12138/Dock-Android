package com.yaphets.dock.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyViewPager extends ViewPager {

    private float xLast;

    private boolean noScroll = false;

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     *
     * @param ev
     *  动作事件
     * @return
     *  true表示处理完成；false表示未能处理，将事件传递下去
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(noScroll) {
            return false;
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xLast = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                //第一个Activity禁止右滑
                if (xLast - curX < 0 && getCurrentItem() == 0) {
                    return false;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 控制Viewpager是否可滑动
     *
     * @param noScroll
     *  true 不能滑动
     */

    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (noScroll)
            return false;
        else
            return super.onTouchEvent(arg0);
    }
}
