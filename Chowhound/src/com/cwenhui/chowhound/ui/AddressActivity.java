package com.cwenhui.chowhound.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.search.core.PoiInfo;
import com.cwenhui.chowhound.adapter.PoiAdapter;
import com.cwenhui.chowhound.bean.DeliveryAddress;
import com.cwenhui.chowhound.common.DialogViewHolder;
import com.cwenhui.chowhound.config.Configs;
import com.cwenhui.chowhound.utils.DBManager;
import com.cwenhui.chowhound.utils.MapUtil;
import com.cwenhui.chowhound.utils.SharedPreferencesHelper;
import com.cwenhui.chowhound.widget.CustomDialog;
import com.example.chowhound.R;

public class AddressActivity extends Activity implements OnClickListener {
	private final String TAG = "AddressActivity";
	private Button back, refleshCurrentPos;		//刷新当前位置按钮
	private TextView location;		//当前定位到的地址
	private int clickedItem;
	/**
	 * 所有布局都是放在这个listview里面的
	 */
	private ListView wholeLayout;
	/**
	 * 附近地址信息
	 */
	private List<PoiInfo> addressInfo;
	/**
	 * poi适配器,用于附近地址listview
	 */
	private PoiAdapter adapter;
	private Button addAddress; // 添加收货地址
	/**
	 * 显示用户收货地址的布局
	 */
	private LinearLayout addressList;
	private DBManager mgr;
	private SharedPreferencesHelper share;
	/**
	 * 从数据库中得到的用户设置的收货地址
	 */
	private List<DeliveryAddress> deliveryAddresses;
	private CustomDialog dialog;
	
