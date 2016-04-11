package com.cwenhui.chowhound.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.cwenhui.chowhound.adapter.ShopTabAdapter;
import com.cwenhui.chowhound.fragment.CommentFragment;
import com.cwenhui.chowhound.fragment.MenuFragment;
import com.cwenhui.chowhound.fragment.ShopDetailFragment;
import com.example.chowhound.R;
import com.viewpagerindicator.TabPageIndicator;

public class ShopActivity extends FragmentActivity {
	private final String Tag = "ShopActivity";
	private TextView shopName;
	private ViewPager mViewPager;
	private TabPageIndicator mTabPageIndicator;
	private ShopTabAdapter tabAdapter;
	private List<Fragment> mFragments = new ArrayList<Fragment>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop);
		initData();
		initView();
	}

	private void initData() {
		MenuFragment menuFragment = new MenuFragment();	
		CommentFragment commentFragment = new CommentFragment();
		ShopDetailFragment shopDetailFragment =new ShopDetailFragment();
		mFragments.add(menuFragment);
		mFragments.add(commentFragment);
		mFragments.add(shopDetailFragment);
		tabAdapter = new ShopTabAdapter(getSupportFragmentManager());
		tabAdapter.setFragments(mFragments);

		Intent intent = getIntent();
		Bundle bundle = new Bundle();
		bundle.putInt("shopId", intent.getIntExtra("shopId", -1));
		bundle.putString("shopName", intent.getStringExtra("shopName"));
		menuFragment.setArguments(bundle);
	}

	private void initView() {
		shopName = (TextView) findViewById(R.id.tv_topbar2_shop_Name);
		shopName.setText(getIntent().getStringExtra("shopName"));
		mViewPager = (ViewPager) findViewById(R.id.vp_activity_shop);
		mTabPageIndicator = (TabPageIndicator) findViewById(R.id.indicator_activity_shop);
		mViewPager.setAdapter(tabAdapter);
		mTabPageIndicator.setViewPager(mViewPager, 0);
	}

}
