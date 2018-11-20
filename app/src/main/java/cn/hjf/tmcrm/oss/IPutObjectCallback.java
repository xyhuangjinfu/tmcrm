package cn.hjf.tmcrm.oss;

public interface IPutObjectCallback {
	void onProgress(long progress, long max);

	void onSuccess(String objectUrl);

	void onFail(Throwable error);
}