	private MapUtil mapUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);
		initData();
		initView();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 应用的最后一个Activity关闭时应释放DB
		mgr.closeDB();
		mapUtil.destroy();
	}

	private void initData() {
		share = SharedPreferencesHelper.getInstance(this);
		// 初始化DBManager
		mgr = new DBManager(this);
		// 初始化mapUtil
		mapUtil = new MapUtil(getApplicationContext());
		
		addressInfo = new ArrayList<PoiInfo>();
		adapter = new PoiAdapter(this, addressInfo,
				R.layout.item_activity_address_neaby);
	}

	private void initView() {
		wholeLayout = (ListView) findViewById(R.id.lv_activity_address_nearby);			//总体布局,放在一个listview里面
		addAddress = (Button) findViewById(R.id.btn_activity_address_add_address);		//添加收货地址
		back = (Button) findViewById(R.id.btn_activity_address_back);
		View headerView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.activity_address_listview_header, null);	//listview头部
		refleshCurrentPos = (Button) headerView.findViewById(R.id.btn_activity_address_reflesh_address);			//刷新当前地址按钮
		location = (TextView) headerView.findViewById(R.id.tv_activity_address_location_address);					//当前位置
		addressList = (LinearLayout) headerView.findViewById(R.id.ll_activity_address_delivery_address_list);		//收货地址列表
		wholeLayout.addHeaderView(headerView);
		wholeLayout.setAdapter(adapter);
		
		addAddress.setOnClickListener(this);
		back.setOnClickListener(this);
		refleshCurrentPos.setOnClickListener(this);
		
		refleshAddressList();
		
		dialog = new CustomDialog(AddressActivity.this, null, R.layout.dialog_activity_address) {		//初始化一个对话框,用于用户编辑和删除收货地址时交互
			
			@Override
			public void convert(DialogViewHolder holder, Map<String, String> data) {
				holder.setClickListener(R.id.btn_dialog_activity_address_edit, new AddressListListener());
				holder.setClickListener(R.id.btn_dialog_activity_address_delete, new AddressListListener());
			}
		};

		wholeLayout.postDelayed(new Runnable() {			//初始化界面时获得准确的当前位置
			@Override
			public void run() {
				getPois();
			}
		}, 1500);
	}
	
	//为刷新当前位置按钮添加旋转动画
	private void loadAnimationForRefleshPos() {
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.reflesh_current_pos);
		refleshCurrentPos.startAnimation(animation);
		
	}

	/**
	 * 更新收货地址列表
	 */
	private void refleshAddressList() {
		addressList.removeAllViews();
		deliveryAddresses = mgr.queryDeliveryAddresses();
		for (DeliveryAddress deliveryAddress : deliveryAddresses) {
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			View view = LayoutInflater.from(this).inflate(
					R.layout.item_activity_address_delivery_address_list, null);
			TextView name = (TextView) view.findViewById(R.id.tv_item_delivery_address_list_name);
			TextView phone = (TextView) view.findViewById(R.id.tv_item_delivery_address_list_phone);
			TextView address = (TextView) view.findViewById(R.id.tv_item_delivery_address_list_address);
			name.setText(deliveryAddress.getAdrReceiver());
			phone.setText(deliveryAddress.getAdrPhone());
			address.setText(deliveryAddress.getAdrAddress());
			if(share.getIntValue(Configs.CURRENT_ADDRESS_ID, -1) == deliveryAddress.getAddressId()){
				view.findViewById(R.id.iv_item_delivery_address_list_checked).setVisibility(View.VISIBLE);
			}
			view.setLayoutParams(lp);
			view.setId(deliveryAddress.getAddressId());		//为每一项收货地址添加id,方便监听时判断是哪个item
			view.setOnClickListener(new AddressListListener());
			view.setOnLongClickListener(new AddressListListener());
			addressList.addView(view);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RESULT_FIRST_USER && resultCode == RESULT_OK) {
			saveCurrentDeliveryAddress(share.getIntValue(Configs.CURRENT_ADDRESS_ID));		//从AddAddressActivity保存收货地址后要更新当前收货地址
			refleshAddressList();		//从AddAddressActivity保存收货地址后要更新收货地址列表
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.btn_activity_address_add_address:
			intent = new Intent(AddressActivity.this, AddAddressActivity.class);
			startActivityForResult(intent, RESULT_FIRST_USER);
			break;
		case R.id.btn_activity_address_back:
			finish();
			break;
		case R.id.btn_activity_address_reflesh_address:
			loadAnimationForRefleshPos();
			getPois();
			break;
		default:
			break;
		}
	}

	/**
	 * 获得当前位置和附近poi
	 */
	private void getPois() {
		addressInfo.clear();
		if(mapUtil.getPoiInfos()!=null){
			addressInfo.addAll(mapUtil.getPoiInfos());				//先clear,再addAll,才能更新listview
			adapter.notifyDataSetChanged();
			location.setText(addressInfo.get(0).name);
		}
	}

	/**
	 * 保存当前收货信息
	 * @param v
	 */
	private void saveCurrentDeliveryAddress(int addressId) {
		if(addressId!=-1){
			DeliveryAddress deliveryAddress = mgr.queryDeliveryAddress(addressId);
			share.setIntValue(Configs.CURRENT_ADDRESS_ID, deliveryAddress.getAddressId());
			share.setStringValue(Configs.CURRENT_RECEIVER, deliveryAddress.getAdrReceiver());
			share.setStringValue(Configs.CURRENT_PHONE, deliveryAddress.getAdrPhone());
			share.setStringValue(Configs.CURRENT_DELIVERY_ADDRESS, deliveryAddress.getAdrAddress());
		}else{
			share.setIntValue(Configs.CURRENT_ADDRESS_ID, -1);
			share.setStringValue(Configs.CURRENT_RECEIVER, "");
			share.setStringValue(Configs.CURRENT_PHONE, "");
			share.setStringValue(Configs.CURRENT_DELIVERY_ADDRESS, location.getText().toString());//未设置当前收货地址时,地址信息保存当前位置,便于在IndexFragment中显示
		}
	}

	/**
	 * 实现对收货地址中每一项的监听
	 * @author chenwenhui
	 */
	class AddressListListener implements OnClickListener, OnLongClickListener {

		@Override
		public void onClick(View v) {
			if(v.getId() == R.id.btn_dialog_activity_address_edit){
				Intent intent = new Intent(AddressActivity.this,AddAddressActivity.class);
				intent.putExtra("addressId", clickedItem);		//传个addressId过去,方便从数据库中获取地址信息
				startActivityForResult(intent, RESULT_FIRST_USER);
				dialog.dismiss();
			}else if(v.getId() == R.id.btn_dialog_activity_address_delete){
				mgr.deleteDeliveryAddress(clickedItem);			//删除收货地址
				saveCurrentDeliveryAddress(-1);					//删除收货地址后把保存在sharedpreference的当前收货地址信息也清除
				refleshAddressList();							//删除后也要刷新收货地址列表
				dialog.dismiss();
			}else{
				//将当前收货地址信息保存在SharedPreference中
				saveCurrentDeliveryAddress(v.getId());
				setResult(RESULT_OK);							//返回OrderConfirmActivity时,通知OrderConfirmActivity显示收货信息;
				finish();
			}
		}

		@Override
		public boolean onLongClick(View v) {
			clickedItem = v.getId();
			dialog.show();
			return false;
		}

	}

}
