package com.thedream.cz.myndkproject.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.data.entity.FindInfo;
import com.thedream.cz.myndkproject.utils.PrintUtil;
import com.thedream.cz.myndkproject.utils.WindowUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cz on 2017/12/1.
 * 瀑布流
 */

public class StaggeredGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int ITEM_DECORATION = 2;

    public static final int HEAD_TYPE = 0x00000111;
    public static final int DEFAULT_TYPE = 0x00000333;

    private List<FindInfo> data = new ArrayList<>();
    private Map<Integer, Integer> heightList = new HashMap<Integer, Integer>();
    private Map<Integer, Boolean> leftList = new HashMap<>();
    private final int mSpanCount;
    private Context mContext;
    private final int screenWidth;
    private final int[] colorArr = {Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.RED};
    private final int viewWidth;
    private final int decoration;
    private View mHeadView;

    public StaggeredGridAdapter(Context context, int spanCount) {
        this.mContext = context;
        this.mSpanCount = spanCount;
        Display display = ((Activity) mContext).getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        screenWidth = point.x;

        //  view之间的间隔
        decoration = WindowUtil.dp2px(mContext, ITEM_DECORATION);
        //  view宽度是屏幕宽度去掉间隔后除以2
        viewWidth = (screenWidth - decoration * 2) / 2;
    }


    public void refresh(List<FindInfo> list) {
        if (list == null) return;
        data.clear();
        data.addAll(list);
        checkHeightAndLocal();
        notifyDataSetChanged();
    }

    private void checkHeightAndLocal() {
        if (data == null) return;
        leftList.clear();
        heightList.clear();
        int left = 0;
        int right = 0;
        for (int i = 0; i < data.size(); i++) {
            //  计算每个View的高度
            FindInfo findInfo = data.get(i);
            int height = viewWidth / findInfo.getWidth() * findInfo.getHeight();
            heightList.put(i, height);
            //  计算每个View的位置
            boolean isContent = i >= mSpanCount && i < (data.size() - mSpanCount);
            if (left <= right) {
                left += height + (isContent ? decoration * 2 : decoration);
                leftList.put(i, true);
            } else {
                right += height + (isContent ? decoration * 2 : decoration);
                leftList.put(i, false);
            }
        }
    }

    public boolean isLeft(int position) {
        if (leftList == null || position >= leftList.size()) return false;
        return leftList.get(position);
    }

    public void loadMore(List<FindInfo> list) {
        if (list == null) return;
        final int size = list.size() - 1;
        data.addAll(list);
        checkHeightAndLocal();
        for (int i = size; i < data.size(); i++) {
            notifyItemChanged(i);
        }
    }

    public void setHeadView(View headView) {
        this.mHeadView = headView;
    }

    public boolean hasHead() {
        return mHeadView != null;
    }

    public int getHeadSize() {
        return hasHead() ? 1 : 0;
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        PrintUtil.printCZ("adapterPosition: " + holder.getAdapterPosition() + "  layoutPosition: " + holder.getLayoutPosition());
        int type = holder.getItemViewType();
        if (type == HEAD_TYPE) {
            setFullSpan(holder);
        }
    }

    private void setFullSpan(RecyclerView.ViewHolder holder) {
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
        }
    }

    public int getRealPosition(int position) {
        return position - getHeadSize();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && hasHead()) {
            return HEAD_TYPE;
        }
        return DEFAULT_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEAD_TYPE) {
            return new HeadHolder(mHeadView);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_find_layout, parent, false);
            return new MyHandler(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == HEAD_TYPE) {

        } else {
            MyHandler myHolder = (MyHandler) holder;
            ViewGroup.LayoutParams params = myHolder.tvText.getLayoutParams();
            params.width = viewWidth;
            params.height = heightList.get(getRealPosition(position));
            myHolder.tvText.setLayoutParams(params);
            myHolder.tvText.setText(data.get(getRealPosition(position)).getText());
            myHolder.tvText.setBackgroundColor(colorArr[getRealPosition(position) % colorArr.length]);
        }
    }

    @Override
    public int getItemCount() {
        return getHeadSize() + (data == null ? 0 : data.size());
    }

    static class MyHandler extends RecyclerView.ViewHolder {

        private final TextView tvText;

        public MyHandler(View itemView) {
            super(itemView);
            tvText = (TextView) itemView.findViewById(R.id.tv_find_text);
        }
    }

    static class HeadHolder extends RecyclerView.ViewHolder {

        public HeadHolder(View itemView) {
            super(itemView);
        }
    }
}
