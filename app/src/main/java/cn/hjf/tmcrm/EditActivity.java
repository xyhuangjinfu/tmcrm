package cn.hjf.tmcrm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.Toast;
import cn.hjf.tmcrm.customer.CustomerInfoView;
import cn.hjf.tmcrm.customer.UploadIdCardImageTask;
import cn.hjf.tmcrm.oss.TencentOSS;
import cn.hjf.tmcrm.storage.FileStorage;

public class EditActivity extends BaseActivity {

	private static final int REQ_CHOOSE_ID_CARD_IMAGE = 1000;

	private CustomerInfoView mCustomerInfoView;

	private FileStorage mFileStorage;
	private TencentOSS mTencentOSS;

	private ProgressDialog mProgressDialog;

	private UploadIdCardImageTask mUploadIdCardImageTask;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);

		mFileStorage = new FileStorage(this);

		mTencentOSS = new TencentOSS(this);

		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.setMax(100);

		mCustomerInfoView = new CustomerInfoView(this, findViewById(R.id.layout_customer_info));
		mCustomerInfoView.setEventListener(new CustomerInfoView.EventListener() {
			@Override
			public void onChooseImage() {
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.setType("image/*");
				startActivityForResult(Intent.createChooser(i, "选择身份证照片"), REQ_CHOOSE_ID_CARD_IMAGE);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQ_CHOOSE_ID_CARD_IMAGE) {
			handleChooseIdCardImage(data);
		}

	}

	private void handleChooseIdCardImage(Intent data) {
		mUploadIdCardImageTask = new UploadIdCardImageTask(this);
		mUploadIdCardImageTask.setCallback(new UploadIdCardImageTask.Callback() {
			@Override
			public void onStart() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mProgressDialog.show();
					}
				});
			}

			@Override
			public void onSuccess(final Image image) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mProgressDialog.cancel();
						mCustomerInfoView.renderIdCard(image);
					}
				});
			}

			@Override
			public void onFail(final Throwable error) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mProgressDialog.cancel();
						Toast.makeText(EditActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
					}
				});
			}

			@Override
			public void onProgress(final int progress, final int max) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mProgressDialog.setMax(max);
						mProgressDialog.setProgress(progress);
					}
				});
			}
		});
		mUploadIdCardImageTask.execute(data.getData());

//		final String path = new File(FileStorage.PATH_ID_CARD_IMAGE, "temp").getAbsolutePath();
//		//复制到本地目录
//		if (!mFileStorage.copyFromUri(data.getData(), path)) {
//			Toast.makeText(this, "文件读取失败，请重试", Toast.LENGTH_SHORT).show();
//			return;
//		}
//		//上传到云存储
//		mProgressDialog.show();
//		new TencentOSS(this).putObject(path, UUID.randomUUID().toString(), new IPutObjectCallback() {
//			@Override
//			public void onProgress(long progress, long max) {
//				mProgressDialog.setProgress((int) (100.0 * progress / max));
//			}
//
//			@Override
//			public void onSuccess(String objectUrl) {
//				mProgressDialog.cancel();
//
//				//更新本地文件名称
//				String realPath = FileStorage.PATH_ID_CARD_IMAGE + "/" + FileStorage.getFileNameFromUrl(objectUrl);
//				FileUtil.rename(path, realPath);
//
//				//显示
//				Image image = new Image();
//				image.setUrl(objectUrl);
//				image.setLocalPath(realPath);
//				mCustomerInfoView.renderIdCard(image);
//			}
//
//			@Override
//			public void onFail(Throwable error) {
//				mProgressDialog.cancel();
//
//				Toast.makeText(EditActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//			}
//		});
	}

}
