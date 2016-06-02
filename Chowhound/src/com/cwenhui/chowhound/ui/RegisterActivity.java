package com.cwenhui.chowhound.ui;

import com.cwenhui.chowhound.fragment.CheckPhoneFragment;
import com.example.chowhound.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;

public class RegisterActivity extends FragmentActivity implements OnClickListener{
	private static final String TAG = "RegisterActivity";
	private CheckPhoneFragment checkPhoneFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initData();
		initView();
	}

	private void initData() {
		checkPhoneFragment = new CheckPhoneFragment();
	}

	private void initView() {
		getSupportFragmentManager().beginTransaction().add(R.id.activity_register_container, checkPhoneFragment).commit();
		Button back = (Button) findViewById(R.id.btn_activity_register_back);
		
		back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_activity_register_back:
			finish();
			break;

		default:
			break;
		}
	}

}
