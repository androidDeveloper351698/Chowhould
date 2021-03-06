package com.cwenhui.chowhound.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cwenhui.chowhound.fragment.IndexFragment;
import com.cwenhui.chowhound.fragment.MineFragment;
import com.cwenhui.chowhound.fragment.OrderFragment;
import com.cwenhui.chowhound.utils.ListenNetStateService;
import com.cwenhui.chowhound.utils.ListenNetStateService.MyBinder;
import com.cwenhui.chowhound.widget.NoScrollViewPager;
import com.cwenhui.chowhound.widget.ZoomOutPageTransformer;
import com.example.chowhound.R;

public class MainActivity extends FragmentActivity implements OnClickListener {
	private final String Tag = "MainActivity";
	private NoScrollViewPager mViewPager;
	private FragmentPagerAdapter mAdapter;
	private List<Fragment> mFragments = new ArrayList<Fragment>();
	private RelativeLayout bottomBar;
	private TranslateAnimation showAnim; // BottomBar的出现动画
	// 底部三个按钮布局
	private LinearLayout mTabBtnIndex;
	private LinearLayout mTabBtnOrder;
	private LinearLayout mTabBtnProfile;
	
	private ListenNetStateService.MyBinder binder;			//用于监听网络并做出处理
	private ServiceConnection connection;					//用来和service关联
	private ImageView networkError;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initData();

		// 设置ViewPager监听
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				resetTabBtn();
				switch (position) {
				case 0:
					((ImageButton) mTabBtnIndex
							.findViewById(R.id.ib_main_tab_bottom_portal_index))
							.setImageResource(R.drawable.ddt_ic_img_radio_portal_index_check);
					break;
				case 1:
					((ImageButton) mTabBtnOrder
							.findViewById(R.id.ib_main_tab_bottom_portal_order))
							.setImageResource(R.drawable.ddt_ic_img_radio_portal_order_check);
					((OrderFragment)mFragments.get(position)).refleshData();		//根据是否切换账号，选择性刷新订单页面
					break;
				case 2:
					((ImageButton) mTabBtnProfile
							.findViewById(R.id.ib_main_tab_bottom_profile))
							.setImageResource(R.drawable.ddt_ic_img_radio_main_tab_profile_check);
				}
				mAdapter.notifyDataSetChanged();
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) { }

			@Override
			public void onPageScrollStateChanged(int arg0) { }
		});

	}

	protected void resetTabBtn() {
		((ImageButton) mTabBtnIndex
				.findViewById(R.id.ib_main_tab_bottom_portal_index))
				.setImageResource(R.drawable.ddt_ic_img_radio_portal_index);
		((ImageButton) mTabBtnOrder
				.findViewById(R.id.ib_main_tab_bottom_portal_order))
				.setImageResource(R.drawable.ddt_ic_img_radio_portal_order);
		((ImageButton) mTabBtnProfile
				.findViewById(R.id.ib_main_tab_bottom_profile))
				.setImageResource(R.drawable.ddt_ic_img_radio_main_tab_profile);

	}

	private void initData() {
		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				return mFragments.size();
			}

			@Override
			public android.support.v4.app.Fragment getItem(int arg0) {
				return mFragments.get(arg0);
			}
		};

		mViewPager.setAdapter(mAdapter);
		mViewPager.setOffscreenPageLimit(4);
	}

	private void initView() {
		mTabBtnIndex = (LinearLayout) findViewById(R.id.ll_main_tab_bottom_portal_index);
		mTabBtnOrder = (LinearLayout) findViewById(R.id.ll_main_tab_bottom_portal_order);
		mTabBtnProfile = (LinearLayout) findViewById(R.id.ll_main_tab_bottom_profile);
		bottomBar = (RelativeLayout) findViewById(R.id.rl_main_tab_bottom);

		mViewPager = (NoScrollViewPager) findViewById(R.id.activity_main_vp);

		networkError = (ImageView) findViewById(R.id.iv_activity_main_network_error);
		connection = new ServiceConnection() {
			
			@Override
			public void onServiceDisconnected(ComponentName name) {}
			
			@Override				//service 就是从service那边传过来的binder，此时就可以调用binder的所有public方法来和activity交互了
			public void onServiceConnected(ComponentName name, IBinder service) {		
				binder = (MyBinder) service;			//下转为MyBinder
				binder.checkNetwrok(networkError, mViewPager);
			}
		};
		Intent intent = new Intent(this, ListenNetStateService.class);		//开启service监听网络
		//BIND_AUTO_CREATE表示在Activity和Service建立关联后自动创建Service，这会使得MyService中的onCreate()方法得到执行，但onStartCommand()方法不会执行
		bindService(intent, connection, BIND_AUTO_CREATE);					//

		// 向ViewPager中添加Fragment
		IndexFragment indexFragment = new IndexFragment();
		OrderFragment orderFragment = new OrderFragment();
		MineFragment mineFragment = new MineFragment();
		mFragments.add(indexFragment);
		mFragments.add(orderFragment);
		mFragments.add(mineFragment);

		mTabBtnIndex.setOnClickListener(this);
		mTabBtnOrder.setOnClickListener(this);
		mTabBtnProfile.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_main_tab_bottom_portal_index:
			mViewPager.setCurrentItem(0, false);
			break;

		case R.id.ll_main_tab_bottom_portal_order:
			mViewPager.setCurrentItem(1, false);
			break;

		case R.id.ll_main_tab_bottom_profile:
			mViewPager.setCurrentItem(2, false);
			break;

		default:
			break;
		}

	}

}
