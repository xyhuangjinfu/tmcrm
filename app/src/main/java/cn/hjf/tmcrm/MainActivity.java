package cn.hjf.tmcrm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.List;
import java.util.UUID;

import cn.hjf.tmcrm.account.LoginActivity;
import cn.hjf.tmcrm.attachment.Attachment;
import cn.hjf.tmcrm.attachment.AttachmentDao;

public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

//		Attachment attachment = new Attachment();
//		attachment.setUuid(UUID.randomUUID().toString());
//		attachment.setFilePath("xxx");
//		attachment.setMimeType("image/*");
//		boolean b = new AttachmentDao(this).insertAll(attachment);
//		Log.e("O_O", "insert : " + b);


		List<Attachment> attachmentList = new AttachmentDao(this).queryAll();

		Log.e("O_O", attachmentList.toString());

		jumpToLogin();
		finish();
	}

	private void jumpToLogin() {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}
}
