package cn.hjf.tmcrm.storage;

import android.support.annotation.Nullable;

public interface IStorageCallback<T> {
	void onSuccess(@Nullable T data);

	void onFail(@Nullable Throwable error);
}
