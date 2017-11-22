package com.thedream.cz.myndkproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenzhuang on 2017/6/1.
 * Describe:    RecyclerView.Adapter的基类
 */

public abstract class BaseAdapter<E, T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    protected List<E> data = new ArrayList<>();
    protected Context mContext;

    public BaseAdapter(Context context) {
        this.mContext = context.getApplicationContext();
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    /**
     * 刷新数据
     * @param list
     */
    public void refreshData(List<E> list){
        if (list == null) return;
        this.data.clear();
        this.data.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 加载更多数据
     * @param list
     */
    public void loadMoreData(List<E> list){
        if (data != null && list != null) {
            final int index = data.size();
            data.addAll(list);
            final int totalSize = data.size();
            for(int i= totalSize - index; totalSize > index && i<totalSize; i++) {
                notifyItemChanged(i);
            }
        }
    }

    /**
     * 返回所有数据
     * @return
     */
    public List<E> getAllData() {
        return data;
    }

    /**
     * 是否是最后一个item
     * @param position
     * @return
     */
    public boolean isLastItem(int position) {
        return position == getItemCount() - 1;
    }

}
