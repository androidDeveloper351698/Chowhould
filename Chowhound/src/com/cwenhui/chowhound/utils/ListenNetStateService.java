package com.cwenhui.chowhound.utils;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class ListenNetStateService extends Service {
	private String TAG = "ListenNetStateService";
    private ConnectivityManager connectivityManager;
    private NetworkInfo info;
    private MyBinder binder = new MyBinder();
    private ImageView networkError;
    private ViewPager content;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                Log.d(TAG, "网络状态已经改变");
                connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                info = connectivityManager.getActiveNetworkInfo();  
                if(networkError!=null && content != null){
                	if(info != null && info.isAvailable()) {
                		networkError.setVisibility(View.GONE);
                		content.setVisibility(View.VISIBLE);
                		Toast.makeText(context, "当前网络状态:"+info.getTypeName(), Toast.LENGTH_SHORT).show();
                	}else{
                		networkError.setVisibility(View.VISIBLE);
                		content.setVisibility(View.GONE);
                		Toast.makeText(context, "没有可用网络", Toast.LENGTH_SHORT).show();
                	}
                	
                }
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
    
    public class MyBinder extends Binder{
    	public boolean checkNetwrok(ImageView img, ViewPager vp){
    		networkError = img;
    		content = vp;
    		//假设一进入应用就是没网络的状态，界面还没来得及初始化，img和vp都是null，无法在service中setVisibility,所以返回一个boolean让activity自己处理
    		if(info != null && info.isAvailable()) {	
    			img.setVisibility(View.GONE);
    			vp.setVisibility(View.VISIBLE);
    			Toast.makeText(getApplicationContext(), "当前网络状态:"+info.getTypeName(), Toast.LENGTH_SHORT).show();
    			return true;
    		}else{
    			img.setVisibility(View.VISIBLE);
    			vp.setVisibility(View.GONE);
    			Toast.makeText(getApplicationContext(), "没有可用网络", Toast.LENGTH_SHORT).show();
    			return false;
    		}
    	}
    }
}
