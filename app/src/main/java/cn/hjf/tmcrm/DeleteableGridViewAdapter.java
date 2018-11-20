package cn.hjf.tmcrm;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public class DeleteableGridViewAdapter extends BaseAdapter {

	private Context mContext;
	private List<String> mLocalPathList;

	public DeleteableGridViewAdapter(Context context, List<String> localPathList) {
		mContext = context;
		mLocalPathList = localPathList;
	}

	@Override
	public int getCount() {
		return mLocalPathList == null ? 0 : mLocalPathList.size();
	}

	@Override
	public Object getItem(int position) {
		return mLocalPathList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}
}
