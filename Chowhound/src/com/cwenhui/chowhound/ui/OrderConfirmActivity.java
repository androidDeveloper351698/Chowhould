package com.cwenhui.chowhound.ui;

import java.util.List;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cwenhui.chowhound.adapter.OrderConfirmAdapter;
import com.cwenhui.chowhound.bean.GoodsBean;
import com.cwenhui.chowhound.config.Configs;
import com.cwenhui.chowhound.utils.HttpUtil;
import com.example.chowhound.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class OrderConfirmActivity extends Activity implements OnClickListener{
	private static final String TAG = "OrderConfirmActivity";
	private ImageView line;
	private TextView orderAccount, realAccount, realSum;
	private Button submit;
	private ListView selectedGoods;
	private List<GoodsBean> selectedData;
	private String shopName;
	private OrderConfirmAdapter adapter;
	private int sum = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_confirm);
		
		initData();
		initView();
	}

	@SuppressWarnings("unchecked")
	private void initData() {
		selectedData = (List<GoodsBean>) getIntent().getExtras().get("selectedGoods");
		shopName = getIntent().getExtras().getString("shopName");Log.e(TAG, shopName);
		adapter = new OrderConfirmAdapter(this, selectedData, R.layout.item_activity_order_confirm_selected_goods);
		for (GoodsBean bean : selectedData) {
			sum += bean.getSelectedNum()*bean.getPrice();
		}
	}

	private void initView() {
		line = (ImageView) findViewById(R.id.iv_activity_order_confirm_line);
		selectedGoods = (ListView) findViewById(R.id.lv_activity_order_confirm_goods);
		orderAccount = (TextView) findViewById(R.id.tv_activity_order_confirm_order_amount);
		realAccount = (TextView) findViewById(R.id.tv_activity_order_confirm_real_amount);
		realSum = (TextView) findViewById(R.id.tv_activity_order_confirm_sum);
		submit = (Button) findViewById(R.id.btn_activity_order_confirm_submit);
		
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.activity_order_confirm_line);
		line.setImageBitmap(createRepeatBitmap(wm.getDefaultDisplay().getWidth(), bitmap));			//将图片横向平铺 repeat-x
		orderAccount.setText(sum+"元");
		realAccount.setText(sum+"元");
		realSum.setText(sum+"元");
		selectedGoods.setAdapter(adapter);
		setListViewHeightBasedOnChildren(selectedGoods);
		
		submit.setOnClickListener(this);
	}
	
	/**
	 * 创建横向重复的bitmap
	 * @param widgetWidth
	 * @param src
	 * @return
	 */
	public Bitmap createRepeatBitmap(int widgetWidth, Bitmap src){
		int count = (widgetWidth + src.getWidth() - 1) / src.getWidth(); 		//计算出平铺填满所给widgetWidth（宽度）最少需要的重复次数
	     Bitmap bitmap = Bitmap.createBitmap(src.getWidth()*count, src.getHeight(), Config.ARGB_8888);
	     Canvas canvas = new Canvas(bitmap);
	     for (int idx = 0; idx < count; ++idx) {
	          canvas.drawBitmap(src, idx * src.getWidth(), 0, null);
	     }
	     return bitmap;
	}

	/**
	 * 根据item的高度来决定listview的高度,解决scrollview内嵌listview的问题
	 * @param listView
	 */
	public void setListViewHeightBasedOnChildren(ListView listView) {
	    if(listView == null) return;
	    ListAdapter listAdapter = listView.getAdapter();
	    if (listAdapter == null) {
	        return;
	    }
	    int totalHeight = 0;
	    for (int i = 0; i < listAdapter.getCount(); i++) {
	        View listItem = listAdapter.getView(i, null, listView);
	        listItem.measure(0, 0);
	        totalHeight += listItem.getMeasuredHeight();
	    }
	    ViewGroup.LayoutParams params = listView.getLayoutParams();
	    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
	    listView.setLayoutParams(params);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_activity_order_confirm_submit:
			submitOrder();
			break;

		default:
			break;
		}
	}
	
	public void submitOrder(){
		// 创建请求参数的封装的对象  
        RequestParams params = new RequestParams();  
        params.put("username", "cwenhui"); 								// 设置请求的参数名和参数值
        for (int i = 0; i < selectedData.size(); i++) {
			params.put("orderDetails["+i+"].order.orderName", shopName);Log.e(TAG, shopName);
			params.put("orderDetails["+i+"].order.orderState", "未完成");
			params.put("orderDetails["+i+"].goods.goodsId", selectedData.get(i).getGoodsId());Log.e(TAG, selectedData.get(i).getGoodsId()+"");
			params.put("orderDetails["+i+"].goodsNum", selectedData.get(i).getSelectedNum());Log.e(TAG, selectedData.get(i).getSelectedNum()+"");
		}
		HttpUtil.post(Configs.APISaveOrder, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] data) {
				if(new String(data).equals("success")){
					Intent intent = new Intent(OrderConfirmActivity.this, OrderTakingActivity.class);
					intent.putExtras(getIntent().getExtras());
					startActivity(intent); 
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable throwable) {
				Log.v(TAG, "throwable:"+throwable.toString());
			}
		});
	}
}