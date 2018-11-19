package cn.hjf.tmcrm.storage;

import android.content.Context;

public abstract class BaseStorage implements IStorage {

	protected Context mContext;

	public BaseStorage(Context context) {
		mContext = context;
	}
}
