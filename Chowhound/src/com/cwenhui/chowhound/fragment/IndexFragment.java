package com.cwenhui.chowhound.fragment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.cwenhui.chowhound.adapter.CommonAdapter;
import com.cwenhui.chowhound.adapter.IndexFragmentGalleryImgAdapter;
import com.cwenhui.chowhound.adapter.PullDownPinnedHeaderAdapter;
import com.cwenhui.chowhound.bean.CommonBean;
import com.cwenhui.chowhound.bean.IndexFragmentShop;
import com.cwenhui.chowhound.config.Configs;
import com.cwenhui.chowhound.ui.AddressActivity;
import com.cwenhui.chowhound.ui.CaptureActivity;
import com.cwenhui.chowhound.ui.ShopActivity;
import com.cwenhui.chowhound.utils.GetDataTask;
//import com.cwenhui.chowhound.utils.ImageLoader;
import com.cwenhui.chowhound.utils.ViewHolder;
import com.cwenhui.chowhound.widget.GuideGallery;
import com.cwenhui.chowhound.widget.LoadingDialog;
import com.example.chowhound.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class IndexFragment extends Fragment implements OnScrollListener,
		OnItemClickListener, OnClickListener, OnRefreshListener2<ListView> {
	final String TAG = "IndexFragment";
	private View mView;
	static final int MENU_MANUAL_REFRESH = 0;
	static final int MENU_DISABLE_SCROLL = 1;
	static final int MENU_SET_MODE = 2;
	static final int MENU_DEMO = 3;
	private static final int INIT = 0;
	public static final int PULL_DOWN = 1;
	public static final int PULL_UP = 2;
	public int PAGE = 2; 										// 默认第2页,初始化数据时已加载了第一页的数据
	private int PAGE_SIZE = 5; 									// 设置每次加载5条数据
	// 详细分类列表(上拉加载下拉刷新)
	public LinkedList<IndexFragmentShop> mListItems;
	public PullToRefreshListView mPullRefreshListView;
	public PullDownPinnedHeaderAdapter adapter;
	private GridView mGridView; 								// 分类控件
	private List<CommonBean> classifyData; 						// 分类数据

	// 图片轮播
	public boolean timeFlag = true;
	public ImageTimerTask timeTask = null; 						// 时钟任务，每隔5秒图片轮播中的图片换一次
	private Thread timeThread = null; 							// 时钟线程
	int galleryposition = 0; 									// Gallery当前轮播的位置
	public GuideGallery images_gallery; 						// 轮播控件
	private int positon = 0; 									// 轮播时的点的位置
	private boolean isExit = false;
	Timer autoGallery = new Timer(); 							// Gallery的计时器

	private ImageView scan; 									// 扫码
	private LinearLayout address;	 							// 选址

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_main_index, container, false);

		initData();
		initView();

		return mView;
	}

	private void initView() {
		// item中的各个控件
		scan = (ImageView) mView.findViewById(R.id.iv_fragment_index_scan);
		address = (LinearLayout) mView.findViewById(R.id.fragment_index_top_bar);
		scan.setOnClickListener(this);
		address.setOnClickListener(this);

		mPullRefreshListView = (PullToRefreshListView) mView.findViewById(R.id.pull_refresh_list_fragment_index);
		mPullRefreshListView.setMode(Mode.BOTH); 								// 设置你需要刷新的模式,BOTH是下拉和上拉都可以
		mPullRefreshListView.setOnRefreshListener(this);						// 设置上拉下拉刷新监听
		ListView actualListView = mPullRefreshListView.getRefreshableView();

		adapter = new PullDownPinnedHeaderAdapter<IndexFragmentShop>(
				getActivity(), mListItems,
				R.layout.item_fragment_main_index_shops_others,
				R.layout.item_fragment_main_index_shops_first) {

		};
		actualListView.setAdapter(adapter);
		actualListView.addHeaderView(LayoutInflater.from(getActivity())
				.inflate(R.layout.fragment_main_index_listview_header, null));
		actualListView.setOnScrollListener(this);
		actualListView.setOnItemClickListener(this);

		// 分类视图
		mGridView = (GridView) mView
				.findViewById(R.id.gv_fragment_main_order_classify);
		// 为分类 GridView 配置适配器
		mGridView.setAdapter(new CommonAdapter<CommonBean>(getActivity()
				.getApplicationContext(), classifyData,
				R.layout.item_fragment_main_index_classify) {

			@Override
			public void convert(ViewHolder holder, CommonBean t) {
				holder.setImageResource(
						R.id.iv_item_fragment_main_index_classify,
						t.getResource());
				holder.setText(R.id.tv_item_fragment_main_index_classify,
						t.getText());
			}
		});

		images_gallery = (GuideGallery) mView
				.findViewById(R.id.gallery_fragment_main_order);
		images_gallery.setImageActivity(this); // 初始化GuideGallery
		IndexFragmentGalleryImgAdapter imageAdapter = new IndexFragmentGalleryImgAdapter(
				this);
		images_gallery.setAdapter(imageAdapter); // 为 GuideGallery设置适配器
		LinearLayout pointLinear = (LinearLayout) mView
				.findViewById(R.id.ll_fragment_main_order_point_linear); // GuideGallery中的点
		for (int i = 0; i < 2; i++) { // 2个点
			ImageView pointView = new ImageView(getActivity());
			if (i == 0) {
				pointView.setBackgroundResource(R.drawable.feature_point_cur);
			} else
				pointView.setBackgroundResource(R.drawable.feature_point);
			pointLinear.addView(pointView);
		}

		timeTask = new ImageTimerTask();
		autoGallery.scheduleAtFixedRate(timeTask, 5000, 5000); // 每隔5秒图片轮播中的图片换一次
		timeThread = new Thread() {
			public void run() {
				while (!isExit) {
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					synchronized (timeTask) {
						if (!timeFlag) {
							timeTask.timeCondition = true;
							timeTask.notifyAll();
						}
					}
					timeFlag = true;
				}
			};
		};
		timeThread.start();

	}

	private void initData() {
//		LoadingDialog.setCancelable(false);
		// 商店数据
		mListItems = new LinkedList<IndexFragmentShop>();
		new GetDataTask(this).getDatas(Configs.APIShopsByPage
				+ "pageNo=1&pageSize=5", INIT);

		// 分类数据
		classifyData = new ArrayList<CommonBean>();
		CommonBean bean;
		bean = new CommonBean("美食外卖", R.drawable.img_fragment_main_index_waimai);
		classifyData.add(bean);
		bean = new CommonBean("甜点饮品", R.drawable.img_fragment_main_index_yinpin);
		classifyData.add(bean);
		bean = new CommonBean("果蔬生鲜", R.drawable.img_fragment_main_index_guoshu);
		classifyData.add(bean);
		bean = new CommonBean("超市便利", R.drawable.img_fragment_main_index_market);
		classifyData.add(bean);
		bean = new CommonBean("鲜花速递", R.drawable.img_fragment_main_index_flower);
		classifyData.add(bean);
		bean = new CommonBean("品质好店",
				R.drawable.img_fragment_main_index_good_shop);
		classifyData.add(bean);
		bean = new CommonBean("品质正餐", R.drawable.img_fragment_main_index_dinner);
		classifyData.add(bean);
		bean = new CommonBean("全部分类",
				R.drawable.img_fragment_main_index_classify_all);
		classifyData.add(bean);
	}

	// 改变点的位置
	public void changePointView(int cur) {
		LinearLayout pointLinear = (LinearLayout) mView.findViewById(R.id.ll_fragment_main_order_point_linear);
		View view = pointLinear.getChildAt(positon);
		View curView = pointLinear.getChildAt(cur);
		if (view != null && curView != null) {
			ImageView pointView = (ImageView) view;
			ImageView curPointView = (ImageView) curView;
			pointView.setBackgroundResource(R.drawable.feature_point);
			curPointView.setBackgroundResource(R.drawable.feature_point_cur);
			positon = cur;
		}

	}

	final Handler autoGalleryHandler = new Handler() { // 接收消息，改变GuideGallery的图像
		public void handleMessage(Message message) {
			super.handleMessage(message);
			switch (message.what) {
			case 1:
				images_gallery.setSelection(message.getData().getInt("pos"));
				break;
			}
		}
	};

	// 时钟任务，设置GuideGallery 的galleryposition(图像位置)，然后通过发送消息来通知改变图像位置，实现轮播效果
	public class ImageTimerTask extends TimerTask {
		public volatile boolean timeCondition = true;

		public void run() {
			synchronized (this) {
				while (!timeCondition) {
					try {
						Thread.sleep(100);
						wait();
					} catch (InterruptedException e) {
						Thread.interrupted();
					}
				}
			}
			try {
				galleryposition = images_gallery.getSelectedItemPosition() + 1;
				System.out.println(galleryposition + "");
				Message msg = new Message();
				Bundle date = new Bundle();// 存放数据
				date.putInt("pos", galleryposition); // 存放改变后的位置
				msg.setData(date);
				msg.what = 1;// 消息标识
				autoGalleryHandler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		timeFlag = false;
	}

	@Override
	public void onPause() {
		super.onPause();
		timeTask.timeCondition = false;
	}
	
	/*
	 * firstVisibleItem:表示在现时屏幕第一个ListItem(部分显示的ListItem也算)在整个ListView的位置
	 * (下标从0开始) visibleItemCount:表示在现时屏幕可以见到的ListItem(部分显示的ListItem也算)总数
	 * totalItemCount:表示ListView的ListItem总数 listView.getFirstVisiblePosition
	 * ()表示在现时屏幕第一个ListItem(第一个ListItem部分显示也算)在整个ListView的位置(下标从0开始) listView
	 * .getLastVisiblePosition()表示在现时屏幕最后一个ListItem(最后ListItem要完全显示出来才算
	 * )在整个ListView的位置(下标从0开始)
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (firstVisibleItem > 1) {
			mView.findViewById(R.id.ll_fragment_main_order_shops_first_item)
					.setVisibility(View.VISIBLE);
		} else {
			mView.findViewById(R.id.ll_fragment_main_order_shops_first_item)
					.setVisibility(View.GONE);
		}

	}

	/*
	 * scrollState值： 当屏幕停止滚动时为SCROLL_STATE_IDLE = 0；
	 * 当屏幕滚动且用户使用的触碰或手指还在屏幕上时为SCROLL_STATE_TOUCH_SCROLL = 1；
	 * 由于用户的操作，屏幕产生惯性滑动时为SCROLL_STATE_FLING = 2
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position != 2) {
			Intent intent = new Intent(getActivity(), ShopActivity.class);
			// 此时position需要减3才对的上mListItems中的数据,因为listview前面几个item用来充当头部了
			intent.putExtra("shopId", mListItems.get(position - 3).getShopId());
			intent.putExtra("shopName", mListItems.get(position - 3).getShopName());
			startActivity(intent);
		}

	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.iv_fragment_index_scan:
			intent = new Intent(getActivity(), CaptureActivity.class);
			startActivity(intent);
			break;

		case R.id.fragment_index_top_bar:
			intent = new Intent(getActivity(), AddressActivity.class);
			startActivity(intent);
		default:
			break;
		}

	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		Toast.makeText(getActivity(), "Pull Down!",Toast.LENGTH_SHORT).show();
		new GetDataTask(IndexFragment.this).getDatas(Configs.APIShopsByPage + "pageNo=1&pageSize="+ PAGE_SIZE, PULL_DOWN);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		Toast.makeText(getActivity(), "Pull Up!",Toast.LENGTH_SHORT).show();
		new GetDataTask(IndexFragment.this).getDatas(Configs.APIShopsByPage + "pageNo=" + PAGE+ "&pageSize=" + PAGE_SIZE, PULL_UP);
	}

}