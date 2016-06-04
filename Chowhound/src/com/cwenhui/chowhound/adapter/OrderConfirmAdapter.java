package com.cwenhui.chowhound.adapter;

import java.util.List;

import android.content.Context;

import com.cwenhui.chowhound.bean.GoodsBean;
import com.cwenhui.chowhound.common.CommonAdapter;
import com.cwenhui.chowhound.common.ViewHolder;
import com.example.chowhound.R;

public class OrderConfirmAdapter extends CommonAdapter<GoodsBean> {

	public OrderConfirmAdapter(Context context, List<GoodsBean> datas, int layoutId){
		super(context, datas, layoutId);
	}

	@Override
	public void convert(ViewHolder holder, GoodsBean t) {
		holder.setText(R.id.tv_item_activity_order_confirm_name, t.getGoodsName())
			.setText(R.id.tv_item_activity_order_confirm_num, "x"+t.getSelectedNum())
			.setText(R.id.tv_item_activity_order_confirm_price, t.getPrice()+"元");
	}
	
}
