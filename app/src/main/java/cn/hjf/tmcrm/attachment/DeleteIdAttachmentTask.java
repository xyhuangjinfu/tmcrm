package cn.hjf.tmcrm.attachment;

import android.content.Context;
import android.os.AsyncTask;
import cn.hjf.tmcrm.oss.IDeleteObjectCallback;
import cn.hjf.tmcrm.oss.TencentOSS;
import cn.hjf.tmcrm.storage.FileStorage;
import cn.hjf.tmcrm.util.FileUtil;

public class DeleteIdAttachmentTask extends AsyncTask<Attachment, Void, Void> {

	public interface Callback {
		void onStart();

		void onSuccess();

		void onFail(Throwable error);
	}

	private Context mContext;

	private FileStorage mFileStorage;
	private TencentOSS mTencentOSS;

	private Callback mCallback;

	public DeleteIdAttachmentTask(Context context) {
		mContext = context;

		mFileStorage = new FileStorage(context);
		mTencentOSS = new TencentOSS(context);
	}

	@Override
	protected Void doInBackground(Attachment... attachments) {
		if (mCallback != null) {
			mCallback.onStart();
		}

		Attachment attachment = attachments[0];

		FileUtil.delete(attachment.getFilePath());

//		mTencentOSS.deleteObject(attachment.getUrl(), new IDeleteObjectCallback() {
//			@Override
//			public void onSuccess() {
//				if (mCallback != null) {
//					mCallback.onSuccess();
//				}
//			}
//
//			@Override
//			public void onFail(Throwable error) {
//				if (mCallback != null) {
//					mCallback.onFail(error);
//				}
//			}
//		});

		return null;
	}

	public void setCallback(Callback callback) {
		mCallback = callback;
	}
}
