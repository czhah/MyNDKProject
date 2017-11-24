package com.thedream.cz.myndkproject.widget.banner;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.widget.banner.listener.OnBannerItemClickListener;
import com.thedream.cz.myndkproject.widget.banner.view.BannerViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenzhuang on 2017/10/19.
 * Describe:  轮播图
 */
public class Banner extends RelativeLayout implements ViewPager.OnPageChangeListener {

    private BannerViewPager mViewPager;
    private LinearLayout llIndicator;

    private MyAdapter mAadpter;
    private List<View> imageViews;
    private List<String> imgUrlList;

    private int count = 0;
    private int indicatorWidth = BannerConfig.PADDING_SIZE;
    private int indicatorHeight = BannerConfig.PADDING_SIZE;
    private int indicatorPadding = BannerConfig.PADDING_SIZE;
    private boolean isAutoPlay = BannerConfig.IS_AUTO_PLAY;
    private int duration = BannerConfig.DURATION;

    private OnBannerItemClickListener onBannerItemClickListener;  //  item点击事件
    private int currentItem = 1;
    private int lastItem = 0;

    private WeakHandler handler = new WeakHandler();

    public Banner(Context context) {
        this(context, null);
    }

    public Banner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Banner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = View.inflate(context, R.layout.view_banner_layout, Banner.this);
        llIndicator = (LinearLayout) view.findViewById(R.id.ll_banner_indicator);
        mViewPager = (BannerViewPager) view.findViewById(R.id.vp_banner);
        mViewPager.addOnPageChangeListener(this);
    }

    public Banner setImageUrl(List<String> imageUrl) {
        this.imgUrlList = imageUrl;
        return this;
    }

    public Banner setOnItemClickListener(OnBannerItemClickListener listener) {
        this.onBannerItemClickListener = listener;
        return this;
    }

    public void play() {
        if(imgUrlList == null || imgUrlList.size() == 0) return ;
        count = imgUrlList.size();
        initIndicator();
        initImage();
        mViewPager.setAdapter(mAadpter = new MyAdapter());
        mViewPager.setCurrentItem(1);
        if(isAutoPlay) {
            startPlay();
        }
    }

    private void initIndicator() {
        for(int i=0; i<count; i++) {
            ImageView dot = new ImageView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(indicatorWidth, indicatorHeight);
            if(i == 0) {
                params.leftMargin = 0;
            }else {
                params.leftMargin = indicatorPadding;
            }
            dot.setLayoutParams(params);
            dot.setBackgroundResource(R.drawable.sel_indicator_enable);
            dot.setEnabled(i == 0);
            llIndicator.addView(dot);
        }
    }

    private void initImage() {
        imageViews = new ArrayList<>();
        for(int i=0; i<=count + 1; i++){
            ImageView img = new ImageView(getContext());
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(getContext()).load(imgUrlList.get(getRealIndex(i)))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.mipmap.ic_launcher).into(img);
            imageViews.add(img);
        }
    }

    private void startPlay() {
        handler.removeCallbacks(task);
        handler.postDelayed(task, duration);
    }

    private void stopPlay() {
        handler.removeCallbacks(task);
    }

    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            if(count > 1 && isAutoPlay) {
                if(currentItem == 0) {
                    currentItem = count;
                    mViewPager.setCurrentItem(currentItem, false);
                    handler.post(task);
                }else if(currentItem == count + 1) {
                    currentItem = 1;
                    mViewPager.setCurrentItem(currentItem, false);
                    handler.post(task);
                }else {
                    currentItem = currentItem + 1;
                    mViewPager.setCurrentItem(currentItem);
                    handler.postDelayed(task, duration);
                }
            }
        }
    };

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int index = getRealIndex(position);
        llIndicator.getChildAt(lastItem).setEnabled(false);
        llIndicator.getChildAt(index).setEnabled(true);
        lastItem = index;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        currentItem = mViewPager.getCurrentItem();

        if(state == ViewPager.SCROLL_STATE_IDLE || state == ViewPager.SCROLL_STATE_DRAGGING) {
            //  滑动停止、准备滑动
            //  跳转到真实位置
            if(currentItem == 0) {
                mViewPager.setCurrentItem(count, false);
            }else if(currentItem == count + 1) {
                mViewPager.setCurrentItem(1, false);
            }
        }
    }

    private class MyAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return imageViews == null ? 0 : imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = imageViews.get(position);
            container.addView(view);
            if(onBannerItemClickListener != null) {
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //  item点击事件
                        onBannerItemClickListener.onItemClick(view, getRealIndex(position));
                    }
                });
            }
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 获取实际位置
     * @param position
     * @return
     */
    private int getRealIndex(int position) {
        if(position == 0) {
            return count - 1;
        }else if(position == count + 1) {
            return 0;
        }else {
            return position - 1;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if(isAutoPlay) {
            if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                stopPlay();
            }else if(event.getAction() == MotionEvent.ACTION_UP){
                startPlay();
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if(isAutoPlay) {
            if(visibility == View.VISIBLE) {
                startPlay();
            }else {
                stopPlay();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if(isAutoPlay)stopPlay();
        super.onDetachedFromWindow();
    }
}
