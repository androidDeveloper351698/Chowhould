package com.cwenhui.chowhound.utils;

import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;

import com.cwenhui.chowhound.config.Configs;
import com.cwenhui.chowhound.fragment.RefineInformationFragment;
import com.cwenhui.chowhound.fragment.SecurityCodeFragment;
import com.cwenhui.chowhound.ui.RegisterActivity;
import com.example.chowhound.R;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class SMSSDKHelper {
	private static final String TAG = "SMSSDKHelper";
	private Handler mHandler;
	private Context mContext;
	private static String phone;

	public SMSSDKHelper(Context context){
		mContext = context;
		SMSSDK.initSDK(mContext, Configs.SHARESDK_APPKEY, Configs.SHARESDK_APPSECRET, true);		//初始化SDK,单例，可以多次调用；任何方法调用前，必须先初始化
		EventHandler eh = new EventHandler() {														//实例化回调接口					

			@Override
			public void afterEvent(int event, int result, Object data) {							//执行后被调用
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				mHandler.sendMessage(msg);
			}

		};
		SMSSDK.registerEventHandler(eh);															//注册回调接口

		initHandler();
	}
	
	public void getVerificationCode(String countryCode, String phone){this.phone = phone;
		SMSSDK.getVerificationCode(countryCode, phone);
	}

	public void submitVerificationCode(String countryCode, String phone, String securityCode){this.phone = phone;
		SMSSDK.submitVerificationCode(countryCode, phone, securityCode);
	}
	
	public void unregisterAllEventHandler(){
		SMSSDK.unregisterAllEventHandler();
	}
	
	private void initHandler() {
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				int event = msg.arg1;					//event表示操作的类型
				int result = msg.arg2;					//result是操作结果，为SMSSDK.RESULT_COMPLETE表示操作成功，为SMSSDK.RESULT_ERROR表示操作失败
				Object data = msg.obj;					//data是从外部传入的数据
				
				if (result == SMSSDK.RESULT_COMPLETE) {
					Log.v(TAG, "--------result" + + event);
					// 短信注册成功后，返回MainActivity,然后提示新好友
					if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
						Toast.makeText(mContext, "提交验证码成功", Toast.LENGTH_SHORT).show();
						
						FragmentTransaction transaction = ((RegisterActivity)mContext).getSupportFragmentManager().beginTransaction();
						RefineInformationFragment refineInformationFragment = new RefineInformationFragment();
						Bundle bundle = new Bundle();
						bundle.putString("phone", phone);	 
						refineInformationFragment.setArguments(bundle); 
						transaction.replace(R.id.activity_register_container, refineInformationFragment);
		                transaction.commit();  
					}
					else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {			// 已经验证
						Toast.makeText(mContext, "验证码已经发送",Toast.LENGTH_SHORT).show();
					}
				} else {	// 当result=SMSSDK.RESULT_ERROR，则data的类型为Throwable；如果服务器有返回错误码，那么这个Throwable的message就存放着服务器返回的json数据
					int status = 0;
					try {
						((Throwable) data).printStackTrace();
						Throwable throwable = (Throwable) data;

						JSONObject object = new JSONObject(
								throwable.getMessage());
						String des = object.optString("detail");
						status = object.optInt("status");
						Log.v(TAG, "detail="+des+"    status="+status);
						if (!TextUtils.isEmpty(des)) {
							Toast.makeText(mContext, des,Toast.LENGTH_SHORT).show();
							return;
						}
					} catch (Exception e) {
						SMSLog.getInstance().w(e);
					}
				}
			}

		};
	}
}
