package com.cwenhui.chowhound.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ShopTabAdapter extends FragmentPagerAdapter {
	private final String TAG = "ShopTabAdapter";
	private List<Fragment> fragments;
	private static String[] TITLES = new String[] { "菜单", "评价", "店铺" };

	public ShopTabAdapter(FragmentManager fm) {
		super(fm);
	}

	public void setFragments(List<Fragment> fragments) {
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int pos) {
		return fragments.get(pos);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return TITLES[position];
	}
	
	

}
