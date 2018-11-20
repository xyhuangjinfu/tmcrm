package cn.hjf.tmcrm.attachment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.MimeTypeFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import cn.hjf.tmcrm.R;

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

		Log.e("O_O", position + " -> " + mAttachmentList.get(position).getMimeType() + " - " + vh.mIvPic);

		render(vh, mAttachmentList.get(position));

		return convertView;
	}

	private void render(final VH vh, final Attachment attachment) {
		vh.mIvPic.setTag(vh.mIvPic.getId(), attachment.getFilePath());

		if (MimeTypeFilter.matches(attachment.getMimeType(), "image/*")) {
			Glide.with(mContext)
					.load(attachment.getFilePath())
					.addListener(new RequestListener<Drawable>() {
						@Override
						public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
							return false;
						}

						@Override
						public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
							if (vh.mIvPic.getTag(vh.mIvPic.getId()).equals(attachment.getFilePath())) {
								vh.mIvPic.setImageDrawable(resource);
							}
							return true;
						}
					})
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
