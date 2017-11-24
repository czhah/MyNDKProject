package com.thedream.cz.myndkproject.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.widget.banner.Banner;

import java.util.ArrayList;
import java.util.List;

public class BannerActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private Button btnPlay;
    private Button btnPause;
    private MyAdapter myAdapter;
    private List<String> urlList = new ArrayList<>();
    private ImageView ivEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        urlList.add("http://img5.imgtn.bdimg.com/it/u=2420932970,3348319291&fm=26&gp=0.jpg");
        urlList.add("http://img1.imgtn.bdimg.com/it/u=1041441079,1056885786&fm=26&gp=0.jpg");
        urlList.add("http://img2.imgtn.bdimg.com/it/u=2904862568,1861126420&fm=26&gp=0.jpg");
        urlList.add("http://img2.imgtn.bdimg.com/it/u=3270180186,3421112412&fm=26&gp=0.jpg");

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        myAdapter = new MyAdapter(initData());
        viewPager.setAdapter(myAdapter);

        btnPlay = (Button) findViewById(R.id.btn_play);
        btnPause = (Button) findViewById(R.id.btn_pause);
        ivEmpty = (ImageView) findViewById(R.id.iv_empty);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int currentItem = viewPager.getCurrentItem();
//                if(currentItem == myAdapter.getCount() - 1) {
//                    viewPager.setCurrentItem(0);
//                }else {
//                    viewPager.setCurrentItem(currentItem + 1);
//                }

                ivEmpty.setVisibility((ivEmpty.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
            }
        });

        Banner banner = (Banner) findViewById(R.id.banner);
        banner.setImageUrl(urlList).play();
    }

    private List<View> initData() {
        List<View> list = new ArrayList<>();
        ImageView iv1 = new ImageView(this);
        iv1.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(this).load("http://img5.imgtn.bdimg.com/it/u=2420932970,3348319291&fm=26&gp=0.jpg")
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.mipmap.ic_launcher).into(iv1);
        list.add(iv1);

        ImageView iv2 = new ImageView(this);
        iv2.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(this).load("http://img1.imgtn.bdimg.com/it/u=1041441079,1056885786&fm=26&gp=0.jpg")
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.mipmap.ic_launcher).into(iv2);
        list.add(iv2);

        ImageView iv3 = new ImageView(this);
        iv3.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(this).load("http://img2.imgtn.bdimg.com/it/u=2904862568,1861126420&fm=26&gp=0.jpg")
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.mipmap.ic_launcher).into(iv3);
        list.add(iv3);

        ImageView iv4 = new ImageView(this);
        iv4.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(this).load("http://img2.imgtn.bdimg.com/it/u=3270180186,3421112412&fm=26&gp=0.jpg")
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.mipmap.ic_launcher).into(iv4);
        list.add(iv4);

        return list;
    }

    private static class MyAdapter extends PagerAdapter {

        private List<View> list;

        public MyAdapter(List<View> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = list.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
