package com.cwenhui.chowhound.utils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 图片加载时候带加载情况的监听
 * @author chenwenhui
 */
public class ImageFirstDisplayListener extends SimpleImageLoadingListener {
	public static final List<String> displayedImages = Collections
			.synchronizedList(new LinkedList<String>());
	
	@Override
	public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
		if (loadedImage != null) { // 加载成功的时候执行
			ImageView imageView = (ImageView) view;
			boolean firstDisplay = !displayedImages.contains(imageUri);
			if (firstDisplay) {
				FadeInBitmapDisplayer.animate(imageView, 500);
				displayedImages.add(imageUri);
			}
		}
	}

}
