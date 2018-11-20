package cn.hjf.tmcrm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import cn.hjf.tmcrm.customer.*;
import cn.hjf.tmcrm.oss.TencentOSS;
import cn.hjf.tmcrm.storage.FileStorage;
import cn.hjf.tmcrm.storage.IStorageCallback;

import java.io.File;
import java.util.ArrayList;

public class EditActivity extends BaseActivity {

	private static final int REQ_CHOOSE_ID_CARD_IMAGE = 1000;

	private CustomerInfoView mCustomerInfoView;

	private FileStorage mFileStorage;
	private TencentOSS mTencentOSS;

	private ProgressDialog mHorizontalProgressDialog;
	private ProgressDialog mCircleProgressDialog;

	private UploadIdCardImageTask mUploadIdCardImageTask;
	private DeleteIdCardImageTask mDeleteIdCardImageTask;

	private Customer mCustomer = new Customer();

	private CustomerStorage mCustomerStorage;

	private Button mBtnSubmit;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);

		mFileStorage = new FileStorage(this);

		mTencentOSS = new TencentOSS(this);

		mCustomerStorage = new CustomerStorage(this);

		mHorizontalProgressDialog = new ProgressDialog(this);
		mHorizontalProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mHorizontalProgressDialog.setMax(100);

		mCircleProgressDialog = new ProgressDialog(this);

		mCustomerInfoView = new CustomerInfoView(this, findViewById(R.id.layout_customer_info));
		mCustomerInfoView.setEventListener(new CustomerInfoView.EventListener() {
			@Override
			public void onChooseImage() {
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.setType("image/*");
				startActivityForResult(Intent.createChooser(i, "选择身份证照片"), REQ_CHOOSE_ID_CARD_IMAGE);
			}

			@Override
			public void onClickImage(Image image) {
				//预览
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

				File imageFile = new File(image.getLocalPath());
				Uri imageUri = FileProvider.getUriForFile(EditActivity.this, EditActivity.this.getApplicationContext().getPackageName() + ".provider", imageFile);

				intent.setDataAndType(imageUri, "image/*");
				startActivity(intent);
			}

			@Override
			public void onDeleteImage(final Image image) {
				//删除
				mDeleteIdCardImageTask = new DeleteIdCardImageTask(EditActivity.this);
				mDeleteIdCardImageTask.setCallback(new DeleteIdCardImageTask.Callback() {
					@Override
					public void onStart() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								mCircleProgressDialog.show();
							}
						});
					}

					@Override
					public void onSuccess() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								mCircleProgressDialog.cancel();

								mCustomer.getIdCardImageList().remove(image);
								mCustomerInfoView.renderCustomer(mCustomer);
							}
						});
					}

					@Override
					public void onFail(final Throwable error) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								mCircleProgressDialog.cancel();
								Toast.makeText(EditActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
							}
						});
					}
				});
				mDeleteIdCardImageTask.execute(image);
			}
		});

		mBtnSubmit = findViewById(R.id.btn_submit);
		mBtnSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mCircleProgressDialog.show();
				mCustomerStorage.saveCustomer(mCustomer, new IStorageCallback<Customer>() {
					@Override
					public void onSuccess(@Nullable Customer data) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								mCircleProgressDialog.cancel();
							}
						});
					}

					@Override
					public void onFail(@Nullable final Throwable error) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								mCircleProgressDialog.cancel();
								Toast.makeText(EditActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
							}
						});
					}
				});
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
						mHorizontalProgressDialog.show();
					}
				});
			}

			@Override
			public void onSuccess(final Image image) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mHorizontalProgressDialog.cancel();

						if (mCustomer.getIdCardImageList() == null) {
							mCustomer.setIdCardImageList(new ArrayList<Image>());
						}
						mCustomer.getIdCardImageList().add(image);
						mCustomerInfoView.renderCustomer(mCustomer);
					}
				});
			}

			@Override
			public void onFail(final Throwable error) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mHorizontalProgressDialog.cancel();
						Toast.makeText(EditActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
					}
				});
			}

			@Override
			public void onProgress(final int progress, final int max) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mHorizontalProgressDialog.setMax(max);
						mHorizontalProgressDialog.setProgress(progress);
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
//		mHorizontalProgressDialog.show();
//		new TencentOSS(this).putObject(path, UUID.randomUUID().toString(), new IPutObjectCallback() {
//			@Override
//			public void onProgress(long progress, long max) {
//				mHorizontalProgressDialog.setProgress((int) (100.0 * progress / max));
//			}
//
//			@Override
//			public void onSuccess(String objectUrl) {
//				mHorizontalProgressDialog.cancel();
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
//				mHorizontalProgressDialog.cancel();
//
//				Toast.makeText(EditActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//			}
//		});
	}

}
