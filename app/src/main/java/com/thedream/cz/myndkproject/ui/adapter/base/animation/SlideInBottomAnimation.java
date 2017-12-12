package com.thedream.cz.myndkproject.ui.adapter.base.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by cz on 2017/12/12.
 */

public class SlideInBottomAnimation implements BaseAnimation {
    @Override
    public Animator[] getAnimators(View view) {
        return new ObjectAnimator[]{
                ObjectAnimator.ofFloat(view, "translationY", view.getMeasuredHeight(), 0)};
    }
}
