package com.cwenhui.chowhound.bean;

import android.content.Context;

public class ShopCartActionItem {
	private String goodsName;
	private String price;
	private String selectedNum;
	private int mActionId = -1;
	
	public ShopCartActionItem(String goodsName, String price,
			String selectedNum, int actionId) {
		super();
		this.goodsName = goodsName;
		this.price = price;
		this.selectedNum = selectedNum;
		this.mActionId = actionId;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getSelectedNum() {
		return selectedNum;
	}

	public void setSelectedNum(String selectedNum) {
		this.selectedNum = selectedNum;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public int getActionId() {
		return mActionId;
	}

	public void setActionId(int actionId) {
		this.mActionId = actionId;
	}

}
