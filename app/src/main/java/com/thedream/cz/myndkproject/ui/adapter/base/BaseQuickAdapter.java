package com.thedream.cz.myndkproject.ui.adapter.base;

import android.content.Context;
import android.support.annotation.IdRes;
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

    private Context mContext;
    protected List<T> mData;

    //header footer
    private LinearLayout mHeaderLayout;
    private LinearLayout mFooterLayout;
    //empty
    private FrameLayout mEmptyLayout;

    private boolean mHeadAndEmptyEnable;
    private boolean mFootAndEmptyEnable;

    protected void refreshData(List<T> list) {
        this.mData = list == null ? new ArrayList<T>() : list;
        notifyDataSetChanged();
    }

    protected void loadMore(List<T> list) {
        if (list == null) return;
        this.mData.addAll(list);
        notifyDataSetChanged();
    }

    protected abstract
    @IdRes
    int getLayoutResId();

    protected abstract void convert(K holder, T item);

    @Override
    public void onViewAttachedToWindow(K holder) {
        super.onViewAttachedToWindow(holder);
        if (holder.getItemViewType() == HEADER_VIEW) {
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
    public int getItemViewType(int position) {
        //  判断没有数据 且包含空布局
        if (getEmptyViewCount() == 1) {
            //  如果包含头布局
            boolean header = mHeadAndEmptyEnable && getHeaderLayoutCount() != 0;
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
            int adapterCount = mData.size();
            if (adjPosition < adapterCount) {
                //  返回数据的类型  这里应该用position吧，使用adjPosition去掉了numHeaders
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

    private int getDefItemViewType(int position) {
        return CONTENT_VIEW;
    }

    @Override
    public K onCreateViewHolder(ViewGroup parent, int viewType) {
        K baseViewHolder = null;
        mContext = parent.getContext();
        LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
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
                //  TODO 底部加载布局
                baseViewHolder = createBaseViewHolder(mFooterLayout);
                break;
            case CONTENT_VIEW:
            default:
                baseViewHolder = createBaseViewHolder(getItemView(mLayoutInflater, getLayoutResId(), parent));
                break;
        }
        baseViewHolder.setAdapter(this);
        return baseViewHolder;
    }

    private View getItemView(LayoutInflater layoutInflater, int layoutResId, ViewGroup parent) {
        return layoutInflater.inflate(layoutResId, parent, false);
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

    protected T getItem(int position) {
        if (position < mData.size()) return mData.get(position);
        return null;
    }

    private void autoLoadMore(int position) {

    }

    protected int getRealPos(int position) {
        return position - getHeaderLayoutCount();
    }

    @Override
    public int getItemCount() {
        int count;
        if (getEmptyViewCount() == 1) {
            //  如果包含空布局
            count = 1;
            //  且包含头布局
            if (mHeadAndEmptyEnable && getHeaderLayoutCount() != 0) {
                count++;
            }
            //  且包含底部布局
            if (mFootAndEmptyEnable && getFooterLayoutCount() != 0) {
                count++;
            }
        } else {
            count = getHeaderLayoutCount() + mData.size() + getFooterLayoutCount() + getLoadMoreViewCount();
        }
        return count;
    }

    private int getEmptyViewCount() {
        return 0;
    }

    private int getHeaderLayoutCount() {
        if (mHeaderLayout == null || mHeaderLayout.getChildCount() == 0) {
            return 0;
        }
        return 1;
    }

    private int getFooterLayoutCount() {
        if (mFooterLayout == null || mFooterLayout.getChildCount() == 0) {
            return 0;
        }
        return 1;
    }

    private int getLoadMoreViewCount() {
        return 0;
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
            int position = getHeaderViewPosition();
            if (position != -1) {
                notifyItemInserted(position);
            }
        }
        return index;
    }

    private int getHeaderViewPosition() {
        //Return to header view notify position
        if (getEmptyViewCount() == 1) {
            if (mHeadAndEmptyEnable) {
                return 0;
            } else {
                //  如果不包含空布局，则不刷新？？？
            }
        } else {
            return 0;
        }
        return -1;
    }

}
