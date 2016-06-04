package com.cwenhui.chowhound.adapter;

import java.util.List;

import android.content.Context;
import android.widget.RatingBar;

import com.cwenhui.chowhound.bean.CommentFragmentBean;
import com.cwenhui.chowhound.common.CommonAdapter;
import com.cwenhui.chowhound.common.ViewHolder;
import com.example.chowhound.R;

public class CommentAdapter extends CommonAdapter<CommentFragmentBean> {

	public CommentAdapter(Context context, List<CommentFragmentBean> datas,
			int layoutId) {
		super(context, datas, layoutId);
	}

	@Override
	public void convert(ViewHolder holder, CommentFragmentBean t) {
		holder.setText(R.id.tv_fragment_comment_delivery_comment,t.getDeliveryComment());
		holder.setText(R.id.tv_fragment_comment_user_time, t.getUserName());
		holder.setText(R.id.tv_fragment_comment_user_comment, t.getComment());
		holder.setText(R.id.tv_item_fragment_comment_consume_time, t.getConsumeTime());
		((RatingBar) holder.getView(R.id.ratingBar_fragment_comment)).setRating(t.getRatingBar());
	}

}
