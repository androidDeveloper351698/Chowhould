package com.cwenhui.chowhound.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.cwenhui.chowhound.adapter.CommentAdapter;
import com.cwenhui.chowhound.bean.CommentFragmentBean;
import com.example.chowhound.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class CommentFragment extends Fragment implements OnClickListener,
		OnRefreshListener2<ListView> {
	private final String TAG = "CommentFragment";
	private View mView;
	private PullToRefreshListView refreshListView;
	private ListView actualListView;
	private List<CommentFragmentBean> fragmentBeans;
	private CommentAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_comment, container, false);
		initData();
		initView();
		return mView;
	}

	private void initView() {
		refreshListView = (PullToRefreshListView) mView
				.findViewById(R.id.pullToRefresh_fragment_comment);
		refreshListView.setMode(Mode.PULL_FROM_END); // 设置仅允许上拉刷新(加载)
		refreshListView.setOnRefreshListener(this);
		actualListView = refreshListView.getRefreshableView();
		actualListView.addHeaderView(LayoutInflater.from(getActivity())
				.inflate(R.layout.item_fragment_comment_header, null));
		actualListView.setAdapter(adapter);
	}

	private void initData() {
		fragmentBeans = new ArrayList<CommentFragmentBean>();
		CommentFragmentBean bean = new CommentFragmentBean(4, "还算准时", "张三丰",
				"非常好吃...才怪", "03-16", "123");
		for (int i = 0; i < 10; i++) {
			fragmentBeans.add(bean);
		}
		adapter = new CommentAdapter(getActivity(), fragmentBeans,
				R.layout.item_fragment_comment);
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		CommentFragmentBean bean = new CommentFragmentBean(2.5f, "晚到了一个多小时",
				"张三丰他爸", "不好吃", "03-16", "123");
		fragmentBeans.add(bean);
		adapter.notifyDataSetChanged();
		refreshListView.postDelayed(new Runnable() {
			@Override
			public void run() {
				refreshListView.onRefreshComplete();
				Toast.makeText(getActivity(), "没有更多数据了", Toast.LENGTH_SHORT).show();
			}
		}, 500);
	}

}
