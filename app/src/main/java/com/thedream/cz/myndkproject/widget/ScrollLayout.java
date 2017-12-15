package com.thedream.cz.myndkproject.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.utils.PrintUtil;

/**
 * Created by Administrator on 2017/12/5.
 */

public class ScrollLayout extends LinearLayout implements NestedScrollingParent {

    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private ImageView ivBanner;
    private int mHeadHeight;
    private RecyclerView mRecyclerView;
    private ValueAnimator mOffsetAnimator;

    public ScrollLayout(Context context) {
        this(context, null);
    }

    public ScrollLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mMaximumVelocity = ViewConfiguration.get(context)
                .getScaledMaximumFlingVelocity();
        mMinimumVelocity = ViewConfiguration.get(context)
                .getScaledMinimumFlingVelocity();
        //  16000
        PrintUtil.printCZ("mMaximumVelocity：" + mMaximumVelocity + " mMinimumVelocity:" + mMinimumVelocity);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ivBanner = (ImageView) findViewById(R.id.iv_banner);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //  这样处理才可以
        ViewGroup.LayoutParams params = mRecyclerView.getLayoutParams();
        params.height = getMeasuredHeight();
        setMeasuredDimension(getMeasuredWidth(), ivBanner.getMeasuredHeight() + mRecyclerView.getMeasuredHeight());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeadHeight = ivBanner.getMeasuredHeight();
        PrintUtil.printCZ("mHeadHeight：" + mHeadHeight);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        boolean hideHead = dy > 0 && getScrollY() < mHeadHeight;
        boolean showHead = dy < 0 && getScrollY() > 0 && !target.canScrollVertically(-1);
        if (hideHead || showHead) {
            scrollBy(0, dy);
            consumed[1] = dy;
        }
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return false;

    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        boolean showHead = false;
        if (target instanceof RecyclerView && velocityY < -mMinimumVelocity * 15) {
            //  这里判断第一个View离Adapter距离不远，则向下拉动时显示Banner图
            final RecyclerView recyclerView = (RecyclerView) target;
            final View firstChild = recyclerView.getChildAt(0);
            final int childAdapterPosition = recyclerView.getChildAdapterPosition(firstChild);
            showHead = childAdapterPosition != -1 && childAdapterPosition < 3;
        }
        boolean hideHead = velocityY > mMinimumVelocity * 5 && getScrollY() < mHeadHeight;
        if (showHead || hideHead) {
            int duration = computeDuration(velocityY);
            int endY = velocityY > 0 ? mHeadHeight : 0;
            animateScroll(duration, endY);
            return true;
        }

        return false;
    }

    /**
     * 根据速度计算滚动动画持续时间
     *
     * @param velocityY
     * @return
     */
    private int computeDuration(float velocityY) {
        final int duration;
        final int distance;
        if (velocityY > 0) {
            distance = Math.abs(mHeadHeight - getScrollY());
            duration = 3 * Math.round(1000 * (distance / velocityY));
        } else {
            distance = Math.abs(getScrollY());
            final float distanceRatio = (float) distance / getHeight();
            duration = (int) ((distanceRatio + 1) * 150);
        }
        return duration;

    }

    private void animateScroll(final int duration, int endY) {
        final int currentOffset = getScrollY();
        if (mOffsetAnimator == null) {
            mOffsetAnimator = new ValueAnimator();
            mOffsetAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            mOffsetAnimator.addUpdateListener(animation -> {
                if (animation.getAnimatedValue() instanceof Integer) {
                    int value = (Integer) animation.getAnimatedValue();
                    scrollTo(0, value);
                }
            });
        } else {
            mOffsetAnimator.cancel();
        }
        mOffsetAnimator.setDuration(Math.min(duration, 600));
        mOffsetAnimator.setIntValues(currentOffset, endY);
        mOffsetAnimator.start();
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > mHeadHeight) {
            y = mHeadHeight;
        }
        if (y != getScrollY()) {
            super.scrollTo(x, y);
        }
    }
}
