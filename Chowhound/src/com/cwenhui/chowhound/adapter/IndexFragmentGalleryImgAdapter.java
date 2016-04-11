package com.cwenhui.chowhound.adapter;

import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.cwenhui.chowhound.fragment.IndexFragment;
import com.example.chowhound.R;
/**
 * 图片轮播的适配器
 * @author jiajia
 *
 */
public class IndexFragmentGalleryImgAdapter extends BaseAdapter{  
	private final String TAG = "ImageAdapter";
    private List<String> imageUrls;       //鍥剧墖鍦板潃list  
//    private Context context;  
    private Fragment fragment;
    private IndexFragmentGalleryImgAdapter self;
    Uri uri;
    Intent intent;
    ImageView imageView;
   public static Integer[] imgs = {
// 			R.drawable.bingyuhuo,
// 			R.drawable.huaqiangu,
//			R.drawable.xiaoshidai,
//			R.drawable.xianxiajian
	   R.drawable.img_fragment_main_index_ad1,
	   R.drawable.img_fragment_main_index_ad2
	};
    
//    private String[] myuri = {
//    		"http://www.36939.net/",
//    		"http://www.36939.net/",
//    		"http://www.36939.net/",
//    		"http://www.36939.net/"
//	};
   
//    public ImageAdapter(/*List<String> imageUrls, */Context context) {  
//       // this.imageUrls = imageUrls;  
//        this.context = context;  
//        this.self = this;
//    }  
    
    public IndexFragmentGalleryImgAdapter(Fragment fragment){
    	this.fragment = fragment;
    	this.self = this;
    }
  
    public int getCount() {  
        return Integer.MAX_VALUE;  
    }  
  
    public Object getItem(int position) {  
        return imageUrls.get(position % imgs.length);  
    }  
   
    public long getItemId(int position) {  
        return position;  
    }  
  
    @SuppressWarnings("unused")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			try {
				switch (msg.what) {
					case 0: {
						self.notifyDataSetChanged();
					}
					break;
				}

				super.handleMessage(msg);
			} catch (Exception e) {
			}
		}
	};
    
    public View getView(int position, View convertView, ViewGroup parent) {  
  
        //Bitmap image;  
        if(convertView==null){  
        	convertView = LayoutInflater.from(fragment.getActivity()).inflate(R.layout.gallery_image,null);
//            convertView = LayoutInflater.from(context).inflate(R.layout.gallery_image,null); //瀹炰緥鍖朿onvertView  
            Gallery.LayoutParams params = new Gallery.LayoutParams(Gallery.LayoutParams.MATCH_PARENT,Gallery.LayoutParams.MATCH_PARENT);
            Log.v(TAG, params.toString());
            convertView.setLayoutParams(params);
            convertView.setTag(imgs);  
  
        }  
       else{  
        }  
         imageView = (ImageView) convertView.findViewById(R.id.gallery_image);  
        imageView.setImageResource(imgs[position % imgs.length]);
        //璁剧疆缂╂斁姣斾緥锛氫繚鎸佸師鏍�  
        imageView.setScaleType(ImageView.ScaleType.FIT_XY); 
//        ((MainActivity)context).changePointView(position % imgs.length);
        ((IndexFragment)fragment).changePointView(position % imgs.length);
        return convertView;  
        
    }  
  
}  
