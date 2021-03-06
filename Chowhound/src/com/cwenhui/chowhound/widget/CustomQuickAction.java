package com.cwenhui.chowhound.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.chowhound.R;

public abstract class CustomQuickAction<T> extends PopupWindow {
	private static final String TAG = "CustomQuickAction";
	private BitmapDrawable mBackground;												//QuickAction背景色
	protected ViewGroup mRootView;													//QuickAction的根布局
	private Display mDefaultDisplay;												//用于获取屏幕宽度、高度等信息
	protected LinearLayout mQuickActionLayout;										//QuickAction的布局
	private int[] mAnchorLocations;													//控制QuickAction显示的控件(按钮之类的)的位置
	private int mScreenWidth;														//屏幕宽
	private int mScreenHeight;														//屏幕高
	protected List<T> mItems = new ArrayList<T>();									//QuickAction的内容
	protected LayoutInflater mInflater;
	protected OnClickQuickActionListener mClickListener;								//QuickAction监听
	
	public CustomQuickAction(Context context) {
		super(context);
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		mDefaultDisplay = wm.getDefaultDisplay();
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		initParams();
		// Quick Action 布局
		initQuickAction(context);
	}
	
	/**
	 * 显示Quick Action
	 * @param anchor
	 */
	public void show(View anchor) { 
//		Log.e(TAG, "show2:"+mItems.size());
		if (!isShowing()) {
			Direction showDirection = computeDisplayPosition(anchor);			// 位置
			int[] locations = preShow(anchor, showDirection);					// 根据位置，显示箭头
			if (locations != null) {											// 显示PopupWindow
				showAtLocation(anchor, Gravity.NO_GRAVITY, locations[0], locations[1]);
			}
//			Log.e(TAG, "locations[0]"+locations[0]+"locations[1]"+locations[1]);
		} else {
			dismiss();
		}
	}
	/**
	 * @param quickAction
	 */
	public void addQuickAction(View quickAction) {
		mQuickActionLayout.addView(quickAction);
	}

	public abstract void addQuickActionItem(T item);
	
	
	public void setOnClickQuickActionListener(OnClickQuickActionListener listener) {
		mClickListener = listener;
	}

