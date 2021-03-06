package com.cwenhui.chowhound.adapter;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.http.Header;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.cwenhui.chowhound.bean.OrderFragmentBean;
import com.cwenhui.chowhound.common.CommonAdapter;
import com.cwenhui.chowhound.common.ViewHolder;
import com.cwenhui.chowhound.config.Configs;
import com.cwenhui.chowhound.utils.HttpUtil;
import com.cwenhui.chowhound.utils.ImageFirstDisplayListener;
import com.example.chowhound.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

public class OrderFragmentAdapter extends CommonAdapter<OrderFragmentBean> implements OnClickListener{
	private static final String TAG = "OrderFragmentAdapter";
	private SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
	private ImageLoader imageLoader = ImageLoader.getInstance();		//图片加载工具
	private DisplayImageOptions options;								//显示图片的各种设置
	private ImageFirstDisplayListener displayListener = new ImageFirstDisplayListener();

	public OrderFragmentAdapter(Context context, List<OrderFragmentBean> datas,
			int layoutId) {
		super(context, datas, layoutId);
		initDisplayImageOptions();
	}
	
	/**
	 * 初始化展示图片需要的选项
	 */
	private void initDisplayImageOptions() {
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ddt_place_holder_brand_default) 		// 设置图片在下载期间显示的图片
		.showImageForEmptyUri(R.drawable.ddt_place_holder_brand_default) 	// 设置图片Uri为空或是错误的时候显示的图片
		.showImageOnFail(R.drawable.ddt_place_holder_brand_default) 		// 设置图片加载/解码过程中错误时候显示的图片
		.cacheInMemory(true) 												// 设置下载的图片是否缓存在内存中
		.cacheOnDisc(true) 													// 设置下载的图片是否缓存在SD卡中
		.considerExifParams(true) 											// 是否考虑JPEG图像EXIF参数（旋转，翻转）
		.displayer(new RoundedBitmapDisplayer(20))							// 是否设置为圆角，弧度为多少
		.displayer(new SimpleBitmapDisplayer())								// 是否图片加载好后渐入的动画时间,此时不要使用FadeInBitmapDisplayer(100),
																			//否则更新listview时会出现闪烁的问题
		.build();
	}
	
	@Override
	public void convert(ViewHolder holder, OrderFragmentBean t) {
		holder.setText(R.id.item_fragment_order_shopname,
				t.getOrderName()).setText(R.id.item_frag_order_goods_name,t.getGoodsName())
				.setText(R.id.tv_item_frag_total_price,String.valueOf(t.getTotalPrice()))
				.setText(R.id.item_frag_order_consume_time,sdf.format(new Timestamp(Long.parseLong(t.getConsumeTime()))))
				.setText(R.id.tv_item_frag_order_status, t.getOrderState());
		imageLoader.displayImage(Configs.APIShopUploadImg+t.getShopImg(), 
				(ImageView)holder.getView(R.id.item_frag_order_shop_image),options,displayListener);
		
		ImageButton del = holder.getView(R.id.item_fragment_order_delete);
		del.setTag(t);
		del.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.item_fragment_order_delete:
			Toast.makeText(mContext, "删除成功", 1).show();
			OrderFragmentBean bean = (OrderFragmentBean) v.getTag();
			delOrderByOrderId(bean.getOrderId());
			mDatas.remove(bean);
			notifyDataSetChanged();
			break;

		default:
			break;
		}
	}
	
	private void delOrderByOrderId(int orderId){
		HttpUtil.get(Configs.APIDeleteOrderByOrderId+orderId, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] data) {}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable throwable) {
				Log.v(TAG, "throwable:  " + throwable.toString());
			}
		});
	}

}
