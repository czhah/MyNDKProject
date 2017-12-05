package com.thedream.cz.myndkproject.widget.recyclerview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by chenzhuang on 2017/10/24.
 * Describe:
 */

public class LinearItemDecoration extends RecyclerView.ItemDecoration {


    private final Paint paint;

    public LinearItemDecoration() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0, 0, 0, 1);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        final int top = parent.getPaddingTop();
        final int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom();
        final int childSize = parent.getAdapter().getItemCount();
        if(childSize == 0) return ;
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            if(child == null) continue;
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + layoutParams.rightMargin;
            final int right = left + 1;
            if (paint != null) {
                c.drawRect(left, top, right, bottom, paint);
            }
        }

    }
}
