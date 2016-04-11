package com.cwenhui.chowhound.bean;

public class IndexFragmentShop {
	private int shopId;
	private String shopImg;				//商店图片路径
	private String shopName;			//商店名字
	private int salesVolume;			//销量
	private String earliestArrivalTime;	//最早送达时间
	private String deliveryFee;			//运费
	private int deliveryCondition;		//送货条件
	private int discount;				//优惠,0表示无优惠, 1表示满减, 2表示满返  3表示满送
	private String discountDetail;		//详细优惠
	
	public IndexFragmentShop() {
//		super();
	}

	public IndexFragmentShop(String shopImg, String shopName, int salesVolume,
			String earliestArrivalTime, String deliveryFee,
			int deliveryCondition, int discount, String discountDetail) {
//		super();
		this.shopImg = shopImg;
		this.shopName = shopName;
		this.salesVolume = salesVolume;
		this.earliestArrivalTime = earliestArrivalTime;
		this.deliveryFee = deliveryFee;
		this.deliveryCondition = deliveryCondition;
		this.discount = discount;
		this.discountDetail = discountDetail;
	}
	
	public int getShopId() {
		return shopId;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
	}

	public String getShopImg() {
		return shopImg;
	}

	public void setShopImg(String shopImg) {
		this.shopImg = shopImg;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public int getSalesVolume() {
		return salesVolume;
	}

	public void setSalesVolume(int salesVolume) {
		this.salesVolume = salesVolume;
	}

	public String getEarliestArrivalTime() {
		return earliestArrivalTime;
	}

	public void setEarliestArrivalTime(String earliestArrivalTime) {
		this.earliestArrivalTime = earliestArrivalTime;
	}

	public String getDeliveryFee() {
		return deliveryFee;
	}

	public void setDeliveryFee(String deliveryFee) {
		this.deliveryFee = deliveryFee;
	}

	public int getDeliveryCondition() {
		return deliveryCondition;
	}

	public void setDeliveryCondition(int deliveryCondition) {
		this.deliveryCondition = deliveryCondition;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	public String getDiscountDetail() {
		return discountDetail;
	}

	public void setDiscountDetail(String discountDetail) {
		this.discountDetail = discountDetail;
	}
	
	

}
