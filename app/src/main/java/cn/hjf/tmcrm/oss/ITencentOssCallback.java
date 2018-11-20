package cn.hjf.tmcrm.oss;

public interface ITencentOssCallback {

	void onPutObjectSuccess(String accessUrl);

	void onGetObjectSuccess(String localFilePath);

	void onDeleteObjectSuccess();
}
