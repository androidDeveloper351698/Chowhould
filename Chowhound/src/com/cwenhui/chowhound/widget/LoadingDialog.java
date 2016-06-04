package com.cwenhui.chowhound.widget;

import java.util.Map;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.WindowManager.BadTokenException;
import android.widget.ImageView;

import com.cwenhui.chowhound.common.DialogViewHolder;
import com.cwenhui.chowhound.ui.LoginActivity;
import com.cwenhui.chowhound.ui.MainActivity;
import com.example.chowhound.R;

public class LoadingDialog {
	private static final String TAG = "LoadingDialog";
//	private static Context mContext;
	public static CustomDialog dialog; // 数据加载提示框
	public static LoadingDialog loadingDialog; 

	/**
	 * 设置是否可以点击取消
	 * @param isCancel
	 */
	public static void setCancelable(boolean isCancel) {
		if(dialog != null){
			dialog.setCancelable(isCancel);
		}
	}

	/**
	 * 显示对话框
	 * @param context
	 */
	public static void showDialog(Context context) {
		if (dialog == null) {
			initDialog(context);
		}
		try{
			dialog.show(); // 显示等待动画,直到获取到内容
			Log.v(TAG, "showDialog");
		}catch(BadTokenException e){
			Log.v(TAG, "BadTokenException");
			dialog.dismiss();
		}
	}
	
	public static void cancelDialog(){
		if(dialog != null){ 
			dialog.cancel();
		}
	}
	
	public static void dismissDialog(){
		if(dialog!=null){
			dialog.dismiss();
		}
	}
	
	public static boolean isShowing(){
		if(dialog != null){
			return dialog.isShowing();
		}else{
			return false;
		}
	}

	/**
	 * 实例化对话框
	 * @param context
	 */
	private static void initDialog(Context context) {
//		mContext = context;
		dialog = new CustomDialog(context, null, R.layout.dialog_loading) {

			@Override
			public void convert(DialogViewHolder holder,
					Map<String, String> data) {
				holder.setImageViewBackgroundResource(
						R.id.dialog_loading_img, R.anim.frame2);
				ImageView img = holder.getView(R.id.dialog_loading_img);
				// 通过ImageView对象拿到背景显示的AnimationDrawable
				final AnimationDrawable mAnimation = (AnimationDrawable) img
						.getBackground();
				img.post(new Runnable() {

					@Override
					public void run() {
						mAnimation.start();
					}
				});
			}
		};
	}
}
