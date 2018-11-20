package cn.hjf.tmcrm.attachment;

import cn.hjf.tmcrm.BizModel;

public class Attachment extends BizModel {

	private String mUrl;
	private String mFilePath;
	private String mMimeType;

	public String getUrl() {
		return mUrl;
	}

	public void setUrl(String url) {
		mUrl = url;
	}

	public String getFilePath() {
		return mFilePath;
	}

	public void setFilePath(String filePath) {
		mFilePath = filePath;
	}

	public String getMimeType() {
		return mMimeType;
	}

	public void setMimeType(String mimeType) {
		mMimeType = mimeType;
	}
}
