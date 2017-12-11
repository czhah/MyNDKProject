package com.thedream.cz.myndkproject.ui.adapter.base;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cz on 2017/12/8.
 * Adapter的基类
 */

public abstract class BaseQuickAdapter<T, K extends BaseViewHolder> extends RecyclerView.Adapter<K> {

    public static final int HEADER_VIEW = 0x00000111;
    public static final int LOADING_VIEW = 0x00000222;
    public static final int FOOTER_VIEW = 0x00000333;
    public static final int CONTENT_VIEW = 0x00000444;
    public static final int EMPTY_VIEW = 0x00000555;

    protected static final int START_REFRESH = 0;

    private Context mContext;
    protected List<T> mData;

    //  header footer
    private LinearLayout mHeaderLayout;
    private LinearLayout mFooterLayout;
    //  empty
    private FrameLayout mEmptyLayout;
    //  加载更多
    private LoadMoreView mLoadMoreView = new SimpleLoadMoreView();
    //  是否显示空页面
    private boolean mIsUseEmpty = false;
    //  是否允许加载更多
    private boolean mLoadMoreEnable = false;
    //  是否正在加载
    private boolean mLoading = false;
    //  预加载下标
    private int mPreLoadNumber = 1;

    //  加载更多监听
    private OnLoadMoreListener onLoadMoreListener;
    //  GridLayoutManager分布
    private SpanSizeLookup mSpanSizeLookup;
    private LayoutInflater mLayoutInflater;

    protected abstract @LayoutRes int getLayoutResId();

    protected abstract void convert(K holder, T info);

    public void refreshData(List<T> list) {
        refreshData(list, true);
    }

    public void refreshData(List<T> list, boolean refresh) {
        this.mData = list == null ? new ArrayList<T>() : list;
        if(refresh) notifyDataChanged(START_REFRESH);
    }

    public void loadMore(List<T> list) {
        loadMore(list, true);
    }

    public void loadMore(List<T> list, boolean refresh) {
        if (list == null) return;
        final int start = this.mData.size();
        this.mData.addAll(list);
        if(refresh) notifyDataChanged(start);
    }

    protected void notifyDataChanged(int start) {
        for(; start < mData.size(); start++) {
            notifyItemChanged(getHeaderLayoutCount() + start);
        }
    }


    @Override
    public void onViewAttachedToWindow(K holder) {
        super.onViewAttachedToWindow(holder);
        int type = holder.getItemViewType();
        if (isFixedViewType(type)) {
            setFullSpan(holder);
        }
    }

