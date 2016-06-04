package com.cwenhui.chowhound.adapter;

import java.util.List;

import android.content.Context;

import com.cwenhui.chowhound.common.CommonAdapter;
import com.cwenhui.chowhound.common.ViewHolder;
import com.example.chowhound.R;

public class SubcatalogAdapter extends CommonAdapter<String> {

	public SubcatalogAdapter(Context context, List<String> datas, int layoutId) {
		super(context, datas, layoutId);
	}
	
	@Override
	public void convert(ViewHolder holder, String t) {
		holder.setText(R.id.tv_item_fragment_index_top_catalog, t);
	}

	public void setDatas(List<String> datas){
		mDatas.clear();
		mDatas.addAll(datas);
		notifyDataSetChanged();
	}

}