	private void initParams() {
		// 点击在PopupWindow外时，dismiss popup window
		setTouchInterceptor(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					dismiss();
					return true;
				}
				return false;
			}
		});
	
		// 设置相关属性
		setFocusable(true);
		setTouchable(true);
		setOutsideTouchable(true);
	
		// 设置动画效果
		setAnimationStyle(R.style.quickaction);

		// 宽高自适应
		setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
		setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
	}
	
	public abstract void initQuickAction(Context context);

	/**
	 * Popup Window自动放置（支持上下左右四个位置）。
	 */
	protected Direction computeDisplayPosition(View anchor) {

		Direction showDirection = null;

		mAnchorLocations = new int[2];
		// 获取针对整个Window的绝对位置
		anchor.getLocationOnScreen(mAnchorLocations);

		mScreenWidth = mDefaultDisplay.getWidth();
		mScreenHeight = mDefaultDisplay.getHeight();

		mRootView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		int popupWidth = mRootView.getMeasuredWidth();
		int popupHeight = mRootView.getMeasuredHeight();

		// ？ 计算有点问题，没有减去状态栏和标题栏的高度
		
		// QuickAction可以显示在anthor上下左右，但仅能显示在一侧
		boolean canShowTop = mAnchorLocations[1] - popupHeight > 0;
		boolean canShowBottom = mAnchorLocations[1] + anchor.getHeight() + popupHeight < mScreenHeight; 
		boolean canShowRight = mAnchorLocations[0] + anchor.getWidth() + popupWidth < mScreenWidth;
		boolean canShowLeft = mAnchorLocations[0] - popupWidth > 0;
		
//		Log.e(TAG, "popupWidth"+popupWidth+"   popupHeight"+popupHeight);

		if(canShowTop && canShowBottom){
			if(mAnchorLocations[1] - popupHeight > (mScreenHeight-mAnchorLocations[1] - anchor.getHeight() - popupHeight)){
				showDirection = Direction.TOP;
			}else{
				showDirection = Direction.BOTTOM;
			}
		}else if (!canShowTop && canShowBottom) {
			showDirection = Direction.BOTTOM;
		}else if (canShowTop && !canShowBottom) {
			showDirection = Direction.TOP;
		}else if(!canShowBottom && !canShowTop){
			if(canShowLeft && canShowRight){
				if(mAnchorLocations[0] - popupWidth > mScreenWidth-mAnchorLocations[0]-anchor.getWidth()-popupWidth){
					showDirection = Direction.LEFT;
				}else{
					showDirection = Direction.RIGHT;
				}
			}else if(!canShowLeft && canShowRight){
				showDirection = Direction.RIGHT;
			}else if(canShowLeft && !canShowRight){
				showDirection = Direction.LEFT;
			}
		}
//		Log.e(TAG, "showDirection:"+showDirection);
		return showDirection;
	}

	protected int[] preShow(View anchor, Direction showDirection) {
		
		if (mBackground == null) {
			// 默认设置为透明
			setBackgroundDrawable(new BitmapDrawable());
		} else {
			setBackgroundDrawable(mBackground);
		}
		
		if (showDirection == null) {
			return null;
		}
		
		int[] locations = new int[2]; 
		
//		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) 
//				mQuickActionLayout.getLayoutParams();
		
		int anchorCenterX = 0;
		int anchorCenterY = 0;
		
		// * 上下箭头如果设置为gone，获取popup window height会总是在变动
		switch (showDirection) {
		case TOP:
//			params.setMargins(0, 0, 0, -6);
			mRootView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			anchorCenterX = mAnchorLocations[0] + anchor.getWidth() / 2;
			
			locations[0] = anchorCenterX - mRootView.getMeasuredWidth() / 2;
			locations[1] = mAnchorLocations[1] - mRootView.getMeasuredHeight();
			
			if (locations[0] <= 0) {
				locations[0] = 0;
			} else if (locations[0] + mRootView.getMeasuredWidth() >= mScreenWidth) {
				locations[0] = mScreenWidth - mRootView.getMeasuredWidth();
			}
			
			break;
			
		case BOTTOM:
			// 中间
//			params.setMargins(0,0, 0, 0);
			mRootView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			anchorCenterX = mAnchorLocations[0] + anchor.getWidth() / 2;
			
			// Popup Window
			locations[0] = anchorCenterX - mRootView.getMeasuredWidth() / 2;
			locations[1] = mAnchorLocations[1] + anchor.getHeight();
			
			if (locations[0] <= 0) {
				locations[0] = 0;
			} else if (locations[0] + mRootView.getMeasuredWidth() >= mScreenWidth) {
				locations[0] = mScreenWidth - mRootView.getMeasuredWidth();
			}
			
			break;
			
		case LEFT:
//			params.setMargins(0, 0, mScreenWidth-mAnchorLocations[0], 0);
			mRootView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			
			anchorCenterY = mAnchorLocations[1] + anchor.getHeight() / 2;
			
			locations[0] = mAnchorLocations[0] - mRootView.getMeasuredWidth();
			locations[1] = anchorCenterY - mRootView.getMeasuredHeight() / 2;
			
			if (locations[1] <= 0) {
				locations[1] = 0;
			} else if (locations[1] + mRootView.getMeasuredHeight() >= mScreenHeight) {
				locations[1] = mScreenHeight - mRootView.getMeasuredHeight();
			}			
			
			break;
			
		case RIGHT:
			mRootView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			
			anchorCenterY = mAnchorLocations[1] + anchor.getHeight() / 2;
			
			locations[0] = mAnchorLocations[0] + anchor.getWidth();
			locations[1] = anchorCenterY - mRootView.getMeasuredHeight() / 2;
			
			if (locations[1] <= 0) {
				locations[1] = 0;
			} else if (locations[1] + mRootView.getMeasuredHeight() >= mScreenHeight) {
				locations[1] = mScreenHeight - mRootView.getMeasuredHeight();
			}			
			break;
		}
		return locations;
	}

	public enum Direction {
		LEFT, RIGHT, TOP, BOTTOM
	}

	public interface OnClickQuickActionListener{
		public void OnClickQuickAction(int actionId);
	}
	
}
