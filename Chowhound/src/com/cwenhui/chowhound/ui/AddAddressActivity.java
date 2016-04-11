package com.cwenhui.chowhound.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baidu.mapapi.search.core.PoiInfo;
import com.cwenhui.chowhound.bean.DeliveryAddress;
import com.cwenhui.chowhound.utils.DBManager;
import com.cwenhui.chowhound.utils.DialogViewHolder;
import com.cwenhui.chowhound.widget.CustomDialog;
import com.example.chowhound.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddAddressActivity extends Activity implements OnClickListener,
		TextWatcher {
	private final String TAG = "AddAddressActivity";
	private Button save, back;
	private EditText mSelectAddress, mReceiver, mPhone, mDetailAddress;
	private String receiver, phone, selectAddress, detailAddress;
	private Map<String, String> dialogData;
	private CustomDialog dialog;
	private DBManager mgr;
	/**
	 * 判断是编辑已有的收货地址还是添加新的收货地址
	 * 从AddressActivity中接收addressId,如果不是编辑状态则默认是-1
	 */
	private int isEdite;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_address);
		initData();
		initView();

	}

	private void initData() {
		//初始化DBManager  
        mgr = new DBManager(this);  
		dialogData = new HashMap<String, String>();
		dialogData.put("content", "输入正确的手机号啦");
		dialogData.put("ok", "ok");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//应用的最后一个Activity关闭时应释放DB  
        mgr.closeDB();  
	}

	private void initView() {
		mSelectAddress = (EditText) findViewById(R.id.et_activity_add_address_select_address);
		save = (Button) findViewById(R.id.btn_activity_add_address_save);
		back= (Button) findViewById(R.id.btn_activity_add_address_back);
		mReceiver = (EditText) findViewById(R.id.et_activity_add_address_receiver);
		mPhone = (EditText) findViewById(R.id.et_activity_add_address_phone);
		mDetailAddress = (EditText) findViewById(R.id.et_activity_add_address_detail_address);
		
		editAddress();
		
		dialog = new CustomDialog(AddAddressActivity.this,  dialogData, R.layout.dialog_activity_add_address) {
			
			@Override
			public void convert(DialogViewHolder holder, Map<String, String> data) {
				holder.initTextView(R.id.tv_dialog_activity_add_address_content, data.get("content"));
				holder.initButton(R.id.btn_dialog_activity_add_address_ok, data.get("ok"));
				holder.setClickListener(R.id.btn_dialog_activity_add_address_ok, AddAddressActivity.this);
			}
		}; 

		mSelectAddress.setOnClickListener(this);
		mSelectAddress.addTextChangedListener(this);
		mReceiver.addTextChangedListener(this);
		mPhone.addTextChangedListener(this);
		mDetailAddress.addTextChangedListener(this);
		save.setOnClickListener(this);
		back.setOnClickListener(this);
	}

	/**
	 * 编辑已有的收货地址,根据addressId从数据库中获得数据并填入相应的控件
	 */
	private void editAddress() {
		isEdite = getIntent().getIntExtra("addressId", -1);
		if(isEdite != -1){		
			DeliveryAddress deliveryAddress = mgr.queryDeliveryAddress(isEdite);
			mReceiver.setText(deliveryAddress.getAdrReceiver());
			mSelectAddress.setText(deliveryAddress.getAdrAddress().split(" ")[0]);
			mDetailAddress.setText(deliveryAddress.getAdrAddress().split(" ")[1]);
			mPhone.setText(deliveryAddress.getAdrPhone());
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.et_activity_add_address_select_address:
			intent = new Intent(AddAddressActivity.this,
					MobileLocationActivity.class);
			startActivityForResult(intent, RESULT_FIRST_USER);
			break;

		case R.id.btn_activity_add_address_save:
			intent = new Intent();
			if(checkPhone()){
				AddAddressActivity.this.setResult(RESULT_OK, intent);
				AddAddressActivity.this.finish();
			}
			break;
		case R.id.btn_dialog_activity_add_address_ok:
			dialog.dismiss();
			break;
		case R.id.btn_activity_add_address_back:
			finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 检测用户输入的手机号是否合法,如果合法则判断是添加新的收货地址还是编辑已有收货地址
	 * @return
	 */
	private boolean checkPhone() {
		String regExp = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";  
		Pattern p = Pattern.compile(regExp);  
		Matcher m = p.matcher(phone);
		if(m.find()){
			DeliveryAddress deliveryAddress = new DeliveryAddress();
			deliveryAddress.setAdrAddress(selectAddress+" "+detailAddress);		
			deliveryAddress.setAdrPhone(phone);
			deliveryAddress.setAdrReceiver(receiver);
			
			if(isEdite == -1){		//添加新的收货地址
				mgr.addDeleveryAddress(deliveryAddress);
			}else{					//编辑已有的收货地址
				deliveryAddress.setAddressId(isEdite);
				mgr.updateDeliveryAddress(deliveryAddress);
			}
			return true;
		}else{
			dialog.show();
			return false;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RESULT_FIRST_USER && resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			PoiInfo poi = (PoiInfo) bundle.get("address");
			mSelectAddress.setText(poi.name);
		}
	}

	@Override
	public void afterTextChanged(Editable e) {
		receiver = mReceiver.getText().toString().trim();
		phone = mPhone.getText().toString().trim();
		selectAddress = mSelectAddress.getText().toString().trim();
		detailAddress = mDetailAddress.getText().toString().trim();
		
		if(receiver.length()>0 && phone.length()>0 && selectAddress.length()>0 && detailAddress.length()>0 /*&& m.find()*/){
			save.setEnabled(true);
		}
		
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
	}

}
