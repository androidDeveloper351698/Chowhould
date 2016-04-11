package com.cwenhui.chowhound.fragment;

import org.apache.http.Header;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cwenhui.chowhound.config.Configs;
import com.cwenhui.chowhound.ui.RegisterActivity;
import com.cwenhui.chowhound.utils.HttpUtil;
import com.example.chowhound.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class RefineInformationFragment extends Fragment implements OnClickListener{
	private static final String TAG = "RefineInformationFragment";
	private View mView;
	private TextView username;
	private Button submit, isMine, registWithPhone, registWithEmail;
	private EditText password;
	private EditText pwdAgain;
	private LinearLayout registing, beRegisted;			//registing 用户名未被注册使用的布局           beRegisted  用户名已被注册使用的布局 

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_refine_information, container, false);
		
		initView();
		initEvent();
		return mView;
	}


	private void initView() {
		username = (TextView) mView.findViewById(R.id.tv_fragment_refine_information_username);
		submit = (Button) mView.findViewById(R.id.btn_fragment_refine_information_submit);
		password = (EditText) mView.findViewById(R.id.et_fragment_refine_information_password);
		pwdAgain = (EditText) mView.findViewById(R.id.et_fragment_refine_information_password_again);
		registing = (LinearLayout) mView.findViewById(R.id.ll_fragment_refine_information_registing);
		beRegisted = (LinearLayout) mView.findViewById(R.id.rl_fragment_refine_information_be_registed);
		isMine = (Button) mView.findViewById(R.id.btn_fragment_refine_information_is_mine);
		registWithPhone = (Button) mView.findViewById(R.id.btn_fragment_refine_information_register_with_phone);
		registWithEmail = (Button) mView.findViewById(R.id.btn_fragment_refine_information_register_with_email);
		
		username.setText(getArguments().getString("phone"));			//接收手机号作为用户名
	}

	private void initEvent() {
		submit.setOnClickListener(this);
		isMine.setOnClickListener(this);
		registWithEmail.setOnClickListener(this);
		registWithPhone.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_fragment_refine_information_submit:
			if(!TextUtils.isEmpty(password.getText()) || !TextUtils.isEmpty(pwdAgain.getText())){
				if(password.getText().toString().trim().equals(pwdAgain.getText().toString().trim())){
					register(getArguments().getString("phone"), password.getText().toString().trim());
				}else{
					Toast.makeText(getActivity(), "两次密码不一致,请重新输入", 1).show();
				}
			}else{
				Toast.makeText(getActivity(), "请输入密码", 1).show();
			}
			break;

		case R.id.btn_fragment_refine_information_is_mine:
			Intent intent = new Intent();
			intent.putExtra("username", getArguments().getString("phone"));
			getActivity().setResult(getActivity().RESULT_OK, intent);
			getActivity().finish();
			break;
			
		case R.id.btn_fragment_refine_information_register_with_phone:
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			CheckPhoneFragment checkPhoneFragment = new CheckPhoneFragment();
			transaction.replace(R.id.activity_register_container, checkPhoneFragment);
            transaction.commit();  
			break;
			
		case R.id.btn_fragment_refine_information_register_with_email:
			Toast.makeText(getActivity(), "暂不支持邮箱注册", 1).show();
			break;
		default:
			break;
		}
	}

	/**
	 * 调用服务器api进行注册
	 * @param string2 
	 * @param string 
	 */
	private void register(final String username, String password) {
		// 创建请求参数的封装的对象  
        RequestParams params = new RequestParams();  
        params.put("username", username); // 设置请求的参数名和参数值  
        params.put("password", password);// 设置请求的参数名和参数 
		HttpUtil.post(Configs.APIRegister, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] data) {
				if(new String(data).equals("success")){
					Toast.makeText(getActivity(), "注册成功,请登陆", 1).show();
					Intent intent = new Intent();
					intent.putExtra("username", username);
					getActivity().setResult(getActivity().RESULT_OK, intent);
					getActivity().finish();
				}else{
					//用户已注册
					((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).
					hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS); //隐藏软键盘 
					registing.setVisibility(View.GONE);
					beRegisted.setVisibility(View.VISIBLE);
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable throwable) {
				Log.v(TAG, "throwable:"+throwable.toString());
			}
		});
	}
	
}
