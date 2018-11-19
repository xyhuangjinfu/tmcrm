package cn.hjf.tmcrm.account;

import cn.hjf.tmcrm.BizModel;

public class Account extends BizModel {

	private String mAccount;
	private String mPwd;

	public String getAccount() {
		return mAccount;
	}

	public void setAccount(String account) {
		mAccount = account;
	}

	public String getPwd() {
		return mPwd;
	}

	public void setPwd(String pwd) {
		mPwd = pwd;
	}
}
