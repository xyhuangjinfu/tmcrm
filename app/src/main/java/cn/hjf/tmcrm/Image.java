package cn.hjf.tmcrm;

public class Image extends BizModel {

	private String mUrl;
	private String mLocalPath;

	public String getUrl() {
		return mUrl;
	}

	public void setUrl(String url) {
		mUrl = url;
	}

	public String getLocalPath() {
		return mLocalPath;
	}

	public void setLocalPath(String localPath) {
		mLocalPath = localPath;
	}
}
