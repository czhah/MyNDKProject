package com.thedream.cz.myndkproject.ui.adapter.base;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.thedream.cz.myndkproject.ui.adapter.base.animation.AlphaInAnimation;
import com.thedream.cz.myndkproject.ui.adapter.base.animation.BaseAnimation;
import com.thedream.cz.myndkproject.ui.adapter.base.animation.ScaleInAnimation;
import com.thedream.cz.myndkproject.ui.adapter.base.animation.SlideInBottomAnimation;
import com.thedream.cz.myndkproject.ui.adapter.base.animation.SlideInLeftAnimation;
import com.thedream.cz.myndkproject.ui.adapter.base.animation.SlideInRightAnimation;
import com.thedream.cz.myndkproject.ui.adapter.base.loadmore.LoadMoreView;
import com.thedream.cz.myndkproject.ui.adapter.base.loadmore.SimpleLoadMoreView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
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

    private Context mContext;
    protected List<T> mData;

    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int ALPHAIN = 0x00000001;
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int SCALEIN = 0x00000002;
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int SLIDEIN_BOTTOM = 0x00000003;
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int SLIDEIN_LEFT = 0x00000004;
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int SLIDEIN_RIGHT = 0x00000005;

    @IntDef({ALPHAIN, SCALEIN, SLIDEIN_BOTTOM, SLIDEIN_LEFT, SLIDEIN_RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AnimationType {
    }

    //  是否允许动画
    private boolean mOpenAnimationEnable = false;
    //  动画插值器：匀速执行
    private Interpolator mInterpolator = new LinearInterpolator();
    //  动画时间
    private int mDuration = 300;
    //  上一个的下标(对瀑布流无效)
    private int mLastPosition = -1;

    //  自定义动画
    private BaseAnimation mCustomAnimation;
    //  显示动画
    private BaseAnimation mSelectAnimation = new AlphaInAnimation();

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
    //  是否是瀑布流(用于判断加载更多时的最后一行)
    private boolean mIsStaggeredLayout = false;

    //  加载更多监听
    private OnLoadMoreListener onLoadMoreListener;
    //  GridLayoutManager分布
    private SpanSizeLookup mSpanSizeLookup;
    private LayoutInflater mLayoutInflater;
    //  Item点击监听
    private OnItemClickListener mOnItemClickListener;
    //  Item长按监听
    private OnItemLongClickListener mOnItemLongClickListener;
    //  子View点击事件
    private OnItemChildClickListener mOnItemChildClickListener;
    //  子View长按事件
    private OnItemChildLongClickListener mOnItemChildLongClickListener;

    protected abstract @LayoutRes int getLayoutResId();

    protected abstract void convert(K holder, T info);

    /**
     * 刷新页面
     *
     * @param list
     */
    public void refreshData(List<T> list) {
        refreshData(list, true);
    }

    public void refreshData(List<T> list, boolean refresh) {
        this.mData = list == null ? new ArrayList<T>() : list;
        if (refresh) notifyDataSetChanged();
    }

    /**
     * 加载更多
     * @param list
     */
    public void loadMore(List<T> list) {
        loadMore(list, true);
    }

    public void loadMore(List<T> list, boolean refresh) {
        if (list == null) return;
        final int newSize = list.size();
        this.mData.addAll(list);
        mLastPosition = -1;
        if (refresh) notifyMoreChange(newSize);
    }

    protected void notifyMoreChange(int newSize) {
        notifyItemRangeInserted(mData.size() - newSize, newSize);
        compatibilityDataSizeChanged(newSize);
    }

    private void compatibilityDataSizeChanged(int size) {
        if (getDataSize() == size) {
            notifyDataSetChanged();
        }
    }

    public void addData(@IntRange(from = 0) int position, @NonNull T data) {
        mData.add(position, data);
        notifyItemInserted(getHeaderLayoutCount() + position);
        compatibilityDataSizeChanged(1);
    }

    @Override
    public void onViewAttachedToWindow(K holder) {
        super.onViewAttachedToWindow(holder);
        int type = holder.getItemViewType();
        if (isFixedViewType(type)) {
            setFullSpan(holder);
        } else {
            addAnimation(holder);
        }
    }

    /**
     * 设置如果是瀑布流布局，则头布局、底部布局、加载更多布局、空页面占满一屏
     * @param holder
     */
    private void setFullSpan(RecyclerView.ViewHolder holder) {
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            mIsStaggeredLayout = true;
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
            int dataSize = getDataSize();
            if (adjPosition < dataSize) {
                return getDefItemViewType(adjPosition);
            } else {
                //  count = getHeaderLayoutCount() + mData.size() + getFooterLayoutCount() + getLoadMoreViewCount();
                //  adjPosition 属于getFooterLayoutCount() + getLoadMoreViewCount();
                adjPosition = adjPosition - dataSize;
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
                bindViewClickListener(baseViewHolder);
                break;
        }
        baseViewHolder.setAdapter(this);
        return baseViewHolder;
    }

    private void bindViewClickListener(RecyclerView.ViewHolder holder) {
        if (holder == null) {
            return;
        }
        View view = holder.itemView;
        if (view == null) {
            return;
        }
        if (mOnItemClickListener != null) {
            view.setOnClickListener(v ->
                    mOnItemClickListener.onItemClick(v, holder.getLayoutPosition() - getHeaderLayoutCount())
            );
        }
        if (mOnItemLongClickListener != null) {
            view.setOnLongClickListener(v ->
                    {
                        return mOnItemLongClickListener.onItemLongClick(view, holder.getLayoutPosition() - getHeaderLayoutCount());
                    }
            );
        }

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
        notifyItemChanged(getLoadMoreViewPosition());
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
                convert(holder, getItem(getRealPos(position)));
                break;
        }
    }

    public T getItem(int position) {
        if (position >= 0 && position < getDataSize()) return mData.get(position);
        return null;
    }

    private void autoLoadMore(int position) {
        if(getLoadMoreViewCount() == 0) {
            return ;
        }
        if (mIsStaggeredLayout) {
            //  瀑布流必须滑动到最后一行时才加载更多
            if (getItemViewType(position) != LOADING_VIEW) {
                return;
            }
        } else {
            //  对非瀑布流有效
            if (position < getItemCount() - mPreLoadNumber) {
                return;
            }
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

    /***************************添加Item显示动画*********************************/
    private void addAnimation(RecyclerView.ViewHolder holder) {
        if (mOpenAnimationEnable) {
            if (holder.getLayoutPosition() > mLastPosition) {
                BaseAnimation animation = null;
                if (mCustomAnimation != null) {
                    animation = mCustomAnimation;
                } else {
                    animation = mSelectAnimation;
                }
                for (Animator anim : animation.getAnimators(holder.itemView)) {
                    startAnim(anim);
                }
                mLastPosition = holder.getLayoutPosition();
            }
        }
    }

    private void startAnim(Animator anim) {
        anim.setInterpolator(mInterpolator);
        anim.setDuration(mDuration).start();
    }

    public void openLoadAnimation(boolean openAnimationEnable) {
        this.mOpenAnimationEnable = openAnimationEnable;
    }

    /**
     * Set the view animation type.
     *
     * @param animationType One of {@link #ALPHAIN}, {@link #SCALEIN}, {@link #SLIDEIN_BOTTOM},
     *                      {@link #SLIDEIN_LEFT}, {@link #SLIDEIN_RIGHT}.
     */
    public void openLoadAnimation(@AnimationType int animationType) {
        this.mOpenAnimationEnable = true;
        mCustomAnimation = null;
        switch (animationType) {
            case ALPHAIN:
                mSelectAnimation = new AlphaInAnimation();
                break;
            case SCALEIN:
                mSelectAnimation = new ScaleInAnimation();
                break;
            case SLIDEIN_BOTTOM:
                mSelectAnimation = new SlideInBottomAnimation();
                break;
            case SLIDEIN_LEFT:
                mSelectAnimation = new SlideInLeftAnimation();
                break;
            case SLIDEIN_RIGHT:
                mSelectAnimation = new SlideInRightAnimation();
                break;
            default:
                break;
        }
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
        if (mEmptyLayout == null) {
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
    protected int getHeaderLayoutCount() {
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

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View view, int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
    }

    public interface OnItemChildClickListener {
        void onItemChildClick(View view, int position);
    }

    public void setOnItemChildClickListener(OnItemChildClickListener listener) {
        this.mOnItemChildClickListener = listener;
    }

    public OnItemChildClickListener getOnItemChildClickListener() {
        return mOnItemChildClickListener;
    }

    public interface OnItemChildLongClickListener {
        boolean onItemChildLongClick(View view, int position);
    }

    public void setOnItemChildLongClickListener(OnItemChildLongClickListener listener) {
        this.mOnItemChildLongClickListener = listener;
    }

    public OnItemChildLongClickListener getOnItemChildLongClickListener() {
        return mOnItemChildLongClickListener;
    }

}
