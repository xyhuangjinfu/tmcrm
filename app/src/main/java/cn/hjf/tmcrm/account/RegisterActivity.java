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
import cn.hjf.tmcrm.storage.IStorageCallback;

public class RegisterActivity extends BaseActivity {

	private EditText mEtAccount;
	private EditText mEtPwd;
	private EditText mEtPwdRepeat;
	private Button mBtnRegister;

	private AccountStorage mAccountStorage;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		mAccountStorage = new AccountStorage(this);

		mEtAccount = findViewById(R.id.et_account);
		mEtPwd = findViewById(R.id.et_pwd);
		mEtPwdRepeat = findViewById(R.id.et_pwd_repeat);
		mBtnRegister = findViewById(R.id.btn_register);

		mBtnRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Account account = createAccount();
				if (account != null) {
					queryAccount(account);
				} else {
					Toast.makeText(RegisterActivity.this, "非法的账号或密码", Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	@Nullable
	private Account createAccount() {
		String acc = mEtAccount.getText().toString();
		String pwd = mEtPwd.getText().toString();
		String pwdRepeat = mEtPwdRepeat.getText().toString();

		if (TextUtils.isEmpty(acc)) {
			return null;
		}

		if (TextUtils.isEmpty(pwd) || !pwd.equals(pwdRepeat)) {
			return null;
		}

		Account account = new Account();
		account.setAccount(acc);
		account.setPwd(pwd);

		return account;
	}

	private void queryAccount(final Account account) {
		mAccountStorage.queryAccount(account.getAccount(), new IStorageCallback<Account>() {
			@Override
			public void onSuccess(@Nullable Account data) {
				if (data == null) {
					register(account);
				} else {
					Toast.makeText(RegisterActivity.this, "该帐号已存在", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFail(@Nullable Throwable error) {
				Toast.makeText(RegisterActivity.this, error == null ? "查询账号失败" : error.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void register(Account account) {
		mAccountStorage.saveAccount(account, new IStorageCallback<Account>() {
			@Override
			public void onSuccess(@Nullable Account data) {
				Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
				finish();
			}

			@Override
			public void onFail(@Nullable Throwable error) {
				Toast.makeText(RegisterActivity.this, "注册失败:" + error.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}
}
