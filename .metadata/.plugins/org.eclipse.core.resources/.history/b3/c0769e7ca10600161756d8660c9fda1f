package com.cwenhui.chowhound.adapter;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cwenhui.chowhound.bean.GoodsBean;
import com.cwenhui.chowhound.bean.IndexFragmentShop;
import com.cwenhui.chowhound.config.Configs;
import com.cwenhui.chowhound.utils.ImageFirstDisplayListener;
import com.example.chowhound.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class PullDownPinnedHeaderAdapter<T> extends BaseAdapter {
	private final String Tag = "PullDownPinnedHeaderAdapter";
	protected Context mContext;
	protected List<T> mDatas;
	protected LayoutInflater mInflater;
	private int itemLayoutId;							//getView 中使用，指定ViewHolder 中的ConvertView
	private int headerLayoutId;
//	public static LinkedList<String> URLS = new LinkedList<String>();	//item的图片url
	private ImageLoader imageLoader = ImageLoader.getInstance();		//图片加载工具
	private DisplayImageOptions options;								//显示图片的各种设置
	private ImageFirstDisplayListener displayListener = new ImageFirstDisplayListener();

	public PullDownPinnedHeaderAdapter(Context context, List<T> datas, int itemLayoutId, int headerLayoutId){
		mContext = context;
		mDatas = datas;
		mInflater = LayoutInflater.from(context);
		this.itemLayoutId = itemLayoutId;
		this.headerLayoutId = headerLayoutId;
		initDisplayImageOptions();
	}
	
	/**
	 * 初始化展示图片需要的选项
	 */
	private void initDisplayImageOptions() {
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ddt_place_holder_brand_default) // 设置图片在下载期间显示的图片
		.showImageForEmptyUri(R.drawable.ddt_place_holder_brand_default) // 设置图片Uri为空或是错误的时候显示的图片
		.showImageOnFail(R.drawable.ddt_place_holder_brand_default) // 设置图片加载/解码过程中错误时候显示的图片
		.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
		.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
		.considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
		.displayer(new RoundedBitmapDisplayer(20))// 是否设置为圆角，弧度为多少
		.displayer(new FadeInBitmapDisplayer(100))// 是否图片加载好后渐入的动画时间
		.build();
	}
	
	@Override
	public int getCount() {
		return mDatas.size()+1;						//因为第一个item被用来充当筛选条件了,所以为了能显示所有的数据,让item的数量增加一个
	}

	@Override
	public T getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		ViewHolderItem holder = null;
		if(position == 0){
			View view = View.inflate(mContext, headerLayoutId, null);
			return view;
		}
		else {
			if(convertView == null){
				convertView = mInflater.inflate(itemLayoutId, parent, false);
				holder = new ViewHolderItem();
				holder.shopImage = (ImageView) convertView.findViewById(R.id.iv_item_fragment_main_index_shops_img);
				holder.shopName = (TextView) convertView.findViewById(R.id.tv_item_fragment_main_index_shops_name);
				holder.salesVolume = (TextView) convertView.findViewById(R.id.tv_item_fragment_main_index_shops_sales_volume);
				holder.deliveryFee= (TextView) convertView.findViewById(R.id.tv_item_fragment_main_index_shops_delivery_fee);
				holder.deliveryCondition= (TextView) convertView.findViewById(R.id.tv_item_fragment_main_index_shops_delivery_condition);
				holder.arrivalTime = (TextView) convertView.findViewById(R.id.tv_item_fragment_main_index_shops_earliest_arrival_time);
				
				convertView.setTag(holder);
			}
			else{
				//复用的convertView,有可能是第一个item(用来充当header),此时holder就为null,我们要重新映射出除了第一个item以外的真正意义上的item
				holder = (ViewHolderItem) convertView.getTag();							
				if(holder == null){
					convertView = mInflater.inflate(itemLayoutId, parent, false);
					holder = new ViewHolderItem();
					holder.shopImage = (ImageView) convertView.findViewById(R.id.iv_item_fragment_main_index_shops_img);
					holder.shopName = (TextView) convertView.findViewById(R.id.tv_item_fragment_main_index_shops_name);
					holder.salesVolume = (TextView) convertView.findViewById(R.id.tv_item_fragment_main_index_shops_sales_volume);
					holder.deliveryFee= (TextView) convertView.findViewById(R.id.tv_item_fragment_main_index_shops_delivery_fee);
					holder.deliveryCondition= (TextView) convertView.findViewById(R.id.tv_item_fragment_main_index_shops_delivery_condition);
					holder.arrivalTime = (TextView) convertView.findViewById(R.id.tv_item_fragment_main_index_shops_earliest_arrival_time);
					
				}
			}
			 
//			Log.v(Tag, ((IndexFragmentShop)getItem(position-1)).getShopImg());
//			holder.shopImage.setTag(((IndexFragmentShop)getItem(position-1)).getShopImg());
//			showImageByAsyncTask(holder.shopImage, ((IndexFragmentShop)getItem(position-1)).getShopImg());	//留个接口用来加载item中的图片
			holder.shopName.setText(((IndexFragmentShop)getItem(position-1)).getShopName());
			holder.salesVolume.setText(String.valueOf(((IndexFragmentShop)getItem(position-1)).getSalesVolume()));
			holder.arrivalTime.setText(((IndexFragmentShop)getItem(position-1)).getEarliestArrivalTime());
			holder.deliveryFee.setText(((IndexFragmentShop)getItem(position-1)).getDeliveryFee());
			holder.deliveryCondition.setText(String.valueOf(((IndexFragmentShop)getItem(position-1)).getDeliveryCondition()));
			// 图片加载时候带加载情况的监听
			Log.v(Tag, ((IndexFragmentShop)getItem(position-1)).getShopImg());
			imageLoader.displayImage(Configs.APIShopUploadImg+((IndexFragmentShop)getItem(position-1)).getShopImg(), 
					holder.shopImage, options, displayListener); 
			return convertView;
		}
	}
	
//	public abstract void showImageByAsyncTask(ImageView itemImage, String url);
	
	class ViewHolderItem{
		ImageView shopImage;					//商店图像
		TextView shopName;						//商店名
		TextView salesVolume;					//销量
		TextView arrivalTime;					//送达时间
		TextView deliveryFee;					//运费
		TextView deliveryCondition;				//起送价格
	}
}
