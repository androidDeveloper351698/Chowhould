package com.cwenhui.chowhound.widget;

import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;

import com.cwenhui.chowhound.utils.DialogViewHolder;

public abstract class CustomDialog extends Dialog {
	private Context mContext;
	private Map<String, String> data;
	private int layoutId;

	public CustomDialog(Context context, Map<String, String> data,
			int layoutId) {
		super(context);
		this.mContext = context;
		this.data = data;
		this.layoutId = layoutId;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCustomDialog();
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		super.setContentView(view, params);
	}

	@Override
	public void setContentView(View view) {
		super.setContentView(view);
	}

	private void setCustomDialog() {
		DialogViewHolder holder = new DialogViewHolder(mContext, layoutId);
		convert(holder, data);
		super.setContentView(holder.getConvertView());
	}
	
	public abstract void convert(DialogViewHolder holder,Map<String, String> data);
}
