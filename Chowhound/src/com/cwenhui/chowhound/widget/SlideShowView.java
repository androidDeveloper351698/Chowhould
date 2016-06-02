package com.cwenhui.chowhound.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.cwenhui.chowhound.adapter.SlideShowViewAdapter;
import com.example.chowhound.R;


/**
 * ViewPager实现的轮播图广告自定义视图，如京东首页的广告轮播图效果；
 * 既支持自动轮播页面也支持手势滑动切换页面
 */
public class SlideShowView extends FrameLayout {
	private static final String TAG = "SlideShowView";

	//轮播图图片数量
	private final static int IMAGE_COUNT = 5;
	//自动轮播的时间间隔
	private final static int TIME_INTERVAL = 5;
	//自动轮播启用开关
	private final static boolean isAutoPlay = true;

	//自定义轮播图的资源
	private String[] imageUrls;
	//放轮播图片的ImageView 的list
	private List<ImageView> imageViewsList;
	//放圆点的View的list
	private List<View> dotViewsList;

	private ViewPager viewPager;
	//当前轮播页
	private int currentItem  = 0;
	//定时任务
	private ScheduledExecutorService scheduledExecutorService;

	private Context context;

	//Handler
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			viewPager.setCurrentItem(currentItem);
		}

	};

	public SlideShowView(Context context) {
		this(context,null);
	}
	public SlideShowView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	public SlideShowView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;

		initData();
		if(isAutoPlay){
			startPlay();
		}

	}
	/**
	 * 开始轮播图切换
	 */
	private void startPlay(){
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, 4, TimeUnit.SECONDS);
	}

	/**
	 * 停止轮播图切换
	 */
	private void stopPlay(){
		scheduledExecutorService.shutdown();
	}

	/**
	 * 初始化相关Data
	 */
	private void initData(){
		imageViewsList = new ArrayList<ImageView>();
		dotViewsList = new ArrayList<View>();

		// 异步任务获取图片
		new GetListTask().execute("");
	}

	/**
	 * 初始化Views等UI
	 */
	private void initUI(Context context){
		if(imageUrls == null || imageUrls.length == 0)
			return;

		LayoutInflater.from(context).inflate(R.layout.layout_slideshow, this, true);

		LinearLayout dotLayout = (LinearLayout)findViewById(R.id.dotLayout);
		dotLayout.removeAllViews();

		// 热点个数与图片特殊相等
		for (int i = 0; i < imageUrls.length; i++) {
			ImageView view =  new ImageView(context);
			view.setTag(imageUrls[i]);
//			if(i==0)//给一个默认图
//				view.setBackgroundResource(R.drawable.mianshi);
			view.setScaleType(ScaleType.FIT_XY);
			imageViewsList.add(view);

			ImageView dotView =  new ImageView(context);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			params.leftMargin = 4;
			params.rightMargin = 4;
			dotLayout.addView(dotView, params);
			dotViewsList.add(dotView);
		}

		viewPager = (ViewPager) findViewById(R.id.viewPager);
		viewPager.setFocusable(true);

		viewPager.setAdapter(new SlideShowViewAdapter(context, imageViewsList));
		viewPager.setOnPageChangeListener(new SlideShowViewChangeListener(viewPager, handler, dotViewsList));
	}

	/**
	 *执行轮播图切换任务
	 */
	private class SlideShowTask implements Runnable{

		@Override
		public void run() {
			synchronized (viewPager) {
				currentItem = (currentItem+1)%imageViewsList.size();
				handler.obtainMessage().sendToTarget();
			}
		}
	}

	/**
	 * 销毁ImageView资源，回收内存
	 *
	 */
	private void destoryBitmaps() {

		for (int i = 0; i < IMAGE_COUNT; i++) {
			ImageView imageView = imageViewsList.get(i);
			Drawable drawable = imageView.getDrawable();
			if (drawable != null) {
				//解除drawable对view的引用
				drawable.setCallback(null);
			}
		}
	}

	/**
	 * 异步任务,获取数据
	 */
	class GetListTask extends AsyncTask<String, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			try {
				// 这里一般调用服务端接口获取一组轮播图片，下面是从百度找的几个图片
				imageUrls = new String[]{
						"http://www.cwenhui.com:8080/spring-mvc-study/upload/img_fragment_main_index_ad1.png",
						"http://www.cwenhui.com:8080/spring-mvc-study/upload/img_fragment_main_index_ad2.png"
				};
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				initUI(context);
			}
		}
	}

}