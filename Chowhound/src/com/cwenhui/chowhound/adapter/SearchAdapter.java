package com.cwenhui.chowhound.adapter;

import java.util.List;

import android.content.Context;
import android.widget.ImageView;

import com.cwenhui.chowhound.bean.SearchBean;
import com.cwenhui.chowhound.common.CommonAdapter;
import com.cwenhui.chowhound.common.ViewHolder;
import com.cwenhui.chowhound.config.Configs;
import com.cwenhui.chowhound.utils.ImageFirstDisplayListener;
import com.example.chowhound.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;


public class SearchAdapter extends CommonAdapter<SearchBean>{
	private ImageLoader imageLoader = ImageLoader.getInstance();		//图片加载工具
	private DisplayImageOptions options;								//显示图片的各种设置
	private ImageFirstDisplayListener displayListener = new ImageFirstDisplayListener();

    public SearchAdapter(Context context, List<SearchBean> data, int layoutId) {
        super(context, data, layoutId);
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
	public void convert(ViewHolder holder, SearchBean t) {
		 holder.setText(R.id.item_goods_name, t.getGoodsName())
         .setText(R.id.item_price, t.getPrice())
         .setText(R.id.item_sales_volume, t.getSalesVolume())
         .setText(R.id.item_shop, t.getShop());
		 imageLoader.displayImage(Configs.APIShopUploadImg+t.getGoodsImg(), 
					(ImageView)holder.getView(R.id.item_goods_img),options,displayListener);
	}
}
