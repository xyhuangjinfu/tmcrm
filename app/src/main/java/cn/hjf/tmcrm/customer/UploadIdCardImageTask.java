package cn.hjf.tmcrm.customer;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import cn.hjf.tmcrm.Image;
import cn.hjf.tmcrm.oss.IPutObjectCallback;
import cn.hjf.tmcrm.oss.TencentOSS;
import cn.hjf.tmcrm.storage.FileStorage;
import cn.hjf.tmcrm.util.FileUtil;

import java.io.File;
import java.util.UUID;

public class UploadIdCardImageTask extends AsyncTask<Uri, Integer, Void> {

	public interface Callback {
		void onStart();

		void onSuccess(Image image);

		void onFail(Throwable error);

		void onProgress(int progress, int max);
	}

	private Context mContext;

	private FileStorage mFileStorage;
	private TencentOSS mTencentOSS;

	private Callback mCallback;

	public UploadIdCardImageTask(Context context) {
		mContext = context;

		mFileStorage = new FileStorage(context);
		mTencentOSS = new TencentOSS(context);
	}

	@Override
	protected void onPreExecute() {

	}

	@Override
	protected Void doInBackground(Uri... uris) {
		//启动
		if (mCallback != null) {
			mCallback.onStart();
		}

		Uri uri = uris[0];
		//复制到本地目录
		final String path = new File(FileStorage.PATH_ID_CARD_IMAGE, "temp").getAbsolutePath();
		if (!mFileStorage.copyFromUri(uri, path)) {
			if (mCallback != null) {
				mCallback.onFail(new Exception("文件读取失败，请重试"));
			}
			return null;
		}

		if (mCallback != null) {
			mCallback.onProgress(100, 300);
		}
		//上传到云存储
		mTencentOSS.putObject(path, UUID.randomUUID().toString(), new IPutObjectCallback() {
			@Override
			public void onProgress(long progress, long max) {
				if (mCallback != null) {
					mCallback.onProgress(100 + (int) (100.0 * progress / max), 300);
				}
			}

			@Override
			public void onSuccess(String objectUrl) {
				//更新本地文件名称
				String realPath = FileStorage.PATH_ID_CARD_IMAGE + "/" + FileStorage.getFileNameFromUrl(objectUrl);
				FileUtil.rename(path, realPath);

				if (mCallback != null) {
					mCallback.onProgress(300, 300);
				}

				Image image = new Image();
				image.setUrl(objectUrl);
				image.setLocalPath(realPath);
				if (mCallback != null) {
					mCallback.onSuccess(image);
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
