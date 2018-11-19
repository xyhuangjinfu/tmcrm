package cn.hjf.tmcrm.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.hjf.tmcrm.BaseActivity;
import cn.hjf.tmcrm.R;

public class RegisterActivity extends BaseActivity {

	private EditText mEtAccount;
	private EditText mEtPwd;
	private EditText mEtPwdRepeat;
	private Button mBtnRegister;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		mEtAccount = findViewById(R.id.et_account);
		mEtPwd = findViewById(R.id.et_pwd);
		mEtPwdRepeat = findViewById(R.id.et_pwd_repeat);
		mBtnRegister = findViewById(R.id.btn_register);

		mBtnRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (validAccount() && validPwd()) {
					//TODO call register api
				} else {
					Toast.makeText(RegisterActivity.this, "非法的账号或密码", Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	private boolean validAccount() {
		String account = mEtAccount.getText().toString();
		return !TextUtils.isEmpty(account);
	}

	private boolean validPwd() {
		String pwd = mEtPwd.getText().toString();
		if (TextUtils.isEmpty(pwd)) {
			return false;
		}
		String pwdRepeat = mEtPwdRepeat.getText().toString();
		return pwd.equals(pwdRepeat);
	}
}
