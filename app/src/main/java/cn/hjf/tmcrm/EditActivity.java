package cn.hjf.tmcrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

public class EditActivity extends BaseActivity {

	private Button mBtnAddAccessory;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);

		mBtnAddAccessory = findViewById(R.id.btn_add_accessory);

		mBtnAddAccessory.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.setType("*/*");
				startActivityForResult(Intent.createChooser(i, "xxxx"), 123);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
