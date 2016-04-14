package com.cwenhui.chowhound.bean;


public class OrderFragmentBean {
	private static final String TAG = "OrderFragmentBean";
	private int orderId;
	private String orderName;
	private String shopImg;
	private String goodsName;
	private String consumeTime;
	private String orderState; // 0 待付款 , 1 等待商家接单, 2 派送中 , 3 买家验收 , 4 订单完成,
	private int totalPrice;

	public OrderFragmentBean(int orderId, String orderName, String shopImg,
			String goodsName, String consumeTime, String orderState,
			int totalPrice) {
		super();
		this.orderId = orderId;
		this.orderName = orderName;
		this.shopImg = shopImg;
		this.goodsName = goodsName;
		this.consumeTime = consumeTime;
		this.orderState = orderState;
		this.totalPrice = totalPrice;
	}

//	public OrderFragmentBean(String orderName, String shopImg,
//			String goodsName, String consumeTime, String orderState,
//			int totalPrice) {
//		this.orderName = orderName;
//		this.shopImg = shopImg;
//		this.goodsName = goodsName;
//		this.consumeTime = consumeTime;
//		this.orderState = orderState;
//		this.totalPrice = totalPrice;
//	}
	
	public String getOrderName() {
		return orderName;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public String getShopImg() {
		return shopImg;
	}

	public void setShopImg(String shopImg) {
		this.shopImg = shopImg;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getConsumeTime() {
		return consumeTime;
	}

	public void setConsumeTime(String consumeTime) {
		this.consumeTime = consumeTime;
	}

	public String getOrderState() {
		return orderState;
	}

	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

}
