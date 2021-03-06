package com.cwenhui.chowhound.utils;

import android.app.Activity;
import android.view.WindowManager;

public class WindowUtil {

	/**
	 * 设置窗口透明度
	 * @param activity		用于获取对应的窗口
	 * @param bgAlpha		透明度
	 */
	public static void setBackgroundAlpha(Activity activity, float bgAlpha) {
		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
		lp.alpha = bgAlpha; // 0.0-1.0
		activity.getWindow().setAttributes(lp);
	}
}
