//package com.cwenhui.chowhound.utils;
//
//import java.io.BufferedInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.HashSet;
//import java.util.Set;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.AsyncTask;
//import android.support.v4.util.LruCache;
//import android.util.Log;
//import android.widget.ImageView;
//import android.widget.ListView;
//
//import com.cwenhui.chowhound.adapter.PullDownPinnedHeaderAdapter;
//import com.example.chowhound.R;
//
//public class ImageLoader {
//	private final String TAG = "ImageLoader";
//	private ImageView mImageView;
//	private String mUrl;
//	private LruCache<String, Bitmap> mCache;		//创建Cache
//	private ListView mListView;
//	private Set<NewsAsyncTask> mTask;				//管理所有asynctask的task
//	
//	public ImageLoader(ListView listView){
//		mListView = listView;
//		mTask = new HashSet<NewsAsyncTask>();
//		int maxMemory = (int)Runtime.getRuntime().maxMemory();		//获取最大可用内存
//		int cacheSize = maxMemory/4;
//		mCache = new LruCache<String, Bitmap>(cacheSize){
//
//			//获取每个存进去的对象的大小,默认返回元素的个数，需重写; sizeOf()方法在每次加入内存缓存时都会调用
//			@Override
//			protected int sizeOf(String key, Bitmap value) {
//				// TODO Auto-generated method stub
//				return value.getByteCount();			 //在每次存入缓存时调用
//			}
//			
//		};
//	}
//	
//	//增加数据到缓存
//	public void addBitmapToCache(String url, Bitmap bitmap){
//		if(getBitmapFromCache(url) == null){
//			mCache.put(url, bitmap);
//			
//		}
//	}
//	//从缓存中获取数据
//	public Bitmap getBitmapFromCache(String url){
//		return mCache.get(url);
//	}
//	
////	private Handler mHandler = new Handler(){
////
////		@Override
////		public void handleMessage(Message msg) {
////			// TODO Auto-generated method stub
////			super.handleMessage(msg);
////			if(mImageView.getTag().equals(mUrl)){
////				mImageView.setImageBitmap((Bitmap) msg.obj);
////			}
////			
////		}
////		
////	};
//	
////	public void showImageByThread(ImageView imageView, final String url){
////		mImageView = imageView;
////		mUrl = url;
////		
////		new Thread(){
////
////			@Override
////			public void run() {
////				// TODO Auto-generated method stub
////				super.run();
////				Bitmap bitmap = getBitmapFromURL(url);
////				//通过这种方式创建的message，可以使用现有的以及回收掉的message，提高message的使用效率
////				Message message = Message.obtain();	
////				message.obj = bitmap;
////				mHandler.sendMessage(message);
////			}
////			
////		}.start();
////		
////	}
//	
//	public Bitmap getBitmapFromURL(String urlString){
//		Bitmap bitmap;
//		InputStream is=null;
//		try {
//			URL url = new URL(urlString);
//			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//			Log.v(TAG, connection.toString());
//			is = new BufferedInputStream(connection.getInputStream());
//			Log.v(TAG, "is.toStirng:     "+is.toString());
//			bitmap = BitmapFactory.decodeStream(is);
//			connection.disconnect();
//			return bitmap;
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}finally{		//??
////			try {
////				Log.v(TAG, is.toString());
////				is.close();
////			} catch (IOException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
//		}
//		
//		return null;
//		
//	}
//	
//	public void showImageByAsynctask(ImageView imageView, String url){		//显示图片的控制权转交给loadImages()
//		mUrl = url;
//		mImageView = imageView;
//		//new NewsAsyncTask(imageView).execute(url);
//		//从缓存中取出图片
//		Bitmap bitmap = getBitmapFromCache(url);
//		//如果缓存中没有则去下载,如果有则加载
//		if(bitmap == null){
////			new NewsAsyncTask().execute(url);
//			imageView.setImageResource(R.drawable.ddt_place_holder_brand_default);		//不再用getView()触发NewsAsyncTask的下载任务
//																	//而是用listview滚动时区触发NewsAsyncTask的下载任务
//		}else {
//			imageView.setImageBitmap(bitmap);
//		}
//		
//
//	}
//	
//	class NewsAsyncTask extends AsyncTask<String, Void, Bitmap>{
//		//private ImageView mImageView;
//		//private String mUrl;
//		
////		public NewsAsyncTask(ImageView imageview){
//		public NewsAsyncTask(){
////			mImageView = imageview;
//			
//		}
//
//		@Override
//		protected Bitmap doInBackground(String... params) {
//			// TODO Auto-generated method stub
//			String url = params[0];
//			//从网络获取图片
//			Bitmap bitmap = getBitmapFromURL(url);
//			if(bitmap != null){
//				//将不在缓存的图片加入缓存
//				addBitmapToCache(url, bitmap);
//			}
//			return bitmap;
//		}
//
//		@Override
//		protected void onPostExecute(Bitmap bitmap) {
//			// TODO Auto-generated method stub
//			super.onPostExecute(bitmap);
//			ImageView imageView = (ImageView) mListView.findViewWithTag(mUrl);
//			if(imageView != null && bitmap != null){
//				imageView.setImageBitmap(bitmap);
//			}
//			
//		}
//		
//	}
//	
//	//用来加载从start到end的所有图片
//	public void loadImages(int start, int end){
//		Log.v(TAG, "start  :"+start+"    "+"end   :"+end +"     url.length:"+PullDownPinnedHeaderAdapter.URLS.size());
//		for(int i = start; i<end; i++){
//			if(PullDownPinnedHeaderAdapter.URLS.size()<end)	break;
////			String url = PullDownPinnedHeaderAdapter.URLS[i];
//			String url = PullDownPinnedHeaderAdapter.URLS.get(i);
//			Bitmap bitmap = getBitmapFromCache(url);
//			//如果缓存中没有则去下载,如果有则加载
//			if(bitmap == null){
//				NewsAsyncTask task = new NewsAsyncTask();
//				task.execute(url);
//				mTask.add(task);					//将task放到集合里统一管理
//			}else {
//				ImageView imageView = (ImageView) mListView.findViewWithTag(url);
//				Log.v(TAG, "tag------>"+url);
//				if(imageView != null){
//					imageView.setImageBitmap(bitmap);
//				}
//			}
//		}
//	}
//
//	public void cancelAllTasks() {
//		// TODO Auto-generated method stub
//		if(mTask != null){
//			for(NewsAsyncTask task:mTask){
//				task.cancel(false);
//			}
//		}
//	}
//
//}
