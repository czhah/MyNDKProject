package com.cz.resource.baserecyclerviewadapterhelper.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by cz on 2017/12/12.
 */

public class SlideInLeftAnimation implements BaseAnimation {
    @Override
    public Animator[] getAnimators(View view) {
        return new ObjectAnimator[]{
                ObjectAnimator.ofFloat(view, "translationX", -view.getMeasuredWidth(), 0)};
    }
}
