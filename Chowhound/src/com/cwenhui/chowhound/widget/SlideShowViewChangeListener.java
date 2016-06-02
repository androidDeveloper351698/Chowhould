package com.cwenhui.chowhound.widget;

import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;



import java.util.List;

import com.example.chowhound.R;

/**
 * ViewPager的监听器
 * 当ViewPager中页面的状态发生改变时调用
 * Created by cwenhui on 2016.02.23
 */
public class SlideShowViewChangeListener implements ViewPager.OnPageChangeListener {
    private ViewPager viewPager;
    private int currentItem = 0;
    private Handler handler;
    private List<View> dotViewsList;
	boolean isAutoPlay = false;

    public SlideShowViewChangeListener(ViewPager viewPager, Handler handler, List<View> dotViewsList) {
        this.viewPager = viewPager;
        this.handler = handler;
        this.dotViewsList = dotViewsList;
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        switch (arg0) {
            case 1:// 手势滑动，空闲中
                isAutoPlay = false;
                break;
            case 2:// 界面切换中
                isAutoPlay = true;
                break;
            case 0:// 滑动结束，即切换完毕或者加载完毕
                // 当前为最后一张，此时从右向左滑，则切换到第一张
                if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !isAutoPlay) {
                    viewPager.setCurrentItem(0);
                }
                // 当前为第一张，此时从左向右滑，则切换到最后一张
                else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {
                    viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1);
                }
                break;
        }
    }

	@Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

	@Override
    public void onPageSelected(int pos) {
        currentItem = pos;
        for(int i=0;i < dotViewsList.size();i++){
            if(i == pos){
                ((View)dotViewsList.get(pos)).setBackgroundResource(R.drawable.feature_point_cur);
            }else {
                ((View)dotViewsList.get(i)).setBackgroundResource(R.drawable.feature_point);
            }
        }
    }
}
