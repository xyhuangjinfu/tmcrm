package cn.hjf.tmcrm.customer;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import cn.hjf.tmcrm.R;
import cn.hjf.tmcrm.attachment.Attachment;
import cn.hjf.tmcrm.attachment.DeletableAttachmentAdapter;
import cn.hjf.tmcrm.widget.ExpandAllGridView;

import java.util.ArrayList;
import java.util.List;

public class CustomerInfoView {

	public interface EventListener {
		void onChooseAttachment();

		void onClickAttachment(Attachment attachment);

		void onDeleteAttachment(Attachment image);
	}

	private final Context mContext;
	private final View mRootView;

	private EditText mEtName;
	private EditText mEtId;
	private Button mBtnAddIdImage;
	private ExpandAllGridView mEgvIdCardImage;
	private DeletableAttachmentAdapter mImageAdapter;
	private List<Attachment> mImageList;

	private EventListener mEventListener;

	private Customer mCustomer;

	public CustomerInfoView(Context context, View rootView) {
		mContext = context;
		mRootView = rootView;

		init();
	}

	private void init() {
		mEtName = mRootView.findViewById(R.id.et_customer_name);
		mEtName.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (mCustomer != null) {
					mCustomer.setName(mEtName.getText().toString());
				}
			}
		});
		mEtId = mRootView.findViewById(R.id.et_customer_id);
		mEtId.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (mCustomer != null) {
					mCustomer.setId(mEtId.getText().toString());
				}
			}
		});

		mEgvIdCardImage = mRootView.findViewById(R.id.egv_customer_id_card);
		mImageList = new ArrayList<>();
		mImageAdapter = new DeletableAttachmentAdapter(mContext, mImageList);
		mEgvIdCardImage.setAdapter(mImageAdapter);
		mImageAdapter.setOnEventListener(new DeletableAttachmentAdapter.OnEventListener() {
			@Override
			public void onClick(int position) {
				if (mEventListener != null) {
					mEventListener.onClickAttachment(mImageList.get(position));
				}

			}

			@Override
			public void onDelete(int position) {
				if (mEventListener != null) {
					mEventListener.onDeleteAttachment(mImageList.get(position));
				}
			}
		});

		mBtnAddIdImage = mRootView.findViewById(R.id.btn_add_id_card);
		mBtnAddIdImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mEventListener != null) {
					mEventListener.onChooseAttachment();
				}
			}
		});

	}

	//--------------------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------

	public void setEventListener(EventListener eventListener) {
		mEventListener = eventListener;
	}

	public void renderCustomer(Customer customer) {
		mCustomer = customer;

		mImageList.clear();
		mImageList.addAll(customer.getIdCardImageList());
		mImageAdapter.notifyDataSetChanged();

		mEtName.setText(customer.getName());
		mEtId.setText(customer.getId());
	}

}
