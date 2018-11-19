package cn.hjf.tmcrm.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import cn.hjf.tmcrm.BaseActivity;
import cn.hjf.tmcrm.R;

public class LoginActivity extends BaseActivity {

	private EditText mEtAccount;
	private EditText mEtPwd;
	private Button mBtnLogin;
	private Button mBtnRegister;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		mBtnLogin = findViewById(R.id.btn_login);
		mBtnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

		mBtnRegister = findViewById(R.id.btn_register);
		mBtnRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
			}
		});
	}
}
