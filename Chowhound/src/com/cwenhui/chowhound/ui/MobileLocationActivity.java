package com.cwenhui.chowhound.ui;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.cwenhui.chowhound.adapter.PoiAdapter;
import com.cwenhui.chowhound.adapter.PoiSearchAdapter;
import com.example.chowhound.R;

public class MobileLocationActivity extends Activity implements BDLocationListener,
		OnGetGeoCoderResultListener, BaiduMap.OnMapStatusChangeListener,
		TextWatcher, OnItemClickListener, OnClickListener {
	private final String TAG = "MobileLocationActivity";
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private ListView poiList;
	private Button back;
	/**
	 * 定位模式
	 */
	private MyLocationConfiguration.LocationMode mCurrentMode;
	/**
	 * 定位端
	 */
	private LocationClient mLocClient;
	/**
	 * 是否是第一次定位
	 */
	private boolean isFirstLoc = true;
	/**
	 * 定位坐标
	 */
	private LatLng locationLatLng;
	/**
	 * 定位城市
	 */
	private String city;
	/**
	 * 反地理编码
	 */
	private GeoCoder geoCoder;
	/**
	 * 界面上方布局
	 */
	private RelativeLayout topLayout;
	/**
	 * 搜索地址输入框
	 */
	private EditText searchAddress;
	/**
	 * 搜索输入框对应的ListView
	 */
	private ListView searchPois;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_mobile_location);
		initView();
	}

	private void initView() {
		mMapView = (MapView) findViewById(R.id.activity_mobile_location_mapView);
		mBaiduMap = mMapView.getMap();

		poiList = (ListView) findViewById(R.id.activity_mobile_location_address_list);
		poiList.setOnItemClickListener(this);

		topLayout = (RelativeLayout) findViewById(R.id.activity_mobile_location_top_map);

		searchAddress = (EditText) findViewById(R.id.activity_mobile_location_search_address);

		searchPois = (ListView) findViewById(R.id.activity_mobile_location_search_pois);
		back = (Button) findViewById(R.id.btn_activity_mobile_location_back);
		back.setOnClickListener(this);
		
		// 创建GeoCoder实例对象
		geoCoder = GeoCoder.newInstance();
		// 设置查询结果监听者,用于处理反地理编码后得到的结果
		geoCoder.setOnGetGeoCodeResultListener(this);
		// 文本输入框改变监听，必须在定位完成之后
		searchAddress.addTextChangedListener(this);

		// 定义地图状态
		MapStatus mMapStatus = new MapStatus.Builder().zoom(18).build(); // 这里我们放大了地图，zoom(18)【地图缩放级别
																			// 3~20】
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
				.newMapStatus(mMapStatus);
		// 改变地图状态
		mBaiduMap.setMapStatus(mMapStatusUpdate);

		// 地图状态改变相关监听
		mBaiduMap.setOnMapStatusChangeListener(this);

		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);

		/**
		 * 定位图层显示方式 有三种方式: compass 罗盘态,显示定位方向圈,保持定位图标在地图中心 following
		 * 跟随态,保持定位图标在地图中心 normal 普通态,更新定位数据时不对地图做任何操作
		 */
		mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;

		/**
		 * 设置定位图层配置信息，只有先允许定位图层后设置定位图层配置信息才会生效 customMarker用户自定义定位图标
		 * enableDirection是否允许显示方向信息 locationMode定位图层显示方式
		 */
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				mCurrentMode, true, null));

		// 初始化定位
		mLocClient = new LocationClient(this);
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

	/**
	 * 定位监听
	 * 
	 * @param bdLocation
	 */
	@Override
	public void onReceiveLocation(BDLocation bdLocation) {

		// 如果bdLocation为空或mapView销毁后不再处理新数据接收的位置
		if (bdLocation == null || mBaiduMap == null) {
			return;
		}

		// 定位数据
		MyLocationData data = new MyLocationData.Builder()
		// 定位精度bdLocation.getRadius()
				.accuracy(bdLocation.getRadius())
				// 此处设置开发者获取到的方向信息，顺时针0-360
				.direction(bdLocation.getDirection())
				// 经度
				.latitude(bdLocation.getLatitude())
				// 纬度
				.longitude(bdLocation.getLongitude())
				// 构建
				.build();

		// 设置定位数据,此时地图会移动到我们定位的地方来(勿忘)
		mBaiduMap.setMyLocationData(data);

		// 是否是第一次定位 为了避免地图不停的做状态的改变,我们设置了是否是第一次定位的判断
		if (isFirstLoc) {
			isFirstLoc = false;
			LatLng ll = new LatLng(bdLocation.getLatitude(),
					bdLocation.getLongitude());
			MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(ll, 18);
			mBaiduMap.animateMapStatus(msu);
		}

		// 获取坐标，待会用于POI信息点与定位的距离
		locationLatLng = new LatLng(bdLocation.getLatitude(),
				bdLocation.getLongitude());
		// 获取城市，待会用于POISearch
		city = bdLocation.getCity();

	}

	// 地理编码查询结果回调函数
	@Override
	public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
	}

	// 反地理编码查询结果回调函数
	@Override
	public void onGetReverseGeoCodeResult(
			ReverseGeoCodeResult reverseGeoCodeResult) {
		List<PoiInfo> poiInfos = reverseGeoCodeResult.getPoiList(); // 获取所有的poi信息
		PoiAdapter poiAdapter = new PoiAdapter(MobileLocationActivity.this, poiInfos, R.layout.item_activity_address_neaby);
		poiList.setAdapter(poiAdapter); // 设置适配器
	}

	/**
	 * 手势操作地图，设置地图状态等操作导致地图状态开始改变
	 * 
	 * @param mapStatus
	 *            地图状态改变开始时的地图状态
	 */
	@Override
	public void onMapStatusChangeStart(MapStatus mapStatus) {
	}

	/**
	 * 地图状态变化中
	 * 
	 * @param mapStatus
	 *            当前地图状态
	 */
	@Override
	public void onMapStatusChange(MapStatus mapStatus) {
	}

	/**
	 * 地图状态改变结束
	 * 
	 * @param mapStatus
	 *            地图状态改变结束后的地图状态
	 */
	@Override
	public void onMapStatusChangeFinish(MapStatus mapStatus) {
		// 地图操作的中心点
		LatLng cenpt = mapStatus.target; // 当地图状态改变结束后获取地图此时的中心点(是一个坐标),然后我们对这个中心点的坐标进行反地理编码,每次可得到当前位置的poi信息
		geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(cenpt));
	}

	// 回退键
	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// activity 恢复时同时恢复地图控件
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// activity 暂停时同时暂停地图控件
		mMapView.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// 退出时停止定位
		mLocClient.stop();
		// 退出时关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);

		// activity 销毁时同时销毁地图控件
		mMapView.onDestroy();

		// 释放资源
		if (geoCoder != null) {
			geoCoder.destroy();
		}

		mMapView = null;
	}

	/**
     * 输入框监听---输入之前
     *
     * @param s     字符序列
     * @param start 开始
     * @param count 总计
     * @param after 之后
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    /**
     * 输入框监听---正在输入
     *
     * @param s      字符序列
     * @param start  开始
     * @param before 之前
     * @param count  总计
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    /**
     * 输入框监听---输入完毕
     *
     * @param s
     */
    @Override
    public void afterTextChanged(Editable s) { 
    	String ss = s.toString().trim();
        if (ss.equals("")) {
            searchPois.setVisibility(View.GONE);
        } 
        else {
            //创建PoiSearch实例
            PoiSearch poiSearch = PoiSearch.newInstance();
            //城市内检索
            PoiCitySearchOption poiCitySearchOption = new PoiCitySearchOption();
            //关键字
            poiCitySearchOption.keyword(ss);
            //城市
            poiCitySearchOption.city(city);
            //设置每页容量，默认为每页10条
            poiCitySearchOption.pageCapacity(10);
            //分页编号
            poiCitySearchOption.pageNum(1);
            poiSearch.searchInCity(poiCitySearchOption);
            //设置poi检索监听者
            poiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
                //poi 查询结果回调
                @Override
                public void onGetPoiResult(PoiResult poiResult) {
                    List<PoiInfo> poiInfos = poiResult.getAllPoi();
                    PoiSearchAdapter poiSearchAdapter = new PoiSearchAdapter(MobileLocationActivity.this, poiInfos, locationLatLng);
                    searchPois.setAdapter(poiSearchAdapter);
                }

                //poi 详情查询结果回调
                @Override
                public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
                }
            });
            
            searchPois.setVisibility(View.VISIBLE);
            searchPois.setOnItemClickListener(this);
        }
    }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		PoiInfo p = (PoiInfo) parent.getAdapter().getItem(position);
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putParcelable("address", p);
		intent.putExtras(bundle);
		MobileLocationActivity.this.setResult(RESULT_OK, intent);
		MobileLocationActivity.this.finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_activity_mobile_location_back:
			finish();
			break;

		default:
			break;
		}
	}

}
