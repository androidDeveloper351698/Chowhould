package com.cwenhui.chowhound.utils;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

import com.cwenhui.chowhound.adapter.PullDownPinnedHeaderAdapter;
import com.cwenhui.chowhound.bean.IndexFragmentShop;
import com.cwenhui.chowhound.config.Configs;
import com.cwenhui.chowhound.fragment.IndexFragment;
import com.cwenhui.chowhound.widget.LoadingDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class GetDataTask {
	private static final String TAG = "GetDataTask";
	private final IndexFragment indexFragment;

	/**
	 * @param indexFragment
	 */
	public GetDataTask(IndexFragment indexFragment) {
		this.indexFragment = indexFragment;
	}

	public void getDatas(String url, final int tag) {
		LoadingDialog.showDialog(indexFragment.getActivity());		//加载数据时显示对话框
		HttpUtil.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] data) {
				IndexFragmentShop fragmentShop = null;
				try {
					JSONArray array = new JSONArray(new String(data));
					if (tag == IndexFragment.PULL_DOWN) { // 如果是下拉刷新，则先清除搜游数据，然后重新加载
						GetDataTask.this.indexFragment.mListItems.clear();
//						PullDownPinnedHeaderAdapter.URLS.clear();					//此处不知是否需要clear，回头将图片整成不一样的测试一下
						GetDataTask.this.indexFragment.PAGE = 2;
					}
					for (int i = 0; i < array.length(); i++) { // 加载数据
						fragmentShop = new IndexFragmentShop(
								Configs.APITestForImg, "程序猿烧饼店", 123456,
								"18:00", "免配送费", i, 0, "none");
//						Log.v(TAG, array.getJSONObject(i).getInt("shopId")+"---------id");
						fragmentShop.setShopId(array.getJSONObject(i).getInt("shopId"));
						fragmentShop.setShopName(array.getJSONObject(i)
								.getString("shopName"));
						fragmentShop.setShopImg(array.getJSONObject(i).getString("imgPath"));
						GetDataTask.this.indexFragment.mListItems.add(fragmentShop);
//						PullDownPinnedHeaderAdapter.URLS.add(Configs.APIShopUploadImg + array.getJSONObject(i).getString("imgPath"));
					}
					GetDataTask.this.indexFragment.adapter.notifyDataSetChanged();
					GetDataTask.this.indexFragment.mPullRefreshListView.onRefreshComplete();
					LoadingDialog.dismissDialog();
					if (tag == IndexFragment.PULL_UP)
						GetDataTask.this.indexFragment.PAGE++; // 上拉后,页数要增加1

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable throwable) {
				Log.v(TAG, "throwable:  " + throwable.toString());
			}
		});
	}
}