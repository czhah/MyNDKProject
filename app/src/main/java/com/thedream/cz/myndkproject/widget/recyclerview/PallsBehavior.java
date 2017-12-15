package com.thedream.cz.myndkproject.widget.recyclerview;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.utils.PrintUtil;

import java.util.List;


/**
 * Created by Administrator on 2017/12/5.
 */

public class PallsBehavior extends CoordinatorLayout.Behavior<View> {

    public PallsBehavior() {
    }

    public PallsBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        PrintUtil.printCZ("layoutDependsOn:" + (dependency.getId() == R.id.iv_banner));
        return dependency instanceof ImageView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        if (child instanceof RecyclerView) {
            int scrollY = ((RecyclerView) child).getScrollY();
            int scrollX = ((RecyclerView) child).getScrollX();
            PrintUtil.printCZ("onDependentViewChanged  --> scrollY:" + scrollY + "  scrollX:" + scrollX);
        }
        return super.onDependentViewChanged(parent, child, dependency);
    }

    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        final int childLpHeight = child.getLayoutParams().height;
        if (childLpHeight == ViewGroup.LayoutParams.MATCH_PARENT
                || childLpHeight == ViewGroup.LayoutParams.WRAP_CONTENT) {
            // If the menu's height is set to match_parent/wrap_content then measure it
            // with the maximum visible height

            final List<View> dependencies = parent.getDependencies(child);
            //  这个是头布局(banner图)
            final View header = findFirstDependency(dependencies);
            if (header != null) {
                if (ViewCompat.getFitsSystemWindows(header)
                        && !ViewCompat.getFitsSystemWindows(child)) {
                    // If the header is fitting system windows then we need to also,
                    // otherwise we'll get CoL's compatible measuring
                    child.setFitsSystemWindows(true);

                    if (ViewCompat.getFitsSystemWindows(child)) {
                        // If the set succeeded, trigger a new layout and return true
                        child.requestLayout();
                        return true;
                    }
                }

                //  父布局的高度
                int availableHeight = View.MeasureSpec.getSize(parentHeightMeasureSpec);
                if (availableHeight == 0) {
                    // If the measure spec doesn't specify a size, use the current height
                    availableHeight = parent.getHeight();
                }

                //  父布局高度 - 头布局高度 + 父布局测量高度  == 结果还是父布局高度啊？？？
                final int height = availableHeight - header.getMeasuredHeight()
                        + getScrollRange(header);
                //  计算当前View(RecyclerView)的模式()
                final int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height,
                        childLpHeight == ViewGroup.LayoutParams.MATCH_PARENT
                                ? View.MeasureSpec.EXACTLY
                                : View.MeasureSpec.AT_MOST);

                // Now measure the scrolling view with the correct height
                parent.onMeasureChild(child, parentWidthMeasureSpec,
                        widthUsed, heightMeasureSpec, heightUsed);

                return true;
            }
        }
        return false;
    }

    ImageView findFirstDependency(List<View> views) {
        for (int i = 0, z = views.size(); i < z; i++) {
            View view = views.get(i);
            if (view instanceof ImageView) {
                return (ImageView) view;
            }
        }
        return null;
    }

    int getScrollRange(View v) {
        return v.getMeasuredHeight();
    }

}
