package cn.hjf.tmcrm.account;

import android.content.Context;
import android.support.annotation.NonNull;
import cn.hjf.tmcrm.storage.BaseStorage;
import cn.hjf.tmcrm.storage.IStorageCallback;
import com.avos.avoscloud.*;

import java.util.List;

public class AccountStorage extends BaseStorage {

	public AccountStorage(Context context) {
		super(context);
	}

	public void saveAccount(Account account, final IStorageCallback<Account> callback) {
		AVObject avObject = transfer(account);
		avObject.saveInBackground(new SaveCallback() {
			@Override
			public void done(AVException e) {
				if (callback != null) {
					if (e == null) {
						callback.onSuccess(null);
					} else {
						callback.onFail(e);
					}
				}
			}
		});
	}

	public void queryAccount(String account, final IStorageCallback<Account> callback) {
		AVQuery<AVObject> avQuery = new AVQuery<>("Account");
		avQuery.whereEqualTo("account", account);
		avQuery.findInBackground(new FindCallback<AVObject>() {
			@Override
			public void done(List<AVObject> list, AVException e) {
				if (callback != null) {
					if (e == null) {
						if (list == null || list.isEmpty()) {
							callback.onSuccess(null);
						} else {
							callback.onSuccess(transfer(list.get(0)));
						}
					} else {
						callback.onFail(e);
					}
				}
			}
		});
	}

	private AVObject transfer(@NonNull Account account) {
		AVObject avObject = new AVObject("Account");
		avObject.put("account", account.getAccount());
		avObject.put("pwd", account.getPwd());
		return avObject;
	}

	private Account transfer(@NonNull AVObject avObject) {
		Account account = new Account();
		account.setAccount(avObject.getString("account"));
		account.setPwd(avObject.getString("pwd"));
		return account;
	}
}
