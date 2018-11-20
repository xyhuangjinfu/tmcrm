package cn.hjf.tmcrm.customer;

import android.content.Context;
import android.os.AsyncTask;
import cn.hjf.tmcrm.Image;
import cn.hjf.tmcrm.oss.IDeleteObjectCallback;
import cn.hjf.tmcrm.oss.TencentOSS;
import cn.hjf.tmcrm.storage.FileStorage;
import cn.hjf.tmcrm.util.FileUtil;

public class DeleteIdCardImageTask extends AsyncTask<Image, Void, Void> {

	public interface Callback {
		void onStart();

		void onSuccess();

		void onFail(Throwable error);
	}

	private Context mContext;

	private FileStorage mFileStorage;
	private TencentOSS mTencentOSS;

	private Callback mCallback;

	public DeleteIdCardImageTask(Context context) {
		mContext = context;

		mFileStorage = new FileStorage(context);
		mTencentOSS = new TencentOSS(context);
	}

	@Override
	protected Void doInBackground(Image... images) {
		if (mCallback != null) {
			mCallback.onStart();
		}

		Image image = images[0];

		FileUtil.delete(image.getLocalPath());

		mTencentOSS.deleteObject(image.getUrl(), new IDeleteObjectCallback() {
			@Override
			public void onSuccess() {
				if (mCallback != null) {
					mCallback.onSuccess();
				}
			}

			@Override
			public void onFail(Throwable error) {
				if (mCallback != null) {
					mCallback.onFail(error);
				}
			}
		});

		return null;
	}

	public void setCallback(Callback callback) {
		mCallback = callback;
	}
}
