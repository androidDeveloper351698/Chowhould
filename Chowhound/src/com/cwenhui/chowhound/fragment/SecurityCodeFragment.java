package com.cwenhui.chowhound.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cwenhui.chowhound.utils.SMSSDKHelper;
import com.example.chowhound.R;

public class SecurityCodeFragment extends Fragment implements OnClickListener {
	private static final String TAG = "SecurityCodeFragment";
	private View mView;
	private Button next, resendVerifyCode;
	private EditText securityCode;
	private SMSSDKHelper smssdkHelper;
	private String phone;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_security_code, container, false);
		initData();
		initView();
		initEvent();
		return mView;
	}

	private void initData() {
		smssdkHelper = new SMSSDKHelper(getActivity());
		if(getArguments() != null){
			Bundle bundle = getArguments();
			phone = (String) bundle.get("phone");
		}
	}

	private void initView() {
		next = (Button) mView.findViewById(R.id.btn_fragment_security_code_next);
		securityCode = (EditText) mView.findViewById(R.id.et_fragment_security_code_code);
		resendVerifyCode = (Button) mView.findViewById(R.id.btn_fragment_security_code_resend_verify_code);
	}

	private void initEvent() {
		next.setOnClickListener(this);
		resendVerifyCode.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_fragment_security_code_next:
			if(!TextUtils.isEmpty(securityCode.getText().toString())){
				smssdkHelper.submitVerificationCode("86", phone, securityCode.getText().toString());
			}else{
				Toast.makeText(getActivity(), "请输入验证码", 1).show();
			}
			break;

		case R.id.btn_fragment_security_code_resend_verify_code:
			smssdkHelper.getVerificationCode("86", phone);
			break;
		default:
			break;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		smssdkHelper.unregisterAllEventHandler();
	}

	
}
