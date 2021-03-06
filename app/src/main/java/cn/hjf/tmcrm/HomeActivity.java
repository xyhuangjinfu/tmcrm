package cn.hjf.tmcrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import cn.hjf.tmcrm.customer.CustomerEditActivity;

public class HomeActivity extends BaseActivity {

	private Button mBtnCreate;
	private Button mBtnCreateCustomer;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		mBtnCreate = findViewById(R.id.btn_create);
		mBtnCreate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(HomeActivity.this, EditActivity.class));
			}
		});

		mBtnCreateCustomer = findViewById(R.id.btn_create_customer);
		mBtnCreateCustomer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(HomeActivity.this, CustomerEditActivity.class));
			}
		});

//		ImageView imageView = (ImageView) findViewById(R.id.iv_test);
//
//		Glide.with(this)
//				.load("https://tmcrm-1258098598.cos.ap-shanghai.myqcloud.com/%E5%BC%A0%E4%B8%89/1")
//				.into(imageView);
	}
}
