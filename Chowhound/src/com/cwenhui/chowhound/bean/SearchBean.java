package com.cwenhui.chowhound.bean;

public class SearchBean {

	private int goodsId;
	private String goodsImg;
	private String goodsName;
	private String price;
	private String salesVolume;
	private String shop;

	// public SearchBean(int goodsImg, String goodsName, String content, String
	// salesVolume, String shop) {
	// this.goodsImg = goodsImg;
	// this.goodsName = goodsName;
	// this.price = content;
	// this.salesVolume = salesVolume;
	// this.shop = shop;
	// }
	public SearchBean(int goodsId, String goodsImg, String goodsName,
			String price, String salesVolume, String shop) {
		super();
		this.goodsId = goodsId;
		this.goodsImg = goodsImg;
		this.goodsName = goodsName;
		this.price = price;
		this.salesVolume = salesVolume;
		this.shop = shop;
	}

	public int getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsImg() {
		return goodsImg;
	}

	public void setGoodsImg(String goodsImg) {
		this.goodsImg = goodsImg;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getSalesVolume() {
		return salesVolume;
	}

	public void setSalesVolume(String salesVolume) {
		this.salesVolume = salesVolume;
	}

	public String getShop() {
		return shop;
	}

	public void setShop(String shop) {
		this.shop = shop;
	}
}
