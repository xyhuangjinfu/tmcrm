package cn.hjf.tmcrm.attachment;

import android.content.Context;
import android.support.v4.content.MimeTypeFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import cn.hjf.tmcrm.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class DeletableAttachmentAdapter extends BaseAdapter {

	private Context mContext;
	private List<Attachment> mAttachmentList;

	private OnEventListener mOnEventListener;

	public interface OnEventListener {
		void onClick(int position);

		void onDelete(int position);
	}

	public DeletableAttachmentAdapter(Context context, List<Attachment> attachmentList) {
		mContext = context;
		mAttachmentList = attachmentList;
	}

	@Override
	public int getCount() {
		return mAttachmentList == null ? 0 : mAttachmentList.size();
	}

	@Override
	public Object getItem(int position) {
		return mAttachmentList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		VH vh;

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_id_card_image, parent, false);
			vh = new VH(convertView);
			convertView.setTag(vh);
		} else {
			vh = (VH) convertView.getTag();
		}

		vh.mIvDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mOnEventListener != null) {
					mOnEventListener.onDelete(position);
				}
			}
		});

		vh.mIvPic.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mOnEventListener != null) {
					mOnEventListener.onClick(position);
				}
			}
		});

		render(vh, mAttachmentList.get(position));

		return convertView;
	}

	private void render(VH vh, Attachment attachment) {
		if (MimeTypeFilter.matches(attachment.getMimeType(), "image/*")) {
			Glide.with(mContext)
					.load(attachment.getFilePath())
					.into(vh.mIvPic);
			return;
		}

		if (MimeTypeFilter.matches(attachment.getMimeType(), "application/pdf")) {
			vh.mIvPic.setImageResource(R.drawable.icon_pdf);
			return;
		}
	}

	private static class VH {
		ImageView mIvPic;
		ImageView mIvDelete;

		public VH(View rootView) {
			mIvPic = rootView.findViewById(R.id.iv_pic);
			mIvDelete = rootView.findViewById(R.id.iv_delete);
		}
	}

	public void setOnEventListener(OnEventListener onEventListener) {
		mOnEventListener = onEventListener;
	}
}
