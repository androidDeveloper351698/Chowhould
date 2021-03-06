package com.cwenhui.chowhound.widget;

import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cwenhui.chowhound.bean.GoodsBean;
import com.cwenhui.chowhound.common.CommonAdapter;
import com.cwenhui.chowhound.common.ViewHolder;
import com.example.chowhound.R;

public class ShopCartQuickAction2 extends CustomQuickAction<GoodsBean> {
	private static final String TAG = "ShopCartQuickAction2";
	private onClickShopCartListener shopCartListener;
	private ListView itemList;
	private TextView clear;
	private CommonAdapter<GoodsBean> adapter;

	public ShopCartQuickAction2(Context context) {
		super(context);
		adapter = new CommonAdapter<GoodsBean>(context, mItems, R.layout.item_shop_cart_quick_action) {
			
			@Override
			public void convert(ViewHolder holder, GoodsBean t) {
				holder.setText(R.id.tv_item_shop_cart_quickaction_name, t.getGoodsName())
					.setText(R.id.tv_item_shop_cart_quickaction_price, t.getPrice()+"元")
					.setText(R.id.tv_item_shop_cart_quickaction_num, t.getSelectedNum()+"");
				Button del = (Button)holder.getView(R.id.btn_item_shop_cart_quickaction_del);
				Button add = (Button)holder.getView(R.id.btn_item_shop_cart_quickaction_add);
				TextView num = (TextView)holder.getView(R.id.tv_item_shop_cart_quickaction_num);
				del.setTag(t); 
				add.setTag(t);
				setOnShopCartClickListener(add, num);
				setOnShopCartClickListener(del, num);
			}
		};
		setOnShopCartClickListener(clear, null);
		itemList.setAdapter(adapter);
	}
	
	private void setOnShopCartClickListener(View view, final TextView selectedNum) {
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(shopCartListener != null){
					if(v.getId() == R.id.btn_item_shop_cart_quickaction_add){
						shopCartListener.onClickAdd(v, selectedNum);
					}else if(v.getId() == R.id.btn_item_shop_cart_quickaction_del){
						shopCartListener.onClickDel(v, selectedNum);
					}else{
						shopCartListener.onClickClear(v);
					}
				}
			}
		});
	}
	
	public void setOnClickShopCartListener(onClickShopCartListener shopCartListener) {
		this.shopCartListener = shopCartListener;
	}

	@Override
	public void addQuickActionItem(GoodsBean item) {
	}
	
	public void addQuickActionItems(List<GoodsBean> items){
		mItems.clear();								//添加前要先移出之前有的,否则数据会重复
		mItems.addAll(items);
		adapter.notifyDataSetChanged();				//通知listview内容改变
	}

	@Override
	public void initQuickAction(Context context) {
		mRootView = (ViewGroup) mInflater.inflate(R.layout.layout_shop_cart_quickaction2, null);
		clear = (TextView) mRootView.findViewById(R.id.tv_layout_shop_cart_quickaction_clear);
		itemList =  (ListView) mRootView.findViewById(R.id.shop_cart_layout_quickaction2);
		itemList.setDividerHeight(0);
		itemList.setDivider(null);
		mRootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		setContentView(mRootView);
	}
	
	@Override
	public void show(View anchor) {												//重写show方法,否则无法正确显示QuickAction的位置
		if (!isShowing()) {
			Direction showDirection = computeDisplayPosition(anchor);			// 方向
			int[] locations = preShow(anchor, showDirection);					// 根据方向，显示箭头
			setQuickActionPosition(locations);
			if (locations != null) {											// 显示PopupWindow
				showAtLocation(anchor, Gravity.NO_GRAVITY, locations[0], locations[1]);
			}
		} else {
			dismiss();
		}
	}

	/**
	 * 设置QuickAction显示的位置,如果item<=3 照常显示, 如果item>3,则控制listview的高度
	 * @param locations
	 */
	private void setQuickActionPosition(int[] locations) {
		LayoutParams layoutParams = itemList.getLayoutParams();  
		if(mItems.size()<=3){												
			locations[1] -= getItemHeight()*(mItems.size()-1);
		    layoutParams.height = getItemHeight()*mItems.size();  
		}else{
			locations[1] -= getItemHeight()*2;
		    layoutParams.height = getItemHeight()*3;  
		}
		itemList.setLayoutParams(layoutParams);  
	}
	
	/**
	 * 获取item的高度
	 * @return
	 */
	private int getItemHeight(){
		ListAdapter adapter = itemList.getAdapter();  
	    if (adapter == null) { 
	        return 0; 
	    } 
	    View item = adapter.getView(0, null, itemList); 
	    item.measure(0, 0); 
	    return item.getMeasuredHeight();
	}
	
	/**
	 * 移走index指向的item
	 * @param index		数据的索引
	 * @param view		用于定位QuickAction的位置
	 */
	public void removeItem(int index, View view){
		mItems.remove(index);
		dismiss();
		if(mItems.size()>0){
			show(view);
		}
	}
	
	/**
	 * 移除所有item
	 * @param view		用于定位QuickAction的位置
	 */
	public void removeAll(View view){
		mItems.clear();
		dismiss();
	}

	public interface onClickShopCartListener{
		public void onClickDel(View view, TextView num);
		public void onClickAdd(View view, TextView num);
		public void onClickClear(View view);
	}
}
