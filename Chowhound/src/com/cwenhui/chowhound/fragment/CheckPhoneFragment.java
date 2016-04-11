package com.cwenhui.chowhound.fragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;

import com.cwenhui.chowhound.config.Configs;
import com.cwenhui.chowhound.utils.SMSSDKHelper;
import com.example.chowhound.R;

public class CheckPhoneFragment extends Fragment implements TextWatcher,
		OnClickListener {
	private static final String TAG = "CheckPhoneFragment";
	private View mView;
	private EditText phone;
	private ImageView clean;
	private Button next;
	private SMSSDKHelper smssdkHelper;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_check_phone, container, false);
		initData();
		initView();
		initEvent();
		return mView;

	}

	private void initData() {
		smssdkHelper = new SMSSDKHelper(getActivity());
	}

	private void initView() {
		phone = (EditText) mView.findViewById(R.id.et_fragment_check_phone_register_phone);
		clean = (ImageView) mView.findViewById(R.id.iv_fragment_check_phone_clean);
		next = (Button) mView.findViewById(R.id.btn_fragment_check_phone_register_next);
	}

	private void initEvent() {
		phone.addTextChangedListener(this);
		clean.setOnClickListener(this);
		next.setOnClickListener(this);
	}

	@Override
	public void afterTextChanged(Editable t) {
		if (t.toString().trim().equals("")) {
			clean.setVisibility(View.GONE);
		} else {
			clean.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_fragment_check_phone_clean:
			phone.setText("");
			break;

		case R.id.btn_fragment_check_phone_register_next:
			String phoneNum = phone.getText().toString().trim();
			if (!TextUtils.isEmpty(phoneNum)) {
				String regExp = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";  
				Pattern p = Pattern.compile(regExp);  
				Matcher m = p.matcher(phone.getText().toString().trim());  
				if(m.find()){
					smssdkHelper.getVerificationCode("86", phoneNum);
					
					SecurityCodeFragment securityCodeFragment = new SecurityCodeFragment();
					Bundle bundle = new Bundle();
					bundle.putString("phone", phoneNum);							//将手机号码传递给SecurityCodeFragment
					securityCodeFragment.setArguments(bundle);
					FragmentTransaction transaction = getFragmentManager().beginTransaction();
					transaction.replace(R.id.activity_register_container, securityCodeFragment);
					transaction.commit();  
				}else{
					Toast.makeText(getActivity(), "电话号不正确,请重新输入", 1).show();
				}
			} else {
				Toast.makeText(getActivity(), "电话不能为空", 1).show();
			}
			break;
		default:
			break;
		}
	}

}