    private void setFullSpan(RecyclerView.ViewHolder holder) {
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if(manager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    if (isFixedViewType(type)){
                        return gridLayoutManager.getSpanCount();
                    }else {
                        return mSpanSizeLookup == null ? 1 : mSpanSizeLookup.getSpanSize(gridLayoutManager, position);
                    }
                }
            });
        }
    }

    protected boolean isFixedViewType(int type) {
        return type == EMPTY_VIEW || type == HEADER_VIEW || type == FOOTER_VIEW || type ==
                LOADING_VIEW;
    }

    @Override
    public int getItemViewType(int position) {
        //  判断没有数据 且包含空布局
        if (getEmptyViewCount() == 1) {
            //  如果包含头布局
            boolean header = /*mHeadAndEmptyEnable && */getHeaderLayoutCount() != 0;
            switch (position) {
                case 0:
                    if (header) {
                        //  HeadView + EmptyView
                        return HEADER_VIEW;
                    } else {
                        //  EmptyView + FootView
                        return EMPTY_VIEW;
                    }
                case 1:
                    if (header) {
                        //  HeadView  +  EmptyView
                        return EMPTY_VIEW;
                    } else {
                        //  EmptyView  +  FootView
                        return FOOTER_VIEW;
                    }
                case 2:
                    //  HeadView  +  EmptyView  +  FootView
                    return FOOTER_VIEW;
                default:
                    return EMPTY_VIEW;
            }
        }
        //  不包含空布局 或  包含空布局，但数据不为空(空布局不显示)
        int numHeaders = getHeaderLayoutCount();
        if (position < numHeaders) {
            //  HeadView + data
            return HEADER_VIEW;
        } else {
            //  获取数据的下标(除开头布局)
            int adjPosition = position - numHeaders;
            int adapterCount = getDataSize();
            if (adjPosition < adapterCount) {
                return getDefItemViewType(adjPosition);
            } else {
                //  count = getHeaderLayoutCount() + mData.size() + getFooterLayoutCount() + getLoadMoreViewCount();
                //  adjPosition 属于getFooterLayoutCount() + getLoadMoreViewCount();
                adjPosition = adjPosition - adapterCount;
                int numFooters = getFooterLayoutCount();
                if (adjPosition < numFooters) {
                    return FOOTER_VIEW;
                } else {
                    return LOADING_VIEW;
                }
            }
        }
    }

    protected int getDefItemViewType(int position) {
        return CONTENT_VIEW;
}

    private int getDataSize() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public K onCreateViewHolder(ViewGroup parent, int viewType) {
        K baseViewHolder = null;
        mContext = parent.getContext();
        mLayoutInflater = LayoutInflater.from(mContext);
        switch (viewType) {
            case HEADER_VIEW:
                baseViewHolder = createBaseViewHolder(mHeaderLayout);
                break;
            case EMPTY_VIEW:
                baseViewHolder = createBaseViewHolder(mEmptyLayout);
                break;
            case FOOTER_VIEW:
                baseViewHolder = createBaseViewHolder(mFooterLayout);
                break;
            case LOADING_VIEW:
                baseViewHolder = createLoadingHolder(parent);
                break;
            case CONTENT_VIEW:
            default:
                baseViewHolder = createDefViewHolder(parent, viewType);
                break;
        }
        baseViewHolder.setAdapter(this);
        return baseViewHolder;
    }

    protected K createDefViewHolder(ViewGroup parent, int type) {
        return createBaseViewHolder(parent, getLayoutResId());
    }

    private K createLoadingHolder(ViewGroup parent) {
        K holder = createBaseViewHolder(parent, mLoadMoreView.getLoadLayoutId());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mLoadMoreView.getLoadMoreStatus() == LoadMoreView.STATUS_FAIL) {
                    notifyLoadMoreLoading();
                }
            }
        });
        return holder;
    }

    private void notifyLoadMoreLoading() {
        if(mLoadMoreView.getLoadMoreStatus() == LoadMoreView.STATUS_LOADING) {
            return ;
        }
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_DEFAULT);
        notifyDataChanged(getLoadMoreViewPosition());
    }

    protected View getItemView(@LayoutRes int layoutResId, ViewGroup parent) {
        return mLayoutInflater.inflate(layoutResId, parent, false);
    }

    protected K createBaseViewHolder(ViewGroup parent, int layoutResId) {
        return createBaseViewHolder(getItemView(layoutResId, parent));
    }

    /**
     * if you want to use subclass of BaseViewHolder in the adapter,
     * you must override the method to create new ViewHolder.
     *
     * @param view view
     * @return new ViewHolder
     */
    @SuppressWarnings("unchecked")
    protected K createBaseViewHolder(View view) {
        Class temp = getClass();
        Class z = null;
        while (z == null && null != temp) {
            z = getInstancedGenericKClass(temp);
            temp = temp.getSuperclass();
        }
        K k;
        // 泛型擦除会导致z为null
        if (z == null) {
            k = (K) new BaseViewHolder(view);
        } else {
            k = createGenericKInstance(z, view);
        }
        return k != null ? k : (K) new BaseViewHolder(view);
    }

    @SuppressWarnings("unchecked")
    private K createGenericKInstance(Class z, View view) {
        try {
            Constructor constructor;
            // inner and unstatic class
            if (z.isMemberClass() && !Modifier.isStatic(z.getModifiers())) {
                constructor = z.getDeclaredConstructor(getClass(), View.class);
                constructor.setAccessible(true);
                return (K) constructor.newInstance(this, view);
            } else {
                constructor = z.getDeclaredConstructor(View.class);
                constructor.setAccessible(true);
                return (K) constructor.newInstance(view);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get generic parameter K
     *
     * @param z
     * @return
     */
    private Class getInstancedGenericKClass(Class z) {
        Type type = z.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            for (Type temp : types) {
                if (temp instanceof Class) {
                    Class tempClass = (Class) temp;
                    if (BaseViewHolder.class.isAssignableFrom(tempClass)) {
                        return tempClass;
                    }
                } else if (temp instanceof ParameterizedType) {
                    Type rawType = ((ParameterizedType) temp).getRawType();
                    if (rawType instanceof Class && BaseViewHolder.class.isAssignableFrom((Class<?>) rawType)) {
                        return (Class<?>) rawType;
                    }
                }
            }
        }
        return null;
    }


    @Override
    public void onBindViewHolder(K holder, int position) {
        //  自动加载更多
        autoLoadMore(position);
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case LOADING_VIEW:
                mLoadMoreView.convert(holder);
                break;
            case HEADER_VIEW:
                break;
            case EMPTY_VIEW:
                break;
            case FOOTER_VIEW:
                break;
            case CONTENT_VIEW:
                //  内容
            default:
                convert(holder, mData.get(getRealPos(position)));
                break;
        }
    }

    protected T getItem(int position) {
        if (position < mData.size()) return mData.get(position);
        return null;
    }

    private void autoLoadMore(int position) {
        if(getLoadMoreViewCount() == 0) {
            return ;
        }
        if(position < getItemCount() - mPreLoadNumber) {
            return ;
        }
        if(mLoadMoreView.getLoadMoreStatus() != LoadMoreView.STATUS_DEFAULT){
            return ;
        }
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_LOADING);
        if(!mLoading) {
            mLoading = true;
            onLoadMoreListener.onLoadMore();
        }
    }

    public int getRealPos(int position) {
        return position - getHeaderLayoutCount();
    }

    @Override
    public int getItemCount() {
        int count;
        if (getEmptyViewCount() == 1) {
            //  如果包含空布局
            count = 1;
            //  且包含头布局
            if (getHeaderLayoutCount() != 0) {
                count++;
            }
            //  且包含底部布局
            if (getFooterLayoutCount() != 0) {
                count++;
            }
        } else {
            count = getHeaderLayoutCount() + getDataSize() + getFooterLayoutCount() + getLoadMoreViewCount();
        }
        return count;
    }

    /******************************空背景布局**************************/
    private int getEmptyViewCount() {
        if (mEmptyLayout == null || mEmptyLayout.getChildCount() == 0) {
            return 0;
        }
        if(!mIsUseEmpty) {
            return 0;
        }
        if(getDataSize() > 0) {
            return 0;
        }
        return 1;
    }

    public void setUserEmpty(boolean userEmpty) {
        this.mIsUseEmpty = userEmpty;
    }

    public void setEmptyView(View emptyView) {
        boolean insert = false;
        if(mEmptyLayout != null) {
            mEmptyLayout = new FrameLayout(emptyView.getContext());
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ViewGroup.LayoutParams lp = emptyView.getLayoutParams();
            if(lp != null) {
                layoutParams.width = lp.width;
                layoutParams.height = lp.height;
            }
            mEmptyLayout.setLayoutParams(layoutParams);
            insert = true;
        }
        mEmptyLayout.removeAllViews();
        mEmptyLayout.addView(emptyView);
        mIsUseEmpty = true;
        if(insert) {
            if(getEmptyViewCount() == 1) {
                int pos = 0;
                if(getHeaderLayoutCount() != 0) {
                    pos ++;
                }
                notifyItemChanged(pos);
            }
        }
    }

    /******************************头布局**************************/
    private int getHeaderLayoutCount() {
        if (mHeaderLayout == null || mHeaderLayout.getChildCount() == 0) {
            return 0;
        }
        return 1;
    }

    public int addHeaderView(View header) {
        return addHeaderView(header, -1, LinearLayout.VERTICAL);
    }

    public int addHeaderView(View header, int index, int orientation) {
        if (mHeaderLayout == null) {
            mHeaderLayout = new LinearLayout(header.getContext());
            if (orientation == LinearLayout.VERTICAL) {
                mHeaderLayout.setOrientation(LinearLayout.VERTICAL);
                mHeaderLayout.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            } else {
                mHeaderLayout.setOrientation(LinearLayout.HORIZONTAL);
                mHeaderLayout.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
        }
        final int childCount = mHeaderLayout.getChildCount();
        if (index < 0 || index > childCount) {
            index = childCount;
        }
        mHeaderLayout.addView(header, index);
        if (mHeaderLayout.getChildCount() == 1) {
            notifyItemInserted(0);
        }
        return index;
    }

    /******************************底部布局(感觉没什么用啊!!!!)**************************/
    private int getFooterLayoutCount() {
        if (mFooterLayout == null || mFooterLayout.getChildCount() == 0) {
            return 0;
        }
        return 1;
    }

    public int addFooterView(View header) {
        return addFooterView(header, -1, LinearLayout.VERTICAL);
    }

    public int addFooterView(View header, int index, int orientation) {
        if (mFooterLayout == null) {
            mFooterLayout = new LinearLayout(header.getContext());
            if (orientation == LinearLayout.VERTICAL) {
                mFooterLayout.setOrientation(LinearLayout.VERTICAL);
                mFooterLayout.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            } else {
                mFooterLayout.setOrientation(LinearLayout.HORIZONTAL);
                mFooterLayout.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
        }
        final int childCount = mFooterLayout.getChildCount();
        if (index < 0 || index > childCount) {
            index = childCount;
        }
        mFooterLayout.addView(header, index);
        if (mFooterLayout.getChildCount() == 1) {
            int position = getFooterViewPosition();
            if (position != -1) {
                notifyItemInserted(position);
            }
        }
        return index;
    }

    private int getFooterViewPosition() {
        //Return to footer view notify position
        if (getEmptyViewCount() == 1) {
            if (getHeaderLayoutCount() != 0) {
                return 2;
            }
        } else {
            return getHeaderLayoutCount() + getDataSize();
        }
        return -1;
    }

    /******************************加载更多布局**************************/
    private int getLoadMoreViewCount() {
        if(!mLoadMoreEnable || onLoadMoreListener == null) {
            return 0;
        }
        if(getDataSize() == 0) {
            return 0;
        }
        return 1;
    }

    public void setLoadMoreEnable(boolean loadMoreEnable) {
        this.mLoadMoreEnable = loadMoreEnable;
    }

    public int getLoadMoreViewPosition() {
        return getHeaderLayoutCount() + getDataSize() + getFooterLayoutCount();
    }

    public void loadMoreComplete() {
        if(getLoadMoreViewCount() == 0) return ;
        mLoading = false;
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_DEFAULT);
        notifyItemChanged(getLoadMoreViewPosition());
    }

    public void loadMoreFail() {
        if(getLoadMoreViewCount() == 0) return ;
        mLoading = false;
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_FAIL);
        notifyItemChanged(getLoadMoreViewPosition());
    }

    public void loadMoreEnd() {
        if(getLoadMoreViewCount() == 0) return ;
        mLoading = false;
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_END);
        notifyItemChanged(getLoadMoreViewPosition());
    }

    public void setPreLoadNumber(int num) {
        this.mPreLoadNumber = num;
    }

    /*****************************各种监听***********************************/
    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.onLoadMoreListener = listener;
    }

    public interface SpanSizeLookup {
        int getSpanSize(GridLayoutManager gridLayoutManager, int position);
    }

    public void setSpanSizeLookUp(SpanSizeLookup spanSizeLookUp) {
        this.mSpanSizeLookup = spanSizeLookUp;
    }
}
