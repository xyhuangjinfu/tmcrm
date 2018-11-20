package cn.hjf.tmcrm.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import cn.hjf.tmcrm.Image;
import cn.hjf.tmcrm.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class IdCardImageAdapter extends BaseAdapter {

	private Context mContext;
	private List<Image> mImageList;

	private OnEventListener mOnEventListener;

	public interface OnEventListener {
		void onClick(int position);

		void onDelete(int position);
	}

	public IdCardImageAdapter(Context context, List<Image> imageList) {
		mContext = context;
		mImageList = imageList;
	}

	@Override
	public int getCount() {
		return mImageList == null ? 0 : mImageList.size();
	}

	@Override
	public Object getItem(int position) {
		return mImageList.get(position);
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

		Glide.with(mContext)
				.load(mImageList.get(position).getLocalPath())
				.into(vh.mIvPic);

		return convertView;
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
