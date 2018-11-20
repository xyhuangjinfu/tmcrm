package cn.hjf.tmcrm.customer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import cn.hjf.tmcrm.Image;
import cn.hjf.tmcrm.R;
import cn.hjf.tmcrm.widget.ExpandAllGridView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CustomerInfoView {

	public interface EventListener {
		void onChooseImage();
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

	public CustomerInfoView(Context context, View rootView) {
		mContext = context;
		mRootView = rootView;

		init();
	}

	private void init() {
		mEtName = mRootView.findViewById(R.id.et_customer_name);
		mEtId = mRootView.findViewById(R.id.et_customer_id);

		mEgvIdCardImage = mRootView.findViewById(R.id.egv_customer_id_card);
		mImageList = new ArrayList<>();
		mImageAdapter = new IdCardImageAdapter(mContext, mImageList);
		mEgvIdCardImage.setAdapter(mImageAdapter);
		mImageAdapter.setOnEventListener(new IdCardImageAdapter.OnEventListener() {
			@Override
			public void onClick(int position) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

				File imageFile = new File(mImageList.get(position).getLocalPath());
				Uri imageUri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", imageFile);

				intent.setDataAndType(imageUri, "image/*");
				mContext.startActivity(intent);
			}

			@Override
			public void onDelete(int position) {

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

	public void renderIdCard(Image image) {
		mImageList.add(image);
		mImageAdapter.notifyDataSetChanged();
	}

	public void renderCustomer(Customer customer) {

	}

}
