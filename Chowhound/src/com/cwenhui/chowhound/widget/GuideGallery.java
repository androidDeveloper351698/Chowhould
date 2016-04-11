package com.cwenhui.chowhound.widget;

import java.util.TimerTask;

import com.cwenhui.chowhound.adapter.IndexFragmentGalleryImgAdapter;
import com.cwenhui.chowhound.fragment.IndexFragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;
/**主界面图片轮播**/
public class GuideGallery extends Gallery {
	private final String TAG = "GuideGallery";
//	private MainActivity m_iact;
	private Fragment fragment;
	public GuideGallery(Context context) {
		super(context);
	}
	
	public GuideGallery(Context context,AttributeSet attrs) {
		super(context, attrs);
	}
	
	public GuideGallery(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

//	public void setImageActivity(MainActivity iact){
//		m_iact = iact;
//	}
	
	public void setImageActivity(Fragment fragment){
		this.fragment = fragment;
	}
	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		int kEvent;
        if(isScrollingLeft(e1, e2)){ //Check if scrolling left
          kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
          System.out.println("AAAA"+this.getSelectedItemPosition());
        }
        else{ //Otherwise scrolling right
          kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
          System.out.println("BBB"+this.getSelectedItemPosition());
        }
        onKeyDown(kEvent, null); 
        if(this.getSelectedItemPosition()==0)
        	this.setSelection(IndexFragmentGalleryImgAdapter.imgs.length);
        System.out.println("DDD"+this.getSelectedItemPosition());
		new java.util.Timer().schedule(new TimerTask(){
		       public void run() {
//		    	   m_iact.timeFlag = false;
		    	   Log.v(TAG, String.valueOf(((IndexFragment)fragment).timeFlag));
		    	   ((IndexFragment)fragment).timeFlag = false;
		    	   this.cancel();
		       }}, 3000);
        return true;  
		
	}
	private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2){
		 System.out.println(this.getSelectedItemPosition());
        return e2.getX() > e1.getX();
        
    }
	
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
//		m_iact.timeTask.timeCondition = false;
		((IndexFragment)fragment).timeTask.timeCondition = false;
		return super.onScroll(e1, e2, distanceX, distanceY);
	}

}
