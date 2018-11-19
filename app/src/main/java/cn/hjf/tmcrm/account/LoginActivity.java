package cn.hjf.tmcrm.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.hjf.tmcrm.BaseActivity;
import cn.hjf.tmcrm.R;
import cn.hjf.tmcrm.storage.IStorageCallback;

public class LoginActivity extends BaseActivity {

	private EditText mEtAccount;
	private EditText mEtPwd;
	private Button mBtnLogin;
	private Button mBtnRegister;

	private AccountStorage mAccountStorage;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		mAccountStorage = new AccountStorage(this);

		mEtAccount = findViewById(R.id.et_account);
		mEtPwd = findViewById(R.id.et_pwd);

		mBtnLogin = findViewById(R.id.btn_login);
		mBtnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Account account = createAccount();
				if (account != null) {
					queryAccount(account);
				}
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

	@Nullable
	private Account createAccount() {
		String acc = mEtAccount.getText().toString();
		String pwd = mEtPwd.getText().toString();

		if (TextUtils.isEmpty(acc)) {
			return null;
		}

		if (TextUtils.isEmpty(pwd)) {
			return null;
		}

		Account account = new Account();
		account.setAccount(acc);
		account.setPwd(pwd);

		return account;
	}

	private void queryAccount(final Account account) {
		mAccountStorage.queryAccount(account.getAccount(), account.getPwd(), new IStorageCallback<Account>() {
			@Override
			public void onSuccess(@Nullable Account data) {
				if (data != null) {
					Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
					finish();
				} else {
					Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFail(@Nullable Throwable error) {
				Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}
}
