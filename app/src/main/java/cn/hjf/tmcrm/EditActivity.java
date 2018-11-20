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
import cn.hjf.tmcrm.attachment.Attachment;
import cn.hjf.tmcrm.attachment.AttachmentViewer;
import cn.hjf.tmcrm.attachment.DeleteIdAttachmentTask;
import cn.hjf.tmcrm.attachment.UploadAttachmentTask;
import cn.hjf.tmcrm.customer.Customer;
import cn.hjf.tmcrm.customer.CustomerInfoView;
import cn.hjf.tmcrm.customer.CustomerStorage;
import cn.hjf.tmcrm.order.OrderInfoView;
import cn.hjf.tmcrm.oss.TencentOSS;
import cn.hjf.tmcrm.storage.FileStorage;
import cn.hjf.tmcrm.storage.IStorageCallback;

import java.io.File;
import java.util.ArrayList;

public class EditActivity extends BaseActivity {

	private static final int REQ_CHOOSE_ID_CARD_IMAGE = 1000;
	private static final int REQ_CHOOSE_ORDER_ATTACHMENT = 1001;

	private CustomerInfoView mCustomerInfoView;

	private FileStorage mFileStorage;
	private TencentOSS mTencentOSS;

	private ProgressDialog mHorizontalProgressDialog;
	private ProgressDialog mCircleProgressDialog;

	private UploadAttachmentTask mUploadAttachmentTask;
	private DeleteIdAttachmentTask mDeleteIdAttachmentTask;

	private Customer mCustomer = new Customer();

	private CustomerStorage mCustomerStorage;

	private Button mBtnSubmit;

	private OrderInfoView mOrderInfoView;

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
		mHorizontalProgressDialog.setCancelable(false);

		mCircleProgressDialog = new ProgressDialog(this);
		mCircleProgressDialog.setCancelable(false);

		mCustomerInfoView = new CustomerInfoView(this, findViewById(R.id.layout_customer_info));
		mCustomerInfoView.setEventListener(new CustomerInfoView.EventListener() {
			@Override
			public void onChooseAttachment() {
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.setType("*/*");
				startActivityForResult(Intent.createChooser(i, "选择身份证照片"), REQ_CHOOSE_ID_CARD_IMAGE);
			}

			@Override
			public void onClickAttachment(Attachment attachment) {
				//预览
				AttachmentViewer.view(EditActivity.this, attachment);
			}

			@Override
			public void onDeleteAttachment(final Attachment attachment) {
				//删除
				mDeleteIdAttachmentTask = new DeleteIdAttachmentTask(EditActivity.this);
				mDeleteIdAttachmentTask.setCallback(new DeleteIdAttachmentTask.Callback() {
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

								mCustomer.getIdCardImageList().remove(attachment);
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
				mDeleteIdAttachmentTask.execute(attachment);
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

		mOrderInfoView = new OrderInfoView(this, findViewById(R.id.layout_order_info));
		mOrderInfoView.setEventListener(new OrderInfoView.EventListener() {
			@Override
			public void onChooseAttachment() {
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.setType("*/*");
				startActivityForResult(Intent.createChooser(i, "选择附件"), REQ_CHOOSE_ORDER_ATTACHMENT);
			}

			@Override
			public void onClickAttachment(Attachment attachment) {
				//预览
//				Intent intent = new Intent(Intent.ACTION_VIEW);
//				intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//				File imageFile = new File(attachment.getFilePath());
//				Uri imageUri = FileProvider.getUriForFile(EditActivity.this, EditActivity.this.getApplicationContext().getPackageName() + ".provider", imageFile);
//
//				intent.setDataAndType(imageUri, attachment.getMimeType());
//				startActivity(intent);

				AttachmentViewer.view(EditActivity.this, attachment);
			}

			@Override
			public void onDeleteAttachment(Attachment attachment) {

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

//		String type = data.getType();
//
//		if (TextUtils.isEmpty(type)) {
//			ContentResolver cr = this.getContentResolver();
//			type = cr.getType(data.getData());
//		}
//
//		Toast.makeText(this, type, Toast.LENGTH_LONG).show();

		if (requestCode == REQ_CHOOSE_ID_CARD_IMAGE || requestCode == REQ_CHOOSE_ORDER_ATTACHMENT) {
			handleChooseIdCardImage(requestCode, data);
			return;
		}
	}

	private void handleChooseIdCardImage(final int requestCode, Intent data) {
		mUploadAttachmentTask = new UploadAttachmentTask(this);
		mUploadAttachmentTask.setCallback(new UploadAttachmentTask.Callback() {
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
			public void onSuccess(final Attachment attachment) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mHorizontalProgressDialog.cancel();

						if (requestCode == REQ_CHOOSE_ID_CARD_IMAGE) {
							if (mCustomer.getIdCardImageList() == null) {
								mCustomer.setIdCardImageList(new ArrayList<Attachment>());
							}
							mCustomer.getIdCardImageList().add(attachment);
							mCustomerInfoView.renderCustomer(mCustomer);
						} else if (requestCode == REQ_CHOOSE_ORDER_ATTACHMENT) {

						}

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
		mUploadAttachmentTask.execute(data);
	}

}
