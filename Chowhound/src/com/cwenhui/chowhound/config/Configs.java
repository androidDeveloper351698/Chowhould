package com.cwenhui.chowhound.config;

public class Configs {
	public static final String IP = "120.27.105.241";
	public static final String PORT = "8080";
	public static final String APIShops = "http://120.27.105.241:8080/spring-mvc-study/api/shops";
	public static final String APIShopsByPage = "http://120.27.105.241:8080/spring-mvc-study/api/shopsByPage?";
	public static final String APIGoodsTypeByShopId = "http://www.cwenhui.com:8080/spring-mvc-study/api/getGoodsTypeByShopId/";
	public static final String APIOrderDetailsByUid = "http://www.cwenhui.com:8080/spring-mvc-study/api/getOrderDetailsByUid?";
	public static final String APIOrdersByUid = "http://www.cwenhui.com:8080/spring-mvc-study/api/OrdersByUid?";//uid=5&pageNo=1&pageSize=6
	public static final String APIOrderDetailsByOrderId = "http://www.cwenhui.com:8080/spring-mvc-study/api/getOrderDetails?";//orderId=2&pageNo=1&pageSize=5
	public static final String APILogin = "http://www.cwenhui.com:8080/spring-mvc-study/api/login";	
	public static final String APIRegister = "http://www.cwenhui.com:8080/spring-mvc-study/api/addUser";
	public static final String APISaveOrder = "http://www.cwenhui.com:8080/spring-mvc-study/api/saveOrderByUsername";
	public static final String APIOderStateByOrderId = "http://www.cwenhui.com:8080/spring-mvc-study/api/getOrderStateByOrderId/";
	public static final String APIDeleteOrderByOrderId = "http://www.cwenhui.com:8080/spring-mvc-study/api/deleteOrderByOrderId/";
	
	public static final String APITestForImg = "http://img.my.csdn.net/uploads/201504/12/1428806103_9476.png";

	public static final String APIShopUploadImg = "http://120.27.105.241:8080/spring-mvc-study/upload/";
	
	public static final boolean DEVELOPER_MODE = false;
	
	public static final String CURRENT_USER_ID="currentUserId";			//当前登录用户的uid
	public static final String CURRENT_USER="currentUser";				//当前登录用户的用户名
	public static final String USERNAMES="usernames";					//用户使用过的用户名
	public static final String LOGIN_STATE="loginState";				//当前的登录状态
	
	//当前收货地址信息
	public static final String CURRENT_ADDRESS_ID="currentAddressId";						//id用来判断是哪个item被选中,从而显示该item的勾
	public static final String CURRENT_DELIVERY_ADDRESS="currentDeliveryAddress";
	public static final String CURRENT_RECEIVER="currentReceiver";
	public static final String CURRENT_PHONE="currentPhone";
	
	//mob短信验证
	public static final String SHARESDK_APPKEY = "112de214a263d";
	public static final String SHARESDK_APPSECRET = "5a2e543493406be13e1e68c36aeff181";
}
