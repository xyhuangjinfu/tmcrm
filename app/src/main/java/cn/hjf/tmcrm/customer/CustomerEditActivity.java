package cn.hjf.tmcrm.customer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import cn.hjf.tmcrm.BaseActivity;
import cn.hjf.tmcrm.R;
import cn.hjf.tmcrm.attachment.Attachment;
import cn.hjf.tmcrm.attachment.AttachmentViewer;
import cn.hjf.tmcrm.attachment.DeleteIdAttachmentTask;
import cn.hjf.tmcrm.storage.IStorageCallback;

import java.util.UUID;

public class CustomerEditActivity extends BaseActivity {

	private static final int REQ_CHOOSE_ID_CARD_IMAGE = 1000;

	public static final String KEY_CUSTOMER = "key_customer";

	private CustomerInfoView mCustomerInfoView;

	private Button mBtnSubmit;

	private Customer mCustomer;

	private DeleteIdAttachmentTask mDeleteIdAttachmentTask;

	private ProgressDialog mHorizontalProgressDialog;
	private ProgressDialog mCircleProgressDialog;

	private CustomerStorage mCustomerStorage;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_customer);

		initCustomer();

		initCustomerView();

		initSubmitBtn();

		initProgressDialog();

		mCustomerStorage = new CustomerStorage(this);

	}

	private void initProgressDialog() {
		mHorizontalProgressDialog = new ProgressDialog(this);
		mHorizontalProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mHorizontalProgressDialog.setMax(100);
		mHorizontalProgressDialog.setCancelable(false);

		mCircleProgressDialog = new ProgressDialog(this);
		mCircleProgressDialog.setCancelable(false);
	}

	private void initCustomer() {
		mCustomer = (Customer) getIntent().getSerializableExtra(KEY_CUSTOMER);
		if (mCustomer == null) {
			mCustomer = new Customer();
			mCustomer.setUuid(UUID.randomUUID().toString());
		}
	}

	private void initCustomerView() {
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
				AttachmentViewer.view(CustomerEditActivity.this, attachment);
			}

			@Override
			public void onDeleteAttachment(final Attachment attachment) {
				//删除
				mDeleteIdAttachmentTask = new DeleteIdAttachmentTask(CustomerEditActivity.this);
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
								Toast.makeText(CustomerEditActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
							}
						});
					}
				});
				mDeleteIdAttachmentTask.execute(attachment);
			}
		});
	}

	private void initSubmitBtn() {
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
								Toast.makeText(CustomerEditActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
							}
						});
					}

					@Override
					public void onFail(@Nullable final Throwable error) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								mCircleProgressDialog.cancel();
								Toast.makeText(CustomerEditActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
							}
						});
					}
				});
			}
		});
	}
}
