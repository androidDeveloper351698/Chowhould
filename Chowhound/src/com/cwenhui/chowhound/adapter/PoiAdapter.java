package com.cwenhui.chowhound.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.cwenhui.chowhound.bean.AddressBean;
import com.cwenhui.chowhound.common.CommonAdapter;
import com.cwenhui.chowhound.common.ViewHolder;
import com.example.chowhound.R;

public class PoiAdapter extends CommonAdapter<PoiInfo> {
	private final String TAG = "PoiAdapter";
	private LinearLayout layout;

	public PoiAdapter(Context context, List<PoiInfo> datas, int layoutId) {
		super(context, datas, layoutId);
	}

	@Override
	public void convert(ViewHolder holder, PoiInfo t) {
		Log.v(TAG, "positiong---->"+holder.getPosition());
		layout = (LinearLayout) holder
				.getView(R.id.ll_item_activity_address_neaby_poiname);
		if (holder.getPosition() == 0 && layout.getChildCount() < 2) {		//第一个数据代表当前位置,在poiName前加个图标并改变字体颜色
			ImageView imageView = new ImageView(mContext);
			ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(32, 32);
			imageView.setLayoutParams(params);
			imageView.setBackgroundColor(Color.TRANSPARENT);
			imageView.setImageResource(R.drawable.baidumap_ico_poi_on);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			layout.addView(imageView, 0, params);
			((TextView) holder
					.getView(R.id.tv_item_activity_address_neaby_poiname))
					.setTextColor(Color.parseColor("#ddFB5403"));
		}
		holder.setText(R.id.tv_item_activity_address_neaby_poiname, t.name)
			.setText(R.id.tv_item_activity_address_neaby_poiaddress, t.address);
		
	}

}
