package cn.hjf.tmcrm.attachment;

import android.content.Context;
import android.os.AsyncTask;

import cn.hjf.tmcrm.storage.FileStorage;
import cn.hjf.tmcrm.util.FileUtil;

public class DeleteAttachmentTask extends AsyncTask<Attachment, Void, Void> {

	public interface Callback {
		void onStart();

		void onSuccess();

		void onFail(Throwable error);
	}

	private Context mContext;

	private FileStorage mFileStorage;

	private Callback mCallback;

	private AttachmentDao mAttachmentDao;

	public DeleteAttachmentTask(Context context) {
		mContext = context;
		mFileStorage = new FileStorage(context);
		mAttachmentDao = new AttachmentDao(context);
	}

	@Override
	protected Void doInBackground(Attachment... attachments) {
		if (mCallback != null) {
			mCallback.onStart();
		}

		Attachment attachment = attachments[0];

		//删除数据库
		boolean dbDeleteSuccess = mAttachmentDao.deleteByUuid(attachment.getUuid());
		if (!dbDeleteSuccess) {
			if (mCallback != null) {
				mCallback.onFail(new Exception("数据库删除失败"));
			}
			return null;
		}

		//删除文件
		boolean fileDeleteSuccess = FileUtil.delete(attachment.getFilePath());
		if (!fileDeleteSuccess) {
			if (mCallback != null) {
				mCallback.onFail(new Exception("文件删除失败"));
			}
			return null;
		}

		//成功
		if (mCallback != null) {
			mCallback.onSuccess();
		}

		return null;
	}

	public void setCallback(Callback callback) {
		mCallback = callback;
	}
}
