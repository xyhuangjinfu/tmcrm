package cn.hjf.tmcrm.storage;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import cn.hjf.tmcrm.util.FileUtil;

import java.io.InputStream;

public final class FileStorage {

	public static final String PATH_APP = Environment.getExternalStorageDirectory() + "/tmcrm";
	public static final String PATH_ID_CARD_IMAGE = PATH_APP + "/idcard";

	private Context mContext;

	public FileStorage(Context context) {
		mContext = context;
	}

	public boolean copyFromUri(Uri uri, String path) {
		try {
			InputStream iStream = mContext.getContentResolver().openInputStream(uri);

			byte[] data = FileUtil.read(iStream);

			return FileUtil.save(path, data);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	//-----------------------------------------------------------------------------------------------------------------------------
	//-----------------------------------------------------------------------------------------------------------------------------

	public static String getFileNameFromUrl(String url) {
		return String.valueOf(url.hashCode());
	}
}
