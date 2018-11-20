package cn.hjf.tmcrm.customer;

import cn.hjf.tmcrm.BizModel;
import cn.hjf.tmcrm.attachment.Attachment;

import java.util.List;

public class Customer extends BizModel {

	private String mName;
	private String mId;
	private List<Attachment> mIdCardImageList;

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public String getId() {
		return mId;
	}

	public void setId(String id) {
		mId = id;
	}

	public List<Attachment> getIdCardImageList() {
		return mIdCardImageList;
	}

	public void setIdCardImageList(List<Attachment> idCardImageList) {
		mIdCardImageList = idCardImageList;
	}
}
