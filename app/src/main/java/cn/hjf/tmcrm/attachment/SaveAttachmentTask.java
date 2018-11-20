package cn.hjf.tmcrm.attachment;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import java.io.File;
import java.util.UUID;

import cn.hjf.tmcrm.storage.FileStorage;

public class SaveAttachmentTask extends AsyncTask<Intent, Integer, Void> {

	public interface Callback {
		void onStart();

		void onSuccess(Attachment attachment);

		void onFail(Throwable error);
	}

	private Context mContext;

	private Callback mCallback;

	private FileStorage mFileStorage;

	private AttachmentDao mAttachmentDao;

	public SaveAttachmentTask(Context context) {
		mContext = context;
		mAttachmentDao = new AttachmentDao(context);
		mFileStorage = new FileStorage(context);
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
		final String path = new File(FileStorage.PATH_ID_CARD_IMAGE, UUID.randomUUID().toString()).getAbsolutePath();
		if (!mFileStorage.copyFromUri(uri, path)) {
			if (mCallback != null) {
				mCallback.onFail(new Exception("文件读取失败，请重试"));
			}
			return null;
		}

		//保存到数据库
		Attachment attachment = new Attachment();
		attachment.setUuid(UUID.randomUUID().toString());
		attachment.setFilePath(path);
		attachment.setMimeType(mimeType);
		boolean saveSuccess = mAttachmentDao.insertAll(attachment);

		if (!saveSuccess) {
			if (mCallback != null) {
				mCallback.onFail(new Exception("保存到数据库失败"));
			}
			return null;
		}

		//成功
		if (mCallback != null) {
			mCallback.onSuccess(attachment);
		}

		return null;
	}

	public void setCallback(Callback callback) {
		mCallback = callback;
	}
}
