package com.thedream.cz.myndkproject.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.utils.PrintUtil;

/**
 * Created by Administrator on 2017/12/5.
 */

public class ScrollLayout extends LinearLayout implements NestedScrollingParent {

    private int mTouchSlop;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private ImageView ivBanner;
    private int mHeadHeight;
    private RecyclerView mRecyclerView;

    public ScrollLayout(Context context) {
        this(context, null);
    }

    public ScrollLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMaximumVelocity = ViewConfiguration.get(context)
                .getScaledMaximumFlingVelocity();
        mMinimumVelocity = ViewConfiguration.get(context)
                .getScaledMinimumFlingVelocity();

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
//        getChildAt(0).measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.AT_MOST));
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

    int totalY = 0;

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        //
        totalY += dy;
        PrintUtil.printCZ("totalY :" + totalY + "  dy:" + dy + "  getScrollY():" + getScrollY());
        boolean hideHead = dy > 0 && getScrollY() < mHeadHeight;
        boolean showHead = dy < 0 && getScrollY() > 0 && !ViewCompat.canScrollVertically(target, -1);

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
        PrintUtil.printCZ("onNestedPreFling  velocityY:" + velocityY);
        return false;
    }

    private void startAnimator(int startY, int endY, float velocityY) {

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
