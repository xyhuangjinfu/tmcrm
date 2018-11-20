package cn.hjf.tmcrm.oss;

public interface IDeleteObjectCallback {
	void onSuccess();

	void onFail(Throwable error);
}
