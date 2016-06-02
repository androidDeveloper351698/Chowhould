package com.cwenhui.chowhound.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.cwenhui.chowhound.config.Configs;
import com.cwenhui.chowhound.utils.ImageFirstDisplayListener;
import com.cwenhui.chowhound.utils.ImageLoaderHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 填充ViewPager的页面适配器
 * Created by cwenhui on 2016.02.23
 */
public class SlideShowViewAdapter extends PagerAdapter {
    private static final String TAG = "SlideShowViewAdapter";
    private Context context;
    private List<ImageView> imageViewsList;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;								//显示图片的各种设置
    private ImageFirstDisplayListener displayListener = new ImageFirstDisplayListener();

    public SlideShowViewAdapter(Context context, List<ImageView> imageViewsList) {
        this.imageViewsList = imageViewsList;
        this.context = context;
        options = ImageLoaderHelper.getInstance().initImageLoader(context, imageLoader, Configs.IMAGE_PATH);
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager)container).removeView(imageViewsList.get(position));
    }

    @Override
    public Object instantiateItem(View container, int position) {
        ImageView imageView = imageViewsList.get(position);

        imageLoader.displayImage(imageView.getTag() + "", imageView,options,displayListener);

        ((ViewPager)container).addView(imageView);
        return imageViewsList.get(position);
    }

    @Override
    public int getCount() {
        return imageViewsList.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

}
