package com.cwenhui.mark.common;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

import com.cwenhui.mark.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.io.File;

/**
 * 异步图片加载工具类
 * Created by cwenhui on 2016.02.23
 */
public class ImageLoaderHelper {
    private static ImageLoaderHelper imageLoaderHelper = new ImageLoaderHelper();

    public static ImageLoaderHelper getInstance(){
        return imageLoaderHelper;
    }

    private ImageLoaderHelper(){}

    /**
     * ImageLoader 图片组件初始化
     * @param context
     * @param loader 使用的ImageLoader
     * @param dirName 图片保存的位置
     */
    public DisplayImageOptions initImageLoader(Context context, ImageLoader loader, String dirName) {
        return initImageLoader(context, loader, dirName, R.drawable.mianshi);
    }

    /**
     * ImageLoader 图片组件初始化
     * @param context
     * @param loader 使用的ImageLoader
     * @param dirName 图片保存的位置
     * @param resId 图片在下载期间、图片Uri为空或是错误的时候、图片加载/解码过程中错误时候显示的图片
     * @return
     */
    public DisplayImageOptions initImageLoader(Context context, ImageLoader loader, String dirName, int resId) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCacheSize(getMemoryCacheSize(context))
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .discCache(new UnlimitedDiscCache(new File(dirName)))
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(resId) 		                    // 设置图片在下载期间显示的图片
                .showImageForEmptyUri(resId) 	                    // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(resId) 		                    // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true) 								// 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) 									// 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true) 							// 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .displayer(new RoundedBitmapDisplayer(20))			// 是否设置为圆角，弧度为多少
                .displayer(new SimpleBitmapDisplayer())				// 是否图片加载好后渐入的动画时间,
                                        //此时不要使用FadeInBitmapDisplayer(100),否则更新listview时会出现闪烁的问题
                .build();
        loader.init(config);
        return options;
    }

    /**
     * 获得缓存空间的大小（1/8 of app memory limit）
     * @param context
     * @return
     */
    private int getMemoryCacheSize(Context context) {
        int memoryCacheSize;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            int memClass = ((ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE))
                    .getMemoryClass();
            memoryCacheSize = (memClass / 8) * 1024 * 1024; // 1/8 of app memory limit
        } else {
            memoryCacheSize = 2 * 1024 * 1024;
        }
        return memoryCacheSize;
    }
}
