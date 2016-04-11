package com.cwenhui.chowhound.utils;

import java.util.List;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

public class MapUtil implements BDLocationListener, OnGetGeoCoderResultListener {
	private final String TAG = "MapUtil";
	private List<PoiInfo> poiInfos = null;
	private GeoCoder geoCoder;
	/**
	 * 定位模式
	 */
	// private MyLocationConfiguration.LocationMode mCurrentMode;
	/**
	 * 定位端
	 */
	private LocationClient mLocClient;

	// private String city;

	public MapUtil(Context context) {
		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		SDKInitializer.initialize(context);
		// 创建GeoCoder实例对象
		geoCoder = GeoCoder.newInstance();
		// 设置查询结果监听者,用于处理反地理编码后得到的结果
		geoCoder.setOnGetGeoCodeResultListener(this);
		// mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
		// 初始化定位
		mLocClient = new LocationClient(context);
		// 注册定位监听
		mLocClient.registerLocationListener(this);
		// 定位选项
		LocationClientOption option = new LocationClientOption();
		/**
		 * coorType - 取值有3个： 返回国测局经纬度坐标系 ：gcj02 返回百度墨卡托坐标系 ：bd09 返回百度经纬度坐标系
		 * ：bd09ll
		 */
		option.setCoorType("bd09ll"); // 如果发现定位不准确,我们应该来检查定位坐标类型是否有误或定位坐标类型不合适,可以换其他坐标类型来重新定位试试看
		// 设置是否需要地址信息，默认为无地址
		option.setIsNeedAddress(true);
		// 设置是否需要返回位置语义化信息，可以在BDLocation.getLocationDescribe()中得到数据，ex:"在天安门附近"，
		// 可以用作地址信息的补充
		option.setIsNeedLocationDescribe(true);
		// 设置是否需要返回位置POI信息，可以在BDLocation.getPoiList()中得到数据
		option.setIsNeedLocationPoiList(true);
		/**
		 * 设置定位模式 Battery_Saving 低功耗模式 耗电量小 Device_Sensors 仅设备(Gps)模式
		 * Hight_Accuracy 高精度模式 耗电量大
		 */
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
		// 设置是否打开gps进行定位
		option.setOpenGps(true);
		// 设置扫描间隔，单位是毫秒 当<1000(1s)时，定时定位无效
		option.setScanSpan(1000);

		// 设置 LocationClientOption(勿忘)
		mLocClient.setLocOption(option);

		// 开始定位
		mLocClient.start();
	}

	@Override
	public void onReceiveLocation(BDLocation bdLocation) {
		// 如果bdLocation为空则不再处理新数据接收的位置
		if (bdLocation == null) {
			return;
		}
		LatLng cenpt = new LatLng(bdLocation.getLatitude(),
				bdLocation.getLongitude());
		geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(cenpt));
		// 获取城市，待会用于POISearch
		// city = bdLocation.getCity();
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
		poiInfos = reverseGeoCodeResult.getPoiList(); // 获取所有的poi信息
	}

	public List<PoiInfo> getPoiInfos() {
		return poiInfos;
	}

	public void destroy() {
		// 退出时停止定位
		mLocClient.stop();
		// 释放资源
		if (geoCoder != null) {
			geoCoder.destroy();
		}
	}

}
