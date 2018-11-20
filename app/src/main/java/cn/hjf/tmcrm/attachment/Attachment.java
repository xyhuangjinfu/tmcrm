package cn.hjf.tmcrm.attachment;

import cn.hjf.tmcrm.BizModel;

public class Attachment extends BizModel {

	private String uuid;

	private String filePath;

	private String mimeType;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
}
