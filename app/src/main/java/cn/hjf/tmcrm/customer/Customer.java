package cn.hjf.tmcrm.customer;

import cn.hjf.tmcrm.BizModel;

import java.util.List;

public class Customer extends BizModel {

	private String mName;
	private String mId;
	private List<String> mIdCardImageUrlList;

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

	public List<String> getIdCardImageUrlList() {
		return mIdCardImageUrlList;
	}

	public void setIdCardImageUrlList(List<String> idCardImageUrlList) {
		mIdCardImageUrlList = idCardImageUrlList;
	}
}
