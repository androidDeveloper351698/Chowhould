package com.cwenhui.chowhound.utils;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DialogViewHolder {
	private final String TAG = "DialogViewHolder";
	private SparseArray<View> mViews;
	private View mConvertView;
	
	public DialogViewHolder(Context context, int layoutId) {
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, null);
	}
	
	public <T extends View>T getView(int viewId){
		View view = mViews.get(viewId);
		if(view == null){
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
		
	}	
	
	public View getConvertView(){
		return mConvertView;
	}
	
	public DialogViewHolder initTextView(int viewId, String text){
		TextView tv = getView(viewId);
		tv.setText(text);
		return this;
	}
	
	public DialogViewHolder initButton(int viewId, String text){
		Button btn = getView(viewId);
		btn.setText(text);
		return this;
	}
	
	public DialogViewHolder initImageView(int viewId, int resId){
		ImageView iv = getView(viewId);
		iv.setImageResource(resId);
		return this;
	}
	
	public DialogViewHolder setImageViewBackgroundResource(int viewId, int resId){
		ImageView iv = getView(viewId);
		iv.setBackgroundResource(resId);
		return this;
	}
	
	public DialogViewHolder setClickListener(int viewId, View.OnClickListener listener){
		Button btn = getView(viewId);
		btn.setOnClickListener(listener);
		return this;
	}
	
	
}
