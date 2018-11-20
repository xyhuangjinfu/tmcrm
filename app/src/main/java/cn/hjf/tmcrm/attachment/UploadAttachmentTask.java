package cn.hjf.tmcrm.attachment;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import cn.hjf.tmcrm.oss.IPutObjectCallback;
import cn.hjf.tmcrm.oss.TencentOSS;
import cn.hjf.tmcrm.storage.FileStorage;
import cn.hjf.tmcrm.util.FileUtil;

import java.io.File;
import java.util.UUID;

public class UploadAttachmentTask extends AsyncTask<Intent, Integer, Void> {

	public interface Callback {
		void onStart();

		void onSuccess(Attachment attachment);

		void onFail(Throwable error);

		void onProgress(int progress, int max);
	}

	private Context mContext;

	private FileStorage mFileStorage;
	private TencentOSS mTencentOSS;

	private Callback mCallback;

	public UploadAttachmentTask(Context context) {
		mContext = context;

		mFileStorage = new FileStorage(context);
		mTencentOSS = new TencentOSS(context);
	}

	@Override
	protected Void doInBackground(Intent... intents) {
		//启动
		if (mCallback != null) {
			mCallback.onStart();
		}

		final Intent intent = intents[0];
		final Uri uri = intent.getData();

		ContentResolver cr = mContext.getContentResolver();
		final String mimeType = cr.getType(uri);

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

				Attachment attachment = new Attachment();
//				attachment.setUrl(objectUrl);
				attachment.setFilePath(realPath);
				attachment.setMimeType(mimeType);
				if (mCallback != null) {
					mCallback.onSuccess(attachment);
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
