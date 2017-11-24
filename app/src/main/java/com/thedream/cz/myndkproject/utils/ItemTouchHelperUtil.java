package com.thedream.cz.myndkproject.utils;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.thedream.cz.myndkproject.adapter.SideDeleteAdapter;
import com.thedream.cz.myndkproject.widget.recyclerview.ItemTouchHelperAdapterCallback;

/**
 * Created by chenzhuang on 2017/10/24.
 * Describe:
 */

public class ItemTouchHelperUtil extends ItemTouchHelper.SimpleCallback{


    private ItemTouchHelperAdapterCallback callBack;

    public ItemTouchHelperUtil(ItemTouchHelperAdapterCallback callback) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.callBack = callback;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        Log.i("cz", "onMove " + viewHolder.getAdapterPosition() + "target  "+target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        Log.i("cz", "onSwiped()  adapter position"+viewHolder.getAdapterPosition()+"  getLayoutPosition:"+viewHolder.getLayoutPosition());
        if(callBack != null) {
            callBack.onSwiped(viewHolder.getAdapterPosition());
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        Log.i("cz", "onChildDraw "+dX +"actionState: "+actionState);
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            // Fade out the view as it is swiped out of the parent's bounds
            final float alpha = 1.0F - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
            SideDeleteAdapter.MyHolder myHolder = (SideDeleteAdapter.MyHolder) viewHolder;
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        Log.i("cz", "isItemViewSwipeEnabled");
        return true;
    }

    //    @Override
//    public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
//
//        return 0.1F;
//    }
//
//    @Override
//    public float getSwipeVelocityThreshold(float defaultValue) {
//        return 5F;
//    }
//
//    @Override
//    public long getAnimationDuration(RecyclerView recyclerView, int animationType, float animateDx, float animateDy) {
//        return 100;
//    }
//
//    @Override
//    public float getMoveThreshold(RecyclerView.ViewHolder viewHolder) {
//        return 0.9F;
//    }
//
//    @Override
//    public boolean isItemViewSwipeEnabled() {
//        return true;
//    }
}
