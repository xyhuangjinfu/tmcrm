package cn.hjf.tmcrm.order;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import cn.hjf.tmcrm.R;
import cn.hjf.tmcrm.attachment.Attachment;
import cn.hjf.tmcrm.attachment.DeletableAttachmentAdapter;
import cn.hjf.tmcrm.widget.ExpandAllGridView;

import java.util.ArrayList;
import java.util.List;

public class OrderInfoView {

	public interface EventListener {
		void onChooseAttachment();

		void onClickAttachment(Attachment attachment);

		void onDeleteAttachment(Attachment attachment);
	}

	private final Context mContext;
	private final View mRootView;

	private Button mBtnAddAttachment;
	private ExpandAllGridView mEgvAttachment;
	private DeletableAttachmentAdapter mAttachmentAdapter;
	private List<Attachment> mAttachmentList;

	private EventListener mEventListener;

	public OrderInfoView(Context context, View rootView) {
		mContext = context;
		mRootView = rootView;

		init();
	}

	private void init() {
		mEgvAttachment = mRootView.findViewById(R.id.egv_attachment);
		mAttachmentList = new ArrayList<>();
		mAttachmentAdapter = new DeletableAttachmentAdapter(mContext, mAttachmentList);
		mEgvAttachment.setAdapter(mAttachmentAdapter);
		mAttachmentAdapter.setOnEventListener(new DeletableAttachmentAdapter.OnEventListener() {
			@Override
			public void onClick(int position) {
				if (mEventListener != null) {
					mEventListener.onClickAttachment(mAttachmentList.get(position));
				}

			}

			@Override
			public void onDelete(int position) {
				if (mEventListener != null) {
					mEventListener.onDeleteAttachment(mAttachmentList.get(position));
				}
			}
		});

		mBtnAddAttachment = mRootView.findViewById(R.id.btn_add_id_card);
		mBtnAddAttachment.setOnClickListener(new View.OnClickListener() {
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
//
//	public void renderCustomer(Customer customer) {
//		mCustomer = customer;
//
//		mImageList.clear();
//		mImageList.addAll(customer.getIdCardImageList());
//		mImageAdapter.notifyDataSetChanged();
//
//		mEtName.setText(customer.getName());
//		mEtId.setText(customer.getId());
//	}

}
