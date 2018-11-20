package cn.hjf.tmcrm.customer;

import cn.hjf.tmcrm.BizModel;
import cn.hjf.tmcrm.Image;

import java.util.List;

public class Customer extends BizModel {

	private String mName;
	private String mId;
	private List<Image> mIdCardImageList;

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

	public List<Image> getIdCardImageList() {
		return mIdCardImageList;
	}

	public void setIdCardImageList(List<Image> idCardImageList) {
		mIdCardImageList = idCardImageList;
	}
}
