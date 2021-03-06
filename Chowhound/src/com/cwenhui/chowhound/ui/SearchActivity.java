package com.cwenhui.chowhound.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.cwenhui.chowhound.adapter.SearchAdapter;
import com.cwenhui.chowhound.bean.SearchBean;
import com.cwenhui.chowhound.config.Configs;
import com.cwenhui.chowhound.utils.HttpUtil;
import com.cwenhui.chowhound.widget.SearchView;
import com.cwenhui.chowhound.widget.SearchView.SearchViewListener;
import com.example.chowhound.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class SearchActivity extends Activity implements SearchViewListener,
		OnItemClickListener, OnRefreshListener2<ListView> {
	private static final int INIT = 0;
	private static final int UP = 1;
	private static final int DOWN =2;
	private int pageNo=1;
	private static String TAG = "SearchActivity";
	/**
	 * 搜索结果列表view
	 */
//	private ListView lvResults;
	private String searchText;
	private PullToRefreshListView lvResults;

	/**
	 * 搜索view
	 */
	private SearchView searchView;

	/**
	 * 搜索结果列表adapter
	 */
	private SearchAdapter resultAdapter;

//	private List<SearchBean> dbData;

	/**
	 * 搜索结果的数据
	 */
	private List<SearchBean> resultData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		initData();
		initView();
	}

	private void initData() {
		resultData = new ArrayList<SearchBean>();
		resultAdapter = new SearchAdapter(this, resultData, R.layout.item_activity_search_result);
	}

	private void initView() {
		lvResults = (PullToRefreshListView) findViewById(R.id.lv_activity_search_results);
		searchView = (SearchView) findViewById(R.id.sv_activity_search);
		
		lvResults.setMode(Mode.PULL_FROM_END);
		lvResults.setAdapter(resultAdapter);

		lvResults.setOnRefreshListener(this);
		searchView.setSearchViewListener(this);
	}


	/**
	 * 获取搜索结果data和adapter
	 */
	private void getResultData(String text, int tag) {
		if(tag == INIT){
			resultData.clear();
			pageNo = 1;
		}
		HttpUtil.get(Configs.APISearchGoods+"keyword="+text+"&pageNo="+pageNo, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] data) {
				SearchBean bean;
				try {
					JSONArray array = new JSONArray(new String(data));
					for (int i = 0; i < array.length(); i++) { // 加载数据
						JSONObject object = array.getJSONObject(i);Log.e(TAG, object.toString());
						bean = new SearchBean(object.getInt("goodsId"), object.getString("goodsImgPath"), 
								object.getString("goodsName"), object.getString("goodsPrice"), object.getString("goodsSales"), "");
						resultData.add(bean);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				resultAdapter.notifyDataSetChanged();
				lvResults.onRefreshComplete();
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable throwable) {
				Log.e(TAG, throwable.toString());
			}
		});
	}

	/**
	 * 点击搜索键时edit text触发的回调
	 * 
	 * @param text
	 */
	@Override
	public void onSearch(String text) {
		if(text.equals("")){
			Toast.makeText(this, "请输入商品名或商店名", 1).show();
			return;
		}
		searchText = text;
		// 更新result数据
		getResultData(text, INIT);
		lvResults.setVisibility(View.VISIBLE);
		Toast.makeText(this, "完成搜素", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onInput() {
		lvResults.setVisibility(View.GONE);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long l) {
		Toast.makeText(SearchActivity.this, position + "", Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		Toast.makeText(this, "Pull Up!",Toast.LENGTH_SHORT).show();
		pageNo++;
		getResultData(searchText, UP);
	}

}
