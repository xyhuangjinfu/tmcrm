package cn.hjf.tmcrm.customer;

import android.content.Context;
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
}
