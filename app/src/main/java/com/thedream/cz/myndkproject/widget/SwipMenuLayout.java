package com.thedream.cz.myndkproject.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * Created by Administrator on 2017/10/26.
 *      侧滑删除
 */

public class SwipMenuLayout extends ViewGroup {

    //  最小滑动距离
    private int mScaledTouchSlop;
    private float downX;
    private float lastX;
    private float lastY;
    private int maxMoveLength;
    private int mMaxVelocity;
    private VelocityTracker mVelocityTracker;
    private int pointerId;
    private ValueAnimator closeAnimator;
    private int mLimit;
    private ValueAnimator expandAnimator;
    private static SwipMenuLayout mViewCache;

    public SwipMenuLayout(Context context) {
        this(context, null);
    }

    public SwipMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMaxVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        Log.i("cz", "mScaledTouchSlop: "+mScaledTouchSlop);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //  是否需要测量父布局(判断当前的高度不是精确值，即WRAP_CONTENT)
        final boolean measureParentHeight = MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY;

        //  测量父布局
        int parentWidth = 0;
        int parentHeight = measureParentHeight ? 0 : MeasureSpec.getSize(heightMeasureSpec);
        maxMoveLength = 0;
        for(int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.setClickable(true);
            if(child.getVisibility() != View.GONE) {
                //  测量子布局
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
                //  判断是否需要测量子控件的高度(父布局高度是wrap_content,子布局的高度是match_parent)
                if(measureParentHeight) {
                    //  需要测量父布局的高度，父布局高度是WRAP_CONTENT
                    parentHeight = Math.max(parentHeight, child.getMeasuredHeight());
                }
                //  拿到第一个子布局的宽度作为父布局宽度(暂时只能左滑拉出右边的按钮)
                if(i == 0) {
                    parentWidth = getPaddingLeft() + getPaddingRight() + child.getMeasuredWidth();
                }else {
                    //  获取向左移动的最大距离
                    maxMoveLength += getPaddingLeft() + getPaddingRight() + child.getMeasuredWidth();
                }
            }
        }
        mLimit = maxMoveLength * 6 / 10;
        setMeasuredDimension(parentWidth, parentHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //  排列顺序
        int left = getPaddingLeft();
        int top = getPaddingTop();
        for(int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if(child.getVisibility() != View.GONE) {
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                left += lp.leftMargin;
                top += lp.topMargin;
                Log.i("cz", "width:"+child.getMeasuredWidth());
                child.layout(left, top, left + child.getMeasuredWidth(), top + child.getMeasuredHeight());
                left += child.getMeasuredWidth();
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);
        final VelocityTracker verTracker = mVelocityTracker;

        float eventX = ev.getRawX();
        float eventY = ev.getRawY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = downX = eventX;
                lastY = eventY;
                //  可能有多个触点，只取第一个
                pointerId = ev.getPointerId(0);
                if(mViewCache != null) {
                    if(mViewCache != this)
                        mViewCache.swipOrigin();
                    getParent().requestDisallowInterceptTouchEvent(true);
                }

                break;
            case MotionEvent.ACTION_MOVE:
                //  如果是上下滑动则不处理滑动事件
                if(lastY - eventY >= mScaledTouchSlop) {
                    //  上下滑动则不处理滑动事件
                    lastX = eventX;
                    lastY = eventY;
                    return false;
                }
                //  超过滑动最大距离
                float moveX = lastX - eventX;
                if(Math.abs(moveX) > 10) {
                    //  滑动时屏蔽父控件
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                scrollBy((int) moveX, 0);
                if (getScrollX() < 0) {
                    scrollTo(0, 0);
                }
                if (getScrollX() > maxMoveLength) {
                    scrollTo(maxMoveLength, 0);
                }

                lastX = eventX;
                lastY = eventY;

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                verTracker.computeCurrentVelocity(1000, mMaxVelocity);
                //  释放
                //  如果滑动距离超过最大滑出距离的1/3，则算滑出，否则回到原点
                if(downX - eventX > mLimit) {
                    swipOut();
                }else {
                    swipOrigin();
                }
                releaseVelocityTracker();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void swipOut() {
        mViewCache = this;
        cancelAnimtor();
        expandAnimator = ValueAnimator.ofInt(getScrollX(), maxMoveLength);
        expandAnimator.setInterpolator(new OvershootInterpolator());
        expandAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scrollTo((int) animation.getAnimatedValue(), 0);
            }
        });
        expandAnimator.setDuration(300).start();
    }

    private void swipOrigin() {
        mViewCache = null;
        cancelAnimtor();
        closeAnimator = ValueAnimator.ofInt(getScrollX(), 0);
        closeAnimator.setInterpolator(new AccelerateInterpolator());
        closeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scrollTo((int) animation.getAnimatedValue(), 0);
            }
        });
        closeAnimator.setDuration(300).start();
    }

    private void cancelAnimtor() {
        if(closeAnimator != null && closeAnimator.isRunning()) {
            closeAnimator.cancel();
            closeAnimator = null;
        }
        if(expandAnimator != null && expandAnimator.isRunning()) {
            expandAnimator.cancel();
            expandAnimator = null;
        }
    }


    private void releaseVelocityTracker() {
        if(null != mVelocityTracker) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }
}
