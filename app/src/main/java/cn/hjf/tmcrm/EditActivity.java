package cn.hjf.tmcrm;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;

public class EditActivity extends BaseActivity {

	private Button mBtnAddAccessory;
	private Button mBtnUploadIdCrad;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);

		new TencentOSS(this).getObject("xxxx");

//		mBtnAddAccessory = findViewById(R.id.btn_add_accessory);
//
//		mBtnAddAccessory.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//				i.setType("*/*");
//				startActivityForResult(Intent.createChooser(i, "xxxx"), 123);
//			}
//		});

		mBtnUploadIdCrad = findViewById(R.id.btn_upload_id_card);
		mBtnUploadIdCrad.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.setType("image/*");
				startActivityForResult(Intent.createChooser(i, "xxxx"), 123);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		String path = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "zzzzzzzzzz").getAbsolutePath();
		//copy
		if (copy(data.getData(), path)) {
			new TencentOSS(this).putObject(path);
		}
	}

	private boolean copy(Uri uri, String path) {
		try {
			InputStream iStream = getContentResolver().openInputStream(uri);

			byte[] data = FileUtil.read(iStream);

			return FileUtil.save(path, data);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
}
