package com.cwenhui.chowhound.utils;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder {
	private final String TAG = "ViewHolder";
	private SparseArray<View> mViews;
	private int mPosition;
	
	public int getPosition() {
		return mPosition;
	}

	private View mConvertView;

	public ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
		this.mPosition = position;
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,false);
		mConvertView.setTag(this);
	}
	
	//��ں�������ȡViewHolder
	public static ViewHolder get(Context context, View convertview, ViewGroup parent, 
			int layoutId, int position){
		if(convertview == null){
			return new ViewHolder(context, parent, layoutId, position);
		}
		else{
			ViewHolder viewholder = (ViewHolder) convertview.getTag(); 
			viewholder.mPosition = position;
			return viewholder;
		}
	}
	
	/**
	 * ͨ��viewid��ȡ�ؼ�
	 * @param viewId
	 * @return
	 */
	public <T extends View>T getView(int viewId){
		View view = mViews.get(viewId);
		if(view == null){
			view = mConvertView.findViewById(viewId);
//			Log.v(TAG, "view          "+view.toString());		//header 的convertview 和 item 的convertview 不同，导致找不到?
			mViews.put(viewId, view);
		}
		return (T) view;
		
	}
	
	public View getConvertView(){
		return mConvertView;
	}
	
	//��ʽ��̣������Լ����ظ�����
	/**
	 * ����textview
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ViewHolder setText(int viewId, String text){
		TextView tv = getView(viewId);
		tv.setText(text);
		return this;
	}
	
	/**
	 * ����imageview
	 * @param viewId
	 * @param resId
	 * @return
	 */
	public ViewHolder setImageResource(int viewId, int resId){
		ImageView iv = getView(viewId);
//		Log.v(TAG, iv.toString());
		iv.setImageResource(resId);
		return this;
	}
	

}
