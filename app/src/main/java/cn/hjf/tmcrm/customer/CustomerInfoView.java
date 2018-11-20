package cn.hjf.tmcrm.customer;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import cn.hjf.tmcrm.Image;
import cn.hjf.tmcrm.R;
import cn.hjf.tmcrm.widget.ExpandAllGridView;

import java.util.ArrayList;
import java.util.List;

public class CustomerInfoView {

	public interface EventListener {
		void onChooseImage();

		void onClickImage(Image image);

		void onDeleteImage(Image image);
	}

	private final Context mContext;
	private final View mRootView;

	private EditText mEtName;
	private EditText mEtId;
	private Button mBtnAddIdImage;
	private ExpandAllGridView mEgvIdCardImage;
	private IdCardImageAdapter mImageAdapter;
	private List<Image> mImageList;

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
		mImageAdapter = new IdCardImageAdapter(mContext, mImageList);
		mEgvIdCardImage.setAdapter(mImageAdapter);
		mImageAdapter.setOnEventListener(new IdCardImageAdapter.OnEventListener() {
			@Override
			public void onClick(int position) {
				if (mEventListener != null) {
					mEventListener.onClickImage(mImageList.get(position));
				}

			}

			@Override
			public void onDelete(int position) {
				if (mEventListener != null) {
					mEventListener.onDeleteImage(mImageList.get(position));
				}
			}
		});

		mBtnAddIdImage = mRootView.findViewById(R.id.btn_add_id_card);
		mBtnAddIdImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mEventListener != null) {
					mEventListener.onChooseImage();
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
