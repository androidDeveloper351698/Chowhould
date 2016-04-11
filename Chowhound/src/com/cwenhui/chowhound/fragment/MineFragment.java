package com.cwenhui.chowhound.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cwenhui.chowhound.config.Configs;
import com.cwenhui.chowhound.ui.LoginActivity;
import com.cwenhui.chowhound.utils.SharedPreferencesHelper;
import com.example.chowhound.R;

public class MineFragment extends Fragment implements OnClickListener {
	private final String TAG = "MineFragment";
	private View mView;
	private LinearLayout topBarButtonList;
	private TextView topBarName;
	private ImageButton topBarSetting;
	private ImageButton topBarNews;
	private LinearLayout moreCoupons; // 更多卡券
	private LinearLayout coupons; // 卡券种类
	private ImageView couponsArrow;
	private boolean isArrowUp = false; // 判断更多卡券后面的箭头是向上(true)还是向下(false)
	private Button login;
	private TextView username;
	private SharedPreferencesHelper share;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_main_mine, container, false);
		initData();
		initView();
		return mView;
	}

	private void initView() {
		topBarButtonList = (LinearLayout) mView.findViewById(R.id.top_bar_button_list);
		topBarButtonList.setVisibility(View.VISIBLE);
		topBarName = (TextView) mView.findViewById(R.id.top_bar_name);
		topBarName.setText("我的");
		topBarSetting = (ImageButton) mView.findViewById(R.id.top_bar_setting);
		topBarNews = (ImageButton) mView.findViewById(R.id.top_bar_news);
		login = (Button) mView.findViewById(R.id.btn_fragment_mine_login);
		username = (TextView) mView.findViewById(R.id.tv_fragment_mine_username);

		moreCoupons = (LinearLayout) mView.findViewById(R.id.ll_frag_mine_more_coupons);
		coupons = (LinearLayout) mView.findViewById(R.id.ll_frag_mine_coupons);
		couponsArrow = (ImageView) mView.findViewById(R.id.iv_frag_mine_coupons_arrow);
		
		if(share.getBooleanValue(Configs.LOGIN_STATE,false)){
			login.setVisibility(View.GONE);
			username.setVisibility(View.VISIBLE);
			username.setText(share.getStringValue(Configs.CURRENT_USER));
		}

		topBarSetting.setOnClickListener(this);
		topBarNews.setOnClickListener(this);
		moreCoupons.setOnClickListener(this);
		login.setOnClickListener(this);
	}

	private void initData() {
		share = SharedPreferencesHelper.getInstance(getActivity());
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.top_bar_setting:
			Toast.makeText(getActivity(), "设置", Toast.LENGTH_SHORT).show();
			share.setBooleanValue(Configs.LOGIN_STATE, false);							//退出当前账号,将当前登录状态记录在SharedPreferenced中
			intent = new Intent(getActivity(), LoginActivity.class);
			startActivityForResult(intent, getActivity().RESULT_FIRST_USER);
			break;

		case R.id.top_bar_news:
			Toast.makeText(getActivity(), "消息", Toast.LENGTH_SHORT).show();
			break;

		case R.id.ll_frag_mine_more_coupons:
			if (!isArrowUp) {															//显示更多卡券
				coupons.setVisibility(View.VISIBLE);
				couponsArrow.setImageResource(R.drawable.frag_mine_arrow_up);
				isArrowUp = true;
			}else{																		//隐藏卡券
				coupons.setVisibility(View.GONE);
				couponsArrow.setImageResource(R.drawable.frag_mine_arrow_down);
				isArrowUp = false;
			}
			break;

		case R.id.btn_fragment_mine_login:
			intent = new Intent(getActivity(), LoginActivity.class);
			startActivityForResult(intent, getActivity().RESULT_FIRST_USER);
		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==getActivity().RESULT_FIRST_USER && resultCode==getActivity().RESULT_OK){	//如果用户登录成功,则显示用户名
			login.setVisibility(View.GONE);
			username.setVisibility(View.VISIBLE);
			username.setText(data.getCharSequenceExtra("username"));
		}else if(!share.getBooleanValue(Configs.LOGIN_STATE, false)){								//如果用户未登录,则显示登陆按钮
			login.setVisibility(View.VISIBLE);
			username.setVisibility(View.GONE);
		}
	}

	
}
